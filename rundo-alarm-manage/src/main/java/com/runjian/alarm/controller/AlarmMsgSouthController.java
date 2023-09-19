package com.runjian.alarm.controller;

import com.runjian.alarm.service.AlarmMsgSouthService;
import com.runjian.alarm.vo.request.PostReceiveAlarmMsgReq;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Miracle
 * @date 2023/9/19 14:39
 */
@RestController
@RequestMapping("/msg/south")
@RequiredArgsConstructor
public class AlarmMsgSouthController {

    private final ValidatorService validatorService;

    private final AlarmMsgSouthService alarmMsgSouthService;

    /**
     * 接收告警信息
     * @param req 告警信息请求体
     * @return
     */
    @PostMapping("/receive")
    public CommonResponse<?> receiveAlarmMsg(@RequestBody PostReceiveAlarmMsgReq req){
        validatorService.validateRequest(req);
        alarmMsgSouthService.receiveAlarmMsg(req.getChannelId(), req.getEventCode(), req.getEventMsgType(), req.getEventDesc(), req.getEventTime());
        return CommonResponse.success();
    }

    /**
     * 上传告警数据
     * @param alarmMsgId 告警信息id
     * @param alarmDataType 告警数据类型
     * @param file 文件
     * @return
     */
    @PostMapping("/upload")
    public CommonResponse<?> uploadAlarmMsg(@RequestParam Long alarmMsgId, @RequestParam Integer alarmDataType, @RequestPart MultipartFile file){
        alarmMsgSouthService.saveAlarmFile(alarmMsgId, alarmDataType, file);
        return CommonResponse.success();
    }

}
