package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.mq.config.RabbitMqConfig;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.constant.GatewayType;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.SignType;
import com.runjian.parsing.service.GatewayService;
import com.runjian.parsing.vo.GatewayMqDto;
import com.runjian.parsing.vo.request.GatewaySignInReq;
import com.runjian.parsing.vo.response.GatewaySignInRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * 公共消息监听器
 * @author Miracle
 * @date 2022/5/25 10:09
 */
@Slf4j
@Component
public class PublicMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private RabbitMqSender rabbitMqSender;

//    @Autowired
//    @Qualifier("dispatchMsgListenerContainer")
//    private SimpleMessageListenerContainer dispatchMsgListenerContainer;

    @Value("gateway.public.queue-id-set")
    private String signInQueueId;

    /**
     * todo 修改固定字符串, 待优化
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "网关注册信息监听器", "接收到网关注册信息，执行注册流程", message);
            GatewayMqDto<GatewaySignInReq> mqRequest = JSON.parseObject(new String(message.getBody()), GatewayMqDto.class);
            // 判断是否是注册信息
            if (mqRequest.getMsgType().equals(MsgType.SIGN_IN.getMsg())){
                GatewaySignInReq req = mqRequest.getData();
                // 进行网关信息存储并发送信息到上层平台
                GatewaySignInRsp gatewaySignInRsp = gatewayService.signIn(mqRequest.getSerialNum(), SignType.MQ.getCode(), GatewayType.getCodeByMsg(req.getGatewayType()), req.getProtocol(), req.getIp(), req.getPort());
                String key1 = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + gatewaySignInRsp.getGatewayId();
                String key2 = MqConstant.GATEWAY_PREFIX + MqConstant.SET_GET_PREFIX + gatewaySignInRsp.getGatewayId();

                RabbitMqProperties.QueueData queueData = rabbitMqProperties.getQueueData(signInQueueId);
                // 判断是否是第一次注册
                if (gatewaySignInRsp.getIsFirstSignIn()){
                    // 生成消息通讯队列
                    addQueue(key1, queueData.getExchangeId());
                    Queue queue = addQueue(key2, queueData.getExchangeId());
                    // 添加监听队列
//                    dispatchMsgListenerContainer.addQueues(queue);
                    gatewaySignInRsp.setMqGetQueue(key1);
                    gatewaySignInRsp.setMqSetQueue(key2);
                }
                GatewayMqDto<GatewaySignInRsp> mqResponse = (GatewayMqDto)CommonResponse.success(gatewaySignInRsp);
                mqResponse.copyRequest(mqRequest);
                // 发送消息到公共频道
                rabbitMqSender.sendMsgByRoutingKey(queueData.getExchangeId(), queueData.getRoutingKey(), UUID.randomUUID().toString().replace("-", ""), mqResponse, true);
            }
        }catch (Exception ex){
            // todo 补偿机制
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "网关注册信息监听器", "网关注册信息，处理失败", message, ex.getMessage());
        }finally {
            // todo 入库操作记录
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    private Queue addQueue(String key, String exchangeId) {
        Queue queue = new Queue(key, true, false, false);
        AbstractExchange exchange = rabbitMqProperties.getExchangeDataMap().get(exchangeId).getExchange();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(key).noargs();
        rabbitMqConfig.addQueue(queue, binding);
        return queue;
    }
}
