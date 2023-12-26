package com.runjian.cascade.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.cascade.dao.GatewayMapper;
import com.runjian.cascade.entity.GatewayInfo;
import com.runjian.cascade.service.GatewayService;
import com.runjian.cascade.vo.response.GetGatewayRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/12/11 15:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayServiceImpl implements GatewayService {

    private final GatewayMapper gatewayMapper;

    @Override
    public PageInfo<GetGatewayRsp> getCascadeGateway(int page, int num) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(gatewayMapper.selectByPage());
    }

    @Override
    public void cascadeGatewayReport(Long gatewayId, String name, String ip, Integer port, String gbCode, String username, String password) {
        Optional<GatewayInfo> cascadeGatewayInfoOp = gatewayMapper.selectById(gatewayId);
        GatewayInfo gatewayInfo = cascadeGatewayInfoOp.orElse(new GatewayInfo());
        LocalDateTime nowTime = LocalDateTime.now();
        gatewayInfo.setName(name);
        gatewayInfo.setIp(ip);
        gatewayInfo.setPort(port);
        gatewayInfo.setGbCode(gbCode);
        gatewayInfo.setUsername(username);
        gatewayInfo.setPassword(password);
        gatewayInfo.setUpdateTime(nowTime);
        if (cascadeGatewayInfoOp.isEmpty()){
            gatewayInfo.setId(gatewayId);
            gatewayInfo.setCreateTime(nowTime);
            gatewayMapper.save(gatewayInfo);
        }else {
            gatewayMapper.update(gatewayInfo);
        }
    }
}
