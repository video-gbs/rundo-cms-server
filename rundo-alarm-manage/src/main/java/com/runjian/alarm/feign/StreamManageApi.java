package com.runjian.alarm.feign;

import com.runjian.alarm.feign.fallback.StreamManageFallback;
import com.runjian.alarm.vo.feign.PostChannelPlayReq;
import com.runjian.alarm.vo.request.PostImageDownloadReq;
import com.runjian.alarm.vo.request.PostRecordDownloadReq;
import com.runjian.alarm.vo.response.GetStreamInfoRsp;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Miracle
 * @date 2023/9/14 17:49
 */
@FeignClient(value = "stream-manage", fallbackFactory = StreamManageFallback.class,decode404 = true)
public interface StreamManageApi {

    @PostMapping("/stream/north/download/video")
    CommonResponse<String> applyStreamId(@RequestBody PostRecordDownloadReq req);


    @PostMapping("/stream/north/download/image")
    CommonResponse<String> applyStreamId(@RequestBody PostImageDownloadReq req);

    @PostMapping(value = "/stream/north/play/live",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<GetStreamInfoRsp> play(@RequestBody PostChannelPlayReq channelPlayReq);
}
