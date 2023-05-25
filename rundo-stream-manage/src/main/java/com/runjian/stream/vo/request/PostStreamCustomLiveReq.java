package com.runjian.stream.vo.request;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import com.runjian.stream.constant.TransferMode;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/4/25 11:47
 */
@Data
public class PostStreamCustomLiveReq implements ValidatorFunction {

    /**
     * 识别编码
     */
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
    private String port;

    /**
     * ip地址（推流模式不需要）
     */
    private String ip;

    /**
     * 流传输模式 1-推流 2-拉流
     */
    @NotNull(message = "流传输模式不能为空")
    @Range(min = 1, max = 2, message = "非法流传输模式")
    private Integer transferMode;

    /**
     * 是否播放音频
     */
    @NotNull(message = "播放音频选项不能为空")
    private Boolean enableAudio;

    /**
     * 是否使用ssrc
     */
    @NotNull(message = "ssrc选项不能为空")
    private Boolean ssrcCheck;

    /**
     * 流类型 (1-udp 2-tcp)
     */
    @NotNull(message = "流类型不能为空")
    private Integer streamMode;

    /**
     * 录像状态
     */
    @NotNull(message = "录像状态不能为空")
    @Range(min = 0, max = 1, message = "非法录像状态")
    private Integer recordState;

    /**
     * 自动关闭状态
     */
    @NotNull(message = "自动关闭状态不能为空")
    @Range(min = 0, max = 1, message = "非法自动关闭状态")
    private Integer autoCloseState;


    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (transferMode.equals(TransferMode.PULL.getCode()) && Objects.isNull(ip)){
            result.setHasErrors(true);
            result.setErrorMsgMap(Map.of("ip", "拉流模式下，拉流的ip不能为空"));
        }
    }
}
