package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.domain.vo.system.EditSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.OrgInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, UserInfo> implements SysUserInfoService {
    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Override
    public void saveSysUserInfo(AddSysUserInfoDTO dto) {
        // 处理基本信息
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(dto, userInfo);
        // 处理密码
        String password = passwordUtil.encode(dto.getPassword());
        userInfo.setPassword(password);
        // sysUserInfo.setTenantId();
        sysUserInfoMapper.insert(userInfo);
        // 处理部门信息
        sysUserInfoMapper.insertUserOrg(userInfo.getId(), dto.getOrgId());
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (roleIds.size() > 0) {
            for (Long roleId : roleIds) {
                sysUserInfoMapper.insertUserRole(userInfo.getId(), roleId);
            }
        }


    }

    @Override
    public EditSysUserInfoVO getSysUserInfoById(Long id) {
        EditSysUserInfoVO vo = new EditSysUserInfoVO();
        UserInfo userInfo = sysUserInfoMapper.selectById(id);
        BeanUtils.copyProperties(userInfo, vo);
        OrgInfoVO orgInfoVO = sysUserInfoMapper.selectOrgInfoByUserId(id);
        List<Long> roleIds = sysUserInfoMapper.selectRoleByUserId(id);
        vo.setPassword(null);
        vo.setRePassword(null);
        vo.setOrgId(orgInfoVO.getOrgId());
        vo.setOrgName(orgInfoVO.getOrgName());
        vo.setRoleIds(roleIds);
        return vo;
    }

    @Override
    public void updateSysUserInfo(UpdateSysUserInfoDTO dto) {
        // 根据id查取原始信息
        UserInfo userInfo = sysUserInfoMapper.selectById(dto.getId());
        // 根据id查取角色信息
        List<Long> oldRoleIds = sysUserInfoMapper.selectRoleByUserId(dto.getId());
        // 处理信息

        if (!"".equals(dto.getPassword())) {
            String password = passwordUtil.encode(dto.getPassword());
            dto.setPassword(password);
        } else {
            dto.setPassword(userInfo.getPassword());
        }

        BeanUtils.copyProperties(dto, userInfo);
        sysUserInfoMapper.updateById(userInfo);
        // 原始关联角色为空 则提交关联角色为新增
        List<Long> newRoleIds = dto.getRoleIds();
        if (CollUtil.isEmpty(oldRoleIds)) {
            for (Long roleId : newRoleIds) {
                sysUserInfoMapper.insertUserRole(userInfo.getId(), roleId);
            }
        }
        // 如果提交的角色为空，则删除所有的角色关联
        if (CollUtil.isEmpty(newRoleIds)) {
            sysUserInfoMapper.deleteUserRole(userInfo.getId(), null);
        }
        // 提交的角色与原始的角色均不为空
        // 采取Lambda表达式取得相同的角色
        List<Long> common = oldRoleIds.stream().filter(p -> newRoleIds.contains(p)).collect(Collectors.toList());
        // 原始角色列表剔除相同部分后删除授权
        oldRoleIds.removeAll(common);
        for (Long roleId : oldRoleIds) {
            sysUserInfoMapper.deleteUserRole(userInfo.getId(), roleId);
        }
        // 新提交的角色列表剔除相同部分后新增授权
        newRoleIds.removeAll(common);
        for (Long roleId : newRoleIds) {
            sysUserInfoMapper.insertUserRole(userInfo.getId(), roleId);
        }

    }


    @Override
    public void changeStatus(StatusSysUserInfoDTO dto) {
        UserInfo userInfo = sysUserInfoMapper.selectById(dto.getId());
        userInfo.setStatus(dto.getStatus());
        sysUserInfoMapper.updateById(userInfo);
    }

    @Override
    public Page<ListSysUserInfoVO> getSysUserInfoByPage(QuerySysUserInfoDTO dto) {
        Long zero = 0L;
        PageSysUserInfoDTO page = new PageSysUserInfoDTO();
        if (dto.getOrgId() != null && !zero.equals(dto.getOrgId())) {
            page.setOrgId(dto.getOrgId());
        }
        if (null != dto.getUserName() && "".equals(dto.getUserName())) {
            page.setUserName(dto.getUserName());
        }
        page.setUserAccount(dto.getUserAccount());
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return sysUserInfoMapper.MySelectPage(page);
    }

    @Override
    public void removeSysUserInfoById(Long id) {
        sysUserInfoMapper.deleteById(id);
    }

    @Override
    public void batchRemoveSysUserInfo(List<Long> ids) {
        for (Long id : ids) {
            sysUserInfoMapper.deleteById(id);
        }
    }

    @Override
    public RelationSysUserInfoVO getRelationSysUserInfoById(Long id) {
        return sysUserInfoMapper.selectRelationSysUserInfoById(id);
    }

    @Override
    public Page<RelationSysUserInfoVO> getRelationSysUserInfoList(QueryRelationSysUserInfoDTO dto) {
        PageRelationSysUserInfoDTO page = new PageRelationSysUserInfoDTO();
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return sysUserInfoMapper.relationSysUserInfoPage(page);
    }


}
