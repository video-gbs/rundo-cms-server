package com.runjian.auth.server.mapper.system;

import com.runjian.auth.server.entity.system.SysOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    Integer saveUserOrg(@Param("userId") Long userId, @Param("orgId") Long orgId);

    List<SysOrg> selectOrgTree(@Param("orgId") Long orgId, @Param("orgName") String orgName);

    Long getByUserId(@Param("userId") Long userId);
}
