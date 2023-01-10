package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.SysAppInfoDTO;
import com.runjian.auth.server.entity.SysAppInfo;
import com.runjian.auth.server.mapper.app.SysAppApiMapper;
import com.runjian.auth.server.mapper.system.SysAppInfoMapper;
import com.runjian.auth.server.service.system.SysAppInfoService;
import com.runjian.auth.server.util.SnowflakeUtil;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用信息 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysAppInfoServiceImpl extends ServiceImpl<SysAppInfoMapper, SysAppInfo> implements SysAppInfoService {

    @Autowired
    private SnowflakeUtil idUtil;

    @Autowired
    private SysAppInfoMapper sysAppInfoMapper;

    @Autowired
    private SysAppApiMapper sysAppApiMapper;

    @Override
    public CommonResponse addSysAppInfo(SysAppInfoDTO dto) {
        SysAppInfo sysAppInfo = new SysAppInfo();
        sysAppInfo.setId(idUtil.nextId());
        sysAppInfo.setAppName(dto.getAppName());
        sysAppInfo.setAppIp(dto.getAppIp());
        sysAppInfo.setAppPort(dto.getAppPort());
        sysAppInfo.setAppDesc(dto.getAppDesc());
        sysAppInfo.setStatus(dto.getStatus().toString());
        // sysAppInfo.setTenantId();
        // sysAppInfo.setDeleteFlag();
        // sysAppInfo.setCreatedBy();
        // sysAppInfo.setUpdatedBy();
        // sysAppInfo.setCreatedTime();
        // sysAppInfo.setUpdatedTime();
        return CommonResponse.success(sysAppInfoMapper.insert(sysAppInfo));
    }

    @Override
    public CommonResponse updateSysAppInfo(SysAppInfoDTO dto) {
        return null;
    }
}
