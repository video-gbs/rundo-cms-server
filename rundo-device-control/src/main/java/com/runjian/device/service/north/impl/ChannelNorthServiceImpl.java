package com.runjian.device.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.utils.DateUtils;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.common.constant.StandardName;
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
import com.runjian.device.vo.response.VideoRecordRsp;
import com.runjian.device.vo.response.ChannelDetailRsp;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
            List<ChannelInfo> channelUpdateList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailSaveList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailUpdateList = new ArrayList<>(channelSyncRsp.getNum());
            LocalDateTime nowTime = LocalDateTime.now();
            // 循环通道进行添加
            for (ChannelDetailRsp rsp : channelSyncRsp.getChannelDetailList()){
                Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(rsp.getChannelId());
                // 判断通道信息是否已存在
                if (channelInfoOp.isEmpty()){
                    ChannelInfo channelInfo = new ChannelInfo();
                    channelInfo.setId(rsp.getChannelId());
                    channelInfo.setChannelType(rsp.getChannelType());
                    channelInfo.setSignState(SignState.TO_BE_ADD.getCode());
                    channelInfo.setOnlineState(rsp.getOnlineState());
                    channelInfo.setDeviceId(deviceId);
                    channelInfo.setStreamMode(StreamType.UDP.getMsg());
                    channelInfo.setCreateTime(nowTime);
                    channelInfo.setUpdateTime(nowTime);
                    channelSaveList.add(channelInfo);
                } else {
                    ChannelInfo channelInfo = channelInfoOp.get();
                    channelInfo.setOnlineState(rsp.getOnlineState());
                    channelInfo.setUpdateTime(nowTime);
                    channelUpdateList.add(channelInfo);
                }
                Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(rsp.getChannelId(), DetailType.CHANNEL.getCode());
                DetailInfo detailInfo = detailInfoOp.orElse(new DetailInfo());
                detailInfo.setDcId(rsp.getChannelId());
                detailInfo.setOriginId(rsp.getOriginId());
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
            if (channelUpdateList.size() > 0){
                channelMapper.batchUpdateOnlineState(channelUpdateList);
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
    @Transactional(rollbackFor = Exception.class)
    public void channelDeleteByDeviceId(Long deviceId, Boolean isDeleteData) {
        List<ChannelInfo> channelInfoList = channelMapper.selectByDeviceId(deviceId);
        if (channelInfoList.size() == 0){
            return;
        }
        if (isDeleteData){
            List<Long> channelInfoIdList = channelInfoList.stream().map(ChannelInfo::getId).collect(Collectors.toList());
            if (channelInfoIdList.size() > 0){
                detailMapper.deleteByDcIdsAndType(channelInfoIdList, DetailType.CHANNEL.getCode());
            }
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
     * @param channelId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @return
     */
    @Override
    public VideoPlayRsp channelPlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck) {
        ChannelInfo channelInfo = getChannelInfoAndValid(channelId);
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
//        DeviceControlReq streamReq = new DeviceControlReq();
//        streamReq.setGatewayId(deviceInfo.getGatewayId());
//        streamReq.setDeviceId(deviceInfo.getId());
//        streamReq.setChannelId(channelInfo.getId());
//        streamReq.putData("isPlayback", false);
//        CommonResponse<?> response = streamManageApi.applyPlay(streamReq);
//        if (response.getCode() != 0){
//            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频点播失败", response.getData(), response.getMsg());
//            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
//        }
        DeviceControlReq deviceReq = new DeviceControlReq();
        deviceReq.setChannelId(channelId);
        deviceReq.putData(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        deviceReq.putData(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        deviceReq.putData(StandardName.STREAM_MODE, channelInfo.getStreamMode());
        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlay(deviceReq);
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频点播失败", videoPlayRspCommonResponse.getData(), videoPlayRspCommonResponse.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, videoPlayRspCommonResponse.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    @Override
    public VideoRecordRsp channelRecord(Long chId, LocalDateTime startTime, LocalDateTime endTime) {
        getChannelInfoAndValid(chId);
        DeviceControlReq deviceReq = new DeviceControlReq();
        deviceReq.setChannelId(chId);
        deviceReq.putData(StandardName.COM_START_TIME, DateUtils.DATE_TIME_FORMATTER.format(startTime));
        deviceReq.putData(StandardName.COM_END_TIME, DateUtils.DATE_TIME_FORMATTER.format(endTime));
        CommonResponse<VideoRecordRsp> response = parsingEngineApi.channelRecord(deviceReq);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频录像数据获取失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return response.getData();
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
        ChannelInfo channelInfo = getChannelInfoAndValid(chId);
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
//        DeviceControlReq streamReq = new DeviceControlReq();
//        streamReq.setGatewayId(deviceInfo.getGatewayId());
//        streamReq.setDeviceId(deviceInfo.getId());
//        streamReq.setChannelId(channelInfo.getId());
//        streamReq.putData("isPlayback", true);
//        CommonResponse<?> response = streamManageApi.applyPlay(streamReq);
//        if (response.getCode() != 0){
//            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频回放失败", response.getData(), response.getMsg());
//            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
//        }
        DeviceControlReq deviceReq = new DeviceControlReq();
        deviceReq.setChannelId(chId);
        deviceReq.putData(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        deviceReq.putData(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        deviceReq.putData(StandardName.STREAM_MODE, channelInfo.getStreamMode());
        deviceReq.putData(StandardName.COM_START_TIME, DateUtils.DATE_TIME_FORMATTER.format(startTime));
        deviceReq.putData(StandardName.COM_END_TIME, DateUtils.DATE_TIME_FORMATTER.format(endTime));
        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlayback(deviceReq);
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频回放失败", videoPlayRspCommonResponse.getData(), videoPlayRspCommonResponse.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, videoPlayRspCommonResponse.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    @NotNull
    private ChannelInfo getChannelInfoAndValid(Long chId) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        // 检测通道是否注册成功
        if (!channelInfo.getSignState().equals(SignState.SUCCESS.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道未注册成功");
        }
        return channelInfo;
    }

    /**
     * 云台控制状态
     * @param channelId 通道ID
     * @param cmdCode 指令code
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     * @param totalSpeed 总速度
     */
    @Override
    public void channelPtzControl(Long channelId, Integer cmdCode, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed, Integer totalSpeed) {
        getChannelInfoAndValid(channelId);
        DeviceControlReq req = new DeviceControlReq();
        req.setChannelId(channelId);
        req.putData(StandardName.PTZ_CMD_CODE, cmdCode);
        req.putData(StandardName.PTZ_HORIZON_SPEED, horizonSpeed);
        req.putData(StandardName.PTZ_VERTICAL_SPEED, verticalSpeed);
        req.putData(StandardName.PTZ_ZOOM_SPEED, zoomSpeed);
        req.putData(StandardName.PTZ_TOTAL_SPEED, totalSpeed);
        CommonResponse<Boolean> response = parsingEngineApi.channelPtzControl(req);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "云台控制失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
    }

}
