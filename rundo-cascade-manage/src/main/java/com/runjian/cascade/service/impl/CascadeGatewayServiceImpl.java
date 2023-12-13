package com.runjian.cascade.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.cascade.dao.CascadeGatewayMapper;
import com.runjian.cascade.entity.CascadeGatewayInfo;
import com.runjian.cascade.service.CascadeGatewayService;
import com.runjian.cascade.vo.response.GetCascadeGatewayRsp;
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
public class CascadeGatewayServiceImpl implements CascadeGatewayService {

    private final CascadeGatewayMapper cascadeGatewayMapper;

    @Override
    public PageInfo<GetCascadeGatewayRsp> getCascadeGateway(int page, int num) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(cascadeGatewayMapper.selectByPage());
    }

    @Override
    public void cascadeGatewayReport(Long gatewayId, String name, String ip, Integer port, String gbDomain, String gbCode, String username, String password) {
        Optional<CascadeGatewayInfo> cascadeGatewayInfoOp = cascadeGatewayMapper.selectById(gatewayId);
        CascadeGatewayInfo cascadeGatewayInfo = cascadeGatewayInfoOp.orElse(new CascadeGatewayInfo());
        LocalDateTime nowTime = LocalDateTime.now();
        cascadeGatewayInfo.setName(name);
        cascadeGatewayInfo.setIp(ip);
        cascadeGatewayInfo.setPort(port);
        cascadeGatewayInfo.setGbDomain(gbDomain);
        cascadeGatewayInfo.setGbCode(gbCode);
        cascadeGatewayInfo.setUsername(username);
        cascadeGatewayInfo.setPassword(password);
        cascadeGatewayInfo.setUpdateTime(nowTime);
        if (cascadeGatewayInfoOp.isEmpty()){
            cascadeGatewayInfo.setId(gatewayId);
            cascadeGatewayInfo.setCreateTime(nowTime);
            cascadeGatewayMapper.save(cascadeGatewayInfo);
        }else {
            cascadeGatewayMapper.update(cascadeGatewayInfo);
        }
    }
}
