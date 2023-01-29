package com.runjian.device.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.constant.StreamType;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.feign.StreamManageApi;
import com.runjian.device.service.DataBaseService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.response.ChannelDetailRsp;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通道北向服务
 * @author Miracle
 * @date 2023/1/9 10:20
 */
@Slf4j
@Service
public class ChannelNorthServiceImpl implements ChannelNorthService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    @Autowired
    private StreamManageApi streamManageApi;

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private DataBaseService dataBaseService;

    /**
     * 通道同步
     * @param deviceId 设备ID
     * @return 通道同步信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChannelSyncRsp channelSync(Long deviceId) {
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        if (deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("设备%s处于离线状态", deviceId));
        }
        if (!deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("设备%s是未注册成功的设备，不允许操作", deviceId));
        }
        CommonResponse<ChannelSyncRsp> response = parsingEngineApi.channelSync(deviceId);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "通道同步失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        ChannelSyncRsp channelSyncRsp = response.getData();
        channelSyncRsp.setNum(channelSyncRsp.getChannelDetailList().size());
        // 判断是否有通道
        if (channelSyncRsp.getNum() > 0){
            List<ChannelInfo> channelSaveList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailSaveList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailUpdateList = new ArrayList<>(channelSyncRsp.getNum());
            LocalDateTime nowTime = LocalDateTime.now();
            // 循环通道进行添加
            for (ChannelDetailRsp rsp : channelSyncRsp.getChannelDetailList()){
                Optional<ChannelInfo> ChannelInfoOp = channelMapper.selectById(rsp.getChannelId());
                // 判断通道信息是否已存在
                if (ChannelInfoOp.isEmpty()){
                    ChannelInfo channelInfo = new ChannelInfo();
                    channelInfo.setId(rsp.getChannelId());
                    channelInfo.setChannelType(rsp.getChannelType());
                    channelInfo.setSignState(SignState.TO_BE_ADD.getCode());
                    channelInfo.setOnlineState(CommonEnum.ENABLE.getCode());
                    channelInfo.setDeviceId(deviceId);
                    channelInfo.setStreamMode(StreamType.UDP.getMsg());
                    channelInfo.setCreateTime(nowTime);
                    channelInfo.setUpdateTime(nowTime);
                    channelSaveList.add(channelInfo);
                }
                Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(rsp.getChannelId(), DetailType.CHANNEL.getCode());
                DetailInfo detailInfo = new DetailInfo();
                detailInfo.setIp(rsp.getIp());
                detailInfo.setPort(rsp.getPort());
                detailInfo.setName(rsp.getName());
                detailInfo.setManufacturer(rsp.getManufacturer());
                detailInfo.setModel(rsp.getModel());
                detailInfo.setFirmware(rsp.getFirmware());
                detailInfo.setPtzType(rsp.getPtzType());
                detailInfo.setUpdateTime(nowTime);
                // 判断数据是否为空，根据情况保存或者更新
                if (detailInfoOp.isEmpty()){
                    detailInfo.setDcId(rsp.getChannelId());
                    detailInfo.setType(DetailType.CHANNEL.getCode());
                    detailInfo.setCreateTime(nowTime);
                    detailSaveList.add(detailInfo);
                } else {
                    detailUpdateList.add(detailInfo);
                }
            }
            // 对数据进行批量保存
            if (channelSaveList.size() > 0){
                channelMapper.batchSave(channelSaveList);
            }
            if (detailSaveList.size() > 0){
                detailMapper.batchSave(detailSaveList);
            }
            if (detailUpdateList.size() > 0){
                detailMapper.batchUpdate(detailUpdateList);
            }
        }
        return channelSyncRsp;
    }

    /**
     * 通道注册状态转为成功
     * @param channelId 通道Id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelSignSuccess(Long channelId) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(channelId);
        if(channelInfo.getSignState().equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "该通道的注册状态已是成功状态");
        }
        channelInfo.setSignState(SignState.SUCCESS.getCode());
        channelInfo.setUpdateTime(LocalDateTime.now());
        channelMapper.updateSignState(channelInfo);

    }

    @Override
    public void channelDeleteByDeviceId(Long deviceId, Boolean isDeleteData) {
        List<ChannelInfo> channelInfoList = channelMapper.selectByDeviceId(deviceId);
        if (channelInfoList.size() == 0){
            return;
        }
        if (isDeleteData){
            List<Long> channelInfoIdList = channelInfoList.stream().map(ChannelInfo::getId).collect(Collectors.toList());
            detailMapper.deleteByDcIdsAndType(channelInfoIdList, DetailType.CHANNEL.getCode());
            channelMapper.deleteByDeviceId(deviceId);
        }else {
            LocalDateTime nowTime = LocalDateTime.now();
            channelInfoList.forEach(channelInfo -> {
                channelInfo.setSignState(SignState.DELETED.getCode());
                channelInfo.setUpdateTime(nowTime);
            });
            channelMapper.batchUpdateSignState(channelInfoList);
        }


    }

    /**
     * 设备点播
     * @param chId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @return
     */
    @Override
    public VideoPlayRsp channelPlay(Long chId, Boolean enableAudio, Boolean ssrcCheck) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
        CommonResponse<?> response = streamManageApi.applyPlay(channelInfo.getId(), deviceInfo.getGatewayId(), true);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频点播失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        DeviceControlReq req = new DeviceControlReq();
        req.setChannelId(chId);
        req.putData("enableAudio", enableAudio);
        req.putData("ssrcCheck", ssrcCheck);
        req.putData("streamMode", channelInfo.getStreamMode());
        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlay(req);
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频点播失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    /**
     * 视频回放
     * @param chId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public VideoPlayRsp channelPlayback(Long chId, Boolean enableAudio, Boolean ssrcCheck, LocalDateTime startTime, LocalDateTime endTime) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
        CommonResponse<?> response = streamManageApi.applyPlay(channelInfo.getId(), deviceInfo.getGatewayId(), false);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频回放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        DeviceControlReq req = new DeviceControlReq();
        req.setChannelId(chId);
        req.putData("enableAudio", enableAudio);
        req.putData("ssrcCheck", ssrcCheck);
        req.putData("streamMode", channelInfo.getStreamMode());
        req.putData("startTime", startTime);
        req.putData("endTime", endTime);
        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlayback(req);
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频回放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    /**
     * 云台控制状态
     * @param chId 通道ID
     * @param commandCode 指令code
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     * @param totalSpeed 总速度
     */
    @Override
    public void channelPtzControl(Long chId, Integer commandCode, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed, Integer totalSpeed) {
        DeviceControlReq req = new DeviceControlReq();
        req.setChannelId(chId);
        req.putData("commandCode", commandCode);
        req.putData("horizonSpeed", horizonSpeed);
        req.putData("verticalSpeed", verticalSpeed);
        req.putData("zoomSpeed", zoomSpeed);
        req.putData("totalSpeed", totalSpeed);
        CommonResponse<Boolean> response = parsingEngineApi.channelPtzControl(req);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "云台控制失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
    }

}
