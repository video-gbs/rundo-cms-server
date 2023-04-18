package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.auth.server.domain.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {

    /**
     * 根据应用ID获取应用的菜单列表
     * @param appId
     * @return
     */
    List<MenuInfo> selectByAppId(@Param("appId") Long appId);

    /**
     * 根据角色编码查询菜单权限
     *
     * @param roleCode
     * @return
     */
    List<MenuInfo> selectMenuByRoleCode(@Param("roleCode") String roleCode);
}
