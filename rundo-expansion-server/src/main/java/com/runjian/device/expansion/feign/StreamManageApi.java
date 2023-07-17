package com.runjian.device.expansion.feign;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.fallback.StreamManageApiFallbackFactory;
import com.runjian.device.expansion.vo.feign.request.FeignStreamOperationReq;
import com.runjian.device.expansion.vo.feign.request.PlayBackFeignReq;
import com.runjian.device.expansion.vo.feign.request.PlayFeignReq;
import com.runjian.device.expansion.vo.feign.request.PutStreamOperationReq;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "stream-manage",fallbackFactory= StreamManageApiFallbackFactory.class)
public interface StreamManageApi {

    /**
     * 点播接口
     * @param playFeignReq
     * @return
     */
    @PostMapping(value = "/stream/north/play/live",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> play(@RequestBody PlayFeignReq playFeignReq);

    /**
     * 回放接口
     * @param playBackReq
     * @return
     */
    @PostMapping(value = "/stream/north/play/record",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> playBack(@RequestBody PlayBackFeignReq playBackReq);
    /**
     * 开启录像
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/pause")
    public CommonResponse<Boolean> recordPause(@RequestBody RecordStreamOperationReq req);


    /**
     * 停止录像
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/resume")
    public CommonResponse<Boolean> recordResume(@RequestBody RecordStreamOperationReq req);


    /**
     * 调整录播的播放速度
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/speed")
    public CommonResponse<?> recordSpeed(@RequestBody RecordStreamSpeedOperationReq req);

    /**
     * 录像拖拉
     * @param req
     * @return
     */
    @PutMapping("/stream/north/record/seek")
    public CommonResponse<?> recordSeek(@RequestBody RecordStreamSeekOperationReq req);

    /**
     * 流信息获取
     * @param req
     * @return
     */
    @GetMapping("/stream/north/stream/media/info")
    public CommonResponse<JSONObject> getStreamMediaInfo(@SpringQueryMap FeignStreamOperationReq req);

    /**
     * 停止播放
     * @param req
     * @return
     */
    @PutMapping("/stream/north/play/stop")
    CommonResponse<?> stopPlay(@RequestBody PutStreamOperationReq req);

}
