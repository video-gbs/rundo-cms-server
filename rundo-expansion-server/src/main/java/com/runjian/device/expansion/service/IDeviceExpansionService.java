package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;

/**
 * 设备扩展服务service
 * @author chenjialing
 */
public interface IDeviceExpansionService extends IService<DeviceExpansion> {
    /**
     * 设备信息添加
     * @param deviceExpansionReq
     * @return
     */
    int add(DeviceExpansionReq deviceExpansionReq);

}