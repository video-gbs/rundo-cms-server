package com.runjian.auth.server.service.login.impl;

import cn.hutool.core.collection.CollUtil;
import com.runjian.auth.server.domain.entity.SysAppInfo;
import com.runjian.auth.server.domain.entity.SysUserInfo;
import com.runjian.auth.server.domain.vo.system.HomeVO;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.service.login.HomeSevice;
import com.runjian.auth.server.service.login.MyRBACService;
import com.runjian.auth.server.util.UserUtils;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeServiceImpl
 * @Description 门户首页
 * @date 2023-02-08 周三 11:53
 */
@Service
public class HomeServiceImpl implements HomeSevice {

    @Autowired
    private MyRBACService myRBACService;

    @Autowired
    private UserUtils userUtils;

    @Override
    public HomeVO getIndex() {
        SysUserInfo sysUserInfo = userUtils.getSysUserInfo();
        Long userId = sysUserInfo.getId();
        // 通过登录的用户查取该用户已授权的角色信息
        List<String> roleCodeList = myRBACService.findRoleInfoByUserAccount(userId);
        if (CollUtil.isEmpty(roleCodeList)) {
            throw new BusinessException(BusinessErrorEnums.USER_NO_AUTH, "未对用户进行角色授权");
        }
        // 查取角色已经授权的应用
        List<SysAppInfo> roleAppInfoList = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            roleAppInfoList.addAll(myRBACService.findAppIdByRoleCode(roleCode));
        }
        if (CollUtil.isEmpty(roleAppInfoList)) {
            throw new BusinessException("未对用户进行应用授权");
        }
        // 去重
        List<SysAppInfo> appInfoList = CollUtil.distinct(roleAppInfoList);
        // 分拣应用种类
        List<SysAppInfoVO> appList = new ArrayList<>();
        List<SysAppInfoVO> configList = new ArrayList<>();
        List<SysAppInfoVO> devOpsList = new ArrayList<>();
        for (SysAppInfo sysAppInfo : appInfoList) {
            switch (sysAppInfo.getAppType()) {
                case 1: {
                    // 功能应用类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(sysAppInfo, sysAppInfoVO);
                    appList.add(sysAppInfoVO);
                    break;
                }
                case 2: {
                    // 配置类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(sysAppInfo, sysAppInfoVO);
                    configList.add(sysAppInfoVO);
                    break;
                }
                case 3: {
                    // 运维类
                    SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
                    BeanUtils.copyProperties(sysAppInfo, sysAppInfoVO);
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
