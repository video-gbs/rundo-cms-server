package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;

import java.util.List;

/**
 * 设备通道扩展服务service
 * @author chenjialing
 */
public interface IDeviceChannelExpansionService extends IService<DeviceExpansion> {
    /**
     * 设备通道信息添加
     * @param deviceChannelExpansionReq
     * @return
     */
    CommonResponse<Long> add(DeviceChannelExpansionReq deviceChannelExpansionReq);

    /**
     * 设备通道信息编辑
     * @param deviceChannelExpansionReq
     * @return
     */
    CommonResponse<Long> edit(DeviceChannelExpansionReq deviceChannelExpansionReq);

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
     * @param deviceExpansionListReq
     * @return
     */
    List<DeviceExpansionResp> list(DeviceExpansionListReq deviceExpansionListReq);

    /**
     * 移动
     * @param deviceExpansionMoveReq
     * @return
     */
    Boolean move(DeviceExpansionMoveReq deviceExpansionMoveReq);

}
