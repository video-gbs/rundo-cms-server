package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.entity.SysOrg;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.service.system.SysOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {

    @Override
    public CommonResponse addSysOrg(SysOrgDTO dto) {
        return null;
    }

    @Override
    public CommonResponse deleteSysOrg() {
        return null;
    }

    @Override
    public CommonResponse getSysOrg() {
        return null;
    }

    @Override
    public CommonResponse moveSysOrg() {
        return null;
    }
}
