package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.common.BatchDTO;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.entity.RoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /**
     * 根据用户ID查询,用户角色编码
     *
     * @param userId
     * @return
     */
    List<String> selectRoleCodeByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询,用户角色id
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleByUserId(@Param("userId") Long userId);

    Page<SysRoleInfoVO> MySelectPage(PageSysRoleInfoDto page);

    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);

    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRoleRelationSysUserInfoDTO page);

    /**** 角色应用相关 ****/
    void insertRoleApp(@Param("roleId") Long roleId, @Param("appId") Long appId);

    void batchInsertRoleApp(@Param("appList") List<BatchDTO> appList);

    void removeRoleApp(@Param("roleId") Long roleId, @Param("appId") Long appId);


    /**** 角色菜单相关 ****/
    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    void batchInsertRoleMenu(@Param("menuList") List<BatchDTO> menuList);

    void removeRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);


    /**** 角色接口相关 ****/
    void insertRoleApi(@Param("roleId") Long roleId, @Param("apiId") Long apiId);

    void batchInsertRoleApi(@Param("apiList") List<BatchDTO> apiList);

    void removeRoleApi(@Param("roleId") Long roleId, @Param("apiId") Long apiId);


    /**** 角色部门相关 ****/
    void insertRoleOrg(@Param("roleId") Long roleId, @Param("orgId") Long orgId);

    void batchInsertRoleOrg(@Param("orgList") List<BatchDTO> orgList);

    void removeRoleOrg(@Param("roleId") Long roleId, @Param("orgId") Long orgId);


    /**** 角色区域相关 ****/
    void insertRoleArea(@Param("roleId") Long roleId, @Param("areaId") Long areaId);

    void batchInsertRoleArea(@Param("areaList") List<BatchDTO> areaList);

    void removeRoleArea(@Param("roleId") Long roleId, @Param("areaId") Long areaId);


    /**** 角色用户相关 ****/
    void insertRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

    void batchInsertRoleUser(@Param("userList") List<BatchDTO> userList);

    void removeRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

}
