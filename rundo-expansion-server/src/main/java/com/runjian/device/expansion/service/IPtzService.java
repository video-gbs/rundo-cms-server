package com.runjian.device.expansion.service;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;

/**
 * @author chenjialing
 */
public interface IPtzService {
    /**
     *
     * @param request
     * @return
     */
    CommonResponse<?> ptzOperation(ChannelPtzControlReq request);
}
