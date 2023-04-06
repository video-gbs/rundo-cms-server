package com.runjian.device.expansion.vo.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenjialing
 */
@Data
@ApiModel(value = "预置位信息表", description = "接口信息表")
public class ChannelPresetListsResp {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("预置位id")
    private Integer presetId;

    @ApiModelProperty("预置位名称")
    private String presetName;

}
