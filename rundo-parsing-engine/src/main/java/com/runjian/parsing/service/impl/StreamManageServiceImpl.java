package com.runjian.parsing.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.service.StreamManageService;
import com.runjian.parsing.vo.feign.PutStreamReceiveResultReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Miracle
 * @date 2023/2/9 16:03
 */
@Service
public class StreamManageServiceImpl implements StreamManageService {

    @Autowired
    private StreamManageApi streamManageApi;

    @Override
    public void streamPlayResult(String streamId, Object data) {
        PutStreamReceiveResultReq req = new PutStreamReceiveResultReq();
        req.setStreamId(streamId);
        req.setIsSuccess((Boolean) data);
        CommonResponse<?> commonResponse = streamManageApi.streamReceiveResult(req);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void streamClose(String streamId) {
        CommonResponse<?> commonResponse = streamManageApi.streamCloseHandle(streamId);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }
}
