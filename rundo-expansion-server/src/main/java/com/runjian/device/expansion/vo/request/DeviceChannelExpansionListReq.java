package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class DeviceChannelExpansionListReq {
    @ApiModelProperty("设备通道名称")
    private String name;

    @ApiModelProperty("设备通道类型")
    private Integer ptzType;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("状态值")
    private Integer onlineState;

    @ApiModelProperty("安放区域id")
    private Long videoAreaId;

    @ApiModelProperty("是否包含下级组织")
    private Boolean includeEquipment = true;

    @ApiModelProperty("当前页")
    private Integer pageNum = 1;

    @ApiModelProperty("总数")
    private Integer pageSize = 20;
}
