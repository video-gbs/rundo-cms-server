package com.runjian.parsing.service.protocol.impl;


import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import org.springframework.stereotype.Service;

/**
 * @author Miracle
 * @date 2023/1/31 10:46
 */
@Service
public class Gb28181SouthProtocol extends AbstractSouthProtocol {

    private final static String CHANNEL_ID = "channelId";
    private final static String DEVICE_ID = "deviceId";
    private final static String IP = "ipAddress";
    private final static String DEVICE_ONLINE_STATE = "online";
    private final static String CHANNEL_ONLINE_STATE = "status";
    private final static String CHANNEL_NAME = "channelName";

    public Gb28181SouthProtocol(GatewayTaskService gatewayTaskService, DeviceMapper deviceMapper, ChannelMapper channelMapper, DeviceControlApi deviceControlApi) {
        super(gatewayTaskService, deviceMapper, channelMapper, deviceControlApi);
    }

    @Override
    public String getProtocolName() {
        return "GB28181";
    }

    @Override
    public JSONObject channelSyncConvert(JSONObject jsonObject) {
        int onlineState = jsonObject.getIntValue(CHANNEL_ONLINE_STATE);
        String channelName = jsonObject.getString(CHANNEL_NAME);
        String channelIp = jsonObject.getString(IP);
        String channelOriginId = jsonObject.getString(CHANNEL_ID);
        jsonObject.put(StandardName.CHANNEL_TYPE, 5);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        jsonObject.put(StandardName.COM_NAME, channelName);
        jsonObject.put(StandardName.COM_IP, channelIp);
        jsonObject.put(StandardName.ORIGIN_ID, channelOriginId);
        return jsonObject;
    }

    @Override
    protected JSONObject deviceSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        return convertOnlineState(jsonObject);
    }

    @Override
    protected JSONObject deviceBatchSignInConvert(JSONObject jsonObject) {
        String deviceOriginId = jsonObject.getString(DEVICE_ID);
        jsonObject.put(StandardName.ORIGIN_ID, deviceOriginId);
        return convertOnlineState(jsonObject);
    }

    @Override
    protected JSONObject deviceSyncConvert(JSONObject jsonObject) {
        return convertOnlineState(jsonObject);
    }

    /**
     * 转换在线状态
     * @param jsonObject
     * @return
     */
    private JSONObject convertOnlineState(JSONObject jsonObject){
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }
}
