package com.runjian.auth.server.entity.video;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 安保区域
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("tb_video_arae")
@ApiModel(value = "VideoArae对象", description = "安保区域")
public class VideoArae implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("安防区域名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("直接上级")
    @TableField("area_pid")
    private Long areaPid;

    @ApiModelProperty("所有上级")
    @TableField("area_pids")
    private String areaPids;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("层级")
    @TableField("level")
    private String level;

    @ApiModelProperty("租户号")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private String deleteFlag;

    @ApiModelProperty("创建人")
    @TableField("created_by")
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField("updated_by")
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField("created_time")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @TableField("updated_time")
    private Date updatedTime;


}
