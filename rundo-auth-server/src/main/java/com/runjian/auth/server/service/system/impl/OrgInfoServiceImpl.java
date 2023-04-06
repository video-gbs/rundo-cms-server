package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.SysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.entity.OrgInfo;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
import com.runjian.auth.server.mapper.OrgInfoMapper;
import com.runjian.auth.server.mapper.UserInfoMapper;
import com.runjian.auth.server.service.system.OrgInfoService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public SysOrgVO save(SysOrgDTO dto) {
        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgPid(dto.getOrgPid());
        OrgInfo parentInfo = orgInfoMapper.selectById(dto.getOrgPid());
        String orgPids = parentInfo.getOrgPids() + "[" + dto.getOrgPid() + "]";
        orgInfo.setOrgPids(orgPids);
        orgInfo.setOrgName(dto.getOrgName());
        orgInfo.setOrgNameStr(parentInfo.getOrgNameStr() + "/" + dto.getOrgName());
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
    public CommonResponse erasureById(Long id) {
        // 1.判断是否为根节点
        OrgInfo orgInfo = orgInfoMapper.selectById(id);
        if (orgInfo.getOrgPid().equals(0L)) {
            return CommonResponse.failure(BusinessErrorEnums.DEFAULT_MEDIA_DELETE_ERROR);
        }
        // 2.查取该节点的所有子代节点
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(OrgInfo::getOrgPids, orgInfo.getOrgPids() + "[" + orgInfo.getId() + "]");
        List<OrgInfo> orgInfoChild = orgInfoMapper.selectList(queryWrapper);
        // 3.剔除自己之后确认是否还存在子节点
        int size = orgInfoChild.size();
        for (int i = size - 1; i >= 0; i--) {
            OrgInfo org = orgInfoChild.get(i);
            if (org.getId().equals(orgInfo.getId())){
                orgInfoChild.remove(org);
            }
        }
        if (CollUtil.isNotEmpty(orgInfoChild)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_ORG_OPERATION2);
        }
        // 4.判断该部门是否有员工、
        LambdaQueryWrapper<UserInfo> userInfoQueryWrapper = new LambdaQueryWrapper<>();
        userInfoQueryWrapper.eq(UserInfo::getOrgId, id);
        List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQueryWrapper);
        if (CollUtil.isNotEmpty(userInfoList)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_ORG_OPERATION);
        }
        // 4.删除目标节点
        orgInfoMapper.deleteById(id);
        return CommonResponse.success(orgInfoMapper.deleteById(id));
    }

    @Override
    public void modifyById(SysOrgDTO dto) {
        OrgInfo orgInfo = new OrgInfo();
        BeanUtils.copyProperties(dto, orgInfo);
        orgInfoMapper.updateById(orgInfo);

        // 更新子节点信息

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
            throw new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "禁止本级移动到本级");
        }
        // 1. 判断目标是否存在
        OrgInfo selectOrg = orgInfoMapper.selectById(dto.getId());
        OrgInfo targetOrg = orgInfoMapper.selectById(dto.getOrgPid());
        Optional.ofNullable(selectOrg).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "移动的节点不存在或已删除"));
        Optional.ofNullable(targetOrg).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "目标位置不存在或已删除"));
        // 2. 判断是否为祖父级向子孙级别移动
        List<OrgInfo> offspringList = getOffspring(selectOrg.getOrgPids() + "[" + selectOrg.getId() + "]");
        boolean flag = offspringList.stream().anyMatch(orgInfo -> orgInfo.getId().equals(targetOrg.getId()));
        if (flag) {
            throw new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "禁止向子级移动");
        }
        // 3. 确认是否为同一层级节点移动
        if (selectOrg.getOrgPid().equals(targetOrg.getOrgPid())) {
            // 同一层级节点移动
            // 确认排序值是否相等
            if (selectOrg.getOrgSort().equals(targetOrg.getOrgSort())) {
                // 排序值相等，将选中的更新时间进行刷新
                selectOrg.setUpdatedTime(LocalDateTimeUtil.now());
                orgInfoMapper.updateById(selectOrg);
            } else {
                // 交换排序顺序
                selectOrg.setOrgSort(targetOrg.getOrgSort());
                orgInfoMapper.updateById(selectOrg);
                targetOrg.setOrgSort(selectOrg.getOrgSort());
                orgInfoMapper.updateById(targetOrg);
            }
        }
        // 4.根据id，查询当前组织的直接下级组织信息
        List<OrgInfo> childrenList = getChildren(selectOrg.getId());
        // 5.更新当前节点信息
        selectOrg.setOrgPid(targetOrg.getId());
        selectOrg.setOrgPids(targetOrg.getOrgPids() + "[" + targetOrg.getId() + "]");
        selectOrg.setLevel(targetOrg.getLevel() + 1);
        orgInfoMapper.updateById(selectOrg);
        // 6.更新子节点信息
        if (childrenList.size() > 0) {
            for (OrgInfo org : childrenList) {
                updateChildren(org, childrenList);
            }
        }
    }

    @Override
    public List<SysOrgTree> findByTree() {
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(true, OrgInfo::getOrgSort);
        queryWrapper.orderByAsc(true, OrgInfo::getUpdatedTime);
        List<OrgInfo> orgInfoList = orgInfoMapper.selectList(null);
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

    private List<OrgInfo> getOffspring(String pidstring) {
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(OrgInfo::getOrgPids, pidstring);
        return orgInfoMapper.selectList(queryWrapper);
    }

}
