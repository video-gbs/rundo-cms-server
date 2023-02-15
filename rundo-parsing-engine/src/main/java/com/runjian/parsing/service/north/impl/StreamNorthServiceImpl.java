package com.runjian.parsing.service.north.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/10 10:26
 */
@Service
public class StreamNorthServiceImpl implements StreamNorthService {

    @Autowired
    private StreamTaskService streamTaskService;

    @Override
    public void streamNorthStopPlay(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_PLAY_STOP, response);
    }

    @Override
    public void streamNorthStartRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_RECORD_START, response);
    }

    @Override
    public void streamNorthStopRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_RECORD_STOP, response);
    }

    @Override
    public void checkStreamRecordStatus(Long dispatchId, List<String> streamIds, DeferredResult<CommonResponse<?>> response) {
        Map<String, Object> mapData = new HashMap<>(streamIds.size());
        mapData.put(StandardName.STREAM_ID_LIST, streamIds);
        customEvent(dispatchId, null, mapData, MsgType.STREAM_CHECK_RECORD, response);
    }

    @Override
    public void checkStreamStatus(Long dispatchId, List<String> streamIds, DeferredResult<CommonResponse<?>> response) {
        Map<String, Object> mapData = new HashMap<>(streamIds.size());
        mapData.put(StandardName.STREAM_ID_LIST, streamIds);
        customEvent(dispatchId, null, mapData, MsgType.STREAM_CHECK_STREAM, response);
    }

    /**
     * 通用消息处理
     * @param dispatchId 调度服务id
     * @param streamId 流id
     * @param mapData 数据
     * @param msgType 消息类型
     * @param response 消息返回体
     */
    private void customEvent(Long dispatchId, String streamId, Map<String, Object> mapData, MsgType msgType, DeferredResult<CommonResponse<?>> response) {
        StreamConvertDto streamConvertDto = new StreamConvertDto();
        streamConvertDto.setStreamId(streamId);
        streamConvertDto.setDataMap(mapData);
        streamTaskService.sendMsgToGateway(dispatchId, null, streamId, msgType.getMsg(), streamConvertDto, response);
    }
}
