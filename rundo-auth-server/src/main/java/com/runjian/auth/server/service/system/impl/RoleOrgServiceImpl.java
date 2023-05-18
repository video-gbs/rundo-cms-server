package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.RoleOrg;
import com.runjian.auth.server.mapper.RoleOrgMapper;
import com.runjian.auth.server.service.system.RoleOrgService;
import org.springframework.stereotype.Service;

@Service
public class RoleOrgServiceImpl extends ServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
}
