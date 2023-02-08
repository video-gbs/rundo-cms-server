package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QueryEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysRoleInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysRoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysRoleInfoServiceImpl extends ServiceImpl<SysRoleInfoMapper, SysRoleInfo> implements SysRoleInfoService {

    @Autowired
    private RundoIdUtil idUtil;
    @Autowired
    private SysRoleInfoMapper roleInfoMapper;

    @Override
    public void addRole(AddSysRoleInfoDTO dto) {
        SysRoleInfo role = new SysRoleInfo();
        Long roleId = idUtil.nextId();
        role.setId(roleId);
        role.setRoleName(dto.getRoleName());
        // 角色是一个特殊的权限，ROLE_前缀 用来满足Spring Security规范
        role.setRoleCode("ROLE_" + roleId.toString());
        role.setRoleDesc(dto.getRoleDesc());
        // role.setParentRoleId();
        // role.setParentRoleIds();
        // role.setTenantId();
        roleInfoMapper.insert(role);
        List<String> appIds = dto.getAppIds();
        List<String> configIds = dto.getConfigIds();
        List<String> devopsIds = dto.getDevopsIds();
        List<Long> orgIds = dto.getOrgIds();
        List<Long> areaIds = dto.getAreaIds();

        List<Long> appIdList = new ArrayList<>();
        appIdList.addAll(getAppIds(appIds));
        appIdList.addAll(getAppIds(configIds));
        appIdList.addAll(getAppIds(devopsIds));

        List<Long> menuIdList = new ArrayList<>();
        menuIdList.addAll(getMenuIds(appIds));
        menuIdList.addAll(getMenuIds(configIds));
        menuIdList.addAll(getMenuIds(devopsIds));

        List<Long> apiIdList = new ArrayList<>();
        apiIdList.addAll(getApiIds(appIds));
        apiIdList.addAll(getApiIds(configIds));
        apiIdList.addAll(getApiIds(devopsIds));

        // TODO 下面的操作后期要优化为批量接口
        if (CollUtil.isNotEmpty(appIdList)) {
            for (Long appId : appIdList) {
                roleInfoMapper.insertRoleApp(roleId, appId);
            }
        }

        if (CollUtil.isNotEmpty(menuIdList)) {
            for (Long menuId : menuIdList) {
                roleInfoMapper.insertRoleMenu(roleId, menuId);
            }
        }

        if (CollUtil.isNotEmpty(apiIdList)) {
            for (Long apiId : apiIdList) {
                roleInfoMapper.insertRoleApi(roleId, apiId);
            }
        }

        if (CollUtil.isNotEmpty(orgIds)) {
            for (Long orgId : orgIds) {
                roleInfoMapper.insertRoleOrg(roleId, orgId);
            }
        }

        if (CollUtil.isNotEmpty(areaIds)) {
            for (Long areaId : areaIds) {
                roleInfoMapper.insertRoleArea(roleId, areaId);
            }
        }


    }

    @Override
    public void updateRole(UpdateSysRoleInfoDTO dto) {

    }

    @Override
    public Page<SysRoleInfoVO> getSysRoleInfoByPage(QuerySysRoleInfoDTO dto) {
        PageSysRoleInfoDto page = new PageSysRoleInfoDto();
        if (dto.getRoleName() != null) {
            page.setRoleName(dto.getRoleName());
        }
        if (dto.getCreatedBy() != null) {
            page.setCreatedBy(dto.getCreatedBy());
        }
        if (dto.getUserAccount() != null) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (dto.getCreatedTimeStart() != null) {
            page.setCreatedTimeStart(dto.getCreatedTimeStart());
        }
        if (dto.getCreatedTimeEnd() != null) {
            page.setCreatedTimeEnd(dto.getCreatedTimeEnd());
        }
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }

        return roleInfoMapper.MySelectPage(page);
    }

    @Override
    public void batchRemove(List<Long> ids) {
        roleInfoMapper.deleteBatchIds(ids);

    }

    @Override
    public Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto) {
        PageEditUserSysRoleInfoDTO page = new PageEditUserSysRoleInfoDTO();
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }

        return roleInfoMapper.selectEditUserSysRoleInfoPage(page);
    }

    @Override
    public void getRoleDetailById(Long id) {
        SysRoleInfo sysRoleInfo = roleInfoMapper.selectById(id);
        // 查询该角色已授权的应用列表

        // 查询该角色已授权的菜单列表
        // 查询该角色已授权的接口列表

        // 查询该角色已授权的部门列表
        // 查询该角色已授权的安防区域

        // 查询该角色已授权的通道


    }

    /**
     * 分拣后获取应用ID
     *
     * @param stringList
     * @return
     */
    private List<Long> getAppIds(List<String> stringList) {
        List<Long> appIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(stringList)) {
            for (String str : stringList) {
                if (str.startsWith("a_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "a_"));
                    appIds.add(menuId);
                }
            }
        }
        return appIds;
    }

    /**
     * 分拣后获取菜单ID
     *
     * @param stringList
     * @return
     */
    private List<Long> getMenuIds(List<String> stringList) {
        List<Long> menuIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(stringList)) {
            for (String str : stringList) {
                if (str.startsWith("m_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "m_"));
                    menuIds.add(menuId);
                }
            }
        }
        return menuIds;
    }

    /**
     * 分拣后获取功能接口ID
     *
     * @param stringList
     * @return
     */
    private List<Long> getApiIds(List<String> stringList) {
        List<Long> apiIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(stringList)) {
            for (String str : stringList) {
                if (str.startsWith("u_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "u_"));
                    apiIds.add(menuId);
                }
            }
        }
        return apiIds;
    }
}
