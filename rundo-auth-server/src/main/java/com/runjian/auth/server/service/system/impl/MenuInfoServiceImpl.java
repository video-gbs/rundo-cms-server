package com.runjian.auth.server.service.system.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.HiddenChangeDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusChangeDTO;
import com.runjian.auth.server.domain.dto.system.SysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.MenuInfoVO;
import com.runjian.auth.server.domain.vo.system.MyMetaClass;
import com.runjian.auth.server.domain.vo.tree.MenuInfoTree;
import com.runjian.auth.server.mapper.MenuInfoMapper;
import com.runjian.auth.server.service.system.AppInfoService;
import com.runjian.auth.server.service.system.MenuInfoService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
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
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements MenuInfoService {
    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<MenuInfoTree> findByTree(QuerySysMenuInfoDTO dto) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (null != dto.getAppId()) {
            queryWrapper.eq(MenuInfo::getAppId, dto.getAppId());
        }
        if (null != dto.getMenuName() && !"".equals(dto.getMenuName())) {
            queryWrapper.like(MenuInfo::getTitle, dto.getMenuName());
        }

        if (null != dto.getUrl() && !"".equals(dto.getUrl())) {
            queryWrapper.like(MenuInfo::getPath, dto.getUrl());
        }
        List<MenuInfo> menuInfoList = menuInfoMapper.selectList(queryWrapper);
        long rootNodeId = 1L;
        int flag = 0;
        for (MenuInfo info : menuInfoList) {
            if (info.getId() == rootNodeId) {
                flag = flag + 1;
            }
        }

        if (flag == 0) {
            MenuInfo menuInfo = menuInfoMapper.selectById(1);
            menuInfoList.add(menuInfo);
        }

        if (null != dto.getAppId()) {
            AppInfo appInfo = appInfoService.getById(dto.getAppId());
            menuInfoList.stream().filter(bean -> {
                if (bean.getId().equals(rootNodeId)) {
                    bean.setAppId(appInfo.getId());
                    bean.setIcon(appInfo.getAppIcon());
                    bean.setTitle(appInfo.getAppName());
                    bean.setPath(appInfo.getAppUrl());
                    bean.setComponent(appInfo.getComponent());
                    bean.setRedirect(appInfo.getRedirect());
                    bean.setName(StrUtil.removePrefix(appInfo.getAppUrl(), "/"));
                    bean.setLevel(1);
                }
                return true;
            }).collect(Collectors.toList());
        }
        menuInfoList.stream().distinct().collect(Collectors.toList());
        List<MenuInfoTree> menuInfoTreeList = menuInfoList.stream().map(
                item -> {
                    MenuInfoTree bean = new MenuInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    MyMetaClass metaClass = new MyMetaClass();
                    metaClass.setTitle(item.getTitle());
                    metaClass.setIcon(item.getIcon());
                    bean.setMeta(metaClass);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(menuInfoTreeList, rootNodeId);
    }

    @Override
    public List<MenuInfoTree> findByTreeByAppType(Integer appType) {
        // 取出当前登录的用户的所有角色
        List<String> roleCodeList = StpUtil.getRoleList();
        // 去重
        List<AppInfo> appInfoList = CollUtil.distinct(appInfoService.getAppByRoleCodelist(roleCodeList));
        // 过滤掉不满足条件的数据
        appInfoList.removeIf(appInfo -> !appInfo.getAppType().equals(appType));
        List<Long> appIds = appInfoList.stream().map(AppInfo::getId).collect(Collectors.toList());

        List<MenuInfoTree> menuInfoTreeByAppTypelist = new ArrayList<>();
        for (Long appId : appIds) {
            LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MenuInfo::getAppId, appId);
            List<MenuInfo> menuInfoList = menuInfoMapper.selectList(queryWrapper);
            long rootId = 0L;
            for (MenuInfo menuInfo : menuInfoList) {
                if (menuInfo.getMenuPid().equals(1L) && menuInfo.getLevel().equals(2)) {
                    rootId = menuInfo.getId();
                }
            }
            menuInfoTreeByAppTypelist.addAll(
                    DataTreeUtil.buildTree(menuInfoList.stream().map(
                            item -> {
                                MenuInfoTree bean = new MenuInfoTree();
                                BeanUtils.copyProperties(item, bean);
                                MyMetaClass metaClass = new MyMetaClass();
                                metaClass.setTitle(item.getTitle());
                                metaClass.setIcon(item.getIcon());
                                bean.setMeta(metaClass);
                                return bean;
                            }
                    ).collect(Collectors.toList()), rootId)
            );

        }
        return menuInfoTreeByAppTypelist;
    }

    @Override
    public void modifyByStatus(StatusChangeDTO dto) {
        MenuInfo menuInfo = menuInfoMapper.selectById(dto.getId());
        menuInfo.setStatus(dto.getStatus());
        menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public void modifyByHidden(HiddenChangeDTO dto) {
        MenuInfo menuInfo = menuInfoMapper.selectById(dto.getId());
        menuInfo.setHidden(dto.getHidden());
        menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public List<MenuInfoTree> getTreeByAppId(Long appId) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuInfo::getAppId, appId);
        List<MenuInfo> menuInfoList = menuInfoMapper.selectList(queryWrapper);
        long root = 0L;
        List<MenuInfoTree> menuInfoTreeList = new ArrayList<>();
        for (MenuInfo menuInfo : menuInfoList) {
            if (menuInfo.getAppId().longValue() == appId && menuInfo.getMenuPid() == 1L) {
                root = menuInfo.getId();
            }
            MenuInfoTree bean = new MenuInfoTree();
            BeanUtils.copyProperties(menuInfo, bean);
            menuInfoTreeList.add(bean);
        }
        return DataTreeUtil.buildTree(menuInfoTreeList, root);
    }

    @Override
    public List<MenuInfo> getMenuByRoleCode(String roleCode) {
        return menuInfoMapper.selectMenuByRoleCode(roleCode);
    }

    @Override
    public List<Long> getMenuIdListByRoleId(Long roleId) {
        return menuInfoMapper.findMenuIdListByRoleId(roleId);
    }

    @Override
    public void save(SysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(dto, menuInfo);
        MenuInfo parentInfo = menuInfoMapper.selectById(dto.getMenuPid());
        String menuPids = parentInfo.getMenuPids() + "[" + dto.getMenuPid() + "]";
        menuInfo.setMenuPids(menuPids);
        menuInfo.setParentName(parentInfo.getTitle());
        if (null == dto.getMenuSort()) {
            menuInfo.setMenuSort(100);
        }
        menuInfo.setMenuSort(dto.getMenuSort());
        menuInfo.setLeaf(0);
        menuInfo.setLevel(parentInfo.getLevel() + 1);
        menuInfoMapper.insert(menuInfo);
    }

    @Override
    public void erasureById(Long id) {
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
    public void modifyById(SysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(dto, menuInfo);
        menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public MenuInfoVO findById(Long id) {
        MenuInfo menuInfo = menuInfoMapper.selectById(id);
        MenuInfoVO menuInfoVO = new MenuInfoVO();
        BeanUtils.copyProperties(menuInfo, menuInfoVO);
        MenuInfo parentMenuInfo = menuInfoMapper.selectById(menuInfo.getMenuPid());
        menuInfoVO.setParentName(parentMenuInfo.getTitle());
        return menuInfoVO;
    }

}
