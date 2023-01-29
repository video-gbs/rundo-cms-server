package com.runjian.auth.server.service.login.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.*;
import com.runjian.auth.server.mapper.video.ChannelOperationMapper;
import com.runjian.auth.server.mapper.video.VideoAraeMapper;
import com.runjian.auth.server.mapper.video.VideoChannelMapper;
import com.runjian.auth.server.model.dto.login.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserDetailsServiceImpl
 * @Description 实现 UserDetailsService 接口
 * @date 2023-01-03 周二 14:14
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.加载用户信息
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, username);
        log.info("查询条件{}", JSONUtil.toJsonStr(queryWrapper));
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(queryWrapper);
        // 如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(sysUserInfo)) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        // TODO 根据用户ID查询权限信息 添加到LoginUser中
        // 2.根据用户ID，加载用户所拥有的全部角色Id roleIds
        // List<Long> roleIds = roleInfoMapper.selectRoleByUserId(sysUserInfo.getId());
        // 3. 根据角色Id列表 roleIds，加载权限
        List<String> authorities = new ArrayList<>();
        // 3.根据角色Id列表 roleIds ，查取用户所拥有的全部资源权限
        // （应用权限，菜单权限，接口权限，安全区划权限，通道权限，通道操作权限）
        // for (Long roleId : roleIds) {
        //     // 3.1 应用权限
        //     // List<Long> appIds = appInfoMapper.selectByRoleId(roleId);
        //     // 3.2 菜单权限
        //     // List<Long> menuIds = menuInfoMapper.selectByRoleId(roleId);
        //     // 3.3 接口权限
        //     List<String> apiList = apiInfoMapper.selectByRoleId(roleId);
        //     // 3.4 安全区划权限
        //     // List<Long> araeIds = videoAraeMapper.selectByRoleId(roleId);
        //     // 3.5 通道权限
        //     // List<Long> channelIds = videoChannelMapper.selectByRoleId(roleId);
        //     // 3.6 通道操作权限
        //     // List<Long> operationIds = channelOperationMapper.selectByRoleId(roleId);
        //     authorities.addAll(apiList);
        //
        // }
        //
        // List<String> roleIdsList = new ArrayList<>();
        // roleIdsList = roleIds.stream().map(roleId -> "ROLE_" + roleId).collect(Collectors.toList());
        //
        //
        // authorities.addAll(roleIdsList);
        //
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        // loginUserDTO.setSysUserInfo(sysUserInfo);
        // loginUserDTO.setAuthorities(
        //         AuthorityUtils.commaSeparatedStringToAuthorityList(
        //                 String.join(",", authorities)
        //         )
        // );

        // 把数据封装为 UserDetails 返回
        return loginUserDTO;
    }


}
