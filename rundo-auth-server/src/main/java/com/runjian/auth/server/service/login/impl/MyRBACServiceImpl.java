package com.runjian.auth.server.service.login.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.entity.system.*;
import com.runjian.auth.server.domain.entity.video.VideoArea;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.login.MyRBACService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AuthenticService
 * @Description 权限相关内容
 * @date 2023-02-08 周三 14:00
 */
@Service
public class MyRBACServiceImpl implements MyRBACService {
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysRoleInfoMapper roleInfoMapper;


    @Override
    public SysUserInfo findUserInfoByUserAccount(String userName) {
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, userName);
        return sysUserInfoMapper.selectOne(queryWrapper);
    }


    @Override
    public List<String> findRoleInfoByUserAccount(Long id) {
        return roleInfoMapper.selectRoleCodeByUserId(id);
    }

    @Override
    public List<SysAppInfo> findAppIdByRoleCode(String roleCode) {
        return roleInfoMapper.findAppByRoleCode(roleCode);
    }

    @Override
    public List<SysMenuInfo> findMenuInfoByRoleCode(String roleCode) {
        return roleInfoMapper.findMenuByRoleCode(roleCode);
    }

    @Override
    public List<String> findApiUrlByRoleCode(String roleCode) {
        return roleInfoMapper.findApiUrlByRoleCode(roleCode);
    }

    @Override
    public List<SysApiInfo> findApiInfoByRoleCode(String roleCode) {
        return roleInfoMapper.findApiInfoByRoleCode(roleCode);
    }

    @Override
    public List<SysOrg> findSysOrgByRoleCode(String roleCode) {
        return roleInfoMapper.findOrgInfoByRoleCode(roleCode);
    }

    @Override
    public List<VideoArea> findAreaByRoleCode(String roleCode) {
        return roleInfoMapper.findVideoAreaByRoleCode(roleCode);
    }

    @Override
    public List<String> findChannelByRoleCode(String roleCode) {

        return null;
    }

}
