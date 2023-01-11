package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.auth.server.util.RundoIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private RundoIdUtil idUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysRoleInfoMapper sysRoleInfoMapper;

    @Override
    public ResponseResult addUser(SysUserInfoDTO dto) {
        // 处理基本信息
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
        sysUserInfo.setDescription(dto.getDescription());
        // sysUserInfo.setTenantId();
        // sysUserInfo.setDeleteFlag();
        // sysUserInfo.setCreatedBy();
        // sysUserInfo.setUpdatedBy();
        // sysUserInfo.setCreatedTime();
        // sysUserInfo.setUpdatedTime();
        sysUserInfoMapper.insert(sysUserInfo);
        // 处理部门信息
        sysOrgMapper.saveUserOrg(userId, dto.getOrgId());
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        for (Long roleId : roleIds) {
            sysRoleInfoMapper.saveUserRole(userId, roleId);
        }

        return new ResponseResult(200, "操作成功");
    }

    @Override
    public ResponseResult updateUser(SysUserInfoDTO dto) {
        return null;
    }

}
