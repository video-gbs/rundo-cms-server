package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.mapper.AppInfoMapper;
import com.runjian.auth.server.service.system.ApiInfoService;
import com.runjian.auth.server.service.system.AppInfoService;
import com.runjian.auth.server.service.system.MenuInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 应用信息 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements AppInfoService {

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private MenuInfoService menuInfoService;

    @Autowired
    private ApiInfoService apiInfoService;

    @Override
    public void save(AddSysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        appInfoMapper.insert(appInfo);
        // 向菜单表中插入一条虚拟根菜单
        AddSysMenuInfoDTO menuInfoDTO = new AddSysMenuInfoDTO();
        Long menuPid = 1L;
        menuInfoDTO.setAppId(appInfo.getId());
        menuInfoDTO.setMenuPid(menuPid);
        menuInfoDTO.setTitle(appInfo.getAppName());
        menuInfoDTO.setIcon(null);
        menuInfoDTO.setMenuSort(1);
        menuInfoDTO.setPath(appInfo.getAppUrl());
        menuInfoDTO.setComponent(null);
        menuInfoDTO.setStatus(0);
        menuInfoDTO.setHidden(0);
        menuInfoService.save(menuInfoDTO);
        // 向接口表插入一条虚拟根接口
        AddSysApiInfoDTO apiInfoDTO = new AddSysApiInfoDTO();
        Long apiPid = 1L;
        apiInfoDTO.setAppId(appInfo.getId());
        apiInfoDTO.setApiPid(apiPid);
        apiInfoDTO.setApiName(appInfo.getAppName());
        apiInfoDTO.setUrl(appInfo.getAppUrl());
        apiInfoDTO.setApiSort(1);
        apiInfoDTO.setStatus(0);
        apiInfoService.save(apiInfoDTO);
    }

    @Override
    public void modifyById(UpdateSysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        appInfoMapper.updateById(appInfo);
    }

    @Override
    public void erasureById(Long id) {
        appInfoMapper.deleteById(id);
    }

    @Override
    public Page<SysAppInfoVO> findByPage(QuerySysAppInfoDTO dto) {
        PageSysAppInfoDTO page = new PageSysAppInfoDTO();
        page.setAppName(dto.getAppName());
        page.setAppIp(dto.getAppIp());
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0){
            page.setSize(dto.getPageSize());
        }else {
            page.setSize(20);
        }
        return appInfoMapper.MySelectPage(page);
    }

    @Override
    public void modifyByStatus(StatusSysAppInfoDTO dto) {
        AppInfo appInfo = appInfoMapper.selectById(dto.getId());
        appInfo.setStatus(dto.getStatus());
        appInfoMapper.updateById(appInfo);
    }

    @Override
    public SysAppInfoVO findById(Long id) {
        AppInfo appInfo = appInfoMapper.selectById(id);
        SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
        BeanUtils.copyProperties(appInfo, sysAppInfoVO);
        return sysAppInfoVO;
    }

    @Override
    public List<SysAppInfoVO> findByList() {
        List<SysAppInfoVO> sysAppInfoVOList = new ArrayList<>();
        List<AppInfo> appInfoList = appInfoMapper.selectList(null);
        for (AppInfo appInfo : appInfoList) {
            SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
            BeanUtils.copyProperties(appInfo, sysAppInfoVO);
            sysAppInfoVOList.add(sysAppInfoVO);
        }
        return sysAppInfoVOList;
    }


}
