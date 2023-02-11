package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.entity.ApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysApiInfoTree;
import com.runjian.auth.server.mapper.ApiInfoMapper;
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
public class SysApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements SysApiInfoService {

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Override
    public void saveSysApiInfo(AddSysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        // 减少表操作此处代表菜单页面的ID
        apiInfo.setApiPid(dto.getApiPid());
        // SysApiInfo parentApiInfo = sysApiInfoMapper.selectById(dto.getApiPid());
        // sysApiInfo.setApiPids(parentApiInfo.getApiPids() + ",[" + dto.getApiPid() + "]");
        apiInfo.setApiName(dto.getApiName());
        apiInfo.setApiSort(dto.getApiSort());
        // sysApiInfo.setLevel(parentApiInfo.getLevel() + 1);
        apiInfo.setUrl(dto.getUrl());
        apiInfo.setLeaf(0);
        apiInfo.setStatus(dto.getStatus());
        // sysApiInfo.setTenantId();
        apiInfoMapper.insert(apiInfo);
        Long apiId = apiInfo.getId();
        Long appId = dto.getAppId();
        // 处理应用 API映射
        apiInfoMapper.insertAppApi(appId, apiId);
    }

    @Override
    public void updateSysApiInfoById(UpdateSysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public SysApiInfoVO getSysApiInfoById(Long id) {
        ApiInfo apiInfo = apiInfoMapper.selectById(id);
        SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
        BeanUtils.copyProperties(apiInfo, sysApiInfoVO);
        return sysApiInfoVO;
    }

    @Override
    public Page<SysApiInfoVO> getSysApiInfoByPage(Integer pageNum, Integer pageSize) {
        Page<SysApiInfoVO> page = new Page<>(pageNum, pageSize);
        return apiInfoMapper.MySelectPage(page);
    }

    @Override
    public void changeStatus(StatusSysApiInfoDTO dto) {
        ApiInfo apiInfo = apiInfoMapper.selectById(dto.getId());
        apiInfo.setStatus(dto.getStatus());
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public List<SysApiInfoTree> getSysApiInfoTree(QuerySysApiInfoDTO dto) {
        List<SysApiInfoVO> sysApiInfoVOList = new ArrayList<>();
        LambdaQueryWrapper<ApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        List<ApiInfo> apiInfoList = apiInfoMapper.selectList(queryWrapper);
        for (ApiInfo apiInfo : apiInfoList) {
            SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
            BeanUtils.copyProperties(apiInfo, sysApiInfoVO);
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
