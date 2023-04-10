package com.runjian.device.expansion.vo.feign.request;


import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class FeignStreamOperationReq{

    /**
     * 流id
     */
    private String streamId;

    /**
     * 通道id
     */
    private Long channelId;


}
