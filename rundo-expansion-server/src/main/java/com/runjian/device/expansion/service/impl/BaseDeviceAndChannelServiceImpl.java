package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IBaseDeviceAndChannelService;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class BaseDeviceAndChannelServiceImpl implements IBaseDeviceAndChannelService {

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    @Autowired
    DeviceExpansionMapper deviceExpansionMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeDevice(Long id) {
        deviceExpansionMapper.deleteById(id);
        //删除对应的通道
        LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
        deviceChannelExpansionMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public void removeDeviceSoft(Long id) {

        DeviceExpansion deviceExpansion = new DeviceExpansion();
        deviceExpansion.setId(id);
        deviceExpansion.setDeleted(1);

        deviceExpansionMapper.updateById(deviceExpansion);
        //删除对应的通道
        LambdaQueryWrapper<DeviceChannelExpansion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceChannelExpansion::getDeviceExpansionId,id);
        deviceChannelExpansionMapper.delete(lambdaQueryWrapper);
    }
}
