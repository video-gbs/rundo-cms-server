package com.runjian.parsing.service.protocol.impl;

import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.SouthProtocol;
import org.springframework.stereotype.Service;

/**
 * @author Miracle
 * @date 2023/4/14 15:06
 */
@Service
public class DefaultSouthProtocol extends AbstractSouthProtocol {
    public DefaultSouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi);
    }

    @Override
    public String getProtocolName() {
        return SouthProtocol.DEFAULT_PROTOCOL;
    }
}
