package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.common.BatchDTO;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.entity.*;
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
     * 根据用户ID查询用户角色编码
     *
     * @param userId
     * @return
     */
    List<String> selectRoleCodeByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询菜单权限
     *
     * @param roleCode
     * @return
     */
    List<MenuInfo> selectMenuByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的API URL
     *
     * @param roleCode
     * @return
     */
    List<String> selectApiUrlByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的角色
     *
     * @param roleCode
     * @return
     */
    List<ApiInfo> selectApiInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有部门信息
     *
     * @param roleCode
     * @return
     */
    List<OrgInfo> selectOrgInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的安防区域信息
     *
     * @param roleCode
     * @return
     */
    List<VideoArea> selectVideoAreaByRoleCode(@Param("roleCode") String roleCode);

    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);

    Page<SysRoleInfoVO> MySelectPage(PageSysRoleInfoDto page);

    void insertRoleApp(@Param("roleId") Long roleId, @Param("appId") Long appId);
    void batchInsertRoleApp(@Param("appList") List<BatchDTO> appList);

    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    void batchInsertRoleMenu(@Param("menuList") List<BatchDTO> menuList);

    void insertRoleApi(@Param("roleId") Long roleId, @Param("apiId") Long apiId);

    void batchInsertRoleApi(@Param("apiList") List<BatchDTO> apiList);
    void insertRoleOrg(@Param("roleId") Long roleId, @Param("orgId") Long orgId);
    void batchInsertRoleOrg(@Param("orgList") List<BatchDTO> orgList);
    void insertRoleArea(@Param("roleId") Long roleId, @Param("areaId") Long areaId);
    void batchInsertRoleArea(@Param("areaList") List<BatchDTO> areaList);
    void insertRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);
    void batchInsertRoleUser(@Param("userList") List<BatchDTO> userList);

    void removeRoleApp(@Param("roleId") Long roleId, @Param("appId") Long appId);

    void removeRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    void removeRoleApi(@Param("roleId") Long roleId, @Param("apiId") Long apiId);

    void removeRoleOrg(@Param("roleId") Long roleId, @Param("orgId") Long orgId);

    void removeRoleArea(@Param("roleId") Long roleId, @Param("areaId") Long areaId);

    void removeRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

    List<Long> findAppIdList(@Param("roleId") Long roleId);

    List<Long> findMenuIdList(@Param("roleId") Long roleId);

    List<Long> findApiIdList(@Param("roleId") Long roleId);

    List<Long> findOrgIdList(@Param("roleId") Long roleId);

    List<Long> findAreaIdList(@Param("roleId") Long roleId);

    List<Long> findUserIdList(@Param("roleId") Long roleId);

    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRoleRelationSysUserInfoDTO page);

}
