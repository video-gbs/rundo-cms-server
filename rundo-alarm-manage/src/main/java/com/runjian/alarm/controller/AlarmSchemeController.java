package com.runjian.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.service.AlarmSchemeService;
import com.runjian.alarm.vo.request.PostAlarmSchemeReq;
import com.runjian.alarm.vo.request.PutAlarmSchemeDisabledReq;
import com.runjian.alarm.vo.request.PutAlarmSchemeReq;
import com.runjian.alarm.vo.request.PutChannelDeployReq;
import com.runjian.alarm.vo.response.GetAlarmChannelDeployRsp;
import com.runjian.alarm.vo.response.GetAlarmChannelRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemePageRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeRsp;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 告警预案接口
 * @author Miracle
 * @date 2023/9/19 10:42
 */
@RestController
@RequestMapping("/scheme")
@RequiredArgsConstructor
public class AlarmSchemeController {

    private final AlarmSchemeService alarmSchemeService;

    private final ValidatorService validatorService;

    /**
     * 分页查询
     * @param page 页码
     * @param num 每页数据
     * @param schemeName 预案名称
     * @param disabled 是否禁用
     * @param createStartTime 创建开始时间
     * @param createEndTime 创建结束时间
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    public CommonResponse<PageInfo<GetAlarmSchemePageRsp>> getAlarmSchemePage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int num,
                                                                              String schemeName, Integer disabled,
                                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createStartTime,
                                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createEndTime) {
        return CommonResponse.success(alarmSchemeService.getAlarmSchemeByPage(page, num, schemeName, disabled, createStartTime, createEndTime));
    }

    /**
     * 获取告警预案详情
     * @param id 告警预案id
     * @return
     */
    @GetMapping("/data")
    public CommonResponse<GetAlarmSchemeRsp> getAlarmSchemeRsp(@RequestParam Long id){
        return CommonResponse.success(alarmSchemeService.getAlarmScheme(id));
    }

    /**
     * 获取通道绑定的告警预案
     * @param channelIds 通道id数组
     * @return
     */
    @GetMapping("/channel")
    public CommonResponse<List<GetAlarmChannelRsp>> getAlarmChannel(@RequestParam Set<Long> channelIds){
        return CommonResponse.success(alarmSchemeService.getAlarmChannel(channelIds));
    }

    /**
     * 添加告警预案
     * @param req 告警预案添加请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addScheme(@RequestBody PostAlarmSchemeReq req) {
        validatorService.validateRequest(req);
        validatorService.validateRequest(req.getAlarmSchemeEventReqList());
        alarmSchemeService.addAlarmScheme(req.getSchemeName(), req.getTemplateId(), req.getChannelIds(), req.getAlarmSchemeEventRelList());
        return CommonResponse.success();
    }

    /**
     * 修改预案是否禁用
     * @param req 告警预案禁用请求体
     * @return
     */
    @PutMapping("/update/disabled")
    public CommonResponse<?> updateSchemeDisabled(@RequestBody PutAlarmSchemeDisabledReq req) {
        validatorService.validateRequest(req);
        alarmSchemeService.updateAlarmSchemeDisabled(req.getId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 修改告警预案
     * @param req 告警预案修改请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateScheme(@RequestBody PutAlarmSchemeReq req) {
        validatorService.validateRequest(req);
        validatorService.validateRequest(req.getAlarmSchemeEventReqList());
        alarmSchemeService.updateAlarmScheme(req.getId(), req.getSchemeName(), req.getTemplateId(), req.getChannelIds(), req.getAlarmSchemeEventRelList());
        return CommonResponse.success();
    }

    /**
     * 删除告警预案
     * @param id 告警预案id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteScheme(@RequestParam Long id) {
        alarmSchemeService.deleteAlarmScheme(id);
        return CommonResponse.success();
    }

    /**
     * 获取预案下的设备布撤防状态
     * @param page 页码
     * @param num 每页数据
     * @param schemeId 告警预案id
     * @return
     */
    @GetMapping("/channel/deploy/page")
    public CommonResponse<PageInfo<GetAlarmChannelDeployRsp>> getChannelDeploy(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, Long schemeId){
        return CommonResponse.success(alarmSchemeService.getAlarmChannelDeploy(page, num, schemeId));
    }

    /**
     * 通道布防
     * @param req 修改通道布防状态请求体
     * @return
     */
    @PutMapping("/channel/defense")
    public CommonResponse<?> updateChannelDeploy(PutChannelDeployReq req){
        validatorService.validateRequest(req);
        alarmSchemeService.defense(new ArrayList<>(req.getChannelIds()), true);
        return CommonResponse.success();
    }
}
