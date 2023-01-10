package com.runjian.auth.server.service.system.impl;

import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {

    @Override
    public CommonResponse addUser(SysUserInfoDTO dto) {
        return null;
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
