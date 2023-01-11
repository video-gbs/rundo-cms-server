package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.UserInfoVo;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.auth.server.util.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysRoleInfoMapper sysRoleInfoMapper;

    @Override
    public ResponseResult addUser(SysUserInfoDTO dto) {
        return new ResponseResult(200, "操作成功");
    }

    @Override
    public ResponseResult<UserInfoVo> getUser(Long userId) {
        // 1 获取用户基本信息
        // 2 获取 用户--部门 映射关系
        // 3 获取用户角色信息
        // 4 组合所有信息
        UserInfoVo userInfoVo = new UserInfoVo();

        return new ResponseResult<>(200, "操作成功", userInfoVo);
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
