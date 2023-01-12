package com.runjian.device.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.feign.StreamManageApi;
import com.runjian.device.service.north.PlayVideoNorthService;
import com.runjian.device.vo.response.VideoPlayRsp;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 设备播放北向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
public class PlayVideoNorthServiceImpl implements PlayVideoNorthService {

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    @Autowired
    private StreamManageApi streamManageApi;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 设备点播
     * @param chId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @return
     */
    @Override
    public VideoPlayRsp play(Long chId, Boolean enableAudio, Boolean ssrcCheck) {
        ChannelInfo channelInfo = getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        DeviceInfo deviceInfo = getDeviceInfo(channelInfo.getDeviceId());
        CommonResponse<String> response = streamManageApi.applyPlay(channelInfo.getId(), deviceInfo.getGatewayId(), true);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "设备播放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }

        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlay(chId, enableAudio, ssrcCheck, response.getData());
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "设备播放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    /**
     * 获取设备信息
     * @param deviceId 设备id
     * @return
     */
    @NotNull
    private DeviceInfo getDeviceInfo(Long deviceId) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("设备%s不存在", deviceId));
        }
        DeviceInfo deviceInfo = deviceInfoOp.get();
        return deviceInfo;
    }

    /**
     *
     * @param chId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public VideoPlayRsp playBack(Long chId, Boolean enableAudio, Boolean ssrcCheck, LocalDateTime startTime, LocalDateTime endTime) {
        ChannelInfo channelInfo = getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        DeviceInfo deviceInfo = getDeviceInfo(channelInfo.getDeviceId());
        CommonResponse<String> response = streamManageApi.applyPlay(channelInfo.getId(), deviceInfo.getGatewayId(), false);
        if (response.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "设备播放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        CommonResponse<VideoPlayRsp> videoPlayRspCommonResponse = parsingEngineApi.channelPlayback(chId, enableAudio, ssrcCheck, response.getData(), startTime, endTime);
        if (videoPlayRspCommonResponse.getCode() != 0){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "设备播放失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return videoPlayRspCommonResponse.getData();
    }

    /**
     * 获取通道信息
     * @param chId 通道id
     * @return
     */
    @NotNull
    private ChannelInfo getChannelInfo(Long chId) {
        Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(chId);
        if (channelInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("通道%s不存在", chId));
        }
        ChannelInfo channelInfo = channelInfoOp.get();
        return channelInfo;
    }

}
