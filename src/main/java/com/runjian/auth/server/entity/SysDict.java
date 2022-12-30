package com.runjian.auth.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.runjian.common.base.BaseDomain;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:43:05
 */
@Getter
@Setter
@TableName("tb_sys_dict")
public class SysDict extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 分组名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 分组编码
     */
    @TableField("group_code")
    private String groupCode;

    /**
     * 字典项名称
     */
    @TableField("item_name")
    private String itemName;

    /**
     * 字典值
     */
    @TableField("item_value")
    private String itemValue;

    /**
     * 字典描述
     */
    @TableField("item_desc")
    private String itemDesc;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 逻辑删除
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private String deleteFlag;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;


}
