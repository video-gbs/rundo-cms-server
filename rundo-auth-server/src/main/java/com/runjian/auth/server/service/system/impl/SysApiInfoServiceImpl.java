package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.dto.SysApiInfoDTO;
import com.runjian.auth.server.entity.SysApiInfo;
import com.runjian.auth.server.mapper.system.SysApiInfoMapper;
import com.runjian.auth.server.service.system.SysApiInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.util.SnowflakeUtil;
import com.runjian.common.config.response.CommonResponse;
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
    private SnowflakeUtil idUtil;

    @Autowired
    private SysApiInfoMapper sysApiInfoMapper;

    @Override
    public CommonResponse addSysApi(SysApiInfoDTO dto) {
        SysApiInfo sysApiInfo = new SysApiInfo();
        Long apiId = idUtil.nextId();
        sysApiInfo.setId(apiId);
        sysApiInfo.setApiPid(dto.getApiPid());
        sysApiInfo.setApiPids(dto.getApiPids());
        sysApiInfo.setApiName(dto.getApiName());
        sysApiInfo.setApiSort(dto.getApiSort());
        sysApiInfo.setUrl(dto.getUrl());
        sysApiInfo.setApiSort(dto.getApiSort());
        sysApiInfo.setStatus(dto.getStatus());
        // sysApiInfo.setApiLevel();
        // sysApiInfo.setLeaf();
        // sysApiInfo.setTenantId();
        // sysApiInfo.setDeleteFlag();
        // sysApiInfo.setCreatedBy();
        // sysApiInfo.setUpdatedBy();
        // sysApiInfo.setCreatedTime();
        // sysApiInfo.setUpdatedTime();

        return CommonResponse.success(sysApiInfoMapper.insert(sysApiInfo));
    }
}
