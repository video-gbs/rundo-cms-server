package com.runjian.device.expansion.vo.request;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author chenjialing
 */
@Data
@ApiModel(value = "设备通道添加请求", description = "接口信息表")
public class DeviceChannelExpansionAddReq {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("编码器id")
    @NotNull(message = "编码器id不能为空")
    @Range(min = 1, message = "非法编码器id")
    private Long deviceExpansionId;

    @ApiModelProperty("通道名称")
    @NotNull(message = "通道名称不能为空")
    private String channelName;


    @ApiModelProperty("通道编码")
    @NotNull(message = "通道编码不能为空")
    private String channelCode;



}
