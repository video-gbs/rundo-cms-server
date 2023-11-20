package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.service.IPlayService;
import com.runjian.device.expansion.vo.feign.request.PlayBackFeignReq;
import com.runjian.device.expansion.vo.feign.request.PlayFeignReq;
import com.runjian.device.expansion.vo.feign.request.PutStreamOperationReq;
import com.runjian.device.expansion.vo.feign.request.WebRtcAudioReq;
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

    @Autowired
    StreamManageApi streamManageApi;
    @Override
    public  CommonResponse<StreamInfo> play(PlayReq playReq) {
        PlayFeignReq playFeignReq = new PlayFeignReq();
        BeanUtil.copyProperties(playReq,playFeignReq);
        CommonResponse<StreamInfo> commonResponse = streamManageApi.play(playFeignReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @Override
    public CommonResponse<StreamInfo> playBack(PlayBackReq playBackReq) {
        PlayBackFeignReq playBackFeignReq = new PlayBackFeignReq();
        BeanUtil.copyProperties(playBackReq,playBackFeignReq);
        CommonResponse<StreamInfo> commonResponse = streamManageApi.playBack(playBackFeignReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @Override
    public CommonResponse<?> stopPlay(PutStreamOperationReq req) {
        CommonResponse<?> commonResponse = streamManageApi.stopPlay(req);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }

    @Override
    public CommonResponse<StreamInfo> webrtcAudio(PlayReq playReq) {
        WebRtcAudioReq webRtcAudioReq = new WebRtcAudioReq();
        webRtcAudioReq.setChannelId(Long.valueOf(playReq.getChannelId()));
        webRtcAudioReq.setRecordState(0);
        webRtcAudioReq.setAutoCloseState(1);
        CommonResponse<StreamInfo> commonResponse = streamManageApi.webrtcAudio(webRtcAudioReq);
        if(commonResponse.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
            throw  new BusinessException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR, commonResponse.getMsg());
        }
        return commonResponse;
    }
}
