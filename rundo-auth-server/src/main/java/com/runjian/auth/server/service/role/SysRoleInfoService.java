package com.runjian.auth.server.service.role;

import com.runjian.auth.server.domain.dto.SysRoleInfoDTO;
import com.runjian.auth.server.entity.SysRoleInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.common.config.response.CommonResponse;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysRoleInfoService extends IService<SysRoleInfo> {

    CommonResponse addRole(SysRoleInfoDTO dto);
}
