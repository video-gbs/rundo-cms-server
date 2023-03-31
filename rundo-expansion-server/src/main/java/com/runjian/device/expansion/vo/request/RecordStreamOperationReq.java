package com.runjian.device.expansion.vo.request;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class RecordStreamOperationReq{

    /**
     * 流id
     */
    @ApiModelProperty("流id")
    @NotBlank(message = "流Id不能为空")
    private String streamId;

    /**
     * 通道id
     */
    @NotBlank(message = "流Id不能为空")
    @ApiModelProperty("通道id")
    private Long channelId;


}
