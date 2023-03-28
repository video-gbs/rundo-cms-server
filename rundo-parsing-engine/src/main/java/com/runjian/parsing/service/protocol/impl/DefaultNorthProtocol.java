package com.runjian.parsing.service.protocol.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.protocol.NorthProtocol;
import com.runjian.parsing.vo.dto.GatewayConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.Map;
import java.util.Optional;

/**
 * 默认协议
 * @author Miracle
 * @date 2023/1/17 14:14
 */
@Service
public class DefaultNorthProtocol implements NorthProtocol {

    @Autowired
    private GatewayTaskService gatewayTaskService;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public String getProtocolName() {
        return DEFAULT_PROTOCOL;
    }

    @Override
    public void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        switch (msgType){
            case DEVICE_DELETE:
                deviceDelete(mainId, response);
                break;
            default:
                customEvent(mainId, idType, MsgType.DEVICE_DELETE.getMsg(), dataMap, response);
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

    public  void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            response.setResult(CommonResponse.success(true));
        } else {
            customEvent(deviceId, IdType.DEVICE, MsgType.DEVICE_DELETE.getMsg(), null, response);
        }
    }




}
