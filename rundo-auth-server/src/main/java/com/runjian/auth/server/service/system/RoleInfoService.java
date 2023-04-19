package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.RoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.RoleDetailVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.domain.vo.tree.AppMenuApiTree;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface RoleInfoService extends IService<RoleInfo> {

    void save(SysRoleInfoDTO dto);

    void modifyById(SysRoleInfoDTO dto);

    Page<SysRoleInfoVO> findByPage(QuerySysRoleInfoDTO dto);

    void deleteById(Long id);

    void erasureBatch(List<Long> ids);

    Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto);

    RoleDetailVO getRoleDetailById(Long id);

    void modifyByStatus(StatusSysRoleInfoDTO dto);

    List<AppMenuApiTree> getAppMenuApiTree(Integer appType);

    void addRelationUser(RoleRelationUserDTO dto);

    Page<RelationSysUserInfoVO> listRelationUser(QueryRoleRelationSysUserInfoDTO dto);

    void rightRelationUser(RoleRelationUserDTO dto);

    void leftRelationUser(RoleRelationUserDTO dto);

    void saveRoleUser(Long roleId, Long userId);

    List<Long> getRoleByUserId(Long userId);

    void removeRoleUser(Long roleId, Long userId);
}
