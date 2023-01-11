package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.domain.vo.SysOrgVO;
import com.runjian.auth.server.entity.system.SysOrg;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.auth.server.util.SnowflakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SnowflakeUtil idUtis;

    @Autowired
    private SysOrgMapper sysOrgMapper;


    @Override
    public ResponseResult addSysOrg(SysOrgDTO dto) {
        SysOrg sysOrg = new SysOrg();

        return new ResponseResult(200, "操作成功", sysOrgMapper.insert(sysOrg));
    }

    @Override
    public ResponseResult deleteSysOrg() {
        return null;
    }

    @Override
    public SysOrgVO getSysOrgById(Long id) {
        return null;
    }

    @Override
    public ResponseResult moveSysOrg() {
        return null;
    }

    @Override
    public ResponseResult updateSysOrg(SysOrgDTO dto) {
        return null;
    }
}
