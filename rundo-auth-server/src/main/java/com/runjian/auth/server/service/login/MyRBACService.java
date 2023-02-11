package com.runjian.auth.server.service.login;

import com.runjian.auth.server.domain.entity.*;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MyRBACService
 * @Description
 * @date 2023-02-09 周四 5:17
 */
public interface MyRBACService {

    /**
     * 通过用户账户，查取用户信息
     *
     * @param userName
     * @return
     */
    SysUserInfo findUserInfoByUserAccount(String userName);

    /**
     * 通过用户编号，查取用户所有的角色信息
     *
     * @param id
     * @return
     */
    List<String> findRoleInfoByUserAccount(Long id);

    /**
     * 通过角色编码，查取角色已有的应用信息
     *
     * @param roleCode
     * @return
     */
    List<SysAppInfo> findAppIdByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有的菜单信息
     *
     * @param roleCode
     * @return
     */
    List<SysMenuInfo> findMenuInfoByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有的API URL
     *
     * @param roleCode
     * @return
     */
    List<String> findApiUrlByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有的角色
     *
     * @param roleCode
     * @return
     */
    List<SysApiInfo> findApiInfoByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有部门信息
     *
     * @param roleCode
     * @return
     */
    List<SysOrg> findSysOrgByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有的安防区域信息
     *
     * @param roleCode
     * @return
     */
    List<VideoArea> findAreaByRoleCode(String roleCode);

    /**
     * 通过角色编码，查取角色已有的视频通道信息
     *
     * @param roleCode
     * @return
     */
    List<String> findChannelByRoleCode(String roleCode);


    void insertRoleApp(Long roleId, Long appId);

    void insertRoleMenu(Long roleId, Long menuId);

    void insertRoleApi(Long roleId, Long apiId);

    void insertRoleOrg(Long roleId, Long orgId);

    void insertRoleArea(Long roleId, Long areaId);

    void removeRoleApp(Long roleId, Long appId);

    void removeRoleMenu(Long roleId, Long menuId);

    void removeRoleApi(Long roleId, Long apiId);

    void removeRoleOrg(Long roleId, Long orgId);

    void removeRoleArea(Long roleId, Long areaId);
}
