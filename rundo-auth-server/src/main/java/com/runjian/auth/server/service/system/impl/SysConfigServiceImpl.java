package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.entity.system.SysConfig;
import com.runjian.auth.server.mapper.system.SysConfigMapper;
import com.runjian.auth.server.service.system.SysConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统全局参数配置 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
}
