package com.runjian.parsing.service.north.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

/**
 * @author Miracle
 * @date 2023/2/10 10:26
 */
@Service
public class StreamNorthServiceImpl implements StreamNorthService {

    @Autowired
    private StreamTaskService streamTaskService;

    /**
     * 通用消息处理
     * @param dispatchId 调度服务id
     * @param streamId 流id
     * @param mapData 数据
     * @param msgType 消息类型
     * @param response 消息返回体
     */
    @Override
    public void customEvent(Long dispatchId, String streamId, Map<String, Object> mapData, MsgType msgType, DeferredResult<CommonResponse<?>> response) {
        StreamConvertDto streamConvertDto = new StreamConvertDto();
        streamConvertDto.setStreamId(streamId);
        streamConvertDto.setDataMap(mapData);
        streamTaskService.sendMsgToGateway(dispatchId, null, streamId, msgType.getMsg(), streamConvertDto, response);
    }
}
