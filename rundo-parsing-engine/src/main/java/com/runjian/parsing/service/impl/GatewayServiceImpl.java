package com.runjian.parsing.service.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.constant.SignType;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.feign.request.PostGatewaySignInReq;
import com.runjian.parsing.service.GatewayService;
import com.runjian.parsing.vo.request.PutGatewayHeartbeatReq;
import com.runjian.parsing.vo.response.GatewayHeartbeatRsp;
import com.runjian.parsing.vo.response.GatewaySignInRsp;
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

    @Override
    public GatewaySignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port, String outTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(serialNum);
        GatewaySignInRsp gatewaySignInRsp = new GatewaySignInRsp();
        GatewayInfo gatewayInfo;
        if (gatewayInfoOp.isEmpty()){
            LocalDateTime nowTime = LocalDateTime.now();
            gatewayInfo = new GatewayInfo();
            gatewayInfo.setSerialNum(serialNum);
            gatewayInfo.setSignType(signType);
            gatewayInfo.setGatewayType(gatewayType);
            gatewayInfo.setProtocol(protocol);
            gatewayInfo.setIp(ip);
            gatewayInfo.setPort(port);
            gatewayInfo.setCreateTime(nowTime);
            gatewayInfo.setUpdateTime(nowTime);
            gatewayMapper.save(gatewayInfo);
            gatewaySignInRsp.setIsFirstSignIn(true);
            gatewaySignInRsp.setGatewayId(gatewayInfo.getId());
        }else {
            gatewayInfo = gatewayInfoOp.get();
            gatewaySignInRsp.setIsFirstSignIn(false);
            gatewaySignInRsp.setGatewayId(gatewayInfoOp.get().getId());
        }
        // todo 对请求失败做处理
        PostGatewaySignInReq req = new PostGatewaySignInReq(gatewayInfo, Instant.ofEpochMilli(Long.parseLong(outTime)).atZone(ZoneId.systemDefault()).toLocalDateTime());
        CommonResponse response = deviceControlApi.gatewaySignIn(req);
        if (response.getCode() == 0){
            log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "网关注册服务", "网关注册成功", gatewayInfo.getId());
        }
        gatewaySignInRsp.setSignType(SignType.MQ.getMsg());
        return gatewaySignInRsp;
    }

    @Override
    public Long heartbeat(String serialNum, LocalDateTime heartbeatTime) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(serialNum);
        if (gatewayInfoOp.isEmpty()){
            return null;
        }
        PutGatewayHeartbeatReq putGatewayHeartbeatReq = new PutGatewayHeartbeatReq();
        putGatewayHeartbeatReq.setHeartbeatTime(heartbeatTime);
        putGatewayHeartbeatReq.setSerialNum(serialNum);
        CommonResponse response = deviceControlApi.gatewayHeartbeat(putGatewayHeartbeatReq);
        // todo 判断上层是否有数据，如果没有重新上传数据到上层平台
        return gatewayInfoOp.get().getId();
    }
}
