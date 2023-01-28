package com.runjian.parsing.service.impl;

import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.GatewayService;
import com.runjian.parsing.service.MessageService;
import com.runjian.parsing.service.TaskService;
import com.runjian.parsing.vo.CommonMqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private TaskService taskService;

    @Override
    public void msgDispatch(CommonMqDto<?> request) {

        if (request.getMsgType().equals(MsgType.DEVICE_SIGN_IN.getMsg())){


        }

    }
}
