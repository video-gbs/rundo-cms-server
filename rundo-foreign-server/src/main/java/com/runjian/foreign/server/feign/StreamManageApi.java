package com.runjian.foreign.server.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.foreign.server.feign.fallBack.StreamManageApiFallbackFactory;
import com.runjian.foreign.server.vo.feign.request.PushStreamCustomLiveFeignReq;
import com.runjian.foreign.server.vo.feign.response.StreamInfo;
import org.springframework.cloud.openfeign.FeignClient;
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
    @PostMapping(value = "/stream/north/custom/live",produces = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse<StreamInfo> customLive(@RequestBody PushStreamCustomLiveFeignReq playFeignReq);

}
