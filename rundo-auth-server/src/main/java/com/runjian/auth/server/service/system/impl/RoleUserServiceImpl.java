package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.RoleApp;
import com.runjian.auth.server.domain.entity.RoleUser;
import com.runjian.auth.server.mapper.RoleAppMapper;
import com.runjian.auth.server.mapper.RoleUserMapper;
import com.runjian.auth.server.service.system.RoleAppService;
import com.runjian.auth.server.service.system.RoleUserService;
import org.springframework.stereotype.Service;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {
}
