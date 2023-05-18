package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.RoleMenu;
import com.runjian.auth.server.mapper.RoleMenuMapper;
import com.runjian.auth.server.service.system.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
