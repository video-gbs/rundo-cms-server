package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.service.IPlayService;
import com.runjian.device.expansion.vo.feign.request.PlayBackFeignReq;
import com.runjian.device.expansion.vo.feign.request.PlayFeignReq;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.PlayBackReq;
import com.runjian.device.expansion.vo.request.PlayReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class PlayServiceImpl implements IPlayService {

    @Autowired
    DeviceControlApi deviceControlApi;
    @Override
    public  CommonResponse<StreamInfo> play(PlayReq playReq) {
        PlayFeignReq playFeignReq = new PlayFeignReq();
        BeanUtil.copyProperties(playReq,playFeignReq);
        return deviceControlApi.play(playFeignReq);
    }

    @Override
    public CommonResponse<StreamInfo> playBack(PlayBackReq playBackReq) {
        PlayBackFeignReq playBackFeignReq = new PlayBackFeignReq();
        BeanUtil.copyProperties(playBackReq,playBackFeignReq);
        return deviceControlApi.playBack(playBackFeignReq);
    }
}
