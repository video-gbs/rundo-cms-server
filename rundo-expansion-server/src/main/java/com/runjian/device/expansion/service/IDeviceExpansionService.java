package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
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
    CommonResponse<Long> add(DeviceExpansionReq deviceExpansionReq);

    /**
     * 设备信息编辑
     * @param deviceExpansionEditReq
     * @return
     */
    CommonResponse<Long> edit(DeviceExpansionEditReq deviceExpansionEditReq);

    /**
     * 设备删除
     * @param id
     * @return
     */
    CommonResponse remove(Long id);

}
