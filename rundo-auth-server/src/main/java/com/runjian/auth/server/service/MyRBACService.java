package com.runjian.auth.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.*;
import com.runjian.auth.server.mapper.video.ChannelOperationMapper;
import com.runjian.auth.server.mapper.video.VideoAraeMapper;
import com.runjian.auth.server.mapper.video.VideoChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AuthenticService
 * @Description 权限相关内容
 * @date 2023-02-08 周三 14:00
 */
@Component("rbacService")
public class MyRBACService {

    /**
     * 用户
     */
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    /**
     * 角色
     */
    @Autowired
    private SysRoleInfoMapper roleInfoMapper;

    /**
     * 应用
     */
    @Autowired
    private SysAppInfoMapper appInfoMapper;
    /**
     * 菜单
     */
    @Autowired
    private SysMenuInfoMapper menuInfoMapper;
    /**
     * 接口
     */
    @Autowired
    private SysApiInfoMapper apiInfoMapper;

    /**
     * 安全区划
     */
    @Autowired
    private VideoAraeMapper videoAraeMapper;

    /**
     * 安全通道
     */
    @Autowired
    private VideoChannelMapper videoChannelMapper;

    /**
     * 通道操作
     */
    @Autowired
    private ChannelOperationMapper channelOperationMapper;


    /**
     * 通过用户账户，查取用户信息
     *
     * @param userName
     * @return
     */
    public SysUserInfo findUserInfoByUserAccount(String userName) {
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, userName);
        return sysUserInfoMapper.selectOne(queryWrapper);
    }


    /**
     * 通过用户编号，查取用户所有的角色信息
     *
     * @param id
     * @return
     */
    public List<String> findRoleInfoByUserAccount(Long id) {
        return roleInfoMapper.selectRoleByUserId(id);
    }

    /**
     * 通过角色编码，查取角色已有的应用信息
     *
     * @param roleCode
     * @return
     */
    public List<String> findAppInfoByRoleCode(String roleCode) {
        return roleInfoMapper.findAppByRoleCode(roleCode);
    }

    /**
     * 通过角色编码，查取角色已有的菜单信息
     *
     * @param roleCode
     * @return
     */
    public List<String> findMenuInfoByRoleCode(String roleCode) {
        return roleInfoMapper.findMenuByRoleCode(roleCode);
    }


    /**
     * 通过角色编码，查取角色已有的接口信息
     *
     * @param roleCode
     * @return
     */
    public List<String> findApiInfoByRoleCode(String roleCode) {
        return roleInfoMapper.findApiByRoleCode(roleCode);
    }

    /**
     * 通过角色编码，查取角色已有的安防区域信息
     *
     * @param roleCode
     * @return
     */
    public List<String> findAreaByRoleCode(String roleCode) {

        return null;
    }

    /**
     * 通过角色编码，查取角色已有的通道信息
     *
     * @param roleCode
     * @return
     */
    public List<String> findChannelByRoleCode(String roleCode) {

        return null;
    }

}
