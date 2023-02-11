package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysMenuInfoTree;
import com.runjian.auth.server.mapper.MenuInfoMapper;
import com.runjian.auth.server.service.login.MyRBACService;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.auth.server.util.UserUtils;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class SysMenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements SysMenuInfoService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private MyRBACService myRBACService;

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<SysMenuInfoTree> getSysMenuTree(QuerySysMenuInfoDTO dto) {
        // 1.通过用户获取角色列表(未删除且未禁用的角色)
        // 如果角色表为空-->该用户未授权或者角色权限已经收回
        List<String> roleCodeList = myRBACService.findRoleInfoByUserAccount(userUtils.getSysUserInfo().getId());
        if (CollUtil.isEmpty(roleCodeList)) {
            throw new BusinessException("未对用户进行角色授权或者角色权限已收回");
        }
        // 通过角色列表获取应用表(未删除且未禁用的应用)
        // 如果应用表为空-->该角色未分配授权或者应用权限已经收回
        List<AppInfo> appInfoList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            appInfoList.addAll(myRBACService.findAppIdByRoleCode(roleCode));
        }
        if (CollUtil.isEmpty(appInfoList)) {
            throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH, "未对用户进行应用授权");
        }
        // 判断所访问的应用是否已经授权
        List<Long> appIdList = new ArrayList<>();
        for (AppInfo appInfo : appInfoList) {
            appIdList.add(appInfo.getId());
        }
        if (!appIdList.contains(dto.getAppId())) {
            throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH, "所访问应用未授权");
        }
        // 通过应用表获取角色的菜单表(未删除、未禁用、未隐藏的菜单)
        List<MenuInfo> roleMenuList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            roleMenuList.addAll(myRBACService.findMenuInfoByRoleCode(roleCode));
        }
        // 通过应用ID查取该应用下的所有菜单
        List<MenuInfo> appMenuList = menuInfoMapper.getMenuInfoByAppId(dto.getAppId());
        // 将已经授权的菜单和应用的菜单取交集，得到用户在这个应用下的可访问菜单
        List<MenuInfo> re = new ArrayList<>();
        for (MenuInfo rome : roleMenuList) {
            for (MenuInfo appMenu : appMenuList) {
                if (rome.equals(appMenu)) {
                    re.add(rome);
                }
            }
        }
        // 检查是否为空集
        // 空集 --> 用户可以对这个应用下的所有菜单都能操作，将应用的所有菜单都渲染到页面
        if (CollUtil.isEmpty(re)) {
            // 渲染查取用户的菜单
            return appMenuList.stream().map(
                    item -> {
                        SysMenuInfoTree bean = new SysMenuInfoTree();
                        BeanUtils.copyProperties(item, bean);
                        return bean;
                    }
            ).collect(Collectors.toList());
        } else {
            // 防止重复渲染，先去重
            List<MenuInfo> menuInfoList = CollUtil.distinct(re);
            return menuInfoList.stream().map(
                    item -> {
                        SysMenuInfoTree bean = new SysMenuInfoTree();
                        BeanUtils.copyProperties(item, bean);
                        return bean;
                    }
            ).collect(Collectors.toList());

        }
    }

    @Override
    public void addSysMenu(AddSysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setAppId(dto.getAppId());
        menuInfo.setMenuPid(dto.getMenuPid());
        MenuInfo parentInfo = menuInfoMapper.selectById(dto.getMenuPid());
        String menuPids = parentInfo.getMenuPids() + "[" + dto.getMenuPid() + "]";
        menuInfo.setMenuPids(menuPids);
        menuInfo.setMenuName(dto.getMenuName());
        menuInfo.setMenuSort(dto.getMenuSort());
        // 新增菜单默认不是叶子节点
        menuInfo.setLeaf(0);
        menuInfo.setUrl(dto.getUrl());
        menuInfo.setIcon(dto.getIcon());
        menuInfo.setLevel(parentInfo.getLevel() + 1);
        menuInfo.setHidden(dto.getHidden());
        menuInfo.setStatus(dto.getStatus());
        menuInfo.setViewImport(dto.getViewImport());
        // TODO 处理租客信息
        // sysMenuInfo.setTenantId();
        menuInfoMapper.insert(menuInfo);
        // 处理应用菜单映射管理
        Long menuId = menuInfo.getId();
        Long appId = dto.getAppId();
        menuInfoMapper.saveAppMenu(menuId, appId);
    }

    @Override
    public void removeSysMenuInfoById(Long id) {
        // 1.确认当前需要删除的菜单有无下级菜单
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(MenuInfo::getMenuPids, "[" + id + "]");
        List<MenuInfo> menuInfoChild = menuInfoMapper.selectList(queryWrapper);
        if (menuInfoChild.size() > 0) {
            // 1.1 有下级菜单不允许删除
            throw new BusinessException("不能删除含有下级菜单的菜单");
        }
        // 1.2 无下级菜单才可以删除
        menuInfoMapper.deleteById(id);
    }


    @Override
    public void updateSysMenuInfoById(UpdateSysMenuInfoDTO dto) {

    }

    @Override
    public SysMenuInfoVO getSysMenuInfoById(Long id) {
        MenuInfo menuInfo = menuInfoMapper.selectById(id);
        SysMenuInfoVO sysMenuInfoVO = new SysMenuInfoVO();
        BeanUtils.copyProperties(menuInfo, sysMenuInfoVO);
        return sysMenuInfoVO;
    }

    @Override
    public List<SysMenuInfoVO> getSysMenuInfoList() {
        return null;
    }


}
