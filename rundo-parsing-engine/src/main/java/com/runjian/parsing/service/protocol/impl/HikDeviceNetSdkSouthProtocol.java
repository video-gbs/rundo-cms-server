package com.runjian.parsing.service.protocol.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

/**
 * @author Miracle
 * @date 2023/5/5 14:56
 */
@Service
public class HikDeviceNetSdkSouthProtocol extends AbstractSouthProtocol {

    private final static String DEVICE_ID = "id";

    private final static String CHANNEL_ID = "id";

    private final static String DEVICE_ONLINE_STATE = "online";

    private final static String CHANNEL_ONLINE_STATE = "online";

    private final static String CHANNEL_NAME = "channelName";

    public HikDeviceNetSdkSouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi, RedissonClient redissonClient, DataSourceTransactionManager dataSourceTransactionManager, TransactionDefinition transactionDefinition) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi, redissonClient, dataSourceTransactionManager, transactionDefinition);
    }


    @Override
    public String getProtocolName() {
        return " HIK_SDK-DEVICE_NET_V6";
    }


    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    @Override
    protected JSONObject deviceSyncConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    @Override
    protected JSONObject channelSyncConvert(JSONObject jsonObject) {
        String channelOriginId = jsonObject.getString(CHANNEL_ID);
        jsonObject.put(StandardName.ORIGIN_ID, channelOriginId);
        int onlineState = jsonObject.getIntValue(CHANNEL_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        String channelName = jsonObject.getString(CHANNEL_NAME);
        jsonObject.put(StandardName.COM_NAME, channelName);
        return jsonObject;
    }
}
