package com.runjian.parsing.service.impl;

import com.runjian.parsing.constant.SignType;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.feign.request.PostGatewaySignInReq;
import com.runjian.parsing.service.GatewayService;
import com.runjian.parsing.vo.response.GatewaySignInRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private DeviceControlApi deviceControlApi;

    @Override
    public GatewaySignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum();
        GatewaySignInRsp gatewaySignInRsp = new GatewaySignInRsp();
        if (gatewayInfoOp.isEmpty()){
            LocalDateTime nowTime = LocalDateTime.now();
            GatewayInfo gatewayInfo = new GatewayInfo();
            gatewayInfo.setSerialNum(serialNum);
            gatewayInfo.setSignType(signType);
            gatewayInfo.setIp(ip);
            gatewayInfo.setPort(port);
            gatewayInfo.setCreateTimel(nowTime);
            gatewayInfo.setUpdateTime(nowTime);
            gatewayMapper.save(gatewayInfo);
            gatewaySignInRsp.setIsFirstSignIn(true);
            gatewaySignInRsp.setGatewayId(gatewayInfo.getId());
            PostGatewaySignInReq req = new PostGatewaySignInReq(gatewayInfo);
            // todo 对请求失败做处理
            deviceControlApi.gatewaySignIn(req);
        }else {
            gatewaySignInRsp.setIsFirstSignIn(false);
            gatewaySignInRsp.setGatewayId(gatewayInfoOp.get().getId());
        }
        gatewaySignInRsp.setSignType(SignType.MQ.getMsg());
        return gatewaySignInRsp;
    }
}
