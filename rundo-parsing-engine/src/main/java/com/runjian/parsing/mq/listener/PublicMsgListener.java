package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MsgType;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
@RequiredArgsConstructor
public class PublicMsgListener implements ChannelAwareMessageListener {

    private final GatewayService gatewayService;

    private final DispatchService dispatchService;

    private final RabbitMqConfig rabbitMqConfig;

    private final RabbitMqProperties rabbitMqProperties;

    private final RabbitMqSender rabbitMqSender;

    private final ValidatorService validatorService;

    private final MqDefaultProperties mqDefaultProperties;


    /**
     * 消息处理
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

            switch (MsgType.getByStr(mqRequest.getMsgType())){
                case GATEWAY_HEARTBEAT:
                    Long gatewayId = gatewayService.heartbeat(mqRequest.getSerialNum(), mqRequest.getData().toString());
                    CommonMqDto<?> gatewayResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                    gatewayResponse.copyRequest(mqRequest);
                    // 判断设备信息是否存在
                    if (Objects.isNull(gatewayId)) {
                        // 发送重新注册命令
                        gatewayResponse.setMsgType(MsgType.GATEWAY_RE_SIGN_IN.getMsg());
                        String mqId = UUID.randomUUID().toString().replace("-", "");
                        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, gatewayResponse, true);
                    }
                    return;
                case DISPATCH_HEARTBEAT:
                    Long dispatchId = dispatchService.heartbeat(mqRequest.getSerialNum(), mqRequest.getData().toString());
                    CommonMqDto<?> dispatchResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                    dispatchResponse.copyRequest(mqRequest);
                    // 判断设备信息是否存在
                    if (Objects.isNull(dispatchId)) {
                        // 发送重新注册命令
                        dispatchResponse.setMsgType(MsgType.DISPATCH_RE_SIGN_IN.getMsg());
                        String mqId = UUID.randomUUID().toString().replace("-", "");
                        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, dispatchResponse, true);
                    }
                    return;
                case GATEWAY_SIGN_IN:
                    log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "公共信息监听器", "接收到网关服务注册信息，执行注册流程", msgBody);
                    // 提取请求体信息
                    GatewaySignInReq gatewaySignInReq = JSONObject.parseObject(mqRequest.getData().toString(), GatewaySignInReq.class);
                    // 校验请求体
                    validatorService.validateRequest(gatewaySignInReq);
                    // 进行网关信息存储并发送信息到上层平台
                    SignInRsp gatewaySignInRsp = gatewayService.signIn(mqRequest.getSerialNum(), SignType.MQ.getCode(), GatewayType.getCodeByMsg(gatewaySignInReq.getGatewayType()), gatewaySignInReq.getProtocol(), gatewaySignInReq.getIp(), gatewaySignInReq.getPort(), gatewaySignInReq.getOutTime());
                    signIn(mqRequest, gatewaySignInRsp, ModuleType.GATEWAY);
                    return;
                case DISPATCH_SIGN_IN:
                    log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "公共信息监听器", "接收到调度服务注册信息，执行注册流程", msgBody);
                    DispatchSignInReq dispatchSignInReq = JSONObject.parseObject(mqRequest.getData().toString(), DispatchSignInReq.class);
                    validatorService.validateRequest(dispatchSignInReq);
                    SignInRsp dispatchSignInRsp = dispatchService.signIn(mqRequest.getSerialNum(), SignType.MQ.getCode(), dispatchSignInReq.getIp(), dispatchSignInReq.getPort(), dispatchSignInReq.getOutTime());
                    signIn(mqRequest, dispatchSignInRsp, ModuleType.STREAM);
                    return;
                default:
                    log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "公共消息监听器", "未知的消息类型", msgBody);
            }
        } catch (Exception ex) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "公共信息监听器", "公共信息处理失败", msgBody, ex);
            ex.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    private void signIn(CommonMqDto<?> mqRequest, SignInRsp signInRsp, ModuleType moduleType) {
        String key1;
        String key2;
        String exchangeId;
        SimpleMessageListenerContainer dispatch;
        switch (moduleType){
            case GATEWAY:
                key1 = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + signInRsp.getGatewayId();
                key2 = MqConstant.GATEWAY_PREFIX + MqConstant.SET_GET_PREFIX + signInRsp.getGatewayId();
                exchangeId = mqDefaultProperties.getGatewayExchangeId();
                dispatch = MqListenerConfig.containerMap.get(MqConstant.GATEWAY_PREFIX);
                break;
            case STREAM:
                key1 = MqConstant.STREAM_PREFIX + MqConstant.GET_SET_PREFIX + signInRsp.getDispatchId();
                key2 = MqConstant.STREAM_PREFIX + MqConstant.SET_GET_PREFIX + signInRsp.getDispatchId();
                exchangeId = mqDefaultProperties.getStreamExchangeId();
                dispatch = MqListenerConfig.containerMap.get(MqConstant.STREAM_PREFIX);
                break;
            default:
                return;
        }
        signInRsp.setSignType(SignType.MQ.getMsg());
        // 判断是否是第一次注册
        if (signInRsp.getIsFirstSignIn()) {
            // 生成消息通讯队列
            rabbitMqConfig.addQueue(exchangeId, key1, 15000);
            Queue queue = rabbitMqConfig.addQueue(exchangeId, key2, 15000);
            // 添加监听队列
            if (Objects.isNull(dispatch)) {
                throw new BusinessException(BusinessErrorEnums.MQ_CONTAINER_NOT_FOUND);
            }
            dispatch.addQueues(queue);
        }
        signInRsp.setMqExchange(rabbitMqProperties.getExchangeData(exchangeId).getExchange().getName());
        signInRsp.setMqGetQueue(key1);
        signInRsp.setMqSetQueue(key2);
        CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success(signInRsp));
        mqResponse.copyRequest(mqRequest);
        // 发送消息到公共频道
        String mqId = UUID.randomUUID().toString().replace("-", "");
        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
    }
}
