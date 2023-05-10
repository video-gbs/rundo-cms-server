package com.runjian.parsing.service.protocol.impl;

import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import org.springframework.stereotype.Service;


/**
 * @author Miracle
 * @date 2023/5/5 14:43
 */
@Service
public class HikDeviceNetSdkNorthProtocol extends AbstractNorthProtocol{


    public HikDeviceNetSdkNorthProtocol(GatewayTaskService gatewayTaskService, DataBaseService dataBaseService, DeviceMapper deviceMapper) {
        super(gatewayTaskService, dataBaseService, deviceMapper);
    }


    @Override
    public String getProtocolName() {
        return " HIK_SDK-DEVICE_NET_V6";
    }


}
