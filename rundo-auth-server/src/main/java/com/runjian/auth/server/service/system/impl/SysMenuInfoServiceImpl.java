package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysMenuInfo;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysMenuInfoTree;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.auth.server.util.UserUtils;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class SysMenuInfoServiceImpl extends ServiceImpl<SysMenuInfoMapper, SysMenuInfo> implements SysMenuInfoService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private SysMenuInfoMapper sysMenuInfoMapper;

    @Override
    public List<SysMenuInfoTree> getSysMenuTree(QuerySysMenuInfoDTO dto) {
        // 通过用户获取角色列表

        // 通过角色渲染查取用户的菜单



        LambdaQueryWrapper<SysMenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (null != dto.getMenuName()) {
            queryWrapper.like(SysMenuInfo::getMenuName, dto.getMenuName());
        }
        if (null != dto.getUrl()) {
            queryWrapper.like(SysMenuInfo::getUrl, dto.getUrl());
        }
        queryWrapper.orderByAsc(true, SysMenuInfo::getMenuSort);
        queryWrapper.orderByAsc(true, SysMenuInfo::getUpdatedTime);
        List<SysMenuInfo> sysMenuInfoList = sysMenuInfoMapper.selectList(queryWrapper);
        List<SysMenuInfoTree> sysMenuInfoTreeList = sysMenuInfoList.stream().map(
                item -> {
                    SysMenuInfoTree bean = new SysMenuInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());

        if (dto.getMenuType() == null) {
            return DataTreeUtil.buildTree(sysMenuInfoTreeList, 1L);
        } else {
            return DataTreeUtil.buildTree(sysMenuInfoTreeList, dto.getMenuType());
        }

    }

    @Override
    public void addSysMenu(AddSysMenuInfoDTO dto) {
        SysMenuInfo sysMenuInfo = new SysMenuInfo();
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
