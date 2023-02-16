package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.south.StreamSouthService;
import com.runjian.stream.vo.request.PutStreamCloseReq;
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
    @PutMapping("/play/result")
    public CommonResponse<?> receiveResult(@RequestBody PutStreamReceiveResultReq req){
        validatorService.validateRequest(req);
        streamSouthService.receiveResult(req.getStreamId(), req.getIsSuccess());
        return CommonResponse.success();
    }

    /**
     * 自动关闭
     * @param req 流id
     * @return 是否关闭
     */
    @PutMapping("/play/close")
    public CommonResponse<Boolean> streamCloseHandle(@RequestBody PutStreamCloseReq req){
        return CommonResponse.success(streamSouthService.streamCloseHandle(req.getStreamId(), req.getCanClose()));
    }
}
