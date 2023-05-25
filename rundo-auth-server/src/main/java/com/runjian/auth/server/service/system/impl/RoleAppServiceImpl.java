package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.RoleApp;
import com.runjian.auth.server.mapper.RoleAppMapper;
import com.runjian.auth.server.service.system.RoleAppService;
import org.springframework.stereotype.Service;

@Service
public class RoleAppServiceImpl extends ServiceImpl<RoleAppMapper, RoleApp> implements RoleAppService {
}
