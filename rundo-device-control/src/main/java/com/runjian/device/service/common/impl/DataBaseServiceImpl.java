package com.runjian.device.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;

import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.common.DataBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/17 16:28
 */
@Service
@RequiredArgsConstructor
public class DataBaseServiceImpl implements DataBaseService {

    private final GatewayMapper gatewayMapper;

    private final DeviceMapper deviceMapper;

    private final ChannelMapper channelMapper;

    @Override
    public GatewayInfo getGatewayInfo(Long gatewayId) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayId);
        if (gatewayInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关%s不存在", gatewayId));
        }
        return gatewayInfoOp.get();
    }

    @Override
    public DeviceInfo getDeviceInfo(Long deviceId) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("设备%s不存在", deviceId));
        }
        return deviceInfoOp.get();
    }

    @Override
    public ChannelInfo getChannelInfo(Long channelId) {
        Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelId);
        if (channelInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("通道%s不存在", channelId));
        }
        return channelInfoOp.get();
    }
}
