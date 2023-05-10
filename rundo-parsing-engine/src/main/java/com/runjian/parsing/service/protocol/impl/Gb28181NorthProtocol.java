package com.runjian.parsing.service.protocol.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.PtzType;
import com.runjian.common.constant.StandardName;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * @author Miracle
 * @date 2023/1/17 14:39
 */
@Service
public class Gb28181NorthProtocol extends AbstractNorthProtocol {

    private final DataBaseService dataBaseService;

    private final DeviceMapper deviceMapper;

    public Gb28181NorthProtocol(GatewayTaskService gatewayTaskService, DataBaseService dataBaseService, DeviceMapper deviceMapper) {
        super(gatewayTaskService, dataBaseService, deviceMapper);
        this.dataBaseService = dataBaseService;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public String getProtocolName() {
        return "GB28181";
    }

    @Override
    public void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        switch (msgType){
            case DEVICE_ADD:
                deviceAdd(mainId, dataMap, response);
            case CHANNEL_PTZ_CONTROL:
                channelPtzControl(mainId, dataMap, response);
                return;
        }
        super.msgDistribute(msgType, mainId, idType, dataMap, response);
    }

    private void channelPtzControl(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        Integer cmdCode = (Integer)dataMap.get(StandardName.PTZ_CMD_CODE);
        if (Objects.isNull(cmdCode)){
            response.setResult(CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "cmdCode不存在"));
            return;
        }
        switch (PtzType.getPtzType(cmdCode)){
            case MOVE_UP_LEFT:
            case MOVE_UP_RIGHT:
            case MOVE_DOWN_LEFT:
            case MOVE_DOWN_RIGHT:
                Integer cmdValue = (Integer)dataMap.get(StandardName.PTZ_CMD_VALUE);
                if (Objects.nonNull(cmdValue)){
                    dataMap.put(StandardName.PTZ_HORIZON_SPEED, cmdValue);
                    dataMap.put(StandardName.PTZ_VERTICAL_SPEED, cmdValue);
                }
        }
        super.customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_CONTROL.getMsg(), dataMap, response);
    }

    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        dataBaseService.getGatewayInfo(gatewayId);
        String originId = dataMap.get(StandardName.DEVICE_ID).toString();
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByGatewayIdAndOriginId(gatewayId, originId);
        if (deviceInfoOp.isPresent()){
            response.setResult(CommonResponse.success(deviceInfoOp.get().getId()));
            return;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        LocalDateTime nowTime = LocalDateTime.now();
        deviceInfo.setOriginId(originId);
        deviceInfo.setGatewayId(gatewayId);
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        deviceMapper.save(deviceInfo);
        response.setResult(CommonResponse.success(deviceInfo.getId()));
    }
}
