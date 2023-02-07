package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.entity.system.SysOrg;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
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
    public SysOrgVO saveSysOrg(AddSysOrgDTO dto) {
        SysOrg sysOrg = new SysOrg();
        sysOrg.setOrgPid(dto.getOrgPid());
        SysOrg parentInfo = sysOrgMapper.selectById(dto.getOrgPid());
        String orgPids = parentInfo.getOrgPids() + "[" + dto.getOrgPid() + "]";
        sysOrg.setOrgPids(orgPids);
        sysOrg.setOrgName(dto.getOrgName());
        sysOrg.setOrgCode(dto.getOrgCode());
        if (null != dto.getOrgSort()) {
            sysOrg.setOrgSort(dto.getOrgSort());
        }
        sysOrg.setOrgSort(100);
        sysOrg.setAdders(dto.getAdders());
        sysOrg.setOrgLeader(dto.getOrgLeader());
        sysOrg.setEmail(dto.getEmail());
        sysOrg.setPhone(dto.getPhone());
        sysOrg.setLevel(parentInfo.getLevel() + 1);
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

        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(sysOrg, sysOrgVO);
        return sysOrgVO;

    }

    @Override
    public String removeSysOrgById(Long id) {
        // 1.判断是否为根节点
        SysOrg sysOrg = sysOrgMapper.selectById(id);
        if (sysOrg.getOrgPid().equals(0L)) {
            return "系统内置根节点不能删除";
        }
        // 2.查取该节点的所有子代节点
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(SysOrg::getOrgPids, sysOrg.getOrgPids() + "[" + sysOrg.getId() + "]");
        List<SysOrg> sysOrgChild = sysOrgMapper.selectList(queryWrapper);
        // 3.删除子代节点
        for (SysOrg org : sysOrgChild) {
            sysOrgMapper.deleteById(org.getId());
        }
        // 4.删除目标节点
        sysOrgMapper.deleteById(id);
        return "删除组织，操作成功!";
    }

    @Override
    public void updateSysOrgById(UpdateSysOrgDTO dto) {
        SysOrg sysOrg = new SysOrg();
        BeanUtils.copyProperties(dto, sysOrg);
        sysOrgMapper.updateById(sysOrg);
    }

    @Override
    public SysOrgVO getSysOrgById(Long id) {
        SysOrg sysOrg = sysOrgMapper.selectById(id);
        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(sysOrg, sysOrgVO);
        return sysOrgVO;
    }

    @Override
    public List<SysOrgVO> getSysOrgList() {
        List<SysOrg> sysOrgList = sysOrgMapper.selectList(null);
        return sysOrgList.stream().map(
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
        SysOrg parentInfo = sysOrgMapper.selectById(dto.getOrgPid());
        // 1-1 同一节点下的移动判断
        SysOrg parentInfoId = sysOrgMapper.selectById(dto.getId());
        if (parentInfoId.getOrgPid().equals(parentInfo.getOrgPid())) {
            // 切换排序顺序
            parentInfoId.setOrgSort(parentInfo.getOrgSort());
            sysOrgMapper.updateById(parentInfo);
            parentInfo.setOrgSort(parentInfoId.getOrgSort());
            sysOrgMapper.updateById(parentInfo);
            return;
        }

        // 2.根据id，查询当前组织的信息
        SysOrg sysOrg = sysOrgMapper.selectById(dto.getId());
        // 3.根据id，查询当前组织的直接下级组织信息
        List<SysOrg> childrenList = getChildren(sysOrg.getId());
        // 4.更新当前节点信息
        sysOrg.setOrgPid(parentInfo.getId());
        sysOrg.setOrgPids(parentInfo.getOrgPids() + "[" + parentInfo.getId() + "]");
        sysOrg.setLevel(parentInfo.getLevel() + 1);
        sysOrgMapper.updateById(sysOrg);
        // 5.更新子节点信息
        if (childrenList.size() > 0) {
            for (SysOrg org : childrenList) {
                updateChildren(org, childrenList);
            }
        }


    }

    @Override
    public String batchDelete(List<Long> ids) {
        // 1.确定节点ID不为空
        if (ids.size() <= 0) {
            return "没有选定删除目标";
        }
        // 2.检索删除的节点中是否包含根节点
        List<SysOrg> sysOrgList = sysOrgMapper.selectBatchIds(ids);
        boolean flag = false;
        for (SysOrg sysOrg : sysOrgList) {
            if (sysOrg.getOrgPid().equals(0L)) {
                flag = true;
            }
        }
        if (flag) {
            return "删除目标中包含系统内置根节点";
        }
        sysOrgMapper.deleteBatchIds(ids);
        return "删除组织，操作成功!";

    }

    @Override
    public List<SysOrgTree> getSysOrgTree() {
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(true, SysOrg::getOrgSort);
        queryWrapper.orderByAsc(true, SysOrg::getUpdatedTime);
        List<SysOrg> sysOrgList = sysOrgMapper.selectList(queryWrapper);
        List<SysOrgTree> sysOrgTreeList = sysOrgList.stream().map(
                item -> {
                    SysOrgTree bean = new SysOrgTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(sysOrgTreeList, 1L);
    }

    @Override
    public IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize) {
        Page<SysOrg> page = Page.of(pageNum, pageSize);
        return sysOrgMapper.selectPage(page, null);
    }

    private void updateChildren(SysOrg sysOrg, List<SysOrg> childrenList) {
        SysOrg parentInfo = sysOrgMapper.selectById(sysOrg.getOrgPid());
        for (SysOrg org : childrenList) {
            org.setOrgPids(parentInfo.getOrgPids() + "[" + sysOrg.getId() + "]");
            org.setLevel(parentInfo.getLevel() + 1);
            sysOrgMapper.updateById(org);
            List<SysOrg> sunChildrenList = getChildren(org.getId());
            if (sunChildrenList.size() > 0) {
                for (SysOrg sun : sunChildrenList) {
                    updateChildren(sun, sunChildrenList);
                }
            }
        }
    }

    private List<SysOrg> getChildren(Long id) {
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysOrg::getOrgPid, id);
        queryWrapper.orderBy(true, true, SysOrg::getOrgSort, SysOrg::getUpdatedTime);
        return sysOrgMapper.selectList(queryWrapper);
    }

}
