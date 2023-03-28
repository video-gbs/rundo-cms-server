package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.request.StreamManageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        streamNorthService.customEvent(req.getDispatchId(), req.getStreamId(), req.getMapData(), MsgType.STREAM_PLAY_STOP, response);
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
        streamNorthService.customEvent(req.getDispatchId(), req.getStreamId(), req.getMapData(), MsgType.STREAM_RECORD_STOP, response);
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
        streamNorthService.customEvent(req.getDispatchId(), req.getStreamId(), req.getMapData(), MsgType.STREAM_RECORD_START, response);
        return response;
    }

    /**
     * 调整录播播放速度
     * @param req
     * @return
     */
    @PutMapping("/record/speed")
    public DeferredResult<CommonResponse<?>> streamRecordSpeed(@RequestBody StreamManageReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        streamNorthService.customEvent(req.getDispatchId(), req.getStreamId(), req.getMapData(), MsgType.STREAM_RECORD_SPEED, response);
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
        Map<String, Object> mapData = new HashMap<>(1);
        mapData.put(StandardName.STREAM_ID_LIST, streamIds);
        streamNorthService.customEvent(dispatchId, null, mapData, MsgType.STREAM_CHECK_RECORD, response);
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
        Map<String, Object> mapData = new HashMap<>(1);
        mapData.put(StandardName.STREAM_ID_LIST, streamIds);
        streamNorthService.customEvent(dispatchId, null, mapData, MsgType.STREAM_CHECK_STREAM, response);
        return response;
    }

    /**
     * 删除所有的流
     * @param dispatchIds 调度服务id数组
     * @return
     */
    @DeleteMapping("/stream/stop/all")
    public CommonResponse<?> stopAllStream(@RequestParam Set<Long> dispatchIds){
        for (Long dispatchId : dispatchIds){
            Map<String, Object> mapData = new HashMap<>(1);
            mapData.put(StandardName.STREAM_DISPATCH_ID, dispatchId);
            streamNorthService.customEvent(dispatchId, null, mapData, MsgType.STREAM_STOP_ALL, null);
        }
        return CommonResponse.success();
    }
}
