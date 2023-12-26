package com.runjian.stream.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/8 15:23
 */
@Data
public class PostStreamPushInitReq {

    /**
     * 通道id
     */
    @NotNull(message = "通道id不能为空")
    @Min(value = 1, message = "通道id不能小于1")
    private Long channelId;

    /**
     * ssrc
     */
    @NotBlank(message = "ssrc不能为空")
    @Size(max = 100, message = "ssrc长度不能超过100")
    private String ssrc;

    /**
     * 目标地址
     */
    @NotBlank(message = "目标地址不能为空")
    @Size(max = 500, message = "目标地址长度不能超过500")
    private String dstUrl;

    /**
     * 目标端口
     */
    @NotNull(message = "目标端口不能为空")
    @Range(min= 1, max = 65535, message = "目标端口范围为1-65535")
    private Integer dstPort;

    /**
     * 传输模式 0:udp 1:tcp
     */
    @NotNull(message = "传输模式不能为空")
    @Range(min= 0, max = 2, message = "非法传输模式")
    private Integer transferMode;

    /**
     * 录像开始时间
     */
    private LocalDateTime startTime;

    /**
     * 录像结束时间
     */
    private LocalDateTime endTime;
}
