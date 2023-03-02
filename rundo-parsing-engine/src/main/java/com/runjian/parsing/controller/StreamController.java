package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.request.StreamManageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Set;

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
    public DeferredResult<CommonResponse<?>> streamPlayStop(@RequestBody StreamManageReq req){
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
    public DeferredResult<CommonResponse<?>> streamRecordStop(@RequestBody StreamManageReq req){
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
    public DeferredResult<CommonResponse<?>> streamRecordStart(@RequestBody StreamManageReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.streamNorthStartRecord(req.getDispatchId(), req.getStreamId(), response);
        return response;
    }

    /**
     * 检测录像状态
     * @param dispatchId 调度服务id
     * @param streamIds 流id数组
     * @return
     */
    @GetMapping("/check/record")
    public DeferredResult<CommonResponse<?>> checkStreamRecordStatus(@RequestParam Long dispatchId, @RequestParam List<String> streamIds){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.checkStreamRecordStatus(dispatchId, streamIds, response);
        return response;
    }

    /**
     * 检测流状态
     * @param dispatchId 调度服务id
     * @param streamIds 流id数组
     * @return
     */
    @GetMapping("/check/stream")
    public DeferredResult<CommonResponse<?>> checkStreamStatus(@RequestParam Long dispatchId, @RequestParam List<String> streamIds){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.checkStreamStatus(dispatchId, streamIds, response);
        return response;
    }

    /**
     * 删除所有的流
     * @param dispatchIds 调度服务id数组
     * @return
     */
    @DeleteMapping("/stream/stop/all")
    public CommonResponse<?> stopAllStream(@RequestParam Set<Long> dispatchIds){
        streamNorthService.stopAllStream(dispatchIds);
        return CommonResponse.success();
    }
}
