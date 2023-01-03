package com.runjian.device.service.south.impl;

import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.service.south.GatewaySouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GatewaySouthServiceImpl implements GatewaySouthService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public void signIn(GatewayInfo gatewayInfo) {
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectById(gatewayInfo.getId());
        if (gatewayInfoOp.isEmpty()){
            gatewayMapper.save(gatewayInfo);
        }

    }

    @Override
    public void update(GatewayInfo gatewayInfo) {
        gatewayMapper.update(gatewayInfo);
    }
}
