package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.constant.*;
import com.runjian.parsing.mq.config.RabbitMqConfig;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.south.DispatchService;
import com.runjian.parsing.service.south.GatewayService;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.request.DispatchSignInReq;
import com.runjian.parsing.vo.request.GatewaySignInReq;
import com.runjian.parsing.vo.response.SignInRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 公共消息监听器
 *
 * @author Miracle
 * @date 2022/5/25 10:09
 */
@Slf4j
@Service
public class PublicMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private MqDefaultProperties mqDefaultProperties;


    /**
     * 消息处理
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String msgBody = null;
        try {
            msgBody = new String(message.getBody());
            CommonMqDto<?> mqRequest = JSONObject.parseObject(msgBody, CommonMqDto.class);
            // 检测心跳信息是否过期
            if (mqRequest.getTime().plusMinutes(3).isBefore(LocalDateTime.now())) {
                log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "注册或心跳信息监听器", "过期的公共消息，进行丢失", msgBody);
                return;
            }

            // 判断是否是注册信息
            if (mqRequest.getMsgType().equals(MsgType.GATEWAY_HEARTBEAT.getMsg())) {
                Long gatewayId = gatewayService.heartbeat(mqRequest.getSerialNum(), mqRequest.getData().toString());
                CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                mqResponse.copyRequest(mqRequest);
                // 判断设备信息是否存在
                if (Objects.isNull(gatewayId)) {
                    // 发送重新注册命令
                    mqResponse.setMsgType(MsgType.GATEWAY_RE_SIGN_IN.getMsg());
                    String mqId = UUID.randomUUID().toString().replace("-", "");
                    rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
                }
            } else if (mqRequest.getMsgType().equals(MsgType.DISPATCH_HEARTBEAT.getMsg())) {
                Long dispatchId = dispatchService.heartbeat(mqRequest.getSerialNum(), mqRequest.getData().toString());
                CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                mqResponse.copyRequest(mqRequest);
                // 判断设备信息是否存在
                if (Objects.isNull(dispatchId)) {
                    // 发送重新注册命令
                    mqResponse.setMsgType(MsgType.DISPATCH_RE_SIGN_IN.getMsg());
                    String mqId = UUID.randomUUID().toString().replace("-", "");
                    rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
                }
            } else if (mqRequest.getMsgType().equals(MsgType.GATEWAY_SIGN_IN.getMsg())) {
                log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "公共信息监听器", "接收到调度服务注册信息，执行注册流程", msgBody);
                // 提取请求体信息
                GatewaySignInReq req = JSONObject.parseObject(mqRequest.getData().toString(), GatewaySignInReq.class);
                // 校验请求体
                validatorService.validateRequest(req);
                // 进行网关信息存储并发送信息到上层平台
                SignInRsp signInRsp = gatewayService.signIn(mqRequest.getSerialNum(), SignType.MQ.getCode(), GatewayType.getCodeByMsg(req.getGatewayType()), req.getProtocol(), req.getIp(), req.getPort(), req.getOutTime());
                signInRsp.setSignType(SignType.MQ.getMsg());
                String key1 = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + signInRsp.getGatewayId();
                String key2 = MqConstant.GATEWAY_PREFIX + MqConstant.SET_GET_PREFIX + signInRsp.getGatewayId();
                // 判断是否是第一次注册
                if (signInRsp.getIsFirstSignIn()) {
                    // 生成消息通讯队列
                    rabbitMqConfig.addQueue(mqDefaultProperties.getGatewayExchangeId(), key1, 15000);
                    Queue queue = rabbitMqConfig.addQueue(mqDefaultProperties.getGatewayExchangeId(), key2, 15000);
                    // 添加监听队列
                    SimpleMessageListenerContainer dispatch = MqListenerConfig.containerMap.get(MqConstant.GATEWAY_PREFIX);
                    if (Objects.isNull(dispatch)) {
                        throw new BusinessException(BusinessErrorEnums.MQ_CONTAINER_NOT_FOUND);
                    }
                    dispatch.addQueues(queue);
                }
                signInRsp.setMqExchange(rabbitMqProperties.getExchangeData(mqDefaultProperties.getGatewayExchangeId()).getExchange().getName());
                signInRsp.setMqGetQueue(key1);
                signInRsp.setMqSetQueue(key2);
                CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success(signInRsp));
                mqResponse.copyRequest(mqRequest);
                // 发送消息到公共频道
                String mqId = UUID.randomUUID().toString().replace("-", "");
                rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
            } else if (mqRequest.getMsgType().equals(MsgType.DISPATCH_SIGN_IN.getMsg())) {
                log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "公共信息监听器", "接收到调度服务注册信息，执行注册流程", msgBody);
                DispatchSignInReq req = JSONObject.parseObject(mqRequest.getData().toString(), DispatchSignInReq.class);
                validatorService.validateRequest(req);
                SignInRsp signInRsp = dispatchService.signIn(mqRequest.getSerialNum(), SignType.MQ.getCode(), req.getIp(), req.getPort(), req.getOutTime());
                signInRsp.setSignType(SignType.MQ.getMsg());
                String key1 = MqConstant.STREAM_PREFIX + MqConstant.GET_SET_PREFIX + signInRsp.getDispatchId();
                String key2 = MqConstant.STREAM_PREFIX + MqConstant.SET_GET_PREFIX + signInRsp.getDispatchId();
                // 判断是否是第一次注册
                if (signInRsp.getIsFirstSignIn()) {
                    // 生成消息通讯队列
                    rabbitMqConfig.addQueue(mqDefaultProperties.getStreamExchangeId(), key1, 15000);
                    Queue queue = rabbitMqConfig.addQueue(mqDefaultProperties.getStreamExchangeId(), key2, 15000);
                    // 添加监听队列
                    SimpleMessageListenerContainer dispatch = MqListenerConfig.containerMap.get(MqConstant.STREAM_PREFIX);
                    if (Objects.isNull(dispatch)) {
                        throw new BusinessException(BusinessErrorEnums.MQ_CONTAINER_NOT_FOUND);
                    }
                    dispatch.addQueues(queue);
                }
                signInRsp.setMqExchange(rabbitMqProperties.getExchangeData(mqDefaultProperties.getStreamExchangeId()).getExchange().getName());
                signInRsp.setMqGetQueue(key1);
                signInRsp.setMqSetQueue(key2);
                CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success(signInRsp));
                mqResponse.copyRequest(mqRequest);
                // 发送消息到公共频道
                String mqId = UUID.randomUUID().toString().replace("-", "");
                rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
            }
        } catch (Exception ex) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "公共信息监听器", "公共信息处理失败", msgBody, ex);
            ex.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
