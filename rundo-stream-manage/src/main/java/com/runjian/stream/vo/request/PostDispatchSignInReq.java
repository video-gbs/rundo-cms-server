package com.runjian.stream.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/3 17:51
 */
@Data
public class PostDispatchSignInReq {

    /**
     * 调度id
     */
    private Long dispatchId;

    /**
     * 序列号
     */
    private String serialNum;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 过期时间
     */
    private LocalDateTime outTime;
}
