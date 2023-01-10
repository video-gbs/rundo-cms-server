package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.dto.SysMenuInfoDTO;
import com.runjian.auth.server.entity.SysMenuInfo;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysMenuInfoServiceImpl extends ServiceImpl<SysMenuInfoMapper, SysMenuInfo> implements SysMenuInfoService {

    @Override
    public CommonResponse addSysMenu(SysMenuInfoDTO dto) {
        return null;
    }
}
