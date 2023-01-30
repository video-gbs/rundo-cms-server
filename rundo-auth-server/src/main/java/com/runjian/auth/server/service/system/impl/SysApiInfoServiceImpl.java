package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.entity.system.SysApiInfo;
import com.runjian.auth.server.mapper.system.SysApiInfoMapper;
import com.runjian.auth.server.model.dto.system.SysApiInfoDTO;
import com.runjian.auth.server.service.system.SysApiInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysApiInfoServiceImpl extends ServiceImpl<SysApiInfoMapper, SysApiInfo> implements SysApiInfoService {
    @Autowired
    private RundoIdUtil idUtil;

    @Autowired
    private SysApiInfoMapper sysApiInfoMapper;

    @Override
    public void saveSysApiInfo(SysApiInfoDTO dto) {
        SysApiInfo sysApiInfo = new SysApiInfo();
        sysApiInfo.setApiPid(dto.getApiPid());
        // sysApiInfo.setApiPids();
        sysApiInfo.setApiName(dto.getApiName());
        sysApiInfo.setApiSort(dto.getApiSort());
        // sysApiInfo.setApiLevel();
        sysApiInfo.setUrl(dto.getUrl());
        sysApiInfo.setLeaf(0);
        sysApiInfo.setStatus(dto.getStatus());
        // sysApiInfo.setTenantId();
        // sysApiInfo.setDeleteFlag();
        // sysApiInfo.setCreatedBy();
        // sysApiInfo.setUpdatedBy();
        // sysApiInfo.setCreatedTime();
        // sysApiInfo.setUpdatedTime();
        sysApiInfoMapper.insert(sysApiInfo);
    }

}
