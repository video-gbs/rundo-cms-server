package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.request.StreamControlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Miracle
 * @date 2023/2/9 17:12
 */
@RestController
@RequestMapping("/stream-manage")
public class StreamController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private StreamNorthService streamNorthService;

    private static final long OUT_TIME = 10000L;

    /**
     * 停止播放
     * @param req 流操作请求体
     * @return
     */
    @PutMapping("/play/stop")
    public DeferredResult<CommonResponse<?>> streamPlayStop(@RequestBody StreamControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.streamNorthStopPlay(req.getDispatchId(), req.getStreamId(), response);
        return response;
    }

    /**
     * 停止录像
     * @param req 流操作请求体
     * @return
     */
    @PutMapping("/record/stop")
    public DeferredResult<CommonResponse<?>> streamRecordStop(@RequestBody StreamControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.streamNorthStopRecord(req.getDispatchId(), req.getStreamId(), response);
        return response;
    }

    /**
     * 打开录像
     * @param req 流操作请求体
     * @return
     */
    @PutMapping("/record/start")
    public DeferredResult<CommonResponse<?>> streamRecordStart(@RequestBody StreamControlReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.streamNorthStartRecord(req.getDispatchId(), req.getStreamId(), response);
        return response;
    }
}
