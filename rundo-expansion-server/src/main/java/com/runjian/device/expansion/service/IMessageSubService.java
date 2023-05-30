package com.runjian.device.expansion.service;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.feign.request.MessageSubReq;
import com.runjian.device.expansion.vo.feign.response.MessageSubRsp;
import com.runjian.device.expansion.vo.request.ChannelPtzControlReq;

import java.util.List;

/**
 * @author chenjialing
 */
public interface IMessageSubService {
    /**
     * @return
     */
    CommonResponse<List<MessageSubRsp>> subMsg();
}
