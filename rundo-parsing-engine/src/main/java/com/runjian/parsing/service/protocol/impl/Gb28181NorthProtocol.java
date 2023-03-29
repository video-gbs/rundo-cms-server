package com.runjian.parsing.service.protocol.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.service.common.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


/**
 * @author Miracle
 * @date 2023/1/17 14:39
 */
@Service
public class Gb28181NorthProtocol extends DefaultNorthProtocol {

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public String getProtocolName() {
        return "GB28181";
    }

    @Override
    public void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        switch (msgType){
            case DEVICE_ADD:
                deviceAdd(mainId, dataMap, response);
                return;
        }
        super.msgDistribute(msgType, mainId, idType, dataMap, response);
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
