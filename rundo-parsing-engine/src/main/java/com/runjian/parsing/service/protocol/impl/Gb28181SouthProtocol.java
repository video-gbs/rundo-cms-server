package com.runjian.parsing.service.protocol.impl;


import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.GatewayType;
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
 * @date 2023/1/31 10:46
 */
@Service
public class Gb28181SouthProtocol extends AbstractSouthProtocol {

    private final static String CHANNEL_ID = "channelId";
    private final static String DEVICE_ID = "deviceId";
    private final static String IP = "address";
    private final static String DEVICE_ONLINE_STATE = "online";
    private final static String CHANNEL_ONLINE_STATE = "status";
    private final static String CHANNEL_NAME = "channelName";

    public Gb28181SouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi, AlarmManageApi alarmManageApi, RedissonClient redissonClient, DataSourceTransactionManager dataSourceTransactionManager, TransactionDefinition transactionDefinition) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi, alarmManageApi, redissonClient, dataSourceTransactionManager, transactionDefinition);
    }

    @Override
    public String getProtocolName() {
        return "GB28181";
    }



    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        return convertDeviceOnlineState(convertDeviceId(jsonObject));
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        return convertDeviceOnlineState(convertDeviceId(jsonObject));
    }

    @Override
    protected JSONObject deviceSyncConvert(JSONObject jsonObject) {
        return convertDeviceOnlineState(jsonObject);
    }

    @Override
    public JSONObject channelSyncConvert(JSONObject jsonObject) {
        jsonObject = convertChannelId(jsonObject);
        jsonObject.put(StandardName.CHANNEL_TYPE, GatewayType.OTHER.getCode());
        jsonObject.put(StandardName.COM_ONLINE_STATE, Integer.parseInt(jsonObject.remove(CHANNEL_ONLINE_STATE).toString()));
        jsonObject.put(StandardName.COM_NAME, jsonObject.remove(CHANNEL_NAME).toString());
        jsonObject.put(StandardName.COM_IP, jsonObject.remove(IP).toString());
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

    /**
     * 转换在线状态
     * @param jsonObject
     * @return
     */
    private JSONObject convertDeviceOnlineState(JSONObject jsonObject){
        int onlineState = Integer.parseInt(jsonObject.remove(DEVICE_ONLINE_STATE).toString());
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
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

    private JSONObject convertChannelId(JSONObject jsonObject){
        String channelOriginId = jsonObject.remove(CHANNEL_ID).toString();
        if (Objects.isNull(channelOriginId)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "设备原始id缺失");
        }
        jsonObject.put(StandardName.ORIGIN_ID, channelOriginId);
        return jsonObject;
    }
}
