package com.runjian.stream.service.common.impl;

import com.runjian.common.constant.CommonEnum;
import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.service.common.DispatchBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/22 11:07
 */
@Service
public class DispatchBaseServiceImpl implements DispatchBaseService {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Override
    @PostConstruct
    public void init(){
        Set<Long> gatewayIds =  dispatchMapper.selectIdByOnlineState(CommonEnum.ENABLE.getCode());
        HEARTBEAT_ARRAY.addOrUpdateTime(gatewayIds, 60L);
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void heartbeat() {
        Set<Long> dispatchIds = HEARTBEAT_ARRAY.pullAndNext();
        if (Objects.isNull(dispatchIds) || dispatchIds.isEmpty()){
            return;
        }
        dispatchMapper.batchUpdateOnlineState(dispatchIds, CommonEnum.DISABLE.getCode(), LocalDateTime.now());
    }
}
