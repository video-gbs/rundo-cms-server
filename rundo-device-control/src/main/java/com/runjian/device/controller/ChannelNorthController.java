package com.runjian.device.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.vo.request.*;
import com.runjian.device.vo.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
     * 分页获取待添加的通道信息
     * @param page 页码
     * @param num 每页数量
     * @param nameOrOriginId 名字或数据id查询
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetChannelByPageRsp>> getChannelByPage(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int num, String nameOrOriginId){
        return CommonResponse.success(channelNorthService.getChannelByPage(page, num, nameOrOriginId));
    }

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
     * 通道删除
     * @param channelIds 通道id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> channelDelete(@RequestParam(value = "channelIds") List<Long> channelIds){
        channelNorthService.channelDeleteByChannelId(channelIds);
        return CommonResponse.success();
    }

    /**
     * 通道注册状态转为成功
     * @param request 通道注册状态转为成功请求体
     */
    @PutMapping("/sign/success")
    public CommonResponse<?> channelSignSuccess(@RequestBody PutChannelSignSuccessReq request){
        validatorService.validateRequest(request);
        channelNorthService.channelSignSuccess(request.getChannelIdList());
        return CommonResponse.success();
    }

    /**
     * 视频点播
     * @param request 点播请求体
     * @return 视频播放返回体
     */
    @PostMapping("/play")
    public CommonResponse<VideoPlayRsp> videoPlay(@RequestBody PutChannelPlayReq request) {
        validatorService.validateRequest(request);
        return CommonResponse.success(channelNorthService.channelPlay(request.getChannelId(), request.getEnableAudio(), request.getSsrcCheck(), request.getStreamType(), request.getRecordState(), request.getAutoCloseState()));
    }

    /**
     * 视频回放
     * @param channelId 回放请求体
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 视频播放返回体
     */
    @GetMapping("/record")
    public CommonResponse<VideoRecordRsp> videoRecordInfo(@RequestParam Long channelId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        return CommonResponse.success(channelNorthService.channelRecord(channelId, startTime, endTime));
    }

    /**
     * 视频回放
     * @param request 回放请求体
     * @return 视频播放返回体
     */
    @PostMapping("/playback")
    public CommonResponse<VideoPlayRsp> videoPlayback(@RequestBody PutChannelPlaybackReq request){
        validatorService.validateRequest(request);
        return CommonResponse.success(channelNorthService.channelPlayback(request.getChannelId(), request.getEnableAudio(), request.getSsrcCheck(), request.getStreamType(), request.getStartTime(), request.getEndTime(), request.getRecordState(), request.getAutoCloseState()));
    }

    /**
     * 云台控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/ptz/control")
    public CommonResponse<?> ptzControl(@RequestBody PutPtzControlReq req){
        validatorService.validateRequest(req);
        channelNorthService.channelPtzControl(req.getChannelId(),req.getCmdCode(), req.getCmdValue(), req.getValueMap());
        return CommonResponse.success();
    }

    /**
     * 获取预置位
     * @param channelId 通道id
     * @return
     */
    @GetMapping("/ptz/preset")
    public CommonResponse<List<PtzPresetRsp>> getPtzPreset(@RequestParam Long channelId){
        return CommonResponse.success(channelNorthService.channelPtzPresetGet(channelId));
    }

    /**
     * 3d放大放小控制
     * @param req 云台控制请求体
     * @return
     */
    @PutMapping("/ptz/3d")
    public CommonResponse<?> ptz3d(@RequestBody PutPtz3dReq req){
        validatorService.validateRequest(req);
        channelNorthService.channelPtz3d(req.getChannelId(), req.getDragType(), req.getLength(), req.getWidth(), req.getMidPointX(), req.getMidPointY(), req.getLengthX(), req.getLengthY());
        return CommonResponse.success();
    }

}


