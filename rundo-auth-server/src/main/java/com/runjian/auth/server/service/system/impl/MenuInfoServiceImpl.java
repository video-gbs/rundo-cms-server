package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.entity.MenuInfo;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.domain.vo.tree.MenuInfoTree;
import com.runjian.auth.server.mapper.MenuInfoMapper;
import com.runjian.auth.server.service.login.MyRBACService;
import com.runjian.auth.server.service.system.MenuInfoService;
import com.runjian.auth.server.util.UserUtils;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements MenuInfoService {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private MyRBACService myRBACService;

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<MenuInfoTree> findByTree(QuerySysMenuInfoDTO dto) {
        List<MenuInfo> menuInfoList = menuInfoMapper.selectByAppId(dto.getAppId());
        List<MenuInfoTree> menuInfoTreeList = menuInfoList.stream().map(
                item -> {
                    MenuInfoTree bean = new MenuInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(menuInfoTreeList, 1L);
    }

    @Override
    public void save(AddSysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(dto,menuInfo);
        MenuInfo parentInfo = menuInfoMapper.selectById(dto.getMenuPid());
        String menuPids = parentInfo.getMenuPids() + "[" + dto.getMenuPid() + "]";
        menuInfo.setMenuPids(menuPids);
        if (null == dto.getMenuSort()){
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

    }

    @Override
    public SysMenuInfoVO findById(Long id) {
        MenuInfo menuInfo = menuInfoMapper.selectById(id);
        SysMenuInfoVO sysMenuInfoVO = new SysMenuInfoVO();
        BeanUtils.copyProperties(menuInfo, sysMenuInfoVO);
        return sysMenuInfoVO;
    }

    @Override
    public List<SysMenuInfoVO> findByList() {
        return null;
    }


}
