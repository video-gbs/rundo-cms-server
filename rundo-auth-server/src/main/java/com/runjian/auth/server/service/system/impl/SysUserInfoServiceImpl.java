package com.runjian.auth.server.service.system.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.entity.SysRoleUser;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.mapper.role.SysRoleUserMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.auth.server.util.SnowflakeUtil;
import com.runjian.common.config.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public CommonResponse addUser(SysUserInfoDTO dto) {
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
        // 2.处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (Objects.nonNull(roleIds)) {
            for (Long roleId : roleIds) {
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setId(idUtil.nextId());
                sysRoleUser.setUserId(userId);
                sysRoleUser.setRoleId(roleId);
                sysRoleUserMapper.insert(sysRoleUser);
            }
        }
        return CommonResponse.success("操作成功");
    }

    @Override
    public CommonResponse updateUser() {
        return null;
    }

    @Override
    public CommonResponse deleteUser() {
        return null;
    }

    @Override
    public CommonResponse batchDeleteUsers() {
        return null;
    }

    @Override
    public CommonResponse getUser() {
        return null;
    }

    @Override
    public CommonResponse getUserList() {
        return null;
    }

    @Override
    public CommonResponse getUserListByPage() {
        return null;
    }
}
