package com.runjian.device.expansion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("rundo_channel_preset_lists")
@ApiModel(value = "预置位信息表", description = "接口信息表")
public class ChannelPresetLists {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("通道id")
    private Long channelExpansionId;

    @ApiModelProperty("预置位id")
    private Integer presetId;

    @ApiModelProperty("预置位名称")
    private String presetName;





    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("编辑时间")
    private Date updatedAt;

    @ApiModelProperty("删除标记")
    private Integer deleted;








}
