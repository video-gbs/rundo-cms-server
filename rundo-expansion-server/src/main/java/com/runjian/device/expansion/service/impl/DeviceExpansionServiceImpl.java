package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.mapper.DeviceExpansionMapper;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class DeviceExpansionServiceImpl extends ServiceImpl<DeviceExpansionMapper, DeviceExpansion> implements IDeviceExpansionService {
    @Autowired
    DeviceExpansionMapper deviceExpansionMapper;

    @Override
    public int add(DeviceExpansionReq deviceExpansionReq) {
        DeviceExpansion deviceExpansion = new DeviceExpansion();
        BeanUtil.copyProperties(deviceExpansionReq,deviceExpansion);
        return deviceExpansionMapper.insert(deviceExpansion);
    }

}
