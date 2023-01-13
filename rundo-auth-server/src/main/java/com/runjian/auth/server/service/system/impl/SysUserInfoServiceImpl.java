package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.SysUserInfoVO;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.SysOrgMapper;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import com.runjian.auth.server.service.system.SysUserInfoService;
import com.runjian.auth.server.util.PasswordUtil;
import com.runjian.auth.server.util.RundoIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RundoIdUtil idUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysRoleInfoMapper sysRoleInfoMapper;

    @Override
    public ResponseResult addUser(SysUserInfoDTO dto) {
        // 处理基本信息
        SysUserInfo sysUserInfo = dto.getUserInfo();
        Long userId = idUtil.nextId();
        String password = passwordUtil.encode(sysUserInfo.getPassword());
        sysUserInfo.setId(userId);
        sysUserInfo.setPassword(password);
        sysUserInfoMapper.insert(sysUserInfo);
        // 处理部门信息
        sysOrgMapper.saveUserOrg(userId, dto.getOrgId());
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        for (Long roleId : roleIds) {
            sysRoleInfoMapper.saveUserRole(userId, roleId);
        }
        return new ResponseResult<>(200, "操作成功");
    }

    @Override
    public ResponseResult updateUser(SysUserInfoDTO dto) {
        return null;
    }

    @Override
    public ResponseResult<SysUserInfoVO> getUser(Long id) {
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectById(id);
        SysUserInfoVO sysUserInfoVO = getSysUserInfoVO(sysUserInfo);
        return new ResponseResult<>(200, "操作成功", sysUserInfoVO);
    }

    @Override
    public ResponseResult<List<SysUserInfoVO>> getUserList() {
        List<SysUserInfo> sysUserInfos = sysUserInfoMapper.selectList(null);
        List<SysUserInfoVO> sysUserVOList = new ArrayList<>();
        for (SysUserInfo sysUserInfo : sysUserInfos) {
            SysUserInfoVO sysUserInfoVO = getSysUserInfoVO(sysUserInfo);
            sysUserVOList.add(sysUserInfoVO);
        }
        return new ResponseResult<>(200, "操作成功", sysUserVOList);
    }

    private SysUserInfoVO getSysUserInfoVO(SysUserInfo sysUserInfo) {
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        sysUserInfoVO.setId(sysUserInfo.getId());
        sysUserInfoVO.setUserAccount(sysUserInfo.getUserAccount());
        sysUserInfoVO.setUserName(sysUserInfo.getUserName());
        // TODO 处理部门ID
        sysUserInfoVO.setOrgId(null);
        sysUserInfoVO.setOrgName(null);
        // TODO 处理角色
        Map<Long, String> roleInfo = new HashMap<>();
        sysUserInfoVO.setRoleIds(roleInfo);

        sysUserInfoVO.setJobNo(sysUserInfo.getJobNo());
        sysUserInfoVO.setCreatedTime(sysUserInfo.getCreatedTime());
        sysUserInfoVO.setUpdatedTime(sysUserInfo.getUpdatedTime());
        sysUserInfoVO.setDeleteFlag(null);
        sysUserInfoVO.setExpiryDateStart(sysUserInfo.getExpiryDateStart());
        sysUserInfoVO.setExpiryDateEnd(sysUserInfo.getExpiryDateEnd());
        sysUserInfoVO.setPhone(sysUserInfo.getPhone());
        sysUserInfoVO.setAddress(sysUserInfo.getAddress());
        sysUserInfoVO.setDescription(sysUserInfo.getDescription());
        return sysUserInfoVO;
    }

}
