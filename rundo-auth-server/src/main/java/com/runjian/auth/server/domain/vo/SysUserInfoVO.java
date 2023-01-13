package com.runjian.auth.server.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysUserInfoDTO
 * @Description 新建用户
 * @date 2023-01-10 周二 9:30
 */
@Data
@ApiModel(value = "新建用户", description = "用户信息")
public class SysUserInfoVO {

    @ApiModelProperty("用户编号")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("逻辑删除")
    private String deleteFlag;

    @ApiModelProperty("有效期起")
    private Date expiryDateStart;
    @ApiModelProperty("有效期止")
    private Date expiryDateEnd;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("所属部门ID")
    private Long orgId;

    @ApiModelProperty("所属部门名称")
    private Long orgName;

    @ApiModelProperty("角色ID列表")
    Map<Long, String> roleIds;


}
