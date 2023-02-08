package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.service.IDeviceChannelExpansionService;
import com.runjian.device.expansion.vo.request.*;
import com.runjian.device.expansion.vo.response.DeviceChannelExpansionResp;
import com.runjian.device.expansion.vo.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class IDeviceChannelExpansionServiceImpl extends ServiceImpl<DeviceChannelExpansionMapper, DeviceChannelExpansion> implements IDeviceChannelExpansionService {
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    @Override
    public CommonResponse<Boolean> add(FindChannelListReq findChannelListReq) {
        //进行添加
        DeviceChannelExpansion deviceChannelExpansion = new DeviceChannelExpansion();
        List<DeviceChannelExpansion> channelList = new ArrayList<>();
        ArrayList<Long> Ids = new ArrayList<>();
        for (DeviceChannelExpansionAddReq deviceChannelExpansionAddReq : findChannelListReq.getChannelList()){
            deviceChannelExpansion.setId(deviceChannelExpansionAddReq.getId());
            deviceChannelExpansion.setDeviceExpansionId(deviceChannelExpansionAddReq.getDeviceExpansionId());
            deviceChannelExpansion.setChannelName(deviceChannelExpansionAddReq.getChannelName());
            deviceChannelExpansion.setChannelCode(deviceChannelExpansionAddReq.getChannelCode());
            deviceChannelExpansion.setOnlineState(deviceChannelExpansionAddReq.getOnlineState());
            deviceChannelExpansion.setVideoAreaId(findChannelListReq.getVideoAreaId());
            channelList.add(deviceChannelExpansion);
            Ids.add(deviceChannelExpansionAddReq.getId());
        }
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        this.saveBatch(channelList);
        //通知控制服务修改添加状态


        return null;
    }

    @Override
    public CommonResponse<Boolean> edit(DeviceChannelExpansionReq deviceChannelExpansionReq) {
        return null;
    }

    @Override
    public CommonResponse remove(Long id) {
        return null;
    }

    @Override
    public CommonResponse<Boolean> removeBatch(List<Long> idList) {
        return null;
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> list(DeviceChannelExpansionListReq deviceChannelExpansionListReq) {
        return null;
    }

    @Override
    public PageResp<DeviceChannelExpansionResp> findList() {
        return null;
    }

    @Override
    public Boolean move(MoveReq moveReq) {
        return null;
    }
}
