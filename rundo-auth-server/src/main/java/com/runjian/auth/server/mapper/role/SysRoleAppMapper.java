package com.runjian.auth.server.mapper.role;

import com.runjian.auth.server.entity.SysRoleApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    /**
     * 根据用户角色查询用户APP应用权限列表
     *
     * @return
     */
    List<String> selectAppAuthByroleCodes(@Param("roleCodes") List<String> roleCodes);
}
