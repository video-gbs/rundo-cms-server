package com.runjian.parsing.service.protocol.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.AbstractSouthProtocol;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import java.util.Objects;

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
        return "HIK-DEVICE_NET_SDK_V6";
    }


    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        if (Objects.isNull(deviceOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "设备原始id缺失");
        }
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        if (Objects.isNull(deviceOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "设备原始id缺失");
        }
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
        if (Objects.isNull(channelOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "通道原始id缺失");
        }
        jsonObject.put(StandardName.ORIGIN_ID, channelOriginId);
        int onlineState = jsonObject.getIntValue(CHANNEL_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        String channelName = jsonObject.getString(CHANNEL_NAME);
        jsonObject.put(StandardName.COM_NAME, channelName);
        return jsonObject;
    }
}
