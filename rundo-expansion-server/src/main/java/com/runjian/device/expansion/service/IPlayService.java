package com.runjian.device.expansion.service;

import com.alibaba.fastjson.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;

/**
 * @author chenjialing
 */
public interface IPlayService {

    /**
     * 直播
     * @param playReq
     * @return
     */
    CommonResponse<StreamInfo> play(PlayReq playReq);

    /**
     * 回放
     * @param playBackReq
     * @return
     */
    CommonResponse<StreamInfo> playBack(PlayBackReq playBackReq);
}
