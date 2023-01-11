package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysMenuInfoDTO;
import com.runjian.auth.server.entity.system.SysMenuInfo;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysMenuInfoService extends IService<SysMenuInfo> {

    ResponseResult addSysMenu(SysMenuInfoDTO dto);
}
