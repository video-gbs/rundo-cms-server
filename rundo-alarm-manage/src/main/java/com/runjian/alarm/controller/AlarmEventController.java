package com.runjian.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.service.AlarmEventService;
import com.runjian.alarm.vo.request.PostAlarmEventReq;
import com.runjian.alarm.vo.request.PutAlarmEventReq;
import com.runjian.alarm.vo.response.GetAlarmEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmEventRsp;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警事件接口
 * @author Miracle
 * @date 2023/9/19 15:19
 */
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class AlarmEventController {

    private final AlarmEventService alarmEventService;

    private final ValidatorService validatorService;


    /**
     * 分页获取事件
     * @param page      页码
     * @param num       每页数量
     * @param eventName 事件名称
     * @param eventCode 事件编码
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    public CommonResponse<PageInfo<GetAlarmEventRsp>> getAlarmEventPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") Integer num,
                                                                        String eventName, String eventCode) {
        return CommonResponse.success(alarmEventService.getAlarmEventByPage(page, num, eventName, eventCode));
    }

    /**
     * 获取事件名称
     * @param eventName 事件名称
     * @return
     */
    @GetMapping("/data")
    @BlankStringValid
    public CommonResponse<List<GetAlarmEventNameRsp>> getAlarmEventName() {
        return CommonResponse.success(alarmEventService.getAlarmEventName());
    }

    /**
     * 获取事件
     * @return
     */
    @GetMapping("/data/name")
    @BlankStringValid
    public CommonResponse<List<GetAlarmEventNameRsp>> getAlarmEvent(String eventName) {
        return CommonResponse.success(alarmEventService.getAlarmEvent(eventName));
    }

    /**
     * 添加事件
     * @param req 添加事件请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addAlarmEvent(@RequestBody PostAlarmEventReq req) {
        validatorService.validateRequest(req);
        alarmEventService.addAlarmEvent(req.getEventName(), req.getEventCode(), req.getEventSort(), req.getEventDesc());
        return CommonResponse.success();
    }

    /**
     * 修改事件
     * @param req 修改事件请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateAlarmEvent(@RequestBody PutAlarmEventReq req) {
        validatorService.validateRequest(req);
        alarmEventService.updateAlarmEvent(req.getId(), req.getEventName(), req.getEventSort(), req.getEventDesc());
        return CommonResponse.success();
    }

    /**
     * 删除告警事件
     * @param id 告警事件id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteAlarmEvent(@RequestParam Long id){
        alarmEventService.deleteAlarmEvent(id);
        return CommonResponse.success();
    }
}