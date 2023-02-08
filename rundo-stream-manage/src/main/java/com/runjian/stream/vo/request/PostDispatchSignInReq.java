package com.runjian.stream.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "主键id不能为空")
    @Range(min = 1, message = "非法主键Id")
    private Long dispatchId;

    /**
     * 序列号
     */
    @NotBlank(message = "主键id不能为空")
    @Size(max = 50, message = "非法调度服务序列号")
    private String serialNum;

    /**
     * ip地址
     */
    @NotBlank(message = "ip地址不能为空")
    @Size(max = 30, message = "非法ip地址")
    private String ip;

    /**
     * 端口
     */
    @NotBlank(message = "端口不能为空")
    @Size(max = 10, message = "非法端口")
    private String port;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime outTime;
}
