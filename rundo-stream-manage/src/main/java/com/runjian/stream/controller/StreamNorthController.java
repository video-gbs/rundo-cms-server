package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.north.StreamNorthService;
import com.runjian.stream.vo.request.PostStreamApplyStreamReq;
import com.runjian.stream.vo.request.PutStreamOperationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/7 20:36
 */
@RestController
@RequestMapping("/stream/north")
public class StreamNorthController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private StreamNorthService streamNorthService;

    /**
     * 申请流id
     * @param req
     * @return
     */
    @PostMapping("/play/apply")
    public CommonResponse<String> applyStreamId(@RequestBody PostStreamApplyStreamReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.applyStreamId(req.getGatewayId(), req.getChannelId(), req.getPlayType(), req.getRecordState(), req.getAutoCloseState()));
    }

    /**
     * 停止播放
     * @param req
     * @return
     */
    @PutMapping("/play/stop")
    public CommonResponse<?> stopPlay(@RequestBody PutStreamOperationReq req){
        validatorService.validateRequest(req);
        streamNorthService.stopPlay(req.getStreamId());
        return CommonResponse.success();
    }

    /**
     * 开启录像
     * @param req
     * @return
     */
    @PutMapping("/record/start")
    public CommonResponse<Boolean> startRecord(@RequestBody PutStreamOperationReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.startRecord(req.getStreamId()));
    }

    /**
     * 停止录像
     * @param req
     * @return
     */
    @PutMapping("/record/stop")
    public CommonResponse<Boolean> stopRecord(@RequestBody PutStreamOperationReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.stopRecord(req.getStreamId()));
    }
}