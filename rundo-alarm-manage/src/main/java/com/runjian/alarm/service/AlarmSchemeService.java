package com.runjian.alarm.service;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.vo.response.GetAlarmChannelRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemePageRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeRsp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/11 10:44
 */
public interface AlarmSchemeService {

    /**
     * 分页获取告警预案
     * @param page 页数
     * @param num 每页数据
     * @param schemeName 预案名称
     * @param disabled 是否禁用
     * @param createStartTime 创建开始时间
     * @param createEndTime 创建结束时间
     * @return
     */
    PageInfo<GetAlarmSchemePageRsp> getAlarmSchemeByPage(int page, int num, String schemeName, Integer disabled, LocalDateTime createStartTime, LocalDateTime createEndTime);

    /**
     * 获取告警预案
     * @param id 主键id
     * @return
     */
    GetAlarmSchemeRsp getAlarmScheme(Long id);

    /**
     * 获取通道绑定预案信息
     * @param channelIds 通道id数组
     * @return
     */
    List<GetAlarmChannelRsp> getAlarmChannel(Set<Long> channelIds);

    /**
     * 添加预案
     * @param schemeName 预案名称
     * @param templateId 时间模板名称
     * @param channelIds 通道id数组
     * @param alarmSchemeEventRelList 告警事件数组
     */
    void addAlarmScheme(String schemeName, Long templateId, Set<Long> channelIds, List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    /**
     * 修改预案的是否启用
     * @param id 预案id
     * @param disabled 是否启用
     */
    void updateAlarmSchemeDisabled(Long id, Integer disabled);

    /**
     * 修改预案
     * @param id 预案id
     * @param schemeName 预案名称
     * @param templateId 时间模板名称
     * @param channelIds 通道id数组
     * @param alarmSchemeEventRelList 告警事件数组
     */
    void updateAlarmScheme(Long id, String schemeName, Long templateId, Set<Long> channelIds, List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    /**
     * 删除告警预案
     * @param id 预案id
     */
    void deleteAlarmScheme(Long id);
}
