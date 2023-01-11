package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysAppInfoDTO;
import com.runjian.auth.server.entity.system.SysAppInfo;
import com.runjian.auth.server.mapper.system.SysAppInfoMapper;
import com.runjian.auth.server.service.system.SysAppInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
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
    private RundoIdUtil idUtil;

    @Autowired
    private SysAppInfoMapper sysAppInfoMapper;

    @Override
    public ResponseResult addSysAppInfo(SysAppInfoDTO dto) {
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
        return new ResponseResult(200, "操作成功", sysAppInfoMapper.insert(sysAppInfo));
    }

    @Override
    public ResponseResult updateSysAppInfo(SysAppInfoDTO dto) {
        return null;
    }
}
