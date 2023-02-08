package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.south.StreamSouthService;
import com.runjian.stream.vo.request.PutStreamReceiveResultReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/7 20:18
 */
@RestController
@RequestMapping("/stream/south")
public class StreamSouthController {

    @Autowired
    private StreamSouthService streamSouthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 接收流播放结果
     * @param req
     */
    @PutMapping("/result")
    public CommonResponse<?> receiveResult(@RequestBody PutStreamReceiveResultReq req){
        validatorService.validateRequest(req);
        streamSouthService.receiveResult(req.getStreamId(), req.getIsSuccess());
        return CommonResponse.success();
    }

    /**
     * 自动关闭
     * @param streamId 流id
     * @return 是否关闭
     */
    @GetMapping("/close")
    public CommonResponse<?> autoCloseHandle(@RequestParam String streamId){
        return CommonResponse.success(streamSouthService.autoCloseHandle(streamId));
    }
}
