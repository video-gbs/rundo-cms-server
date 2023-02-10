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
        return roleInfoMapper.selectAppByRoleCode(roleCode);
    }

    @Override
    public List<SysMenuInfo> findMenuInfoByRoleCode(String roleCode) {
        return roleInfoMapper.selectMenuByRoleCode(roleCode);
    }

    @Override
    public List<String> findApiUrlByRoleCode(String roleCode) {
        return roleInfoMapper.selectApiUrlByRoleCode(roleCode);
    }

    @Override
    public List<SysApiInfo> findApiInfoByRoleCode(String roleCode) {
        return roleInfoMapper.selectApiInfoByRoleCode(roleCode);
    }

    @Override
    public List<SysOrg> findSysOrgByRoleCode(String roleCode) {
        return roleInfoMapper.selectOrgInfoByRoleCode(roleCode);
    }

    @Override
    public List<VideoArea> findAreaByRoleCode(String roleCode) {
        return roleInfoMapper.selectVideoAreaByRoleCode(roleCode);
    }

    @Override
    public List<String> findChannelByRoleCode(String roleCode) {
        return null;
    }

    @Override
    public void insertRoleApp(Long roleId, Long appId) {
        roleInfoMapper.insertRoleApp(roleId, appId);
    }

    @Override
    public void insertRoleMenu(Long roleId, Long menuId) {
        roleInfoMapper.insertRoleApp(roleId, menuId);
    }

    @Override
    public void insertRoleApi(Long roleId, Long apiId) {
        roleInfoMapper.insertRoleApp(roleId, apiId);
    }

    @Override
    public void insertRoleOrg(Long roleId, Long orgId) {
        roleInfoMapper.insertRoleApp(roleId, orgId);
    }

    @Override
    public void insertRoleArea(Long roleId, Long areaId) {
        roleInfoMapper.insertRoleApp(roleId, areaId);
    }

    @Override
    public void removeRoleApp(Long roleId, Long appId) {
        roleInfoMapper.removeRoleApp(roleId, appId);
    }

    @Override
    public void removeRoleMenu(Long roleId, Long menuId) {
        roleInfoMapper.removeRoleMenu(roleId, menuId);
    }

    @Override
    public void removeRoleApi(Long roleId, Long apiId) {
        roleInfoMapper.removeRoleApi(roleId, apiId);
    }

    @Override
    public void removeRoleOrg(Long roleId, Long orgId) {
        roleInfoMapper.removeRoleOrg(roleId, orgId);
    }

    @Override
    public void removeRoleArea(Long roleId, Long areaId) {
        roleInfoMapper.removeRoleArea(roleId, areaId);
    }


}
