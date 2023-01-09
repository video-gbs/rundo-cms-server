package com.runjian.auth.server.mapper.role;

import com.runjian.auth.server.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {


    /**
     * 根据用户角色查询用户Menu菜单权限列表
     *
     * @return
     */
    List<String> selectMenuAuthByroleCodes(@Param("roleCodes") List<String> roleCodes);

}
