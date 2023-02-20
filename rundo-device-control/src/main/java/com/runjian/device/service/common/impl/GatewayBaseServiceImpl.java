package com.runjian.device.service.common.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.device.dao.GatewayMapper;
import com.runjian.device.service.common.GatewayBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/17 17:14
 */
@Slf4j
@Service
public class GatewayBaseServiceImpl implements GatewayBaseService {

    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    @PostConstruct
    public void systemStart() {
        // 将所有的网关设置为离线状态
        gatewayMapper.setAllOnlineState(CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }
}
