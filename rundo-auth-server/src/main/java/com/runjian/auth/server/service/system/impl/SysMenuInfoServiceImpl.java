package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysAppInfo;
import com.runjian.auth.server.domain.entity.system.SysMenuInfo;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysMenuInfoTree;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.service.MyRBACService;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.auth.server.util.UserUtils;
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
public class SysMenuInfoServiceImpl extends ServiceImpl<SysMenuInfoMapper, SysMenuInfo> implements SysMenuInfoService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private MyRBACService myRBACService;

    @Autowired
    private SysMenuInfoMapper sysMenuInfoMapper;

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
        List<SysAppInfo> appInfoList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            appInfoList.addAll(myRBACService.findAppIdByRoleCode(roleCode));
        }
        if (CollUtil.isEmpty(appInfoList)){
            throw new BusinessException("未对用户进行应用授权");
        }
        // 判断所访问的应用是否已经授权
        List<Long> appIdList = new ArrayList<>();
        for (SysAppInfo sysAppInfo : appInfoList) {
            appIdList.add(sysAppInfo.getId());
        }
        if (!appIdList.contains(dto.getAppId())) {
            throw new BusinessException("所访问应用未授权");
        }
        // 通过应用表获取角色的菜单表(未删除、未禁用、未隐藏的菜单)
        List<SysMenuInfo> roleMenuList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            roleMenuList.addAll(myRBACService.findMenuInfoByRoleCode(roleCode));
        }
        // 通过应用ID查取该应用下的所有菜单
        List<SysMenuInfo> appMenuList = sysMenuInfoMapper.getMenuInfoByAppId(dto.getAppId());
        // 将已经授权的菜单和应用的菜单取交集，得到用户在这个应用下的可访问菜单
        List<SysMenuInfo> re = new ArrayList<>();
        for (SysMenuInfo rome : roleMenuList) {
            for (SysMenuInfo appMenu : appMenuList) {
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
            List<SysMenuInfo> sysMenuInfoList = CollUtil.distinct(re);
            return sysMenuInfoList.stream().map(
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
        SysMenuInfo sysMenuInfo = new SysMenuInfo();
        sysMenuInfo.setAppId(dto.getAppId());
        sysMenuInfo.setMenuPid(dto.getMenuPid());
        SysMenuInfo parentInfo = sysMenuInfoMapper.selectById(dto.getMenuPid());
        String menuPids = parentInfo.getMenuPids() + "[" + dto.getMenuPid() + "]";
        sysMenuInfo.setMenuPids(menuPids);
        sysMenuInfo.setMenuName(dto.getMenuName());
        sysMenuInfo.setMenuSort(dto.getMenuSort());
        // 新增菜单默认不是叶子节点
        sysMenuInfo.setLeaf(0);
        sysMenuInfo.setUrl(dto.getUrl());
        sysMenuInfo.setIcon(dto.getIcon());
        sysMenuInfo.setLevel(parentInfo.getLevel() + 1);
        sysMenuInfo.setHidden(dto.getHidden());
        sysMenuInfo.setStatus(dto.getStatus());
        sysMenuInfo.setViewImport(dto.getViewImport());
        // TODO 处理租客信息
        // sysMenuInfo.setTenantId();
        sysMenuInfoMapper.insert(sysMenuInfo);
        // 处理应用菜单映射管理
        Long menuId = sysMenuInfo.getId();
        Long appId = dto.getAppId();
        sysMenuInfoMapper.saveAppMenu(menuId, appId);
    }

    @Override
    public void removeSysMenuInfoById(Long id) {
        // 1.确认当前需要删除的菜单有无下级菜单
        LambdaQueryWrapper<SysMenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysMenuInfo::getMenuPids, "[" + id + "]");
        List<SysMenuInfo> menuInfoChild = sysMenuInfoMapper.selectList(queryWrapper);
        if (menuInfoChild.size() > 0) {
            // 1.1 有下级菜单不允许删除
            throw new BusinessException("不能删除含有下级菜单的菜单");
        }
        // 1.2 无下级菜单才可以删除
        sysMenuInfoMapper.deleteById(id);
    }


    @Override
    public void updateSysMenuInfoById(UpdateSysMenuInfoDTO dto) {

    }

    @Override
    public SysMenuInfoVO getSysMenuInfoById(Long id) {
        SysMenuInfo menuInfo = sysMenuInfoMapper.selectById(id);
        SysMenuInfoVO sysMenuInfoVO = new SysMenuInfoVO();
        BeanUtils.copyProperties(menuInfo, sysMenuInfoVO);
        return sysMenuInfoVO;
    }

    @Override
    public List<SysMenuInfoVO> getSysMenuInfoList() {
        return null;
    }


}
