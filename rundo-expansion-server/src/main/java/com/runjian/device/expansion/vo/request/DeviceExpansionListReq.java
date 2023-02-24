package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class DeviceExpansionListReq {
    @ApiModelProperty("设备名称")
    @NotNull(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty("设备类型")
    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;

    @ApiModelProperty("ip")
    @NotNull(message = "ip不能为空")
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
