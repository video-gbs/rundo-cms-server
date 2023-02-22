package com.runjian.auth.server.domain.dto.system;

import com.runjian.auth.server.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RelationSysUserInfoDTO
 * @Description 关联用户
 * @date 2023-02-03 周五 9:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ApiModel(value = "字典分页查询", description = "应用信息查询条件")
public class QueryRelationSysUserInfoDTO extends CommonPage {
    @ApiModelProperty("用户账户")
    private String userAccount;
}
