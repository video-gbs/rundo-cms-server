package com.runjian.parsing.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.StreamManageService;
import com.runjian.parsing.vo.feign.PutStreamReceiveResultReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Miracle
 * @date 2023/2/9 16:03
 */
@Service
public class StreamManageServiceImpl implements StreamManageService {

    @Autowired
    private StreamManageApi streamManageApi;

    @Override
    public void streamSouthPlayResult(String streamId, Object data) {
        PutStreamReceiveResultReq req = new PutStreamReceiveResultReq();
        req.setStreamId(streamId);
        req.setIsSuccess((Boolean) data);
        CommonResponse<?> commonResponse = streamManageApi.streamReceiveResult(req);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void streamSouthClose(String streamId) {
        CommonResponse<?> commonResponse = streamManageApi.streamCloseHandle(streamId);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void streamNorthStopPlay(String streamId, DeferredResult<CommonResponse<?>> response) {
        // todo
    }

    @Override
    public void streamSouthStopPlay(Long taskId, Object data) {
// todo
    }

    @Override
    public void streamNorthStartRecord(String streamId, DeferredResult<CommonResponse<?>> response) {
// todo
    }

    @Override
    public void streamSouthStartRecord(Long taskId, Object data) {
// todo
    }

    @Override
    public void streamNorthStopRecord(String streamId, DeferredResult<CommonResponse<?>> response) {
// todo
    }

    @Override
    public void streamSouthStopRecord(Long taskId, Object data) {
// todo
    }
}
