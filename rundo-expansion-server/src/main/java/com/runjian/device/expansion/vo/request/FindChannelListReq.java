package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class FindChannelListReq {



    @ApiModelProperty("安防区域id")
    @NotNull(message = "安防区域id不能为空")
    @Range(min = 1, message = "非法安防区域id")
    private Long videoAreaId;

    @ApiModelProperty("通道信息列表数据")
    @NotNull(message = "通道信息列表数据不得为空")
    private List<DeviceChannelExpansionAddReq> channelList;
}
