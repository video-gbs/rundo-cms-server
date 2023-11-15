package com.runjian.device.expansion.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.entity.DeviceExpansion;
import com.runjian.device.expansion.vo.feign.response.ChannelSyncRsp;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
import com.runjian.device.expansion.vo.feign.response.VideoRecordRsp;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备通道扩展服务service
 * @author chenjialing
 */
public interface IDeviceChannelExpansionService extends IService<DeviceChannelExpansion> {
    /**
     * 设备通道信息添加
     * @param findChannelListReq
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
    CommonResponse<Boolean> remove(Long id);

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
    PageResp<ChannelExpansionFindlistRsp> findList(int page, int num, String originName);

    /**
     * 移动
     * @param moveReq
     * @return
     */
    Boolean move(MoveReq moveReq);

    /**
     * 通道同步
     * @param deviceId
     * @return
     */
    CommonResponse<ChannelSyncRsp> channelSync(Long deviceId);

    /**
     * 查询安防通道关联的信息
     * @param areaId
     * @return
     */
    DeviceChannelExpansion findOneDeviceByVideoAreaId(Long areaId);

    /**
     * 通道状态同步
     */
    void syncChannelStatus(String msgHandle,String msgLock);

    /**
     * 分页获取编码器
     * @param videoAreaId
     * @return
     */
    List<DeviceChannelExpansionPlayResp> playList(Long videoAreaId);

    /**
     * 获取录像列表
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    CommonResponse<VideoRecordRsp> channelRecord(Long channelId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 安防通道列表获取
     * @param resourceKey
     * @return
     */
    CommonResponse<Object> videoAreaList(String resourceKey);
}
