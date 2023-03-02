package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.entity.OrgInfo;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.mapper.OrgInfoMapper;
import com.runjian.auth.server.service.system.OrgInfoService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessException;
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
public class OrgInfoServiceImpl extends ServiceImpl<OrgInfoMapper, OrgInfo> implements OrgInfoService {

    @Autowired
    private OrgInfoMapper orgInfoMapper;

    @Override
    public SysOrgVO save(AddSysOrgDTO dto) {
        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgPid(dto.getOrgPid());
        OrgInfo parentInfo = orgInfoMapper.selectById(dto.getOrgPid());
        String orgPids = parentInfo.getOrgPids() + "[" + dto.getOrgPid() + "]";
        orgInfo.setOrgPids(orgPids);
        orgInfo.setOrgName(dto.getOrgName());
        orgInfo.setOrgCode(dto.getOrgCode());
        if (null != dto.getOrgSort()) {
            orgInfo.setOrgSort(dto.getOrgSort());
        }
        orgInfo.setOrgSort(100);
        orgInfo.setAdders(dto.getAdders());
        orgInfo.setOrgLeader(dto.getOrgLeader());
        orgInfo.setEmail(dto.getEmail());
        orgInfo.setPhone(dto.getPhone());
        orgInfo.setLevel(parentInfo.getLevel() + 1);
        orgInfo.setLeaf(0);
        orgInfo.setDescription(dto.getDescription());
        orgInfo.setStatus(0);

        // sysOrg.setTenantId();

        orgInfoMapper.insert(orgInfo);

        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(orgInfo, sysOrgVO);
        return sysOrgVO;

    }

    @Override
    public String erasureById(Long id) {
        // 1.判断是否为根节点
        OrgInfo orgInfo = orgInfoMapper.selectById(id);
        if (orgInfo.getOrgPid().equals(0L)) {
            throw new BusinessException("系统内置根节点不能删除");
        }
        // 2.查取该节点的所有子代节点
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(OrgInfo::getOrgPids, orgInfo.getOrgPids() + "[" + orgInfo.getId() + "]");
        List<OrgInfo> orgInfoChild = orgInfoMapper.selectList(queryWrapper);
        // 3.删除子代节点
        for (OrgInfo org : orgInfoChild) {
            orgInfoMapper.deleteById(org.getId());
        }
        // 4.删除目标节点
        orgInfoMapper.deleteById(id);
        return "删除组织，操作成功!";
    }

    @Override
    public void modifyById(UpdateSysOrgDTO dto) {
        OrgInfo orgInfo = new OrgInfo();
        BeanUtils.copyProperties(dto, orgInfo);
        orgInfoMapper.updateById(orgInfo);
    }

    @Override
    public SysOrgVO findById(Long id) {
        OrgInfo orgInfo = orgInfoMapper.selectById(id);
        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(orgInfo, sysOrgVO);
        return sysOrgVO;
    }

    @Override
    public List<SysOrgVO> findByList() {
        List<OrgInfo> orgInfoList = orgInfoMapper.selectList(null);
        return orgInfoList.stream().map(
                item -> {
                    SysOrgVO sysOrgVO = new SysOrgVO();
                    BeanUtils.copyProperties(item, sysOrgVO);
                    return sysOrgVO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void moveSysOrg(MoveSysOrgDTO dto) {
        //  0-1 禁止本级移动到本级
        if (dto.getId().equals(dto.getOrgPid())) {
            return;
        }
        // 1.根据上级组织ID，查询上级组织ID信息
        OrgInfo parentInfo = orgInfoMapper.selectById(dto.getOrgPid());
        // 1-1 同一节点下的移动判断
        OrgInfo parentInfoId = orgInfoMapper.selectById(dto.getId());
        if (parentInfoId.getOrgPid().equals(parentInfo.getOrgPid())) {
            // 切换排序顺序
            parentInfoId.setOrgSort(parentInfo.getOrgSort());
            orgInfoMapper.updateById(parentInfo);
            parentInfo.setOrgSort(parentInfoId.getOrgSort());
            orgInfoMapper.updateById(parentInfo);
            return;
        }

        // 2.根据id，查询当前组织的信息
        OrgInfo orgInfo = orgInfoMapper.selectById(dto.getId());
        // 3.根据id，查询当前组织的直接下级组织信息
        List<OrgInfo> childrenList = getChildren(orgInfo.getId());
        // 4.更新当前节点信息
        orgInfo.setOrgPid(parentInfo.getId());
        orgInfo.setOrgPids(parentInfo.getOrgPids() + "[" + parentInfo.getId() + "]");
        orgInfo.setLevel(parentInfo.getLevel() + 1);
        orgInfoMapper.updateById(orgInfo);
        // 5.更新子节点信息
        if (childrenList.size() > 0) {
            for (OrgInfo org : childrenList) {
                updateChildren(org, childrenList);
            }
        }


    }

    @Override
    public String erasureBatch(List<Long> ids) {
        // 1.确定节点ID不为空
        if (ids.size() <= 0) {
            return "没有选定删除目标";
        }
        // 2.检索删除的节点中是否包含根节点
        List<OrgInfo> orgInfoList = orgInfoMapper.selectBatchIds(ids);
        boolean flag = false;
        for (OrgInfo orgInfo : orgInfoList) {
            if (orgInfo.getOrgPid().equals(0L)) {
                flag = true;
            }
        }
        if (flag) {
            return "删除目标中包含系统内置根节点";
        }
        orgInfoMapper.deleteBatchIds(ids);
        return "删除组织，操作成功!";

    }

    @Override
    public List<SysOrgTree> findByTree() {
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(true, OrgInfo::getOrgSort);
        queryWrapper.orderByAsc(true, OrgInfo::getUpdatedTime);
        List<OrgInfo> orgInfoList = orgInfoMapper.selectList(queryWrapper);
        List<SysOrgTree> sysOrgTreeList = orgInfoList.stream().map(
                item -> {
                    SysOrgTree bean = new SysOrgTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(sysOrgTreeList, 1L);
    }

    @Override
    public IPage<OrgInfo> findByPage(Integer pageNum, Integer pageSize) {
        Page<OrgInfo> page = Page.of(pageNum, pageSize);
        return orgInfoMapper.selectPage(page, null);
    }

    private void updateChildren(OrgInfo orgInfo, List<OrgInfo> childrenList) {
        OrgInfo parentInfo = orgInfoMapper.selectById(orgInfo.getOrgPid());
        for (OrgInfo org : childrenList) {
            org.setOrgPids(parentInfo.getOrgPids() + "[" + orgInfo.getId() + "]");
            org.setLevel(parentInfo.getLevel() + 1);
            orgInfoMapper.updateById(org);
            List<OrgInfo> sunChildrenList = getChildren(org.getId());
            if (sunChildrenList.size() > 0) {
                for (OrgInfo sun : sunChildrenList) {
                    updateChildren(sun, sunChildrenList);
                }
            }
        }
    }

    private List<OrgInfo> getChildren(Long id) {
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrgInfo::getOrgPid, id);
        queryWrapper.orderBy(true, true, OrgInfo::getOrgSort, OrgInfo::getUpdatedTime);
        return orgInfoMapper.selectList(queryWrapper);
    }

}
