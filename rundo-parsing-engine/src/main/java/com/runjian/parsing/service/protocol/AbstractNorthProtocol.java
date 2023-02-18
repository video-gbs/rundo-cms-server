package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.constant.MsgType;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/1/30 14:15
 */
public abstract class AbstractNorthProtocol implements NorthProtocol {

    @Override
    public void deviceSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.DEVICE_SYNC.getMsg(), null, response);
    }

    @Override
    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(gatewayId, IdType.GATEWAY, MsgType.DEVICE_ADD.getMsg(), dataMap, response);
    }

    @Override
    public  void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.DEVICE_DELETE.getMsg(), null, response);
    }

    @Override
    public void channelSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.CHANNEL_SYNC.getMsg(), null, response);
    }

    @Override
    public void channelPtzControl(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_CONTROL.getMsg(), dataMap, response);
    }

    @Override
    public void channelPlay(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PLAY.getMsg(), dataMap, response);
    }

    @Override
    public void channelRecord(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_RECORD_INFO.getMsg(), dataMap, response);
    }

    @Override
    public void channelPlayback(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PLAYBACK.getMsg(), dataMap, response);
    }
}
