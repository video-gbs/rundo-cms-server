package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.service.MessageService;
import com.runjian.parsing.vo.GatewayMqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DispatchMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // todo 消息分发

        log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "消息分配服务", "信息接收成功", message.toString());
        GatewayMqDto mqRequest = JSON.parseObject(new String(message.getBody()), GatewayMqDto.class);
        messageService.msgDispatch(mqRequest);
    }
}
