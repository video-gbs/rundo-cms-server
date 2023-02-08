package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceChannelExpansionReq;
import com.runjian.device.expansion.vo.request.FindChannelListReq;
import com.runjian.device.expansion.vo.request.MoveReq;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;

import java.util.List;

/**
 * @author chenjialing
 */
public class IDeviceChannelExpansionServiceImpl extends ServiceImpl<DeviceChannelExpansionMapper, DeviceChannelExpansion> implements IDeviceChannelExpansionService {

    @Override
    public CommonResponse<Boolean> add(FindChannelListReq findChannelListReq) {
        //获取设备控制服务中的通道信息
        return null;
    }

    @Override
    public CommonResponse<Boolean> edit(DeviceChannelExpansionReq deviceChannelExpansionReq) {
        return null;
    }

    @Override
    public CommonResponse remove(Long id) {
        return null;
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        return null;
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> list(DeviceChannelExpansionListReq deviceChannelExpansionListReq) {
        return null;
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> findList() {
        return null;
    }

    @Override
    public Boolean move(MoveReq moveReq) {
        return null;
    }
}
