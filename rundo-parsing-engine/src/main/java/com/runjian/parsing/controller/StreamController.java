package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.common.validator.ValidatorService;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.request.StreamManageReq;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StreamController {

    private final ValidatorService validatorService;

    private final StreamNorthService streamNorthService;

    /**
     * 流通用消息处理
     * @param req
     * @return
     */
    @PostMapping("/custom/event")
    public DeferredResult<CommonResponse<?>> customEvent(@RequestBody StreamManageReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(req.getOutTime());
        streamNorthService.customEvent(req.getDispatchId(), req.getStreamId(), req.getDataMap(), MsgType.getByStr(req.getMsgType()), response);
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
