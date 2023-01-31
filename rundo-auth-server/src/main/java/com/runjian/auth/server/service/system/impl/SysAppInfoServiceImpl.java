package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.AddSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysAppInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysAppInfo;
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
public class SysAppInfoServiceImpl extends ServiceImpl<SysAppInfoMapper, SysAppInfo> implements SysAppInfoService {

    @Autowired
    private SysAppInfoMapper sysAppInfoMapper;

    @Override
    public void saveSysAppInfo(AddSysAppInfoDTO dto) {
        SysAppInfo sysAppInfo = new SysAppInfo();
        BeanUtils.copyProperties(dto, sysAppInfo);
        sysAppInfoMapper.insert(sysAppInfo);
    }

    @Override
    public void updateSysAppInfoById(UpdateSysAppInfoDTO dto) {
        SysAppInfo sysAppInfo = new SysAppInfo();
        BeanUtils.copyProperties(dto, sysAppInfo);
        sysAppInfoMapper.updateById(sysAppInfo);
    }

    @Override
    public void removeSysAppInfoById(Long id) {
        sysAppInfoMapper.deleteById(id);
    }

    @Override
    public Page<SysAppInfoVO> getSysAppInfoByPage(QuerySysAppInfoDTO dto) {
        PageSysAppInfoDTO pageSysAppInfoDTO = new PageSysAppInfoDTO();
        pageSysAppInfoDTO.setAppName(dto.getAppName());
        pageSysAppInfoDTO.setAppIp(dto.getAppIp());
        pageSysAppInfoDTO.setCurrent(dto.getCurrent());
        pageSysAppInfoDTO.setSize(dto.getPageSize());

        Page<SysAppInfoVO> page = sysAppInfoMapper.MySelectPage(pageSysAppInfoDTO);
        return page;
    }

    @Override
    public SysAppInfoVO getSysAppInfoById(Long id) {
        SysAppInfo sysAppInfo = sysAppInfoMapper.selectById(id);
        SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
        BeanUtils.copyProperties(sysAppInfo, sysAppInfoVO);
        return sysAppInfoVO;
    }

    @Override
    public List<SysAppInfoVO> getSysAppInfoList() {
        List<SysAppInfoVO> sysAppInfoVOList = new ArrayList<>();
        List<SysAppInfo> sysAppInfoList = sysAppInfoMapper.selectList(null);
        for (SysAppInfo sysAppInfo : sysAppInfoList) {
            SysAppInfoVO sysAppInfoVO = new SysAppInfoVO();
            BeanUtils.copyProperties(sysAppInfo, sysAppInfoVO);
            sysAppInfoVOList.add(sysAppInfoVO);
        }
        return sysAppInfoVOList;
    }


}
