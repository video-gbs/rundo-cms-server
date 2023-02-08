package com.runjian.auth.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.entity.system.*;
import com.runjian.auth.server.domain.entity.video.VideoArea;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
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
public interface SysRoleInfoMapper extends BaseMapper<SysRoleInfo> {

    /**
     * 根据用户ID查询用户角色
     *
     * @param userId
     * @return
     */
    List<String> selectRoleCodeByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询应用权限
     *
     * @param roleCode
     * @return
     */
    List<SysAppInfo> findAppByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色编码查询菜单权限
     *
     * @param roleCode
     * @return
     */
    List<SysMenuInfo> findMenuByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的API URL
     *
     * @param roleCode
     * @return
     */
    List<String> findApiUrlByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的角色
     *
     * @param roleCode
     * @return
     */
    List<SysApiInfo> findApiInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有部门信息
     *
     * @param roleCode
     * @return
     */
    List<SysOrg> findOrgInfoByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色编码，查取角色已有的安防区域信息
     *
     * @param roleCode
     * @return
     */
    List<VideoArea> findVideoAreaByRoleCode(@Param("roleCode") String roleCode);

    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);

    Page<SysRoleInfoVO> MySelectPage(PageSysRoleInfoDto page);

    void insertRoleApp(@Param("roleId") Long roleId, @Param("appId") Long appId);

    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    void insertRoleApi(@Param("roleId") Long roleId, @Param("apiId") Long apiId);

    void insertRoleOrg(@Param("roleId") Long roleId, @Param("orgId") Long orgId);

    void insertRoleArea(@Param("roleId") Long roleId, @Param("areaId") Long areaId);


}
