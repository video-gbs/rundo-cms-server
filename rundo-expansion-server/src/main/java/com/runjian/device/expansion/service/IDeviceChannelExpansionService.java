package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;

import java.util.List;

/**
 * 设备通道扩展服务service
 * @author chenjialing
 */
public interface IDeviceChannelExpansionService extends IService<DeviceChannelExpansion> {
    /**
     * 设备通道信息添加
     * @param channelIdList
     * @return
     */
    CommonResponse<Boolean> add(FindChannelListReq findChannelListReq);

    /**
     * 设备通道信息编辑
     * @param deviceChannelExpansionReq
     * @return
     */
    CommonResponse<Boolean> edit(DeviceChannelExpansionReq deviceChannelExpansionReq);

    /**
     * 设备通道删除
     * @param id
     * @return
     */
    CommonResponse remove(Long id);

    /**
     * 设备通道删除
     * @param idList
     * @return
     */
    CommonResponse<Boolean> removeBatch(List<Long> idList);
    /**
     * 分页获取编码器
     * @param deviceChannelExpansionListReq
     * @return
     */
    PageResp<DeviceChannelExpansionResp> list(DeviceChannelExpansionListReq deviceChannelExpansionListReq);

    /**
     * 分页获取通道信息
     * @param
     * @return
     */
    PageResp<DeviceChannelExpansionResp> findList();

    /**
     * 移动
     * @param moveReq
     * @return
     */
    Boolean move(MoveReq moveReq);

}
