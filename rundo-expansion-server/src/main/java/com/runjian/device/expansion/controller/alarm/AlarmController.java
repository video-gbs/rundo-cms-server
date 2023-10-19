package com.runjian.device.expansion.controller.alarm;

import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.service.IAlarmService;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.response.GetAlarmDeployChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmMsgChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmSchemeChannelRsp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 告警扩展信息接口
 * @author Miracle
 * @date 2023/10/17 17:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

    private final IAlarmService alarmService;

    private final IDeviceChannelExpansionService deviceChannelExpansionService;

    @Value("${resourceKeys.channelKey:safety_channel}")
    String resourceKey;

    /**
     * 获取通道列表
     * @return
     */
    @GetMapping("/channel/tree")
    public CommonResponse<Object> videoAreaList(){
        return deviceChannelExpansionService.videoAreaList(resourceKey);
    }

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
    @GetMapping("/scheme/channel")
    @BlankStringValid
    public CommonResponse<PageResp<GetAlarmSchemeChannelRsp>> getAlarmSchemeChannel(int page, int num, Long videoAreaId, Integer includeEquipment, String channelName, String deviceName, Integer onlineState) {
        return CommonResponse.success(alarmService.getAlarmSchemeChannel(page, num, videoAreaId, includeEquipment, channelName, deviceName, onlineState));
    }

    /**
     * 获取预案下布防的通道
     * @param page 第几页
     * @param num 每页数据量
     * @param schemeId 预案id
     * @return
     */
    @GetMapping("/deploy/channel")
    public CommonResponse<PageListResp<GetAlarmDeployChannelRsp>> getAlarmDeployChannel(int page, int num, Long schemeId){
        return CommonResponse.success(alarmService.getAlarmDeployChannel(page, num, schemeId));
    }

    /**
     * 获取告警信息
     * @param page 第几页
     * @param num 每页数据量
     * @param eventCode 事件编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping("/msg/channel")
    public CommonResponse<PageListResp<GetAlarmMsgChannelRsp>> getAlarmMsgChannel(int page, int num, String eventCode, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        return CommonResponse.success(alarmService.getAlarmMsgChannel(page, num, eventCode, startTime, endTime));
    }


}
