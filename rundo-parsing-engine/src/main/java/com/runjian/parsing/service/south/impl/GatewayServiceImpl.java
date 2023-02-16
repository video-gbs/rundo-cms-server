package com.runjian.parsing.service.south.impl;

import com.alibaba.druid.util.StringUtils;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.vo.request.PostGatewaySignInReq;
import com.runjian.parsing.service.south.GatewayService;
import com.runjian.parsing.vo.request.PutGatewayHeartbeatReq;
import com.runjian.parsing.vo.response.SignInRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private DeviceControlApi deviceControlApi;



    /**
     * 网关注册
     * @param serialNum 序列号
     * @param signType 注册类型
     * @param gatewayType 网关类型
     * @param protocol 协议
     * @param ip ip地址
     * @param port 端口
     * @param outTime 心跳过期时间
     * @return
     */
    @Override
    public SignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port, String outTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(serialNum);
        SignInRsp signInRsp = new SignInRsp();
        GatewayInfo gatewayInfo = gatewayInfoOp.orElse(new GatewayInfo());
        if (gatewayInfoOp.isEmpty()){
            LocalDateTime nowTime = LocalDateTime.now();
            gatewayInfo.setSerialNum(serialNum);
            gatewayInfo.setSignType(signType);
            gatewayInfo.setGatewayType(gatewayType);
            gatewayInfo.setProtocol(protocol);
            gatewayInfo.setIp(ip);
            gatewayInfo.setPort(port);
            gatewayInfo.setCreateTime(nowTime);
            gatewayInfo.setUpdateTime(nowTime);
            gatewayMapper.save(gatewayInfo);
            signInRsp.setIsFirstSignIn(true);
        }else {
            signInRsp.setIsFirstSignIn(false);
        }
        signInRsp.setGatewayId(gatewayInfo.getId());
        // todo 对请求失败做处理
        PostGatewaySignInReq req = new PostGatewaySignInReq(gatewayInfo, Instant.ofEpochMilli(Long.parseLong(outTime)).atZone(ZoneId.systemDefault()).toLocalDateTime());
        CommonResponse<?> response = deviceControlApi.gatewaySignIn(req);
        if (response.getCode() == 0){
            log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "网关注册服务", "网关注册成功", gatewayInfo.getId());
        }
        return signInRsp;
    }

    /**
     * 心跳
     * @param serialNum 网关序列号
     * @param heartbeatTime 心跳过期时间
     * @return 网关id
     */
    @Override
    public Long heartbeat(String serialNum, String heartbeatTime) {
        if (!StringUtils.isNumber(heartbeatTime) || System.currentTimeMillis() >= Long.parseLong(heartbeatTime)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("非法时间戳:%s", heartbeatTime));
        }
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(serialNum);
        if (gatewayInfoOp.isEmpty()){
            return null;
        }
        PutGatewayHeartbeatReq putGatewayHeartbeatReq = new PutGatewayHeartbeatReq();
        putGatewayHeartbeatReq.setOutTime(Instant.ofEpochMilli(Long.parseLong(heartbeatTime)).atZone(ZoneId.systemDefault()).toLocalDateTime());
        putGatewayHeartbeatReq.setGatewayId(gatewayInfoOp.get().getId());
        CommonResponse<?> response = deviceControlApi.gatewayHeartbeat(putGatewayHeartbeatReq);
        if (response.getCode() != 0){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return gatewayInfoOp.get().getId();
    }
}
