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
 * 接口信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:43:05
 */
@Getter
@Setter
@TableName("tb_sys_api_info")
public class SysApiInfo extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 接口直接父ID
     */
    @TableField("api_pid")
    private Long apiPid;

    /**
     * 接口间接父ID
     */
    @TableField("api_pids")
    private String apiPids;

    /**
     * 接口名称
     */
    @TableField("api_name")
    private String apiName;

    /**
     * 接口排序
     */
    @TableField("api_sort")
    private String apiSort;

    /**
     * 接口层级
     */
    @TableField("api_level")
    private String apiLevel;

    /**
     * 跳转链接
     */
    @TableField("url")
    private String url;

    /**
     * 是否叶子节点
     */
    @TableField("leaf")
    private String leaf;

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
