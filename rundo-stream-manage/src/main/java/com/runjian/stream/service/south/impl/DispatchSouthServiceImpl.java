package com.runjian.stream.service.south.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;

import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.service.common.DispatchBaseService;
import com.runjian.stream.service.south.DispatchSouthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 调度服务南向处理器
 * @author Miracle
 * @date 2023/2/3 14:22
 */
@Service
@RequiredArgsConstructor
public class DispatchSouthServiceImpl implements DispatchSouthService {

    private final DispatchMapper dispatchMapper;

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
    public Boolean updateHeartbeat(Long dispatchId, LocalDateTime outTime) {
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectById(dispatchId);
        if (dispatchInfoOp.isEmpty()){
            return false;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        // 判断两个时间的先后
        if (nowTime.isAfter(outTime)){
            return true;
        }
        // 获取两个时间的相差秒数
        Duration dur = Duration.between(nowTime, outTime);
        long betweenSecond = dur.toSeconds();
        // 判断相差秒数是否大于心跳时钟的最大值
        if (betweenSecond > DispatchBaseService.HEARTBEAT_ARRAY.getCircleSize()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("网关过期时间过长，范围1~%s", DispatchBaseService.HEARTBEAT_ARRAY.getCircleSize()));
        }
        DispatchBaseService.HEARTBEAT_ARRAY.addOrUpdateTime(dispatchId, betweenSecond);
        dispatchMapper.updateOnlineState(dispatchId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
        return true;
    }
}
