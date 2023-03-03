package com.runjian.device.service.common.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.device.constant.SignState;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.service.common.GatewayBaseService;
import com.runjian.device.service.common.RedisBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/2/17 17:14
 */
@Slf4j
@Service
public class GatewayBaseServiceImpl implements GatewayBaseService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private RedisBaseService redisBaseService;

    @Autowired
    private ParsingEngineApi parsingEngineApi;


    @Override
    @PostConstruct
    public void init() {
        // 将所有的网关设置为离线状态
        Set<Long> gatewayIds =  gatewayMapper.selectIdByOnlineState(CommonEnum.ENABLE.getCode());
        if (gatewayIds.size() > 0){
            heartbeatArray.addOrUpdateTime(gatewayIds, 60L);
        }
    }

    /**
     * 定时任务-网关心跳处理
     */
    @Override
    @Scheduled(fixedRate = 1000)
    public void heartbeat() {
        Set<Long> gatewayIds = heartbeatArray.pullAndNext();
        if (Objects.isNull(gatewayIds) || gatewayIds.isEmpty()){
            return;
        }
        gatewayOffline(gatewayIds);
    }

    @Override
    public void gatewayOffline(Set<Long> gatewayIds) {
        LocalDateTime nowTime = LocalDateTime.now();
        gatewayMapper.batchUpdateOnlineState(gatewayIds, CommonEnum.DISABLE.getCode(), nowTime);
        // 根据网关查询所有在线的设备
        List<DeviceInfo> deviceInfoList = deviceMapper.selectByGatewayIdsAndOnlineState(gatewayIds, CommonEnum.ENABLE.getCode());
        deviceInfoList.forEach(deviceInfo -> {
            deviceInfo.setOnlineState(CommonEnum.DISABLE.getCode());
            deviceInfo.setUpdateTime(nowTime);
        });
        // 修改设备的在线状态
        deviceMapper.batchUpdateOnlineState(deviceInfoList);
        // 根据设备查询所有在线的通道
        List<ChannelInfo> channelInfoList = channelMapper.selectByDeviceIdsAndOnlineState(deviceInfoList.stream().map(DeviceInfo::getId).collect(Collectors.toList()), CommonEnum.ENABLE.getCode());
        channelInfoList.forEach(channelInfo -> {
            channelInfo.setOnlineState(CommonEnum.DISABLE.getCode());
            channelInfo.setUpdateTime(nowTime);
        });

        // redis同步设备和通道的在线状态
        redisBaseService.batchUpdateDeviceOnlineState(deviceInfoList
                .stream()
                .filter(deviceInfo -> deviceInfo.getSignState().equals(SignState.SUCCESS.getCode()))
                .collect(Collectors.toMap(DeviceInfo::getId, DeviceInfo::getOnlineState)));
        // 修改通道的在线状态
        if (channelInfoList.size() > 0){
            channelMapper.batchUpdateOnlineState(channelInfoList);
            redisBaseService.batchUpdateChannelOnlineState(channelInfoList.stream()
                    .filter(channelInfo -> channelInfo.getSignState().equals(SignState.SUCCESS.getCode()))
                    .collect(Collectors.toMap(ChannelInfo::getId, ChannelInfo::getOnlineState)));
        }
    }

    @Override
    @Scheduled(fixedDelay = 600000)
    public void deviceTotalSync() {
        Set<Long> gatewayIds =  gatewayMapper.selectIdByOnlineState(CommonEnum.ENABLE.getCode());
        // 发送全量同步消息
        if (gatewayIds.size() > 0){
            parsingEngineApi.deviceTotalSync(gatewayIds);
        }
    }
}
