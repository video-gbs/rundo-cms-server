package com.runjian.device.expansion.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long videoAreaId;

    @NotNull(message = "安防区域id的目标value不能为空")
    @JsonProperty(value = "pResourceValue")
    private String pResourceValue;

    @ApiModelProperty("通道信息列表数据")
    @NotNull(message = "通道信息列表数据不得为空")
    private List<DeviceChannelExpansionAddReq> channelList;
}
