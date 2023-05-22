package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.vo.dto.GatewayConvertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

/**
 * 默认协议
 * @author Miracle
 * @date 2023/1/17 14:14
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractNorthProtocol implements NorthProtocol {

    protected final GatewayTaskService gatewayTaskService;

    protected final DataBaseService dataBaseService;

    protected final DeviceMapper deviceMapper;

    protected final ChannelMapper channelMapper;

    @Override
    public void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        switch (msgType){
            case DEVICE_DELETE_SOFT:
            case DEVICE_DELETE_HARD:
                deviceDelete(mainId, response, msgType);
                return;
            case CHANNEL_DELETE_SOFT:
            case CHANNEL_DELETE_HARD:
                channelDelete(mainId, response, msgType);
                return;
            default:
                customEvent(mainId, idType, msgType.getMsg(), dataMap, response);
        }
    }

    @Override
    public void customEvent(Long mainId, IdType idType, String msgType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        GatewayConvertDto gatewayConvertDto = new GatewayConvertDto();
        gatewayConvertDto.setDataMap(dataMap);
        Long deviceId = null;
        Long channelId = null;
        switch (idType){
            case CHANNEL:
                ChannelInfo channelInfo = dataBaseService.getChannelInfo(mainId);
                gatewayConvertDto.setChannelId(channelInfo.getOriginId());
                channelId = mainId;
                mainId = channelInfo.getDeviceId();
            case DEVICE:
                DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(mainId);
                gatewayConvertDto.setDeviceId(deviceInfo.getOriginId());
                deviceId = mainId;
                mainId = deviceInfo.getGatewayId();
            case GATEWAY:
                gatewayTaskService.sendMsgToGateway(mainId, deviceId, channelId, msgType, gatewayConvertDto, response);
                break;
        }
    }

    public void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response, MsgType msgType) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            response.setResult(CommonResponse.success(true));
        } else {
            customEvent(deviceId, IdType.DEVICE, msgType.getMsg(), Map.of(StandardName.DEVICE_ID, deviceInfoOp.get().getOriginId()), response);
        }
    }

    public void channelDelete(Long channelId, DeferredResult<CommonResponse<?>> response, MsgType msgType){
        Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelId);
        if (channelInfoOp.isEmpty()){
            response.setResult(CommonResponse.success(true));
        }else {
            ChannelInfo channelInfo = channelInfoOp.get();
            DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelId);
            customEvent(channelId, IdType.CHANNEL, msgType.getMsg(), Map.of(StandardName.DEVICE_ID, deviceInfo.getOriginId(), StandardName.CHANNEL_ID, channelInfo.getOriginId()), response);
        }
    }

}
