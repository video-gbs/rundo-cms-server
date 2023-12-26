package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.north.StreamPushNorthService;
import com.runjian.stream.vo.request.PostStreamPushInitReq;
import com.runjian.stream.vo.request.PostStreamPushRunReq;
import com.runjian.stream.vo.response.PostStreamPushInitRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/12/8 15:20
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stream/push/north")
public class StreamPushNorthController {

    private final StreamPushNorthService streamPushNorthService;

    private final ValidatorService validatorService;

    /**
     * 推送初始化
     * @param req
     * @return
     */
    @PostMapping("/init")
    public CommonResponse<PostStreamPushInitRsp> streamPushInit(@RequestBody PostStreamPushInitReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamPushNorthService.streamPushInit(req.getChannelId(), req.getSsrc(), req.getDstUrl(), req.getDstPort(), req.getTransferMode(), req.getStartTime(), req.getEndTime()));
    }

    /**
     * 进行流推送
     */
    @PutMapping("/run")
    public CommonResponse<?> streamPushRun(@RequestBody PostStreamPushRunReq req){
        validatorService.validateRequest(req);
        streamPushNorthService.streamPushRun(req.getStreamPushId());
        return CommonResponse.success();
    }

    /**
     * 停止流推送
     */
    @DeleteMapping("/stop")
    public CommonResponse<?> streamPushStop(@RequestParam Long streamPushId){
        streamPushNorthService.streamPushStop(streamPushId);
        return CommonResponse.success();
    }
}

