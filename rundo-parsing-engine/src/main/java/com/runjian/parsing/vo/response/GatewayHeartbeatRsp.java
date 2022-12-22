package com.runjian.parsing.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GatewayHeartbeatRsp {

    /**
     * 是否成功
     */
    @JsonIgnore
    private Long gatewayId;

    /**
     * 是否需要重新注册
     */
    private Boolean needReSignIn;
}
