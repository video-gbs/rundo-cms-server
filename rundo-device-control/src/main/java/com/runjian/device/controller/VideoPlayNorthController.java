package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.PlayVideoNorthService;
import com.runjian.device.vo.request.PutChannelPlayReq;
import com.runjian.device.vo.request.PutChannelPlaybackReq;
import com.runjian.device.vo.response.VideoPlayRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 视频播放北向控制器
 * @author Miracle
 * @date 2023/1/12 17:08
 */
@RestController
@RequestMapping("/video")
public class VideoPlayNorthController {

    @Autowired
    private PlayVideoNorthService playVideoNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 视频点播
     * @param request 点播请求体
     * @return 视频播放返回体
     */
    @PutMapping("/play")
    public CommonResponse<VideoPlayRsp> videoPlay(@RequestBody PutChannelPlayReq request){
        validatorService.validateRequest(request);
        return CommonResponse.success(playVideoNorthService.play(request.getChId(), request.getEnableAudio(), request.getSsrcCheck()));
    }

    /**
     * 视频回放
     * @param request 回放请求体
     * @return 视频播放返回体
     */
    @PutMapping("/playback")
    public CommonResponse<VideoPlayRsp> videoPlayback(@RequestBody PutChannelPlaybackReq request){
        validatorService.validateRequest(request);
        return CommonResponse.success(playVideoNorthService.playBack(request.getChId(), request.getEnableAudio(), request.getSsrcCheck(), request.getStartTime(), request.getEndTime()));
    }

}
