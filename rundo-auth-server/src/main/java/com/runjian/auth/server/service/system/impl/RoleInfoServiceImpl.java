package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.common.BatchDTO;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.*;
import com.runjian.auth.server.domain.vo.system.*;
import com.runjian.auth.server.domain.vo.tree.AppMenuApiTree;
import com.runjian.auth.server.mapper.AppMenuApiMapper;
import com.runjian.auth.server.mapper.RoleInfoMapper;
import com.runjian.auth.server.service.system.RoleInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements RoleInfoService {

    @Autowired
    private RundoIdUtil idUtil;
    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private AppMenuApiMapper appMenuApiMapper;


    @Override
    public void save(AddSysRoleInfoDTO dto) {
        RoleInfo role = new RoleInfo();
        Long roleId = idUtil.nextId();
        role.setId(roleId);
        role.setRoleName(dto.getRoleName());
        // 角色新建后默认是启用状态： 0启用，1禁用
        role.setStatus(0);
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
        // 筛选出与A开头 应用
        List<Long> appIdList = new ArrayList<>();
        appIdList.addAll(getAppIds(appIds));
        appIdList.addAll(getAppIds(configIds));
        appIdList.addAll(getAppIds(devopsIds));
        if (CollUtil.isNotEmpty(appIdList)) {
            for (Long appId : appIdList) {
                roleInfoMapper.insertRoleApp(roleId, appId);
            }
        }

        // 筛选出与M开头 菜单
        List<Long> menuIdList = new ArrayList<>();
        menuIdList.addAll(getMenuIds(appIds));
        menuIdList.addAll(getMenuIds(configIds));
        menuIdList.addAll(getMenuIds(devopsIds));
        if (CollUtil.isNotEmpty(menuIdList)) {
            for (Long menuId : menuIdList) {
                roleInfoMapper.insertRoleMenu(roleId, menuId);
            }
        }
        // 筛选出与U开头 接口
        List<Long> apiIdList = new ArrayList<>();
        apiIdList.addAll(getApiIds(appIds));
        apiIdList.addAll(getApiIds(configIds));
        apiIdList.addAll(getApiIds(devopsIds));
        if (CollUtil.isNotEmpty(apiIdList)) {
            for (Long apiId : apiIdList) {
                roleInfoMapper.insertRoleApi(roleId, apiId);
            }
        }

        List<Long> orgIds = dto.getOrgIds();
        if (CollUtil.isNotEmpty(orgIds)) {
            for (Long orgId : orgIds) {
                roleInfoMapper.insertRoleOrg(roleId, orgId);
            }
        }

        List<Long> areaIds = dto.getAreaIds();
        if (CollUtil.isNotEmpty(areaIds)) {
            for (Long areaId : areaIds) {
                roleInfoMapper.insertRoleArea(roleId, areaId);
            }
        }


    }

    @Override
    public void modifyById(UpdateSysRoleInfoDTO dto) {
        // 1 查取原始角色基础信息
        RoleInfo roleInfo = roleInfoMapper.selectById(dto.getId());
        roleInfo.setRoleName(dto.getRoleName());
        roleInfo.setRoleDesc(dto.getRoleDesc());
        roleInfoMapper.updateById(roleInfo);

        /**
         * 应用
         */
        // 获取原始已授权应用ID
        List<Long> oldAppIdList = roleInfoMapper.findAppIdList(dto.getId());
        // 筛选出与A开头的id 应用
        List<Long> appIdList = new ArrayList<>();
        appIdList.addAll(getAppIds(dto.getAppIds()));
        appIdList.addAll(getAppIds(dto.getConfigIds()));
        appIdList.addAll(getAppIds(dto.getDevopsIds()));
        if (CollUtil.isEmpty(oldAppIdList) && CollUtil.isNotEmpty(appIdList)) {
            // 原始授权应用为空，本次为新增
            for (Long appId : appIdList) {
                roleInfoMapper.insertRoleApp(dto.getId(), appId);
            }
        }
        if (CollUtil.isEmpty(appIdList)) {
            // 如果提交的应用为空，则删除所有的角色关联应用
            roleInfoMapper.removeRoleApp(dto.getId(), null);
        }
        // 提交的应用与原始应用均不为空，采取Lambda表达式取得相同的应用
        List<Long> commonApp = oldAppIdList.stream().filter(appIdList::contains).collect(Collectors.toList());
        // 原始应用列表剔除相同部分后进行删除
        oldAppIdList.retainAll(commonApp);
        for (Long appId : oldAppIdList) {
            roleInfoMapper.removeRoleApp(dto.getId(), appId);
        }
        // 新提交的应用列表剔除相同部分后新增授权
        appIdList.removeAll(commonApp);
        for (Long appId : appIdList) {
            roleInfoMapper.insertRoleApp(dto.getId(), appId);
        }

        /**
         * 菜单
         */
        // 获取原始已授权 菜单ID
        List<Long> oldMenuIdList = roleInfoMapper.findMenuIdList(dto.getId());
        // 筛选出与M开头的id 菜单
        List<Long> menuIdList = new ArrayList<>();
        menuIdList.addAll(getMenuIds(dto.getAppIds()));
        menuIdList.addAll(getMenuIds(dto.getConfigIds()));
        menuIdList.addAll(getMenuIds(dto.getDevopsIds()));
        if (CollUtil.isEmpty(oldMenuIdList) && CollUtil.isNotEmpty(menuIdList)) {
            // 原始授权菜单为空，本次为新增
            for (Long menuId : menuIdList) {
                roleInfoMapper.insertRoleMenu(dto.getId(), menuId);
            }
        }
        if (CollUtil.isEmpty(menuIdList)) {
            // 如果提交的菜单为空，则删除所有的角色关联菜单
            roleInfoMapper.removeRoleMenu(dto.getId(), null);
        }
        // 提交的应用与原始应用均不为空，采取Lambda表达式取得相同的应用
        List<Long> commonMenu = oldMenuIdList.stream().filter(menuIdList::contains).collect(Collectors.toList());
        // 原始菜单列表剔除相同部分后进行删除
        oldMenuIdList.retainAll(commonMenu);
        for (Long menuId : oldMenuIdList) {
            roleInfoMapper.removeRoleMenu(dto.getId(), menuId);
        }
        // 新提交的菜单列表剔除相同部分后新增授权
        menuIdList.removeAll(commonMenu);
        for (Long menuId : menuIdList) {
            roleInfoMapper.insertRoleMenu(dto.getId(), menuId);
        }

        /**
         * 接口
         */
        // 获取原始已授权 接口ID
        List<Long> oldApiIdList = roleInfoMapper.findApiIdList(dto.getId());
        // 筛选出与U开头的id 接口
        List<Long> apiIdList = new ArrayList<>();
        apiIdList.addAll(getApiIds(dto.getAppIds()));
        apiIdList.addAll(getApiIds(dto.getConfigIds()));
        apiIdList.addAll(getApiIds(dto.getDevopsIds()));
        if (CollUtil.isEmpty(oldApiIdList) && CollUtil.isNotEmpty(apiIdList)) {
            // 原始授权接口为空，本次为新增
            for (Long apiId : apiIdList) {
                roleInfoMapper.insertRoleApi(dto.getId(), apiId);
            }
        }
        if (CollUtil.isEmpty(apiIdList)) {
            // 如果提交的接口为空，则删除所有的角色关联接口
            roleInfoMapper.removeRoleApi(dto.getId(), null);
        }
        // 提交的接口与原始接口均不为空，采取Lambda表达式取得相同的应用
        List<Long> commonApi = oldApiIdList.stream().filter(apiIdList::contains).collect(Collectors.toList());
        // 原始接口列表剔除相同部分后进行删除
        oldApiIdList.retainAll(commonApi);
        for (Long apiId : oldApiIdList) {
            roleInfoMapper.removeRoleApi(dto.getId(), apiId);
        }
        // 新提交的接口列表剔除相同部分后新增授权
        apiIdList.removeAll(commonApi);
        for (Long apiId : apiIdList) {
            roleInfoMapper.insertRoleApi(dto.getId(), apiId);
        }

        /**
         * 组织
         */
        // 获取原始已授权组织ID
        List<Long> oldOrgIdList = roleInfoMapper.findOrgIdList(dto.getId());
        List<Long> orgIdList = dto.getOrgIds();
        if (CollUtil.isEmpty(oldOrgIdList) && CollUtil.isNotEmpty(orgIdList)) {
            // 原始授权组织为空，本次为新增
            for (Long orgId : orgIdList) {
                roleInfoMapper.insertRoleOrg(dto.getId(), orgId);
            }
        }
        if (CollUtil.isEmpty(orgIdList)) {
            // 如果提交的组织为空，则删除所有的角色关联应用
            roleInfoMapper.removeRoleOrg(dto.getId(), null);
        }
        // 提交的应用与原始应用均不为空，采取Lambda表达式取得相同的应用
        List<Long> commonOrg = oldOrgIdList.stream().filter(orgIdList::contains).collect(Collectors.toList());
        // 原始组织列表剔除相同部分后进行删除
        oldOrgIdList.retainAll(commonOrg);
        for (Long apiId : oldOrgIdList) {
            roleInfoMapper.removeRoleOrg(dto.getId(), apiId);
        }
        // 新提交的组织列表剔除相同部分后新增授权
        orgIdList.removeAll(commonOrg);
        for (Long orgId : orgIdList) {
            roleInfoMapper.insertRoleOrg(dto.getId(), orgId);
        }

        /**
         * 区域
         */
        // 获取原始已授权区域的ID
        List<Long> oldAreaIdList = roleInfoMapper.findAreaIdList(dto.getId());
        List<Long> areaIdList = dto.getAreaIds();
        if (CollUtil.isEmpty(oldAreaIdList) && CollUtil.isNotEmpty(areaIdList)) {
            // 原始授权区域为空且本次参数不为空，本次为新增
            for (Long areaId : areaIdList) {
                roleInfoMapper.insertRoleArea(dto.getId(), areaId);
            }
        }
        if (CollUtil.isEmpty(areaIdList)) {
            // 如果提交的区域为空，则删除所有的角色关联应用
            roleInfoMapper.removeRoleArea(dto.getId(), null);
        }
        // 提交的应用与原始应用均不为空，采取Lambda表达式取得相同的应用
        List<Long> commonArea = oldAreaIdList.stream().filter(areaIdList::contains).collect(Collectors.toList());
        // 原始区域列表剔除相同部分后进行删除
        oldAreaIdList.retainAll(commonArea);
        for (Long areaId : oldAreaIdList) {
            roleInfoMapper.removeRoleArea(dto.getId(), areaId);
        }
        // 新提交的区域列表剔除相同部分后新增授权
        areaIdList.removeAll(commonArea);
        for (Long areaId : areaIdList) {
            roleInfoMapper.insertRoleArea(dto.getId(), areaId);
        }
    }

    @Override
    public Page<SysRoleInfoVO> findByPage(QuerySysRoleInfoDTO dto) {
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
    public void erasureBatch(List<Long> ids) {
        roleInfoMapper.deleteBatchIds(ids);
    }

    @Override
    public void modifyByStatus(StatusSysRoleInfoDTO dto) {
        RoleInfo roleInfo = roleInfoMapper.selectById(dto.getId());
        roleInfo.setStatus(dto.getStatus());
        roleInfoMapper.updateById(roleInfo);
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
    public RoleDetailVO getRoleDetailById(Long id) {
        // 返回实体
        RoleDetailVO roleDetailVO = new RoleDetailVO();

        // 查询角色基本信息
        RoleInfo roleInfo = roleInfoMapper.selectById(id);
        roleDetailVO.setId(roleInfo.getId());
        roleDetailVO.setRoleName(roleInfo.getRoleName());
        roleDetailVO.setRoleDesc(roleInfo.getRoleDesc());

        // 查询该角色已授权的应用列表
        List<AppInfo> appInfoList = roleInfoMapper.selectAppByRoleCode(roleInfo.getRoleCode());
        // 查询该角色已授权的菜单列表
        List<MenuInfo> menuInfoList = roleInfoMapper.selectMenuByRoleCode(roleInfo.getRoleCode());
        // 查询该角色已授权的接口列表
        List<ApiInfo> apiInfoList = roleInfoMapper.selectApiInfoByRoleCode(roleInfo.getRoleCode());

        // 应用类
        List<String> appIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, 1);
        appIds = appIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setAppIds(appIds);
        // 配置类
        List<String> configIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, 2);
        configIds = configIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setConfigIds(configIds);
        // 运维类
        List<String> devopsIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, 3);
        devopsIds = devopsIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setDevopsIds(devopsIds);

        // 查询该角色已授权的部门列表
        List<OrgInfo> orgList = roleInfoMapper.selectOrgInfoByRoleCode(roleInfo.getRoleCode());
        List<String> orgIds = orgList.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
        orgIds = orgIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setOrgIds(orgIds);
        // 查询该角色已授权的安防区域
        List<VideoArea> areaList = roleInfoMapper.selectVideoAreaByRoleCode(roleInfo.getRoleCode());
        List<String> areaIds = areaList.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
        areaIds = areaIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setAreaIds(areaIds);
        return roleDetailVO;
    }


    @Override
    public List<AppMenuApiTree> getAppMenuApiTree(Integer appType) {
        List<AppMenuApi> appMenuApiList = appMenuApiMapper.selectByAppType(appType);

        List<AppMenuApiVO> vos = new ArrayList<>();
        // 虚拟根
        AppMenuApiVO root = new AppMenuApiVO();
        root.setId(1L);
        root.setIdStr("");
        root.setPid(0L);
        root.setName("虚拟根");
        vos.add(root);
        for (AppMenuApi appMenuApi : appMenuApiList) {
            // appId !=null   menuId == null apiId ==null
            if (null != appMenuApi.getAppId()) {
                AppMenuApiVO vo_app = new AppMenuApiVO();
                vo_app.setId(appMenuApi.getAppId());
                vo_app.setIdStr("A_" + appMenuApi.getAppId());
                vo_app.setName(appMenuApi.getAppName());
                vo_app.setPid(1L);
                vos.add(vo_app);
            }

            // appId !=null   menuId != null  apiId ==null
            if (null != appMenuApi.getAppId() && null != appMenuApi.getMenuId()) {
                AppMenuApiVO vo_menu = new AppMenuApiVO();
                vo_menu.setId(appMenuApi.getMenuId());
                vo_menu.setIdStr("M_" + appMenuApi.getMenuId());
                vo_menu.setName(appMenuApi.getTitle());
                vo_menu.setPid(appMenuApi.getAppId());
                vos.add(vo_menu);
            }

            // appId !=null menuId ！= null apiId ！=null
            if (null != appMenuApi.getAppId() && null != appMenuApi.getMenuId() && null != appMenuApi.getApiId()) {
                AppMenuApiVO vo_api = new AppMenuApiVO();
                vo_api.setId(appMenuApi.getApiId());
                vo_api.setIdStr("U_" + appMenuApi.getApiId());
                vo_api.setName(appMenuApi.getApiName());
                vo_api.setPid(appMenuApi.getMenuId());
                vos.add(vo_api);
            }
            // appId !=null menuId == null apiId ==null
            if (null != appMenuApi.getAppId() && null == appMenuApi.getMenuId() && null != appMenuApi.getApiId()) {
                AppMenuApiVO vo_api = new AppMenuApiVO();
                vo_api.setId(appMenuApi.getApiId());
                vo_api.setIdStr("U_" + appMenuApi.getApiId());
                vo_api.setName(appMenuApi.getApiName());
                vo_api.setPid(appMenuApi.getAppId());
                vos.add(vo_api);
            }

        }
        List<AppMenuApiVO> treeVos = vos.stream().distinct().collect(Collectors.toList());
        List<AppMenuApiTree> appMenuApiTreeList = treeVos.stream().map(
                item -> {
                    AppMenuApiTree vo = new AppMenuApiTree();
                    vo.setId(item.getId());
                    vo.setIdStr(item.getIdStr());
                    vo.setPid(item.getPid());
                    vo.setName(item.getName());
                    return vo;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(appMenuApiTreeList, 1L);
    }


    @Override
    public void addRelationUser(RoleRelationUserDTO dto) {
        // 1.根据角色ID查取以往关联的用户列表
        List<Long> oldUserIds = roleInfoMapper.findUserIdList(dto.getRoleId());
        // 如果旧关联为空，则本次为新关联
        if (CollUtil.isEmpty(oldUserIds)) {
            List<BatchDTO> batchUserIdList = new ArrayList<>();
            for (Long userId : dto.getUserIdList()) {
                // roleInfoMapper.insertRoleUser(dto.getRoleId(), userId);
                BatchDTO batchDTO = new BatchDTO();
                batchDTO.setRoleId(dto.getRoleId());
                batchDTO.setObjId(userId);
                batchUserIdList.add(batchDTO);
            }
            roleInfoMapper.batchInsertRoleUser(batchUserIdList);
            return;
        }
        // 如果新关联为空，则是取消关联
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            //
            roleInfoMapper.removeRoleUser(dto.getRoleId(), null);
            return;
        }
        // 2.求取新旧的相同点
        List<Long> commonUserId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 原始应用列表剔除相同部分后进行删除
        oldUserIds.retainAll(commonUserId);
        for (Long userId : oldUserIds) {
            roleInfoMapper.removeRoleUser(dto.getRoleId(), userId);
        }
        // 新提交的应用列表剔除相同部分后新增授权
        List<BatchDTO> batchUserIdList = new ArrayList<>();
        dto.getUserIdList().removeAll(commonUserId);
        for (Long userId : dto.getUserIdList()) {
            // roleInfoMapper.insertRoleUser(dto.getRoleId(), userId);
            BatchDTO batchDTO = new BatchDTO();
            batchDTO.setRoleId(dto.getRoleId());
            batchDTO.setObjId(userId);
            batchUserIdList.add(batchDTO);
        }
        roleInfoMapper.batchInsertRoleUser(batchUserIdList);

    }

    @Override
    public Page<RelationSysUserInfoVO> listRelationUser(QueryRoleRelationSysUserInfoDTO dto) {
        PageRoleRelationSysUserInfoDTO page = new PageRoleRelationSysUserInfoDTO();

        if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (null != dto.getRoleId()) {
            page.setRoleId(dto.getRoleId());
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
        return roleInfoMapper.relationSysUserInfoPage(page);

    }

    @Override
    public void rightRelationUser(RoleRelationUserDTO dto) {
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            return;
        }
        List<Long> oldUserIds = roleInfoMapper.findUserIdList(dto.getRoleId());
        if (CollUtil.isEmpty(oldUserIds)) {
            return;
        }
        List<Long> commonId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 将相同部分后进行删除
        for (Long userId : commonId) {
            roleInfoMapper.removeRoleUser(dto.getRoleId(), userId);
        }
    }

    @Override
    public void leftRelationUser(RoleRelationUserDTO dto) {
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            return;
        }
        List<Long> oldUserIds = roleInfoMapper.findUserIdList(dto.getRoleId());
        if (CollUtil.isEmpty(oldUserIds)) {
            for (Long userId : dto.getUserIdList()) {
                roleInfoMapper.insertRoleUser(dto.getRoleId(), userId);
            }
            return;
        }
        // 求取新旧的相同点
        List<Long> commonId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 新提交的应用列表剔除相同部分后新增授权
        dto.getUserIdList().removeAll(commonId);
        for (Long userId : dto.getUserIdList()) {
            roleInfoMapper.insertRoleUser(dto.getRoleId(), userId);
        }
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
                if (str.startsWith("A_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "A_"));
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
                if (str.startsWith("M_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "M_"));
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
                if (str.startsWith("U_")) {
                    Long menuId = Long.valueOf(StrUtil.removePrefix(str, "U_"));
                    apiIds.add(menuId);
                }
            }
        }
        return apiIds;
    }

    private List<String> getAppMenuApi(List<AppInfo> appInfoList,
                                       List<MenuInfo> menuInfoList,
                                       List<ApiInfo> apiInfoList,
                                       Integer appType
    ) {
        // 4.拼接返回结果
        List<String> resultList = new ArrayList<>();
        // 1.先根据appType选出目标分类的应用
        for (AppInfo appInfo : appInfoList) {
            if (appType.equals(appInfo.getAppType())) {
                resultList.add("A_" + appInfo.getId());
            }
        }
        // 2.根据目标分类的结果过滤掉不属于这个应用分类的菜单
        for (MenuInfo menuInfo : menuInfoList) {
            if (menuInfo.getId().equals(1L)) {
                continue;
            }
            for (AppInfo appInfo : appInfoList) {
                if (menuInfo.getAppId().equals(appInfo.getId())) {
                    resultList.add("M_" + appInfo.getId());
                }
            }
        }
        // 3.根据目标分类的结果过滤掉不属于这个应用分类的接口
        for (ApiInfo apiInfo : apiInfoList) {
            if (apiInfo.getId().equals(1L)) {
                continue;
            }
            for (AppInfo appInfo : appInfoList) {
                if (apiInfo.getAppId().equals(appInfo.getId())) {
                    resultList.add("U_" + appInfo.getId());
                }
            }
        }

        return resultList;
    }
}
