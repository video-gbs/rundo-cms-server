package com.runjian.device.expansion.controller.play;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.service.IPlayService;
import com.runjian.device.expansion.vo.feign.request.FeignStreamOperationReq;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenjialing
 */
@Api(tags = "流播放操作")
@Slf4j
@RestController
@RequestMapping("/expansion/play")
public class ChannelPlayController {
    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private IPlayService playService;

    @Autowired
    private StreamManageApi streamManageApi;

    @PostMapping(value = "/live",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("直播接口")
    public CommonResponse<StreamInfo>  live(@RequestBody PlayReq request) {
        validatorService.validateRequest(request);

        return playService.play(request);
    }

    @PostMapping(value = "/back",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("回放接口")
    public CommonResponse<StreamInfo>  back(@RequestBody PlayBackReq request) {
        validatorService.validateRequest(request);

        return playService.playBack(request);
    }


    @PutMapping("/record/pause")
    @ApiOperation("设备录像暂停")
    public CommonResponse<Boolean> recordPause(@RequestBody RecordStreamOperationReq req){
        return streamManageApi.recordPause(req);
    }


    /**
     * 停止录像
     * @param req
     * @return
     */
    @PutMapping("/record/resume")
    @ApiOperation("设备录像恢复")
    public CommonResponse<Boolean> recordResume(@RequestBody RecordStreamOperationReq req){
        return streamManageApi.recordResume(req);
    }


    /**
     * 调整录播的播放速度
     * @param req
     * @return
     */
    @PutMapping("/record/speed")
    @ApiOperation("设备录像倍速")
    public CommonResponse<?> recordSpeed(@RequestBody RecordStreamSpeedOperationReq req){
        return streamManageApi.recordSpeed(req);
    }


    @PutMapping("/record/seek")
    @ApiOperation("设备录像拖拉")
    public CommonResponse<?> recordSeek(@RequestBody RecordStreamSeekOperationReq req){
        return streamManageApi.recordSeek(req);
    }

    @GetMapping("/streamId/info")
    @ApiOperation("通道流信息")
    public CommonResponse<?> streamIdInfo(@RequestBody ChannelStreamOperationReq req){
        validatorService.validateRequest(req);
        FeignStreamOperationReq feignStreamOperationReq = new FeignStreamOperationReq();
        feignStreamOperationReq.setChannelId(req.getChannelId());
        feignStreamOperationReq.setStreamId(req.getStreamId());
        return streamManageApi.getStreamMediaInfo(feignStreamOperationReq);
    }
}
