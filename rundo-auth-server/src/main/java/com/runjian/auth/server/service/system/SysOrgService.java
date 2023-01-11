package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.domain.vo.SysOrgVO;
import com.runjian.auth.server.entity.system.SysOrg;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysOrgService extends IService<SysOrg> {

    ResponseResult addSysOrg(SysOrgDTO dto);

    SysOrgVO getSysOrgById(Long id);

    ResponseResult deleteSysOrg();

    ResponseResult moveSysOrg();

    ResponseResult updateSysOrg(SysOrgDTO dto);
}
