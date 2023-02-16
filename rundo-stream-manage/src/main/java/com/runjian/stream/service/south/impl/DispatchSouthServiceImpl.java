package com.runjian.stream.service.south.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;

import com.runjian.common.utils.CircleArray;
import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.south.DispatchSouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 调度服务南向处理器
 * @author Miracle
 * @date 2023/2/3 14:22
 */
@Service
public class DispatchSouthServiceImpl implements DispatchSouthService {

    @Autowired
    private DispatchMapper dispatchMapper;

    /**
     * 心跳时钟
     */
    private static volatile CircleArray<Long> heartbeatArray = new CircleArray<>(600);

    @PostConstruct
    public void init(){
        dispatchMapper.setAllOnlineState(CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void heartbeat() {
        Set<Long> dispatchIds = heartbeatArray.pullAndNext();
        if (Objects.isNull(dispatchIds) || dispatchIds.isEmpty()){
            return;
        }
        dispatchMapper.batchUpdateOnlineState(dispatchIds, CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }



    @Override
    public void signIn(Long dispatchId, String serialNum, String ip, String port, LocalDateTime outTime) {
        Optional<DispatchInfo> gatewayInfoOp = dispatchMapper.selectById(dispatchId);
        DispatchInfo dispatchInfo = gatewayInfoOp.orElse(new DispatchInfo());
        LocalDateTime nowTime = LocalDateTime.now();
        dispatchInfo.setIp(ip);
        dispatchInfo.setPort(port);
        dispatchInfo.setUpdateTime(nowTime);
        dispatchInfo.setOnlineState(CommonEnum.ENABLE.getCode());
        if (gatewayInfoOp.isEmpty()){
            dispatchInfo.setId(dispatchId);
            dispatchInfo.setSerialNum(serialNum);
            dispatchInfo.setCreateTime(nowTime);
            dispatchInfo.setUrl("http://" + ip + ":" + port);
            dispatchInfo.setName(ip + MarkConstant.MARK_SPLIT_RAIL + port + MarkConstant.MARK_SPLIT_SEMICOLON + dispatchId);
            dispatchMapper.save(dispatchInfo);
        }else {
            dispatchMapper.update(dispatchInfo);
        }
        updateHeartbeat(dispatchInfo.getId(), outTime);
    }



    @Override
    public void updateHeartbeat(Long dispatchId, LocalDateTime outTime) {
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
        heartbeatArray.addOrUpdateTime(dispatchId, betweenSecond);
        dispatchMapper.updateOnlineState(dispatchId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
    }
}
