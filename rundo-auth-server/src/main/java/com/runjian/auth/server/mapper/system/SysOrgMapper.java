package com.runjian.auth.server.mapper.system;

import com.runjian.auth.server.entity.system.SysOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 组织机构表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysOrgMapper extends BaseMapper<SysOrg> {

    int saveUserOrg(@Param("userId")Long userId, @Param("orgId")Long orgId);
}
