package com.runjian.parsing.vo.request;

import com.runjian.parsing.entity.DispatchInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/7 18:30
 */
@Data
public class PostDispatchSignInReq {

    public PostDispatchSignInReq(DispatchInfo dispatchInfo, LocalDateTime outTime){
        BeanUtils.copyProperties(dispatchInfo, this);
        this.outTime = outTime;
        this.dispatchId = dispatchInfo.getId();
    }

    /**
     * 主键id
     */
    private Long dispatchId;

    /**
     * 网关唯一序列号 网关ID
     */
    private String serialNum;

    /**
     * 注册类型 1-MQ  2-RETFUL
     */
    private Integer signType;

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
