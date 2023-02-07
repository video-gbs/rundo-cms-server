package com.runjian.device.expansion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;

/**
 * @author chenjialing
 */
public interface DeviceExpansionMapper extends BaseMapper<DeviceExpansion> {

    /**
     * 分页获取编码器信息列表
     * @param page
     * @param deviceExpansionListReq
     * @return
     */
    Page<DeviceExpansion> listPage(Page<DeviceExpansion> page, DeviceExpansionListReq deviceExpansionListReq);
}
