package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.entity.system.SysOrg;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements SysOrgService {

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Override
    public void saveSysOrg(AddSysOrgDTO dto) {
        SysOrg sysOrg = new SysOrg();
        sysOrg.setOrgPid(dto.getOrgPid());
        // TODO 处理上级节点
        // sysOrg.setOrgPids();
        sysOrg.setOrgName(dto.getOrgName());
        sysOrg.setOrgCode(dto.getOrgCode());
        sysOrg.setOrgSort(dto.getOrgSort());
        sysOrg.setAdders(dto.getAdders());
        sysOrg.setOrgLeader(dto.getOrgLeader());
        sysOrg.setEmail(dto.getEmail());
        sysOrg.setPhone(dto.getPhone());
        // TODO 处理组织机构层级
        // sysOrg.setLevel();
        sysOrg.setLeaf(0);
        sysOrg.setDescription(dto.getDescription());
        sysOrg.setStatus(0);

        // sysOrg.setTenantId();
        // sysOrg.setDeleteFlag();
        // sysOrg.setCreatedBy();
        // sysOrg.setUpdatedBy();
        // sysOrg.setCreatedTime();
        // sysOrg.setUpdatedTime();

        sysOrgMapper.insert(sysOrg);

    }


    public List<SysOrgTree> getSysOrgTree(Long orgId, String orgName) {
        if (orgId != null) {
            List<SysOrg> orgList = sysOrgMapper.selectOrgTree(orgId, orgName);

            List<SysOrgTree> sysOrgTreeList = orgList.stream().map(
                    item -> {
                        SysOrgTree bean = new SysOrgTree();
                        BeanUtils.copyProperties(item, bean);
                        return bean;
                    }
            ).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(orgName)) {
                return sysOrgTreeList;
            } else {
                return DataTreeUtil.buiidTree(sysOrgTreeList, orgId);
            }

        } else {
            throw new RuntimeException("查询组织机构ID不能为空");
        }
    }

    @Override
    public IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize) {
        Page<SysOrg> page = Page.of(pageNum, pageSize);
        return sysOrgMapper.selectPage(page, null);
    }



}
