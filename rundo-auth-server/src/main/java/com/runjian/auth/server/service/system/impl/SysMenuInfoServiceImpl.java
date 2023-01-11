package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysMenuInfoDTO;
import com.runjian.auth.server.entity.system.SysMenuInfo;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private RundoIdUtil idUtil;

    @Autowired
    private SysMenuInfoMapper sysMenuInfoMapper;

    @Override
    public ResponseResult addSysMenu(SysMenuInfoDTO dto) {
        SysMenuInfo sysMenuInfo = new SysMenuInfo();
        sysMenuInfo.setId(idUtil.nextId());
        sysMenuInfo.setMenuPid(dto.getMenuPid());
        sysMenuInfo.setMenuPids(dto.getMenuPids());
        sysMenuInfo.setMenuName(dto.getMenuName());
        sysMenuInfo.setMenuSort(dto.getMenuSort());
        sysMenuInfo.setUrl(dto.getUrl());
        sysMenuInfo.setIcon(dto.getIcon());
        sysMenuInfo.setHidden(dto.getHidden().toString());
        sysMenuInfo.setViewImport(dto.getViewImport());
        // sysMenuInfo.setLeaf();
        // sysMenuInfo.setStatus(dto.getStatus());
        // sysMenuInfo.setTenantId();
        // sysMenuInfo.setDeleteFlag();
        // sysMenuInfo.setCreatedBy();
        // sysMenuInfo.setUpdatedBy();
        // sysMenuInfo.setCreatedTime();
        // sysMenuInfo.setUpdatedTime();

        return new ResponseResult(200, "操作成功", sysMenuInfoMapper.insert(sysMenuInfo));
    }

    @Override
    public ResponseResult updateSysMenu(SysMenuInfoDTO dto) {
        return null;
    }
}
