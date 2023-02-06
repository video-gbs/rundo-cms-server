package com.runjian.stream.feign;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Miracle
 * @date 2023/2/6 15:27
 */
@FeignClient
public interface ParsingEngineApi {
    CommonResponse<?> channelStopPlay(String streamId);
}
