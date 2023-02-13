package com.runjian.parsing.service.south.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.south.StreamSouthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void streamSouthPlayResult(Long dispatchId, String streamId, Object data) {
        JSONObject jsonObject = JSONObject.parseObject(data.toString());
        jsonObject.put(StandardName.STREAM_ID, streamId);
        jsonObject.put(StandardName.STREAM_DISPATCH_ID, dispatchId);
        CommonResponse<?> commonResponse = streamManageApi.streamReceiveResult(jsonObject);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void streamSouthClose(Long dispatchId, String streamId, Object data) {

        JSONObject jsonObject = JSONObject.parseObject(data.toString());
        jsonObject.put(StandardName.STREAM_ID, streamId);
        jsonObject.put(StandardName.STREAM_DISPATCH_ID, dispatchId);
        CommonResponse<Boolean> commonResponse = streamManageApi.streamCloseHandle(jsonObject);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        streamTaskService.sendMsgToGateway(dispatchId, null, streamId, MsgType.STREAM_CLOSE.getMsg(), commonResponse.getData(), null);
    }

    @Override
    public void streamSouthStopPlay(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamSouthStartRecord(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void streamSouthStopRecord(Long taskId, Object data) {
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
