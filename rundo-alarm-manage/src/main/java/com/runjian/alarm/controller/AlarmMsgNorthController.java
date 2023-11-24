package com.runjian.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.service.AlarmEventHandleService;
import com.runjian.alarm.service.AlarmMsgNorthService;
import com.runjian.alarm.vo.request.PutRecoverAlarmMsgReq;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import com.runjian.alarm.vo.response.GetStreamInfoRsp;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
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

    private final AlarmEventHandleService alarmEventHandleService;

    private final ValidatorService validatorService;

    /**
     * 分页获取告警信息
     * @param page 页码
     * @param num 每页数据
     * @param alarmDesc 告警类型
     * @param alarmStartTime 告警开始时间
     * @param alarmEndTime 告警结束时间
     * @return
     */
    @GetMapping("/page")
    public CommonResponse<PageInfo<GetAlarmMsgRsp>> getAlarmMsgPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                    Long channelId,
                                                                    String alarmDesc,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmStartTime,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmEndTime,
                                                                    @RequestParam(required = false) List<Long> channelIds){
        return CommonResponse.success(alarmMsgNorthService.getAlarmMsgByPage(page, num, channelId, alarmDesc, alarmStartTime, alarmEndTime, channelIds));
    }

    /**
     * 删除告警信息
     * @param idList 告警信息id数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteAlarmMsg(@RequestParam List<Long> idList){
        alarmMsgNorthService.deleteAlarmMsg(idList);
        return CommonResponse.success();
    }

    /**
     * 进行直播观看
     * @param channelId 通道id
     * @return
     */
    @GetMapping("/live")
    public CommonResponse<GetStreamInfoRsp> getStreamInfo(@RequestParam Long channelId){
        return CommonResponse.success(alarmMsgNorthService.channelPlay(channelId));
    }

    /**
     * 告警恢复操作
     * @param req 告警恢复请求体
     * @return
     */
    @PutMapping("/recover")
    public CommonResponse<?> recoverAlarmMsg(@RequestBody PutRecoverAlarmMsgReq req){
        validatorService.validateRequest(req);
        alarmEventHandleService.recoverAlarmFileHandle(req.getAlarmMsgId(), req.getAlarmFileType());
        return CommonResponse.success();
    }


}
