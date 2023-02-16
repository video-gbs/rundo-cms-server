package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.service.common.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/17 16:28
 */
@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Override
    public GatewayInfo getGatewayInfo(Long gatewayId) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayId);
        if (gatewayInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关%s不存在", gatewayId));
        }
        GatewayInfo gatewayInfo = gatewayInfoOp.get();
        return gatewayInfo;
    }

    @Override
    public DeviceInfo getDeviceInfo(Long deviceId) {
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectById(deviceId);
        if (deviceInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("设备%s不存在", deviceId));
        }
        DeviceInfo deviceInfo = deviceInfoOp.get();
        return deviceInfo;
    }

    @Override
    public ChannelInfo getChannelInfo(Long channelId) {
        Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(channelId);
        if (channelInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("通道%s不存在", channelId));
        }
        return channelInfoOp.get();
    }

    @Override
    public DispatchInfo getDispatchInfo(Long dispatchId) {
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectById(dispatchId);
        if (dispatchInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("流媒体服务%s不存在", dispatchId));
        }
        return dispatchInfoOp.get();
    }
}
