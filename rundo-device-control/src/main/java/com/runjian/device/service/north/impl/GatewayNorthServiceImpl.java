package com.runjian.device.service.north.impl;

import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.service.north.GatewayNorthService;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/6 17:21
 */
@Service
public class GatewayNorthServiceImpl implements GatewayNorthService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public List<GetGatewayNameRsp> getGatewayNameList() {
        return gatewayMapper.selectAllNameAndId();
    }
}
