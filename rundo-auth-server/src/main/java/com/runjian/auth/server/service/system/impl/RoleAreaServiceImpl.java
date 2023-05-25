package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.RoleArea;
import com.runjian.auth.server.mapper.RoleAreaMapper;
import com.runjian.auth.server.service.system.RoleAreaService;
import org.springframework.stereotype.Service;

@Service
public class RoleAreaServiceImpl  extends ServiceImpl<RoleAreaMapper, RoleArea> implements RoleAreaService {
}
