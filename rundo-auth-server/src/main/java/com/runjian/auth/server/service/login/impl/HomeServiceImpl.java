package com.runjian.auth.server.service.login.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.vo.system.HomeVO;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.mapper.RoleInfoMapper;
import com.runjian.auth.server.service.login.HomeSevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeServiceImpl
 * @Description 门户首页
 * @date 2023-02-08 周三 11:53
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeSevice {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Override
    public HomeVO getIndex() {
        Long userId =StpUtil.getLoginIdAsLong();
        // 通过登录的用户查取该用户已授权的角色信息
        List<String> roleCodeList = StpUtil.getRoleList();
        if (CollUtil.isEmpty(roleCodeList)) {
            log.info("未对用户{}，进行角色授权", userId);
            return null;
        }
        // 查取角色已经授权的应用
        List<AppInfo> roleAppInfoList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            roleAppInfoList.addAll(roleInfoMapper.selectAppByRoleCode(roleCode));
        }
        if (CollUtil.isEmpty(roleAppInfoList)) {
            log.info("未对用户{}，进行应用授权", userId);
            return null;
        }
        // 去重
        List<AppInfo> appInfoList = CollUtil.distinct(roleAppInfoList);
        // 分拣应用种类
        List<SysAppInfoVO> appList = new ArrayList<>();
        List<SysAppInfoVO> configList = new ArrayList<>();
        List<SysAppInfoVO> devOpsList = new ArrayList<>();
        for (AppInfo appInfo : appInfoList) {
            switch (appInfo.getAppType()) {
                case 1: {
                    // 功能应用类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(appInfo, sysAppInfoVO);
                    appList.add(sysAppInfoVO);
                    break;
                }
                case 2: {
                    // 配置类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(appInfo, sysAppInfoVO);
                    configList.add(sysAppInfoVO);
                    break;
                }
                case 3: {
                    // 运维类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(appInfo, sysAppInfoVO);
                    devOpsList.add(sysAppInfoVO);
                    break;
                }
            }
        }
        HomeVO homeVO = new HomeVO();
        homeVO.setAppList(appList);
        homeVO.setConfigList(configList);
        homeVO.setDevOpsList(devOpsList);
        return homeVO;
    }
}
