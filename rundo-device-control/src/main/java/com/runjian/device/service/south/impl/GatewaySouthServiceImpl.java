package com.runjian.device.service.south.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.device.constant.CircleArray;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.south.GatewaySouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 网关南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Service
public class GatewaySouthServiceImpl implements GatewaySouthService {

    @Autowired
    private GatewayMapper gatewayMapper;

    /**
     * 心跳时钟
     */
    private volatile CircleArray<Long> heartbeatArray = new CircleArray(600);


    @Override
    public void heartbeat() {
        Set<Long> gatewayIds = heartbeatArray.next();
        if (Objects.isNull(gatewayIds) || gatewayIds.isEmpty()){
            return;
        }
        gatewayMapper.batchUpdateOnlineState(gatewayIds, CommonEnum.DISABLE.getCode());
    }

    @Override
    public void signIn(GatewayInfo gatewayInfo, LocalDateTime outTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayInfo.getId());
        if (gatewayInfoOp.isEmpty()){
            gatewayMapper.save(gatewayInfo);
        }
        updateHeartbeat(gatewayInfo.getId(), outTime);
    }

    @Override
    public void update(GatewayInfo gatewayInfo) {
        gatewayMapper.update(gatewayInfo);
    }

    @Override
    public void updateHeartbeat(Long gatewayId, LocalDateTime outTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isAfter(outTime)){
            return;
        }
        Duration dur = Duration.between(nowTime, outTime);
        long betweenSecond = dur.toSeconds();
        heartbeatArray.addOrUpdateTime(gatewayId, betweenSecond);
        gatewayMapper.updateOnlineState(gatewayId, CommonEnum.ENABLE.getCode());
    }
}
