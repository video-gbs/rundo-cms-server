package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysApiInfoTree;
import com.runjian.auth.server.mapper.system.SysApiInfoMapper;
import com.runjian.auth.server.service.system.SysApiInfoService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 接口信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysApiInfoServiceImpl extends ServiceImpl<SysApiInfoMapper, SysApiInfo> implements SysApiInfoService {

    @Autowired
    private SysApiInfoMapper sysApiInfoMapper;

    @Override
    public void saveSysApiInfo(AddSysApiInfoDTO dto) {
        SysApiInfo sysApiInfo = new SysApiInfo();
        sysApiInfo.setApiPid(dto.getApiPid());
        SysApiInfo parentApiInfo = sysApiInfoMapper.selectById(dto.getApiPid());
        sysApiInfo.setApiPids(parentApiInfo.getApiPids() + ",[" + dto.getApiPid() + "]");
        sysApiInfo.setApiName(dto.getApiName());
        sysApiInfo.setApiSort(dto.getApiSort());
        sysApiInfo.setLevel(parentApiInfo.getLevel() + 1);
        sysApiInfo.setUrl(dto.getUrl());
        sysApiInfo.setLeaf(0);
        sysApiInfo.setStatus(dto.getStatus());
        // sysApiInfo.setTenantId();
        // sysApiInfo.setDeleteFlag();
        // sysApiInfo.setCreatedBy();
        // sysApiInfo.setUpdatedBy();
        // sysApiInfo.setCreatedTime();
        // sysApiInfo.setUpdatedTime();
        sysApiInfoMapper.insert(sysApiInfo);
        Long apiId = sysApiInfo.getId();
        Long appId = dto.getAppId();
        // 处理应用 API映射
        sysApiInfoMapper.insertAppApi(appId, apiId);
    }

    @Override
    public void updateSysApiInfoById(UpdateSysApiInfoDTO dto) {
        SysApiInfo sysApiInfo = new SysApiInfo();
        BeanUtils.copyProperties(dto, sysApiInfo);
        sysApiInfoMapper.updateById(sysApiInfo);
    }

    @Override
    public SysApiInfoVO getSysApiInfoById(Long id) {
        SysApiInfo sysApiInfo = sysApiInfoMapper.selectById(id);
        SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
        BeanUtils.copyProperties(sysApiInfo, sysApiInfoVO);
        return sysApiInfoVO;
    }

    @Override
    public Page<SysApiInfoVO> getSysApiInfoByPage(Integer pageNum, Integer pageSize) {
        Page<SysApiInfoVO> page = new Page<>(pageNum, pageSize);
        return sysApiInfoMapper.MySelectPage(page);
    }

    @Override
    public void changeStatus(StatusSysApiInfoDTO dto) {
        SysApiInfo sysApiInfo = sysApiInfoMapper.selectById(dto.getId());
        sysApiInfo.setStatus(dto.getStatus());
        sysApiInfoMapper.updateById(sysApiInfo);
    }

    @Override
    public List<SysApiInfoTree> getSysApiInfoTree(QuerySysApiInfoDTO dto) {
        List<SysApiInfoVO> sysApiInfoVOList = new ArrayList<>();
        LambdaQueryWrapper<SysApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        List<SysApiInfo> sysApiInfoList = sysApiInfoMapper.selectList(queryWrapper);
        for (SysApiInfo sysApiInfo : sysApiInfoList) {
            SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
            BeanUtils.copyProperties(sysApiInfo, sysApiInfoVO);
            sysApiInfoVOList.add(sysApiInfoVO);
        }
        List<SysApiInfoTree> sysApiInfoTreeList = sysApiInfoVOList.stream().map(
                item -> {
                    SysApiInfoTree bean = new SysApiInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(sysApiInfoTreeList, 1L);

    }

    @Override
    public List<SysApiInfoVO> getSysApiInfoList() {
        return null;
    }


}
