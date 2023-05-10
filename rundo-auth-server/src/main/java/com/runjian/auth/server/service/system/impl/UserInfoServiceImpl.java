package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.constant.StatusConstant;
import com.runjian.auth.server.domain.dto.page.PageRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.QueryRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.SysUserInfoDTO;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.domain.vo.system.EditSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.OrgInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.mapper.OrgInfoMapper;
import com.runjian.auth.server.mapper.UserInfoMapper;
import com.runjian.auth.server.service.system.OrgInfoService;
import com.runjian.auth.server.service.system.RoleInfoService;
import com.runjian.auth.server.service.system.UserInfoService;
import com.runjian.auth.server.service.system.VideoAreaService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private OrgInfoMapper orgInfoMapper;

    @Autowired
    private OrgInfoService orgInfoService;

    @Lazy
    @Autowired
    private RoleInfoService roleInfoService;

    @Autowired
    private VideoAreaService videoAreaService;

    @Override
    public void save(SysUserInfoDTO dto) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserAccount, dto.getUserAccount());
        long count = userInfoMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "账号已存在");
        }
        // 处理基本信息
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(dto, userInfo);
        // 处理密码
        String password = passwordUtil.encode(dto.getPassword());
        userInfo.setPassword(password);
        userInfo.setStatus(StatusConstant.ENABLE);
        // sysUserInfo.setTenantId();
        userInfoMapper.insert(userInfo);
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (roleIds.size() > 0) {
            saveRoleUser(roleIds, userInfo.getId());
        }
    }

    @Override
    public EditSysUserInfoVO findById(Long id) {
        EditSysUserInfoVO vo = new EditSysUserInfoVO();
        UserInfo userInfo = userInfoMapper.selectById(id);
        BeanUtils.copyProperties(userInfo, vo);
        OrgInfoVO orgInfoVO = orgInfoService.getOrgInfoByUserId(id);
        List<Long> roleIds = roleInfoService.getRoleByUserId(id);
        vo.setPassword(null);
        vo.setRePassword(null);
        vo.setOrgId(orgInfoVO.getOrgId());
        vo.setOrgName(orgInfoVO.getOrgName());
        vo.setRoleIds(roleIds);
        return vo;
    }

    @Override
    public void modifyById(SysUserInfoDTO dto) {
        // 根据id查取原始信息
        UserInfo userInfo = userInfoMapper.selectById(dto.getId());
        // 处理信息
        if (!"".equals(dto.getPassword())) {
            String password = passwordUtil.encode(dto.getPassword());
            dto.setPassword(password);
        } else {
            dto.setPassword(userInfo.getPassword());
        }
        BeanUtils.copyProperties(dto, userInfo);
        userInfoMapper.updateById(userInfo);

        // 根据id查取角色信息
        List<Long> oldRoleIds = roleInfoService.getRoleByUserId(dto.getId());
        log.debug("原始角色信息{}", JSONUtil.toJsonStr(oldRoleIds));
        // 获取本次传输得角色ID
        List<Long> newRoleIds = dto.getRoleIds();
        log.debug("本次传输得角色信息{}", JSONUtil.toJsonStr(newRoleIds));
        // 取出要回收的角色权限
        List<Long> commonRoleIds = newRoleIds.stream().filter(oldRoleIds::contains).collect(Collectors.toList());
        log.debug("新旧相同的角色信息{}", JSONUtil.toJsonStr(commonRoleIds));
        // 取出要回收的角色信息
        List<Long> removeRoleIds = oldRoleIds.stream().filter(item -> !commonRoleIds.contains(item)).collect(Collectors.toList());
        log.debug("要回收的角色信息{}", JSONUtil.toJsonStr(removeRoleIds));
        removeRoleUser(removeRoleIds, userInfo.getId());
        // 取出要新的授权的角色权限
        List<Long> roleIds = newRoleIds.stream().filter(item -> !commonRoleIds.contains(item)).collect(Collectors.toList());
        log.debug("新授权的角色信息{}", JSONUtil.toJsonStr(roleIds));
        saveRoleUser(roleIds, userInfo.getId());
    }

    @Override
    public void modifyByStatus(StatusSysUserInfoDTO dto) {
        UserInfo userInfo = userInfoMapper.selectById(dto.getId());
        userInfo.setStatus(dto.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Page<ListSysUserInfoVO> findByPage(QuerySysUserInfoDTO dto) {
        PageSysUserInfoDTO page = new PageSysUserInfoDTO();
        List<Long> orgIds = new ArrayList<>();
        switch (dto.getContain()) {
            case 0: { // 没有勾选 查询包含下级子节点
                if (null != dto.getUserName() && !"".equals(dto.getUserName())) {
                    page.setUserName(dto.getUserName());
                }
                if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
                    page.setUserAccount(dto.getUserAccount());
                }
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
                orgIds = Collections.singletonList(dto.getOrgId());
                return userInfoMapper.MySelectPageByContain(page, orgIds);
            }
            case 1: { // 已经勾选 查询包含下级子节点
                // 查询当前组织的下级组织，并获取ID
                orgIds = orgInfoMapper.mySelectOrg(dto.getOrgId());
                if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
                    page.setUserAccount(dto.getUserAccount());
                }
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
                return userInfoMapper.MySelectPageByContain(page, orgIds);
            }
            default: {
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR);
            }
        }
    }

    @Override
    public void erasureById(Long id) {
        userInfoMapper.deleteById(id);
    }

    @Override
    public void erasureBatch(List<Long> ids) {
        userInfoMapper.deleteBatchIds(ids);
    }

    @Override
    public RelationSysUserInfoVO findRelationById(Long id) {
        RelationSysUserInfoVO vo = userInfoMapper.selectRelationSysUserInfoById(id);
        List<String> areaNameList = videoAreaService.getAreaNameByUserId(id);
        String areaName = String.join(",", areaNameList);
        vo.setAreaName(areaName);
        return vo;
    }

    @Override
    public Page<RelationSysUserInfoVO> findRelationList(QueryRelationSysUserInfoDTO dto) {
        PageRelationSysUserInfoDTO page = new PageRelationSysUserInfoDTO();
        if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
            page.setUserAccount(dto.getUserAccount());
        }
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
        return userInfoMapper.relationSysUserInfoPage(page);
    }

    @Override
    public List<Long> getUserIdListByRoleId(Long roleId) {
        return userInfoMapper.selectUserIdListByRoleId(roleId);
    }

    private void saveRoleUser(List<Long> roleIds, Long userId) {
        for (Long roleId : roleIds) {
            roleInfoService.saveRoleUser(roleId, userId);
        }
    }

    private void removeRoleUser(List<Long> roleIds, Long userId) {
        for (Long roleId : roleIds) {
            roleInfoService.removeRoleUser(roleId, userId);
        }
    }
}
