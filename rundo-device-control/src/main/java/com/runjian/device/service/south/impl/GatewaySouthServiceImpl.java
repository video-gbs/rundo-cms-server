package com.runjian.device.service.south.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.south.GatewaySouthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.runjian.device.service.common.GatewayBaseService.heartbeatArray;

/**
 * 网关南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewaySouthServiceImpl implements GatewaySouthService {

    private final GatewayMapper gatewayMapper;

    /**
     * 网关注册
     * @param gatewayInfo 网关信息
     */
    @Override
    public void signIn(GatewayInfo gatewayInfo, LocalDateTime outTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayInfo.getId());
        if (gatewayInfoOp.isEmpty()){
            gatewayInfo.setName(gatewayInfo.getProtocol() + ":" + gatewayInfo.getId());
            gatewayMapper.save(gatewayInfo);
        } else {
            GatewayInfo oldGatewayInfo = gatewayInfoOp.get();
            oldGatewayInfo.setIp(gatewayInfo.getIp());
            oldGatewayInfo.setPort(gatewayInfo.getPort());
            oldGatewayInfo.setUpdateTime(gatewayInfo.getUpdateTime());
            update(oldGatewayInfo);
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
    public Boolean updateHeartbeat(Long gatewayId, LocalDateTime outTime) {
        Optional<GatewayInfo> gatewayInfoOptional = gatewayMapper.selectById(gatewayId);
        if (gatewayInfoOptional.isEmpty()){
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
        if (betweenSecond > heartbeatArray.getCircleSize()){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("网关过期时间过长，范围1~%s", heartbeatArray.getCircleSize()));
        }
        heartbeatArray.addOrUpdateTime(gatewayId, betweenSecond);
        gatewayMapper.updateOnlineState(gatewayId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
        return true;
    }
}
