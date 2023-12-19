package com.runjian.parsing.service.protocol.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.AlarmManageApi;
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

    private final static String DEVICE_ID = "deviceId";

    private final static String CHANNEL_ID = "channelId";

    private final static String DEVICE_ONLINE_STATE = "online";

    private final static String CHANNEL_ONLINE_STATE = "online";

    private final static String CHANNEL_NAME = "channelName";

    public HikDeviceNetSdkSouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi, AlarmManageApi alarmManageApi, RedissonClient redissonClient, DataSourceTransactionManager dataSourceTransactionManager, TransactionDefinition transactionDefinition) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi, alarmManageApi, redissonClient, dataSourceTransactionManager, transactionDefinition);
    }


    @Override
    public String getProtocolName() {
        return "HIK-DEVICE_NET_SDK_V6";
    }


    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        return convertDeviceOnline(convertDeviceId(jsonObject));
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        return convertDeviceOnline(convertDeviceId(jsonObject));
    }

    @Override
    protected JSONObject deviceSyncConvert(JSONObject jsonObject) {
        return convertDeviceOnline(convertDeviceId(jsonObject));
    }

    @Override
    protected JSONObject channelSyncConvert(JSONObject jsonObject) {
        return convertChannelName(convertChannelOnline(convertChannelId(jsonObject)));
    }

    private JSONObject convertChannelOnline(JSONObject jsonObject){
        int onlineState = Integer.parseInt(jsonObject.remove(CHANNEL_ONLINE_STATE).toString());
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    private JSONObject convertDeviceOnline(JSONObject jsonObject){
        int onlineState = Integer.parseInt(jsonObject.remove(DEVICE_ONLINE_STATE).toString());
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }

    private JSONObject convertChannelId(JSONObject jsonObject){
        String channelOriginId = jsonObject.remove(CHANNEL_ID).toString();
        if (Objects.isNull(channelOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "通道原始id缺失");
        }
        jsonObject.put(StandardName.ORIGIN_ID, channelOriginId);
        return jsonObject;
    }

    private JSONObject convertDeviceId(JSONObject jsonObject){
        String deviceOriginId = jsonObject.remove(DEVICE_ID).toString();
        if (Objects.isNull(deviceOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "设备原始id缺失");
        }
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        return jsonObject;
    }

    private JSONObject convertChannelName(JSONObject jsonObject){
        String channelName = jsonObject.remove(CHANNEL_NAME).toString();
        jsonObject.put(StandardName.COM_NAME, channelName);
        return jsonObject;
    }
}
