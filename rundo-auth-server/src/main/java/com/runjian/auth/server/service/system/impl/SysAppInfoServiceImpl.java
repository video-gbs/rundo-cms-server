package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.AddSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysAppInfoDTO;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import com.runjian.auth.server.mapper.system.SysAppInfoMapper;
import com.runjian.auth.server.service.system.SysAppInfoService;
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
public class SysAppInfoServiceImpl extends ServiceImpl<SysAppInfoMapper, AppInfo> implements SysAppInfoService {

    @Autowired
    private SysAppInfoMapper sysAppInfoMapper;

    @Override
    public void saveSysAppInfo(AddSysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        sysAppInfoMapper.insert(appInfo);
    }

    @Override
    public void updateSysAppInfoById(UpdateSysAppInfoDTO dto) {
        AppInfo appInfo = new AppInfo();
        BeanUtils.copyProperties(dto, appInfo);
        sysAppInfoMapper.updateById(appInfo);
    }

    @Override
    public void removeSysAppInfoById(Long id) {
        sysAppInfoMapper.deleteById(id);
    }

    @Override
    public Page<SysAppInfoVO> getSysAppInfoByPage(QuerySysAppInfoDTO dto) {
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
        return sysAppInfoMapper.MySelectPage(page);
    }

    @Override
    public void changeStatus(StatusSysAppInfoDTO dto) {
        AppInfo appInfo = sysAppInfoMapper.selectById(dto.getId());
        appInfo.setStatus(dto.getStatus());
        sysAppInfoMapper.updateById(appInfo);
    }

    @Override
    public SysAppInfoVO getSysAppInfoById(Long id) {
        AppInfo appInfo = sysAppInfoMapper.selectById(id);
        SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
        BeanUtils.copyProperties(appInfo, sysAppInfoVO);
        return sysAppInfoVO;
    }

    @Override
    public List<SysAppInfoVO> getSysAppInfoList() {
        List<SysAppInfoVO> sysAppInfoVOList = new ArrayList<>();
        List<AppInfo> appInfoList = sysAppInfoMapper.selectList(null);
        for (AppInfo appInfo : appInfoList) {
            SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
            BeanUtils.copyProperties(appInfo, sysAppInfoVO);
            sysAppInfoVOList.add(sysAppInfoVO);
        }
        return sysAppInfoVOList;
    }


}
