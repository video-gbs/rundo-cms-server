package com.runjian.device.expansion.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.AlarmManageApiFallbackFactory;
import com.runjian.device.expansion.feign.fallback.StreamManageApiFallbackFactory;
import com.runjian.device.expansion.vo.response.GetAlarmDeployChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmMsgChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmSchemeChannelRsp;
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
     * @param alarmCode 告警类型
     * @param alarmStartTime 告警开始时间
     * @param alarmEndTime 告警结束时间
     * @return
     */
    @GetMapping("/msg/north/page")
    CommonResponse<PageInfo<GetAlarmMsgChannelRsp>> getAlarmMsgPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num,
                                                                    String alarmCode,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmStartTime,
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime alarmEndTime);

    /**
     * 获取预案下的设备布撤防状态
     * @param page 页码
     * @param num 每页数据
     * @param schemeId 告警预案id
     * @return
     */
    @GetMapping("/scheme/channel/deploy/page")
    CommonResponse<PageInfo<GetAlarmDeployChannelRsp>> getChannelDeploy(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int num, Long schemeId);

    /**
     * 获取通道绑定的告警预案
     * @param channelIds 通道id数组
     * @return
     */
    @GetMapping("/scheme/channel")
    CommonResponse<List<GetAlarmSchemeChannelRsp>> getAlarmChannel(@RequestParam Set<Long> channelIds);
}
