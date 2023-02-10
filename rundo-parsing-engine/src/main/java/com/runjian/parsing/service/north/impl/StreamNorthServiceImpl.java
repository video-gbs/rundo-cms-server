package com.runjian.parsing.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/10 10:26
 */
@Service
public class StreamNorthServiceImpl implements StreamNorthService {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private StreamTaskService streamTaskService;

    @Override
    public void streamNorthStopPlay(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_STOP_PLAY, response);
    }

    @Override
    public void streamNorthStartRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_START_RECORD, response);
    }

    @Override
    public void streamNorthStopRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response) {
        customEvent(dispatchId, streamId, null, MsgType.STREAM_STOP_RECORD, response);
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
        Optional<DispatchInfo> dispatchInfoOp = dispatchMapper.selectById(dispatchId);
        if (dispatchInfoOp.isEmpty()){
            response.setResult(CommonResponse.failure(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("流媒体服务'%s'不存在", dispatchId)));
            return;
        }
        StreamConvertDto streamConvertDto = new StreamConvertDto();
        streamConvertDto.setStreamId(streamId);
        streamConvertDto.setDataMap(mapData);
        streamTaskService.sendMsgToGateway(dispatchInfoOp.get().getSerialNum(), dispatchId, null, streamId, msgType.getMsg(), streamConvertDto, response);
    }
}
