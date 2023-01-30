package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.entity.system.SysMenuInfo;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.domain.dto.system.AddSysMenuInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysMenuInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysMenuInfoVO;
import com.runjian.auth.server.service.system.SysMenuInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private SysMenuInfoMapper sysMenuInfoMapper;

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
        // sysMenuInfo.setDeleteFlag();
        // sysMenuInfo.setCreatedBy();
        // sysMenuInfo.setUpdatedBy();
        // sysMenuInfo.setCreatedTime();
        // sysMenuInfo.setUpdatedTime();
        sysMenuInfoMapper.insert(sysMenuInfo);
        // 处理应用菜单映射管理
        Long menuId = sysMenuInfo.getId();
        Long appId = dto.getAppId();
        sysMenuInfoMapper.saveAppMenu(menuId, appId);
    }

    @Override
    public ResponseResult updateSysMenu(AddSysMenuInfoDTO dto) {
        return null;
    }

    @Override
    public ResponseResult<List<SysMenuInfoVO>> sysMenuInfoList() {
        List<SysMenuInfo> sysMenuInfoList = sysMenuInfoMapper.selectList(null);
        List<SysMenuInfoVO> sysMenuInfos = new ArrayList<SysMenuInfoVO>();
        for (SysMenuInfo sysMenuInfo : sysMenuInfoList) {
            SysMenuInfoVO sysMenuInfoVO = new SysMenuInfoVO();
            sysMenuInfoVO.setId(sysMenuInfo.getId());
            sysMenuInfoVO.setIcon(sysMenuInfo.getIcon());
            sysMenuInfoVO.setMenuName(sysMenuInfo.getMenuName());
            sysMenuInfoVO.setMenuSort(sysMenuInfo.getMenuSort());
            sysMenuInfoVO.setUrl(sysMenuInfo.getUrl());
            sysMenuInfoVO.setViewImport(sysMenuInfo.getViewImport());
            sysMenuInfoVO.setStatus(sysMenuInfo.getStatus());
            sysMenuInfoVO.setHidden(sysMenuInfo.getHidden());
        }
        return new ResponseResult<>(200, "操作全部", sysMenuInfos);
    }

    @Override
    public void updateSysMenuInfoById(UpdateSysMenuInfoDTO dto) {

    }

    @Override
    public SysMenuInfoVO getSysMenuInfoById(Long id) {
        SysMenuInfo menuInfo = sysMenuInfoMapper.selectById(id);
        SysMenuInfoVO sysMenuInfoVO = new SysMenuInfoVO();
        BeanUtils.copyProperties(menuInfo,sysMenuInfoVO);
        return sysMenuInfoVO;
    }

    @Override
    public List<SysMenuInfoVO> getSysMenuInfoList() {
        return null;
    }
}
