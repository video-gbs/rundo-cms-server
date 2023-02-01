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

import java.util.ArrayList;
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
        SysOrg parentInfo = sysOrgMapper.selectById(dto.getOrgPid());
        String orgPids = parentInfo.getOrgPids() + "[" + dto.getOrgPid() + "]";
        sysOrg.setOrgPids(orgPids);
        sysOrg.setOrgName(dto.getOrgName());
        sysOrg.setOrgCode(dto.getOrgCode());
        sysOrg.setOrgSort(dto.getOrgSort());
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

    }

    @Override
    public String removeSysOrgById(Long id) {
        // 1.确认当前需要删除的组织有无下级组织
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysOrg::getOrgPids, "[" + id + "]");
        List<SysOrg> sysOrgChild = sysOrgMapper.selectList(queryWrapper);
        if (sysOrgChild.size() > 0) {
            // 1.1 有下级组织不允许删除
            return "不能删除含有下级组织的机构";
        }
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
                item ->{
                    SysOrgVO sysOrgVO = new SysOrgVO();
                    BeanUtils.copyProperties(item,sysOrgVO);
                    return sysOrgVO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void moveSysOrg(MoveSysOrgDTO dto) {
        // TODO
        // 1.根据上级组织ID，查询上级组织ID信息
        SysOrg parentInfo = sysOrgMapper.selectById(dto.getOrgPid());
        // 2.根据id，查询当前组织的信息
        SysOrg sysOrg = sysOrgMapper.selectById(dto.getId());
        // 3.根据id，查询当前组织的下级组织信息
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysOrg::getOrgPids, "[" + dto.getId() + "]");
        List<SysOrg> childrenList = sysOrgMapper.selectList(queryWrapper);
        // 4.更新当前节点信息

        // 5.更新子节点信息

    }


    @Override
    public List<SysOrgTree> getSysOrgTree() {
        List<SysOrgVO> sysOrgVOList = new ArrayList<>();
        LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<>();
        List<SysOrg> sysOrgList = sysOrgMapper.selectList(queryWrapper);
        for (SysOrg sysOrg : sysOrgList) {
            SysOrgVO sysOrgVO = new SysOrgVO();
            BeanUtils.copyProperties(sysOrg, sysOrgVO);
            sysOrgVOList.add(sysOrgVO);
        }
        List<SysOrgTree> sysOrgTreeList = sysOrgList.stream().map(
                item -> {
                    SysOrgTree bean = new SysOrgTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buiidTree(sysOrgTreeList, 1L);
    }

    @Override
    public IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize) {
        Page<SysOrg> page = Page.of(pageNum, pageSize);
        return sysOrgMapper.selectPage(page, null);
    }


}
