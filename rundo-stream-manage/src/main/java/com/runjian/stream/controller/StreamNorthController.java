package com.runjian.stream.controller;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.service.north.StreamNorthService;
import com.runjian.stream.vo.request.*;
import com.runjian.stream.vo.response.PostVideoPlayRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/7 20:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stream/north")
public class StreamNorthController {

    private final ValidatorService validatorService;

    private final StreamNorthService streamNorthService;

    @PostMapping("/custom/live")
    public CommonResponse<PostVideoPlayRsp> customLive(@RequestBody PostStreamCustomLiveReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.customLive(req.getDispatchId(), req.getCode(), req.getProtocol(), req.getTransferMode(), req.getPort(), req.getIp(), req.getStreamMode(), req.getEnableAudio(), req.getSsrcCheck(), req.getRecordState(), req.getAutoCloseState()));
    }

    /**
     * 直播播放
     * @param req
     * @return
     */
    @PostMapping("/play/live")
    public CommonResponse<PostVideoPlayRsp> applyStreamId(@RequestBody PostStreamLivePlayReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.streamLivePlay(req.getChannelId(), req.getStreamType(), req.getEnableAudio(), req.getSsrcCheck(), req.getRecordState(), req.getAutoCloseState(), req.getBitStreamId()));
    }

    /**
     * 录像播放
     * @param req
     * @return
     */
    @PostMapping("/play/record")
    public CommonResponse<PostVideoPlayRsp> applyStreamId(@RequestBody PostStreamRecordPlayReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.streamRecordPlay(req.getChannelId(), req.getStreamType(), req.getEnableAudio(), req.getSsrcCheck(), req.getPlayType(), req.getRecordState(), req.getAutoCloseState(), req.getStartTime(), req.getEndTime(), req.getBitStreamId()));
    }

    /**
     * 下载录像
     * @param req 录像下载请求体
     * @return 流id
     */
    @PostMapping("/download/video")
    public CommonResponse<String> applyStreamId(@RequestBody PostRecordDownloadReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.downloadRecord(req.getChannelId(), req.getStreamType(), req.getEnableAudio(), req.getPlayType(), req.getStartTime(), req.getEndTime(), req.getUploadId(), req.getUploadUrl()));
    }

    /**
     * 下载图片
     * @param req 图片下载请求体
     * @return 流id
     */
    @PostMapping("/download/picture")
    public CommonResponse<String> applyStreamId(@RequestBody PostPictureDownloadReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.downloadPicture(req.getChannelId(), req.getStreamType(), req.getPlayType(), req.getTime(), req.getUploadId(), req.getUploadUrl()));
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

    /**
     * 调整录播的播放速度
     * @param req
     * @return
     */
    @PutMapping("/record/speed")
    public CommonResponse<?> recordSpeed(@RequestBody PutRecordSpeedReq req){
        validatorService.validateRequest(req);
        streamNorthService.speedRecord(req.getStreamId(), req.getSpeed());
        return CommonResponse.success();
    }

    /**
     * 调整录播的播放进度
     * @param req
     * @return
     */
    @PutMapping("/record/seek")
    public CommonResponse<?> recordSeek(@RequestBody PutRecordSeekReq req){
        validatorService.validateRequest(req);
        streamNorthService.seekRecord(req.getStreamId(), req.getCurrentTime(), req.getTargetTime());
        return CommonResponse.success();
    }

    /**
     * 暂停录播播放
     * @param req
     * @return
     */
    @PutMapping("/record/pause")
    public CommonResponse<?> recordPause(@RequestBody PutStreamOperationReq req){
        validatorService.validateRequest(req);
        streamNorthService.pauseRecord(req.getStreamId());
        return CommonResponse.success();
    }

    /**
     * 恢复录播
     * @param req
     * @return
     */
    @PutMapping("/record/resume")
    public CommonResponse<?> recordResume(@RequestBody PutStreamOperationReq req){
        validatorService.validateRequest(req);
        streamNorthService.resumeRecord(req.getStreamId());
        return CommonResponse.success();
    }


    /**
     * 查询录像状态
     * @param streamIdList 流id列表
     * @return
     */
    @GetMapping("/stream/data")
    public CommonResponse<List<StreamInfo>> getStream(@RequestParam List<String> streamIdList, @RequestParam Integer recordState, @RequestParam Integer streamState){
        return CommonResponse.success(streamNorthService.getRecordStates(streamIdList, recordState, streamState));
    }

    /**
     * 获取流信息
     * @param req 流Id
     * @return
     */
    @GetMapping("/stream/media/info")
    public CommonResponse<JSONObject> getStreamMediaInfo(@SpringQueryMap PutStreamOperationReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(streamNorthService.getStreamMediaInfo(req.getStreamId()));
    }

}
