package com.runjian.device.expansion.schedule;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.aspect.annotation.ChannelStatusPoint;
import com.runjian.device.expansion.aspect.annotation.DeviceStatusPoint;
import com.runjian.device.expansion.config.MessageSubMapConf;
import com.runjian.device.expansion.constant.SubMsgType;
import com.runjian.device.expansion.service.IMessageSubService;
import com.runjian.device.expansion.vo.feign.response.MessageSubRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 定时发送心跳
 * @author chenjialing
 */
@Component
@Slf4j
public class MessageSubSchedule {
    @Autowired
    MessageSubMapConf messageSubMapConf;

    @Autowired
    IMessageSubService messageSubService;
    //每1分钟执行一次
    @Scheduled(fixedRate=60000)
    public void subCheck(){
        if(ObjectUtils.isEmpty(messageSubMapConf.getMessageSubRspMap())){
            //进行缓存信息的初始化
            CommonResponse<List<MessageSubRsp>> listCommonResponse = messageSubService.subMsg();
            if(BusinessErrorEnums.SUCCESS.getErrCode().equals(listCommonResponse.getCode())){
                HashMap<String, MessageSubRsp> messageHashMap = new HashMap<>();

                for (MessageSubRsp messageSubRsp : listCommonResponse.getData()) {
                    messageHashMap.put(messageSubRsp.getMsgType(),messageSubRsp);
                }
                messageSubMapConf.setMessageSubRspMap(messageHashMap);
            }else {
                //服务调用失败
                log.info(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--redis消息订阅返回",listCommonResponse, null);
            }
        }
    }
}
