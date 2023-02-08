package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QueryEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysRoleInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysRoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RoleDetailVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysRoleInfoService extends IService<SysRoleInfo> {

    void addRole(AddSysRoleInfoDTO dto);

    void updateRole(UpdateSysRoleInfoDTO dto);

    Page<SysRoleInfoVO> getSysRoleInfoByPage(QuerySysRoleInfoDTO dto);

    void batchRemove(List<Long> ids);

    Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto);

    RoleDetailVO getRoleDetailById(Long id);
}
