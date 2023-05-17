package com.runjian.parsing.service.protocol.impl;

import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.AbstractNorthProtocol;
import com.runjian.parsing.service.protocol.SouthProtocol;
import org.springframework.stereotype.Service;

/**
 * @author Miracle
 * @date 2023/4/14 15:05
 */
@Service
public class DefaultNorthProtocol extends AbstractNorthProtocol {
    public DefaultNorthProtocol(GatewayTaskService gatewayTaskService, DataBaseService dataBaseService, DeviceMapper deviceMapper, ChannelMapper channelMapper) {
        super(gatewayTaskService, dataBaseService, deviceMapper, channelMapper);
    }

    @Override
    public String getProtocolName() {
        return SouthProtocol.DEFAULT_PROTOCOL;
    }
}
