package com.runjian.parsing.service.protocol.impl;

import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.AbstractNorthProtocol;
import org.springframework.stereotype.Service;


/**
 * @author Miracle
 * @date 2023/5/5 14:43
 */
@Service
public class DhDeviceNetSdkNorthProtocol extends AbstractNorthProtocol {

    public DhDeviceNetSdkNorthProtocol(GatewayTaskService gatewayTaskService, DataBaseService dataBaseService, DeviceMapper deviceMapper, ChannelMapper channelMapper) {
        super(gatewayTaskService, dataBaseService, deviceMapper, channelMapper);
    }

    @Override
    public String getProtocolName() {
        return "DH-DEVICE_NET_SDK_V6";
    }


}
