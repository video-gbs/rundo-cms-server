package com.runjian.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.service.AlarmMsgNorthService;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import com.runjian.common.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 告警消息北向接口
 * @author Miracle
 * @date 2023/9/19 15:01
 */
@RestController
@RequestMapping("/msg/north")
@RequiredArgsConstructor
public class AlarmMsgNorthController {

    private final AlarmMsgNorthService alarmMsgNorthService;

    /**
     * 分页获取告警信息
     * @param page 页码
     * @param num 每页数据
     * @param alarmCode 告警类型
     * @param alarmStartTime 告警开始时间
     * @param alarmEndTime 告警结束时间
     * @return
     */
    @GetMapping("/page")
    public CommonResponse<PageInfo<GetAlarmMsgRsp>> getAlarmMsgPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                    String alarmCode,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmStartTime,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmEndTime){
        return CommonResponse.success(alarmMsgNorthService.getAlarmMsgByPage(page, num, alarmCode, alarmStartTime, alarmEndTime));
    }

    /**
     * 删除告警信息
     * @param idList 告警信息id数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteAlarmMsg(@RequestParam("ids") List<Long> idList){
        alarmMsgNorthService.deleteAlarmMsg(idList);
        return CommonResponse.success();
    }
}
