package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.StreamManageService;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miracle
 * @date 2023/2/9 15:33
 */
@Slf4j
@Service
public class StreamMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private StreamManageService streamManageService;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Value("${gateway.public.queue-id-set}")
    private String signInQueueId;

    private RabbitMqProperties.QueueData queueData;

    @PostConstruct
    public void init() {
        queueData = rabbitMqProperties.getQueueData(signInQueueId);
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            CommonMqDto<?> mqRequest = JSON.parseObject(new String(message.getBody()), CommonMqDto.class);
            Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectBySerialNum(mqRequest.getSerialNum());
            if (dispatchInfoOp.isEmpty()){
                CommonMqDto<StreamConvertDto> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                mqResponse.copyRequest(mqRequest);
                // 发送重新注册命令
                mqResponse.setMsgType(MsgType.DISPATCH_RE_SIGN_IN.getMsg());
                String mqId = UUID.randomUUID().toString().replace("-", "");
                rabbitMqSender.sendMsgByRoutingKey(queueData.getExchangeId(), queueData.getRoutingKey(), mqId, mqResponse, true);
            }
            StreamConvertDto streamConvertDto = JSONObject.parseObject(new String(message.getBody()), StreamConvertDto.class);

            if (mqRequest.getMsgType().equals(MsgType.STREAM_PLAY_RESULT.getMsg())){
                streamManageService.streamPlayResult(streamConvertDto.getStreamId(), streamConvertDto.getDataMap());
            } else if (mqRequest.getMsgType().equals(MsgType.STREAM_CLOSE.getMsg())) {

                streamManageService.streamClose(streamConvertDto.getStreamId());
            }

        } catch (Exception ex) {
            if (ex instanceof BusinessException){
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ流媒体消息处理服务", "处理失败", new String(message.getBody()), ex.getMessage());
            }else {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ流媒体消息处理服务", "处理失败", new String(message.getBody()), ex);
            }
        } finally {
            channel.basicAck(deliveryTag, true);
        }
    }
}
