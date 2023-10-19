package com.runjian.device.expansion.service;

import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.response.GetAlarmDeployChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmMsgChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmSchemeChannelRsp;
import com.runjian.device.expansion.vo.response.PageResp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/10/17 11:07
 */
public interface IAlarmService {

    /**
     * 获取预案通道
     * @param page 第几页
     * @param num 每页数据量
     * @param videoAreaId 资源节点id
     * @param includeEquipment 是否包含下级组织
     * @param channelName 通道名称
     * @param deviceName 设备名称
     * @param onlineState 在线状态
     * @return
     */
    PageResp<GetAlarmSchemeChannelRsp> getAlarmSchemeChannel(int page, int num, Long videoAreaId, Integer includeEquipment, String channelName, String deviceName, Integer onlineState);

    /**
     * 获取预案下布防的通道
     * @param page 第几页
     * @param num 每页数据量
     * @param schemeId 预案id
     * @return
     */
    PageListResp<GetAlarmDeployChannelRsp> getAlarmDeployChannel(int page, int num, Long schemeId);

    /**
     * 获取告警信息
     * @param page 第几页
     * @param num 每页数据量
     * @param channelId 通道id
     * @param eventCode 事件编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    PageListResp<GetAlarmMsgChannelRsp> getAlarmMsgChannel(int page, int num, Long channelId, String eventCode, LocalDateTime startTime, LocalDateTime endTime);
}
