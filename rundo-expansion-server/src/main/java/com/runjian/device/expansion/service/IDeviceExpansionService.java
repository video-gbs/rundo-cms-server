package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.feign.response.DeviceAddResp;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
import com.runjian.device.expansion.vo.request.DeviceExpansionEditReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionListReq;
import com.runjian.device.expansion.vo.request.DeviceExpansionReq;
import com.runjian.device.expansion.vo.request.MoveReq;
import com.runjian.device.expansion.vo.response.DeviceExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;

import java.util.List;

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
    CommonResponse<DeviceAddResp> add(DeviceExpansionReq deviceExpansionReq);

    /**
     * 设备信息编辑
     * @param deviceExpansionEditReq
     * @return
     */
    CommonResponse<Long> edit(DeviceExpansionEditReq deviceExpansionEditReq,int kind);

    /**
     * 设备删除
     * @param id
     * @return
     */
    CommonResponse remove(Long id);

    /**
     * 设备删除
     * @param idList
     * @return
     */
    CommonResponse<Boolean> removeBatch(List<Long> idList);
    /**
     * 分页获取编码器
     * @param deviceExpansionListReq
     * @return
     */
    PageResp<DeviceExpansionResp> list(DeviceExpansionListReq deviceExpansionListReq);

    /**
     * 移动
     * @param deviceExpansionMoveReq
     * @return
     */
    Boolean move(MoveReq deviceExpansionMoveReq);

    /**
     * 查询安防通道绑定信息
     * @param areaId
     * @return
     */
    DeviceExpansion findOneDeviceByVideoAreaId(Long areaId);

    /**
     *
     */
    void syncDeviceStatus(String msgHandle,String msgLock);


    /**
     * 获取设备待注册列表
     * @param page
     * @param num
     * @param signState
     * @param deviceName
     * @param ip
     * @return
     */
    Object getDeviceByPage(int page, int num, Integer signState, String deviceName, String ip);

    /**
     * 安防通道
     * @param resourceKey
     * @return
     */
    CommonResponse<Object> videoAreaList(String resourceKey);
}
