package com.runjian.auth.server.entity.system;

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
 * 系统全局参数配置
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("tb_sys_config")
@ApiModel(value = "SysConfig对象", description = "系统全局参数配置")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("参数名称")
    @TableField("param_name")
    private String paramName;

    @ApiModelProperty("参数编码")
    @TableField("param_code")
    private String paramCode;

    @ApiModelProperty("参数值")
    @TableField("param_value")
    private String paramValue;

    @ApiModelProperty("参数描述")
    @TableField("param_desc")
    private String paramDesc;

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
