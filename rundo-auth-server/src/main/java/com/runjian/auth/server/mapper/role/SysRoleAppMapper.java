package com.runjian.auth.server.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.auth.server.entity.SysRoleApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色应用关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysRoleAppMapper extends BaseMapper<SysRoleApp> {

    List<Long> selectAppByRoleId(@Param("roleId") Long roleId);
}
