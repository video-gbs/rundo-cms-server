package com.runjian.parsing.service.south.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.StandardName;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.south.StreamSouthService;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/2/9 16:03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamSouthServiceImpl implements StreamSouthService {

    private final StreamManageApi streamManageApi;

    private final StreamTaskService streamTaskService;


    @Override
    public void msgDistribute(String msgType, Long dispatchId, Long taskId, Object data) {
        switch (MsgType.getByStr(msgType)){
            case STREAM_PLAY_RESULT:
                streamPlayResult(data);
                break;
            case STREAM_CLOSE:
                streamClose(dispatchId, data);
                break;
            default:
                customEvent(taskId, data);
        }
    }

    @Override
    public void customEvent(Long taskId, Object dataMap) {
        if (Objects.isNull(taskId)){
            return;
        }
        streamTaskService.getTaskValid(taskId, TaskState.RUNNING);
        streamTaskService.taskSuccess(taskId, dataMap);
    }

    @Override
    public void errorEvent(Long taskId, CommonMqDto<?> response) {
        log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流南向信息处理服务", "流媒体异常消息记录", response.getMsgType(), response);
        if (Objects.nonNull(taskId)){
            streamTaskService.getTaskValid(taskId, TaskState.RUNNING);
            streamTaskService.removeDeferredResult(taskId, TaskState.ERROR, response.getMsg()).setResult(response);
        }else {
            streamManageApi.commonError(response);
        }
    }

    private void streamPlayResult(Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        CommonResponse<?> commonResponse = streamManageApi.streamReceiveResult(JSONObject.parseObject(data.toString()));
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }


    private void streamClose(Long dispatchId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(data.toString());
        String streamId = jsonObject.getString(StandardName.STREAM_ID);
        CommonResponse<Boolean> commonResponse = streamManageApi.streamCloseHandle(jsonObject);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        StreamConvertDto streamConvertDto = new StreamConvertDto(streamId);
        streamConvertDto.setStreamId(streamId);
        streamConvertDto.put(StandardName.COM_RESULT, commonResponse.getData());
        streamTaskService.sendMsgToGateway(dispatchId, null, streamId, MsgType.STREAM_CLOSE.getMsg(), streamConvertDto, null);
    }


}
