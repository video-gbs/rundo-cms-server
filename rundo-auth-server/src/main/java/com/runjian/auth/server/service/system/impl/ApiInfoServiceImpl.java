package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.SysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.entity.ApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.ApiInfoTree;
import com.runjian.auth.server.mapper.ApiInfoMapper;
import com.runjian.auth.server.service.system.ApiInfoService;
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
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Override
    public void save(SysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        ApiInfo parentApiInfo = apiInfoMapper.selectById(dto.getApiPid());
        apiInfo.setApiPids(parentApiInfo.getApiPids() + "[" + dto.getApiPid() + "]");
        apiInfo.setLevel(parentApiInfo.getLevel() + 1);
        apiInfo.setLeaf(0);
        apiInfoMapper.insert(apiInfo);
    }

    @Override
    public void modifyById(SysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public SysApiInfoVO findById(Long id) {
        ApiInfo apiInfo = apiInfoMapper.selectById(id);
        SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
        BeanUtils.copyProperties(apiInfo, sysApiInfoVO);
        return sysApiInfoVO;
    }

    @Override
    public Page<SysApiInfoVO> findByPage(Integer pageNum, Integer pageSize) {
        Page<SysApiInfoVO> page = new Page<>(pageNum, pageSize);
        return apiInfoMapper.MySelectPage(page);
    }

    @Override
    public void modifyByStatus(StatusSysApiInfoDTO dto) {
        ApiInfo apiInfo = apiInfoMapper.selectById(dto.getId());
        apiInfo.setStatus(dto.getStatus());
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public List<ApiInfoTree> findByTree(QuerySysApiInfoDTO dto) {
        List<ApiInfo> apiInfoList = getByList(dto);
        List<ApiInfoTree> apiInfoTreeList = apiInfoList.stream().map(
                item -> {
                    ApiInfoTree bean = new ApiInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(apiInfoTreeList, 1L);

    }

    @Override
    public List<ApiInfoTree> getTreeByAppId(Long appId) {
        LambdaQueryWrapper<ApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiInfo::getAppId, appId);
        List<ApiInfo> apiInfoList = apiInfoMapper.selectList(queryWrapper);
        long rootId = 0;
        List<ApiInfoTree> apiInfoTreeList = new ArrayList<>();
        for (ApiInfo apiInfo : apiInfoList) {
            if (apiInfo.getAppId().longValue() == appId && apiInfo.getApiPid() == 1L) {
                rootId = apiInfo.getId();
            }
            ApiInfoTree bean = new ApiInfoTree();
            BeanUtils.copyProperties(apiInfo, bean);
            apiInfoTreeList.add(bean);
        }
        return DataTreeUtil.buildTree(apiInfoTreeList, rootId);
    }

    @Override
    public List<ApiInfo> getApiInfoByRoleCode(String roleCode) {
        return apiInfoMapper.selectApiInfoByRoleCode(roleCode);
    }

    @Override
    public List<Long> getApiIdListByRoleId(Long roleId) {
        return apiInfoMapper.findApiIdListByRoleId(roleId);
    }

    @Override
    public List<SysApiInfoVO> findByList(QuerySysApiInfoDTO dto) {
        return getByList(dto).stream().map(
                item -> {
                    SysApiInfoVO vo = new SysApiInfoVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }
        ).collect(Collectors.toList());
    }


    private List<ApiInfo> getByList(QuerySysApiInfoDTO dto) {
        LambdaQueryWrapper<ApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (null != dto.getUrl()) {
            queryWrapper.eq(ApiInfo::getUrl, dto.getUrl());
        }
        if (null != dto.getApiName()) {
            queryWrapper.eq(ApiInfo::getApiName, dto.getApiName());
        }
        if (null != dto.getAppId()) {
            queryWrapper.eq(ApiInfo::getAppId, dto.getAppId()).or().eq(ApiInfo::getAppId, 0L);
        }
        return apiInfoMapper.selectList(queryWrapper);
    }

}
