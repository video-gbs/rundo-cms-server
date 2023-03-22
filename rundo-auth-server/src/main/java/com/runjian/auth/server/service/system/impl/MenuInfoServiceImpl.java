package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.MenuInfoVO;
import com.runjian.auth.server.domain.vo.system.MyMetaClass;
import com.runjian.auth.server.domain.vo.tree.MenuInfoTree;
import com.runjian.auth.server.mapper.AppInfoMapper;
import com.runjian.auth.server.mapper.MenuInfoMapper;
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
    private MenuInfoMapper menuInfoMapper;

    @Autowired
    private AppInfoMapper appInfoMapper;

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
        MenuInfo menuInfo = menuInfoMapper.selectById(1);
        menuInfoList.add(menuInfo);
        Long rootNodeId = 1L;
        if (null != dto.getAppId()) {
            AppInfo appInfo = appInfoMapper.selectById(dto.getAppId());
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
        menuInfoList.stream().distinct();
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
        List<MenuInfoTree> menuInfoTreeByAppTypelist = new ArrayList<>();
        LambdaQueryWrapper<AppInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppInfo::getAppType, appType);
        List<AppInfo> appInfoList = appInfoMapper.selectList(queryWrapper);
        for (AppInfo appInfo : appInfoList) {
            QuerySysMenuInfoDTO dto = new QuerySysMenuInfoDTO();
            dto.setAppId(appInfo.getId());
            menuInfoTreeByAppTypelist.addAll(findByTree(dto));
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
    public void save(AddSysMenuInfoDTO dto) {
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
    public void modifyById(UpdateSysMenuInfoDTO dto) {
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
