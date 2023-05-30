package com.runjian.foreign.server.vo.feign.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/4/25 11:47
 */
@Data
public class PushStreamCustomLiveFeignReq {

    /**
     * 识别编码
     */
    @NotBlank(message = "识别编码不能为空")
    @Range(min = 1, message = "非法设备识别编码")
    private Long code;

    /**
     * 流媒体服务Id
     */
    @NotNull(message = "流媒体服务id不能为空")
    @Range(min = 1, message = "非法流媒体id")
    private Long dispatchId;

    /**
     * 协议
     */
    @NotBlank(message = "流传输协议不能为空")
    private String protocol;

    /**
     * 端口
     */
    @NotBlank(message = "端口不能为空")
    private String port = "0";

    /**
     * ip地址（推流模式不需要）
     */
    private String ip = "";

    /**
     * 流传输模式 1-推流 2-拉流
     */
    @NotNull(message = "流传输模式不能为空")
    @Range(min = 1, max = 2, message = "非法流传输模式")
    private Integer transferMode = 1;

    /**
     * 是否播放音频
     */
    @NotNull(message = "播放音频选项不能为空")
    private Boolean enableAudio = true;

    /**
     * 是否使用ssrc
     */
    @NotNull(message = "ssrc选项不能为空")
    private Boolean ssrcCheck = true;

    /**
     * 流类型 (1-udp 2-tcp)
     */
    @NotNull(message = "流类型不能为空")
    private Integer streamMode = 2;

    /**
     * 录像状态
     */
    @NotNull(message = "录像状态不能为空")
    @Range(min = 0, max = 1, message = "非法录像状态")
    private Integer recordState = 0;

    /**
     * 自动关闭状态
     */
    @NotNull(message = "自动关闭状态不能为空")
    @Range(min = 0, max = 1, message = "非法自动关闭状态")
    private Integer autoCloseState = 1;


}