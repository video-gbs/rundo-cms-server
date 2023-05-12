package com.runjian.device.expansion.schedule;

import com.runjian.device.expansion.aspect.annotation.ChannelStatusPoint;
import com.runjian.device.expansion.aspect.annotation.DeviceStatusPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时发送心跳
 * @author chenjialing
 */
@Component
public class DeviceAndChannelStatusSchedule {


    //每5秒钟执行一次
    @Scheduled(cron="0/5 * * * * ?")
    @ChannelStatusPoint
    @DeviceStatusPoint
    public void sendMsg(){

    }
}
