package com.runjian.auth.server.service.system.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.UserInfoVo;
import com.runjian.auth.server.entity.*;
import com.runjian.auth.server.mapper.role.SysRoleUserMapper;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.mapper.user.SysUserOrgMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.auth.server.util.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {
    @Autowired
    private SnowflakeUtil idUtil;

    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysUserOrgMapper sysUserOrgMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysRoleInfoMapper sysRoleInfoMapper;

    @Override
    public ResponseResult addUser(SysUserInfoDTO dto) {
        log.info("添加用户传参{}", JSONUtil.toJsonPrettyStr(dto));
        // 1.处理用户基本信息
        SysUserInfo sysUserInfo = new SysUserInfo();
        Long userId = idUtil.nextId();
        sysUserInfo.setId(userId);
        sysUserInfo.setUserAccount(dto.getUserAccount());
        sysUserInfo.setUserName(dto.getUserName());
        String password = passwordUtil.encode(dto.getPassword());
        sysUserInfo.setPassword(password);
        sysUserInfo.setEmail(dto.getEmail());
        sysUserInfo.setPhone(dto.getPhone());
        sysUserInfo.setJobNo(dto.getJobNo());
        sysUserInfo.setAddress(dto.getAddress());
        sysUserInfo.setExpiryDateStart(dto.getExpiryDateStart());
        sysUserInfo.setExpiryDateEnd(dto.getExpiryDateEnd());
        // sysUserInfo.setTenantId();
        // sysUserInfo.setDeleteFlag();
        // sysUserInfo.setCreatedBy();
        // sysUserInfo.setUpdatedBy();
        // sysUserInfo.setCreatedTime();
        // sysUserInfo.setUpdatedTime();
        sysUserInfoMapper.insert(sysUserInfo);
        // 2.处理用户部门信息
        SysUserOrg sysUserOrg = new SysUserOrg();
        sysUserOrg.setId(idUtil.nextId());
        sysUserOrg.setOrgId(dto.getOrgId());
        sysUserOrg.setUserId(userId);
        sysUserOrgMapper.insert(sysUserOrg);

        // 3.处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (Objects.nonNull(roleIds)) {
            for (Long roleId : roleIds) {
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setId(idUtil.nextId());
                sysRoleUser.setUserId(userId);
                sysRoleUser.setRoleId(roleId);
                log.info("添加用户角色映射关系{}", JSONUtil.toJsonPrettyStr(sysRoleUser));
                sysRoleUserMapper.insert(sysRoleUser);
            }
        }
        return new ResponseResult(200, "操作成功");
    }

    @Override
    public ResponseResult<UserInfoVo> getUser(Long userId) {
        // 1 获取用户基本信息
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(userId);
        // 2 获取 用户--部门 映射关系
        SysUserOrg sysUserOrg = getSysUserOrg(userId);
        // 2.1 获取部门详细信息
        SysOrg sysOrg = sysOrgMapper.selectById(sysUserOrg.getOrgId());
        // 3 获取用户角色信息
        List<SysRoleUser> sysRoleUserList = getSysRoleUser(userId);
        // 组合所有信息
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserAccount(sysUserInfo.getUserAccount());
        userInfoVo.setUserName(sysUserInfo.getUserName());
        userInfoVo.setExpiryDateStart(sysUserInfo.getExpiryDateStart());
        userInfoVo.setExpiryDateEnd(sysUserInfo.getExpiryDateEnd());
        userInfoVo.setOrgId(sysUserOrg.getOrgId());
        userInfoVo.setOrgName(sysOrg.getOrgName());

        userInfoVo.setOrgPIds(sysOrg.getOrgIds());
        // TODO 待处理 上级部门的名称并组合成字符串
        userInfoVo.setOrgNameStr(null);

        userInfoVo.setJobNo(sysUserInfo.getJobNo());
        userInfoVo.setPhone(sysUserInfo.getPhone());
        userInfoVo.setEmail(sysUserInfo.getEmail());
        userInfoVo.setAddress(sysUserInfo.getAddress());
        userInfoVo.setDescription(sysUserInfo.getDescription());

        List<Long> roleIds = new ArrayList<>();
        List<String> roleIdNames = new ArrayList<>();
        for (SysRoleUser sysRoleUser : sysRoleUserList) {
            SysRoleInfo sysRole = sysRoleInfoMapper.selectById(sysRoleUser.getRoleId());
            roleIds.add(sysRole.getId());
            roleIdNames.add(sysRole.getRoleName());
        }
        userInfoVo.setRoleIds(roleIds);
        userInfoVo.setRoleNames(roleIdNames);

        return new ResponseResult<>(200, "操作成功", userInfoVo);
    }

    /**
     * 通过部门ID 获取所的上级部门的名称并组合成字符串
     *
     * @param orgId
     * @return
     */
    private String getOrgNameStr(Long orgId) {
        SysOrg sysOrg = sysOrgMapper.selectById(orgId);


        return null;
    }

    /**
     * 根据userId用户获取 用户-组织 映射关系
     *
     * @param userId 用户ID
     * @return
     */
    private SysUserOrg getSysUserOrg(Long userId) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        return sysUserOrgMapper.selectOne(queryWrapper);
    }

    /**
     * 根据userId用户获取 用户-角色 映射关系
     *
     * @param userId 用户ID
     * @return
     */
    private List<SysRoleUser> getSysRoleUser(Long userId) {
        LambdaQueryWrapper<SysRoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleUser::getUserId, userId);
        return sysRoleUserMapper.selectList(queryWrapper);
    }

    @Override
    public ResponseResult updateUser() {
        return null;
    }

    @Override
    public ResponseResult deleteUser() {
        return null;
    }

    @Override
    public ResponseResult batchDeleteUsers() {
        return null;
    }


    @Override
    public ResponseResult getUserList() {
        return null;
    }

    @Override
    public ResponseResult getUserListByPage() {
        return null;
    }
}
