package com.runjian.alarm.controller;

import com.runjian.alarm.service.AlarmMsgSouthService;
import com.runjian.common.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Miracle
 * @date 2023/11/2 17:52
 */

@RestController
@RequestMapping("/alarm-manage-open")
@RequiredArgsConstructor
public class AlarmOpenController {

    private final AlarmMsgSouthService alarmMsgSouthService;

    /**
     * 上传告警数据
     * @param alarmMsgId 告警信息id
     * @param alarmDataType 告警数据类型
     * @param file 文件
     * @return
     */
    @PostMapping("/file/upload")
    public CommonResponse<?> uploadAlarmMsg(@RequestParam Long alarmMsgId, @RequestParam Integer alarmDataType, @RequestPart MultipartFile file){
        alarmMsgSouthService.saveAlarmFile(alarmMsgId, alarmDataType, file);
        return CommonResponse.success();
    }
}
