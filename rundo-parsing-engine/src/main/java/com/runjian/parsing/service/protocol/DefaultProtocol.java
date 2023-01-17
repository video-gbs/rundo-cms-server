package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.DataBaseService;
import com.runjian.parsing.service.TaskService;
import com.runjian.parsing.vo.CommonMqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 默认协议
 * @author Miracle
 * @date 2023/1/17 14:14
 */
@Service
public class DefaultProtocol implements Protocol {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DeviceMapper deviceMapper;


    @Value("${gateway.default.exchange-id}")
    private String gatewayExchangeId;

    @Override
    public String getProtocolName() {
        return DEFAULT_PROTOCOL;
    }

    @Override
    public void deviceSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(deviceInfo.getGatewayId());
        String mqId = UUID.randomUUID().toString().replace("-", "");
        Long taskId = taskService.createAsyncTask(deviceInfo.getGatewayId(), deviceId, null, null, mqId, MsgType.DEVICE_SYNC, response);
        String mqKey = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + deviceInfo.getGatewayId();
        CommonMqDto<String> request = new CommonMqDto<>();
        request.setTime(LocalDateTime.now());
        request.setSerialNum(gatewayInfo.getSerialNum());
        request.setMsgId(taskId.toString());
        request.setMsgType(MsgType.DEVICE_SYNC.getMsg());
        request.setData(deviceInfo.getOriginId());
        rabbitMqSender.sendMsgByRoutingKey(gatewayExchangeId, mqKey, mqId, request, true);
    }

    @Override
    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {

    }


    @Override
    public  void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response) {

    }

    @Override
    public void channelSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {

    }

    @Override
    public void channelPlay(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {

    }

    @Override
    public void channelPlayback(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {

    }
}
