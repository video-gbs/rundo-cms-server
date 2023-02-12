package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.RoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RoleDetailVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.domain.vo.tree.RoleAppTree;

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

    void save(AddSysRoleInfoDTO dto);

    void modifyById(UpdateSysRoleInfoDTO dto);

    Page<SysRoleInfoVO> findByPage(QuerySysRoleInfoDTO dto);

    void erasureBatch(List<Long> ids);

    Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto);

    RoleDetailVO getRoleDetailById(Long id);

    void modifyByStatus(StatusSysRoleInfoDTO dto);

    RoleAppTree getAppIdTree(Integer appType);
}
