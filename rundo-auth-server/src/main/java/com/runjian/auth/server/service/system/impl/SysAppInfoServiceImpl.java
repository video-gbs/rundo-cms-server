package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.dto.SysAppInfoDTO;
import com.runjian.auth.server.entity.SysAppInfo;
import com.runjian.auth.server.mapper.system.SysAppInfoMapper;
import com.runjian.auth.server.service.system.SysAppInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
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

    @Override
    public CommonResponse addSysAppInfo(SysAppInfoDTO dto) {
        return null;
    }

    @Override
    public CommonResponse updateSysAppInfo(SysAppInfoDTO dto) {
        return null;
    }
}
