package com.runjian.parsing.service.south.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.south.StreamSouthService;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/2/9 16:03
 */
@Service
public class StreamSouthServiceImpl implements StreamSouthService {

    @Autowired
    private StreamManageApi streamManageApi;

    @Autowired
    private StreamTaskService streamTaskService;


    @Override
    public void streamPlayResult(Long dispatchId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        CommonResponse<?> commonResponse = streamManageApi.streamReceiveResult(JSONObject.parseObject(data.toString()));
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void streamClose(Long dispatchId, Object data) {
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


    @Override
    public void streamStopPlay(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamStartRecord(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamStopRecord(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamCheckRecord(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamCheckStream(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void customEvent(Long taskId, Object dataMap) {
        streamTaskService.getTaskValid(taskId, TaskState.RUNNING);
        streamTaskService.taskSuccess(taskId, dataMap);
    }

    @Override
    public void errorEvent(Long taskId, CommonResponse<?> response) {
        streamTaskService.getTaskValid(taskId, TaskState.RUNNING);
        streamTaskService.removeDeferredResult(taskId, TaskState.ERROR, response.getMsg()).setResult(response);
    }
}
