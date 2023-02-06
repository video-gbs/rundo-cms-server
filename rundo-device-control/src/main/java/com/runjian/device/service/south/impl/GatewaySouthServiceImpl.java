package com.runjian.device.service.south.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.device.utils.CircleArray;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.south.GatewaySouthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * 网关南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
public class GatewaySouthServiceImpl implements GatewaySouthService {

    @Autowired
    private GatewayMapper gatewayMapper;

    /**
     * 心跳时钟
     */
    private volatile CircleArray<Long> heartbeatArray = new CircleArray<>(600);

    @PostConstruct
    public void init(){
        // 将所有的网关设置为离线状态
        gatewayMapper.setAllOnlineState(CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }

    /**
     * 定时任务-网关心跳处理
     */
    @Override
    @Scheduled(fixedRate = 1000)
    public void heartbeat() {
        Set<Long> gatewayIds = heartbeatArray.pullAndNext();
        if (gatewayIds.isEmpty()){
            return;
        }
        gatewayMapper.batchUpdateOnlineState(gatewayIds, CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }

    /**
     * 网关注册
     * @param gatewayInfo 网关信息
     */
    @Override
    public void signIn(GatewayInfo gatewayInfo, LocalDateTime outTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayInfo.getId());
        if (gatewayInfoOp.isEmpty()){
            gatewayMapper.save(gatewayInfo);
        }
        updateHeartbeat(gatewayInfo.getId(), outTime);
    }

    /**
     * 更新网关信息
     * @param gatewayInfo 网关信息
     */
    @Override
    public void update(GatewayInfo gatewayInfo) {
        Optional<GatewayInfo> gatewayInfoOptional = gatewayMapper.selectById(gatewayInfo.getId());
        if (gatewayInfoOptional.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关%s不存在", gatewayInfo.getId()));
        }
        gatewayMapper.update(gatewayInfo);
    }

    /**
     * 更新心跳信息
     * @param gatewayId 网关ID
     * @param outTime 过期时间
     */
    @Override
    public void updateHeartbeat(Long gatewayId, LocalDateTime outTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        // 判断两个时间的先后
        if (nowTime.isAfter(outTime)){
            return;
        }
        // 获取两个时间的相差秒数
        Duration dur = Duration.between(nowTime, outTime);
        long betweenSecond = dur.toSeconds();
        // 判断相差秒数是否大于心跳时钟的最大值
        if (betweenSecond > heartbeatArray.getCircleSize()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("网关过期时间过长，范围1~%s", heartbeatArray.getCircleSize()));
        }
        heartbeatArray.addOrUpdateTime(gatewayId, betweenSecond);
        gatewayMapper.updateOnlineState(gatewayId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
    }
}
