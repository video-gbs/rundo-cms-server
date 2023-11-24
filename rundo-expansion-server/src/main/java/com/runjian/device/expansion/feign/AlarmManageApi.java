package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AlarmManageApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.response.GetAlarmChannelRsp;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.response.GetAlarmDeployChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmMsgChannelRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/10/17 15:10
 */
@FeignClient(value = "alarm-manage",fallbackFactory= AlarmManageApiFallbackFactory.class)
public interface AlarmManageApi {

    /**
     * 分页获取告警信息
     * @param page 页码
     * @param num 每页数据
     * @param alarmDesc 告警描述
     * @param alarmStartTime 告警开始时间
     * @param alarmEndTime 告警结束时间
     * @return
     */
    @GetMapping("/msg/north/page")
    CommonResponse<PageListResp<GetAlarmMsgChannelRsp>> getAlarmMsgPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                        @RequestParam(required = false) Long channelId,
                                                                        @RequestParam(required = false) String alarmDesc,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmStartTime,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmEndTime,
                                                                        @RequestParam(required = false) List<Long> channelIds);

    /**
     * 获取预案下的设备布撤防状态
     * @param page 页码
     * @param num 每页数据
     * @param schemeId 告警预案id
     * @return
     */
    @GetMapping("/scheme/channel/deploy/page")
    CommonResponse<PageListResp<GetAlarmDeployChannelRsp>> getChannelDeploy(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, @RequestParam(required = false) Long schemeId);

    /**
     * 获取通道绑定的告警预案
     * @param channelIds 通道id数组
     * @return
     */
    @GetMapping("/scheme/channel")
    CommonResponse<List<GetAlarmChannelRsp>> getAlarmChannel(@RequestParam Set<Long> channelIds);
}
