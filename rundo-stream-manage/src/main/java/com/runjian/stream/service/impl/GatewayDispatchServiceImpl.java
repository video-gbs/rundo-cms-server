package com.runjian.stream.service.impl;

import com.runjian.stream.dao.GatewayDispatchMapper;
import com.runjian.stream.entity.GatewayDispatchInfo;
import com.runjian.stream.service.GatewayDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/3 15:13
 */
@Service
public class GatewayDispatchServiceImpl implements GatewayDispatchService {

    @Autowired
    private GatewayDispatchMapper gatewayDispatchMapper;

    @Override
    public void Binding(Long gatewayId, Set<Long> dispatchIds) {
        GatewayDispatchInfo gatewayDispatchInfo = new GatewayDispatchInfo();
        List<GatewayDispatchInfo> gatewayDispatchInfoList = new ArrayList<>(dispatchIds.size());
        LocalDateTime nowTime = LocalDateTime.now();
        for (Long dispatchId : dispatchIds){
            gatewayDispatchInfo.setGatewayId(gatewayId);
            gatewayDispatchInfo.setDispatchId(dispatchId);
            gatewayDispatchInfo.setCreateTime(nowTime);
            gatewayDispatchInfo.setUpdateTime(nowTime);
            gatewayDispatchInfoList.add(gatewayDispatchInfo);
        }
        gatewayDispatchMapper.saveAll(gatewayDispatchInfoList);



    }
}
