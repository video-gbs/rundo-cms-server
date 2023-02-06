package com.runjian.device.expansion.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.expansion.service.north.ChannelNorthService;
import com.runjian.device.expansion.vo.feign.PutPtzControlReq;
import com.runjian.device.expansion.vo.feign.VideoRecordRsp;
import com.runjian.device.expansion.vo.request.PutChannelPlaybackReq;
import com.runjian.device.expansion.vo.request.PutChannelSignSuccessReq;
import com.runjian.device.expansion.vo.response.ChannelSyncRsp;
import com.runjian.device.expansion.vo.response.VideoPlayRsp;
import com.runjian.device.expansion.vo.request.PutChannelPlayReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 通道北向控制器
 * @author Miracle
 * @date 2023/1/10 10:05
 */
@RestController
@RequestMapping("/channel/north")
public class ChannelNorthController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private ChannelNorthService channelNorthService;

    /**
     * 通道同步
     * @param deviceId 设备ID
     * @return ChannelSyncRsp
     */
    @GetMapping("/sync")
    public CommonResponse<ChannelSyncRsp> channelSync(@RequestParam Long deviceId){
        return CommonResponse.success(channelNorthService.channelSync(deviceId));
    }

    /**
     * 通道注册状态转为成功
     * @param request 通道注册状态转为成功请求体
     */
    @PutMapping("/sign/success")
    public CommonResponse channelSignSuccess(@RequestBody PutChannelSignSuccessReq request){
        validatorService.validateRequest(request);
        channelNorthService.channelSignSuccess(request.getChannelId());
        return CommonResponse.success();
    }

    /**
     * 视频点播
     * @param request 点播请求体
     * @return 视频播放返回体
     */
    @PutMapping("/play")
    public CommonResponse<VideoPlayRsp> videoPlay(@RequestBody PutChannelPlayReq request) {
        validatorService.validateRequest(request);
        return CommonResponse.success(channelNorthService.channelPlay(request.getChId(), request.getEnableAudio(), request.getSsrcCheck()));
    }

    /**
     * 视频回放
     * @param chId 回放请求体
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 视频播放返回体
     */
    @GetMapping("/record")
    public CommonResponse<VideoRecordRsp> videoPlayback(@RequestParam Long chId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){
        return CommonResponse.success(channelNorthService.channelRecord(chId, startTime, endTime));
    }

    /**
     * 视频回放
     * @param request 回放请求体
     * @return 视频播放返回体
     */
    @PutMapping("/playback")
    public CommonResponse<VideoPlayRsp> videoPlayback(@RequestBody PutChannelPlaybackReq request){
        validatorService.validateRequest(request);
        return CommonResponse.success(channelNorthService.channelPlayback(request.getChId(), request.getEnableAudio(), request.getSsrcCheck(), request.getStartTime(), request.getEndTime()));
    }

    /**
     * 云台控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/control")
    public CommonResponse<?> ptzControl(@RequestBody PutPtzControlReq req){
        validatorService.validateRequest(req);
        channelNorthService.channelPtzControl(req.getChannelId(),req.getCommandCode(),req.getHorizonSpeed(),req.getVerticalSpeed(),req.getZoomSpeed(),req.getTotalSpeed());
        return CommonResponse.success();
    }

}


