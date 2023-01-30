package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysRoleInfo;

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

    ResponseResult updateRole(AddSysRoleInfoDTO dto);
}
