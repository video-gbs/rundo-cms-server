package com.runjian.device.expansion.service.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.config.MessageSubMapConf;
import com.runjian.device.expansion.constant.SubMsgType;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.service.IMessageSubService;
import com.runjian.device.expansion.service.IPtzService;
import com.runjian.device.expansion.vo.feign.request.FeignPtzControlReq;
import com.runjian.device.expansion.vo.feign.request.MessageSubReq;
import com.runjian.device.expansion.vo.feign.response.MessageSubRsp;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class MessageSubServiceImpl implements IMessageSubService {
    @Autowired
    DeviceControlApi deviceControlApi;


    private final String serviceName = "expansion-server";
    @Override
    public CommonResponse<List<MessageSubRsp>> subMsg() {
        //进行相关请求订阅
        HashSet<String> subSets = new HashSet<>();
        for (SubMsgType subMsgType : SubMsgType.values()) {
            //请求传输
            subSets.add(subMsgType.getName());
        }
        MessageSubReq messageSubReq = new MessageSubReq();
        messageSubReq.setServiceName(serviceName);
        messageSubReq.setMsgTypes(subSets);
        return deviceControlApi.subMsg(messageSubReq);
    }
}
