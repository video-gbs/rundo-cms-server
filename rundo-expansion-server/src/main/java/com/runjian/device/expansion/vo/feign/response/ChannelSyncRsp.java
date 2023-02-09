package com.runjian.device.expansion.vo.feign.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户主动同步返回值
 * @author Miracle
 * @date 2023/1/9 9:55
 */
@Data
@ApiModel(value = "设备通道同步")
public class ChannelSyncRsp {

    /**
     * 设备通道总数
     */
    @ApiModelProperty("设备通道总数")
    private Integer total;

    /**
     * 同步成功的通道数
     */
    @ApiModelProperty("同步成功的通道数")
    private Integer num;

    /**
     * 设备ID
     */
    private Long deviceId;



}
