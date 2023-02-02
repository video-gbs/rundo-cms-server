package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysUserInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysOrg;
import com.runjian.auth.server.domain.entity.system.SysUserInfo;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {
    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysRoleInfoMapper sysRoleInfoMapper;

    @Override
    public void saveSysUserInfo(AddSysUserInfoDTO dto) {
        // 处理基本信息
        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtils.copyProperties(dto, sysUserInfo);
        // 处理密码
        String password = passwordUtil.encode(dto.getPassword());
        sysUserInfo.setPassword(password);
        // sysUserInfo.setTenantId();
        // sysUserInfo.setDeleteFlag();
        // sysUserInfo.setCreatedBy();
        // sysUserInfo.setUpdatedBy();
        // sysUserInfo.setCreatedTime();
        // sysUserInfo.setUpdatedTime();
        sysUserInfoMapper.insert(sysUserInfo);
        // 处理部门信息
        sysUserInfoMapper.insertUserOrg(sysUserInfo.getId(), dto.getOrgId());
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (roleIds.size() > 0) {
            for (Long roleId : roleIds) {
                sysUserInfoMapper.insertUserRole(sysUserInfo.getId(), roleId);
            }
        }


    }

    @Override
    public void updateSysUserInfo(UpdateSysUserInfoDTO dto) {
        // 根据id查取原始信息
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(dto.getId());
        // 根据id查取角色信息
        List<Long> oldRoleIds = sysUserInfoMapper.selectRoleByUserId(dto.getId());
        // 处理信息
        BeanUtils.copyProperties(dto, sysUserInfo);
        String password = passwordUtil.encode(dto.getPassword());
        sysUserInfo.setPassword(password);
        sysUserInfoMapper.updateById(sysUserInfo);
        // 原始关联角色为空 则提交关联角色为新增
        List<Long> newRoleIds = dto.getRoleIds();
        if (oldRoleIds == null || oldRoleIds.isEmpty()) {
            for (Long roleId : newRoleIds) {
                sysUserInfoMapper.insertUserRole(sysUserInfo.getId(), roleId);
            }
        }
        // 如果提交的角色为空，则删除所有的角色关联
        if (null == newRoleIds || newRoleIds.isEmpty()) {
            sysUserInfoMapper.deleteUserRole(sysUserInfo.getId(), null);
        }
        // 提交的角色与原始的角色均不为空
        // 采取Lambda表达式取得相同的角色
        List<Long> common = oldRoleIds.stream().filter(p -> newRoleIds.contains(p)).collect(Collectors.toList());
        // 原始角色列表剔除相同部分后删除授权
        oldRoleIds.removeAll(common);
        for (Long roleId : oldRoleIds) {
            sysUserInfoMapper.deleteUserRole(sysUserInfo.getId(), roleId);
        }
        // 新提交的角色列表剔除相同部分后新增授权
        newRoleIds.removeAll(common);
        for (Long roleId : newRoleIds) {
            sysUserInfoMapper.insertUserRole(sysUserInfo.getId(), roleId);
        }

    }

    @Override
    public ResponseResult<ListSysUserInfoVO> getUser(Long id) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(id);
        ListSysUserInfoVO sysUserInfoVO = getSysUserInfoVO(sysUserInfo);
        return new ResponseResult<>(200, "操作成功", sysUserInfoVO);
    }

    @Override
    public ResponseResult<List<ListSysUserInfoVO>> getUserList() {
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(null);
        List<ListSysUserInfoVO> sysUserVOList = new ArrayList<>();
        for (SysUserInfo sysUserInfo : sysUserInfos) {
            ListSysUserInfoVO sysUserInfoVO = getSysUserInfoVO(sysUserInfo);
            sysUserVOList.add(sysUserInfoVO);
        }
        return new ResponseResult<>(200, "操作成功", sysUserVOList);
    }

    @Override
    public void changeStatus(StatusSysUserInfoDTO dto) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(dto.getId());
        sysUserInfo.setStatus(dto.getStatus());
        sysUserInfoMapper.updateById(sysUserInfo);
    }

    @Override
    public Page<ListSysUserInfoVO> getSysUserInfoByPage(QuerySysUserInfoDTO dto) {
        return null;
    }

    /**
     * 封装处理用户基本信息
     *
     * @param sysUserInfo
     * @return
     */
    private ListSysUserInfoVO getSysUserInfoVO(SysUserInfo sysUserInfo) {
        ListSysUserInfoVO sysUserInfoVO = new ListSysUserInfoVO();
        sysUserInfoVO.setId(sysUserInfo.getId());
        sysUserInfoVO.setUserAccount(sysUserInfo.getUserAccount());
        sysUserInfoVO.setUserName(sysUserInfo.getUserName());
        // TODO 处理部门ID
        // SysOrg sysOrg = getByUserId(sysUserInfo.getId());
        // sysUserInfoVO.setOrgId(sysOrg.getId());
        // sysUserInfoVO.setOrgName(sysOrg.getOrgName());
        // TODO 处理角色
        // Map<Long, String> roleInfo = new HashMap<>();
        // sysUserInfoVO.setRoleIds(roleInfo);
        //
        // sysUserInfoVO.setJobNo(sysUserInfo.getJobNo());
        // sysUserInfoVO.setCreatedTime(sysUserInfo.getCreatedTime());
        // sysUserInfoVO.setUpdatedTime(sysUserInfo.getUpdatedTime());
        // sysUserInfoVO.setDeleteFlag(null);
        // sysUserInfoVO.setExpiryDateStart(sysUserInfo.getExpiryDateStart());
        // sysUserInfoVO.setExpiryDateEnd(sysUserInfo.getExpiryDateEnd());
        // sysUserInfoVO.setPhone(sysUserInfo.getPhone());
        // sysUserInfoVO.setAddress(sysUserInfo.getAddress());
        // sysUserInfoVO.setDescription(sysUserInfo.getDescription());
        return sysUserInfoVO;
    }

    private SysOrg getByUserId(Long userId) {
        String orgTreeName = "";
        Long orgId = null;//sysOrgMapper.getByUserId(userId);
        SysOrg sysOrgInfo = sysOrgMapper.selectById(orgId);
        String orgPids = sysOrgInfo.getOrgPids();

        List<SysOrg> sysOrgList = sysOrgMapper.selectOrgTree(orgId, null);
        for (SysOrg sysOrg : sysOrgList) {
            orgTreeName = orgTreeName.concat("/").concat(sysOrg.getOrgName());
        }
        SysOrg sysOrg = new SysOrg();
        sysOrg.setId(orgId);
        sysOrg.setOrgName(orgTreeName);
        return sysOrg;
    }


}
