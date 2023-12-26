package com.runjian.parsing.service.protocol.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.AlarmManageApi;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.AbstractSouthProtocol;
import com.runjian.parsing.service.protocol.SouthProtocol;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

/**
 * @author Miracle
 * @date 2023/4/14 15:06
 */
@Service
public class DefaultSouthProtocol extends AbstractSouthProtocol {

    public DefaultSouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi, AlarmManageApi alarmManageApi, RedissonClient redissonClient, DataSourceTransactionManager dataSourceTransactionManager, TransactionDefinition transactionDefinition) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi, alarmManageApi, redissonClient, dataSourceTransactionManager, transactionDefinition);
    }

    @Override
    public String getProtocolName() {
        return SouthProtocol.DEFAULT_PROTOCOL;
    }

    @Override
    protected JSONObject channelSyncConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceNodeSyncConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceNodeSubscribeConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceChannelSubscribeConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    protected JSONObject deviceSyncConvert(JSONObject jsonObject) {
        return jsonObject;
    }
}
