package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.AddSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysRoleInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysRoleInfo;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        role.setRoleCode(roleId.toString());
        role.setRoleDesc(dto.getRoleDesc());
        // role.setParentRoleId();
        // role.setParentRoleIds();
        // role.setTenantId();
        // role.setDeleteFlag();
        // role.setCreatedBy();
        // role.setUpdatedBy();
        // role.setCreatedTime();
        // role.setUpdatedTime();
        roleInfoMapper.insert(role);
        // 处理应用ID
        // List<Long> appIds = dto.getAppIds();
        // if (Objects.nonNull(appIds)){
        //     for (Long id : appIds) {
        //         roleInfoMapper.saveRoleApp(roleId, id);
        //     }
        // }
        // // 处理 菜单ID
        // List<Long> menuIds = dto.getMenuIds();
        // if (Objects.nonNull(appIds)){
        //     for (Long id : appIds) {
        //         roleInfoMapper.saveRoleMenu(roleId, id);
        //     }
        // }
        // // 处理 部门ID
        // List<Long> orgIds = dto.getOrgIds();
        // if (Objects.nonNull(orgIds)){
        //     for (Long id : orgIds) {
        //         roleInfoMapper.saveRoleOrg(roleId, id);
        //     }
        // }
        // //处理 安防区域ID
        // List<Long> areaIds = dto.getAreaIds();
        // if (Objects.nonNull(areaIds)){
        //     for (Long id : areaIds) {
        //         roleInfoMapper.saveRoleArea(roleId, id);
        //     }
        // }
        // // 处理视频通道资源
        // List<Long> channelIds = dto.getAreaIds();
        // if (Objects.nonNull(channelIds)){
        //     for (Long id : channelIds) {
        //         roleInfoMapper.saveRoleChannel(roleId, id);
        //     }
        // }
        // // 处理视频通道操作
        // List<Long> operationIds = dto.getAreaIds();
        // if (Objects.nonNull(operationIds)){
        //     for (Long id : operationIds) {
        //         roleInfoMapper.saveRoleChannelOperation(roleId, id);
        //     }
        // }

    }

    @Override
    public void updateRole(UpdateSysRoleInfoDTO dto) {

    }

    @Override
    public Page<SysRoleInfoVO> getSysRoleInfoByPage(QuerySysRoleInfoDTO dto) {
        return null;
    }

    @Override
    public void batchRemove(List<Long> ids) {

    }
}
