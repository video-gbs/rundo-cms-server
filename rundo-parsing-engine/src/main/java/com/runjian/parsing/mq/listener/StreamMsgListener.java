package com.runjian.parsing.mq.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.south.StreamSouthService;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private StreamSouthService streamSouthService;

    @Autowired
    private MqDefaultProperties mqDefaultProperties;


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
                rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
                return;
            }
            DispatchInfo dispatchInfo = dispatchInfoOp.get();

            if (!StringUtils.isNumber(mqRequest.getMsgId())){
                if (mqRequest.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ流媒体消息处理服务", "流媒体异常消息记录", mqRequest.getMsgType(), mqRequest.getMsg());
                }else if (mqRequest.getMsgType().equals(MsgType.STREAM_PLAY_RESULT.getMsg())){
                    streamSouthService.streamPlayResult(dispatchInfo.getId(), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_CLOSE.getMsg())) {
                    streamSouthService.streamClose(dispatchInfo.getId(), mqRequest.getData());
                }
            }else {
                if (mqRequest.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                    streamSouthService.errorEvent(Long.parseLong(mqRequest.getMsgId()), mqRequest);
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_PLAY_STOP.getMsg())) {
                    streamSouthService.streamStopPlay(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_RECORD_START.getMsg())) {
                    streamSouthService.streamStartRecord(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_RECORD_STOP.getMsg())) {
                    streamSouthService.streamStopRecord(Long.parseLong(mqRequest.getMsgId()),mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_CHECK_STREAM.getMsg())) {
                    streamSouthService.streamCheckStream(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.STREAM_CHECK_RECORD.getMsg())) {
                    streamSouthService.streamCheckRecord(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                }
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
