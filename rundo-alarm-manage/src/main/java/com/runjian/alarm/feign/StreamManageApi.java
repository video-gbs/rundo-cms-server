package com.runjian.alarm.feign;

import com.runjian.alarm.vo.request.PostPictureDownloadReq;
import com.runjian.alarm.vo.request.PostRecordDownloadReq;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Miracle
 * @date 2023/9/14 17:49
 */
@FeignClient(value = "stream-manage", decode404 = true)
public interface StreamManageApi {

    @PostMapping("/stream/north/download/video")
    CommonResponse<String> applyStreamId(@RequestBody PostRecordDownloadReq req);


    @PostMapping("/stream/north/download/picture")
    CommonResponse<String> applyStreamId(@RequestBody PostPictureDownloadReq req);
}
