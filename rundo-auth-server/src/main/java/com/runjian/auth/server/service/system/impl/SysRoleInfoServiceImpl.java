package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.model.dto.system.SysRoleInfoDTO;
import com.runjian.auth.server.entity.system.SysRoleInfo;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public ResponseResult addRole(SysRoleInfoDTO dto) {
        SysRoleInfo role = new SysRoleInfo();
        Long roleId = idUtil.nextId();
        role.setId(roleId);
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(roleId.toString());
        role.setRoleSort("1");
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
        List<Long> appIds = dto.getAppIds();
        if (Objects.nonNull(appIds)){
            for (Long id : appIds) {
                roleInfoMapper.saveRoleApp(roleId, id);
            }
        }
        // 处理 菜单ID
        List<Long> menuIds = dto.getMenuIds();
        if (Objects.nonNull(appIds)){
            for (Long id : appIds) {
                roleInfoMapper.saveRoleMenu(roleId, id);
            }
        }
        // 处理 部门ID
        List<Long> orgIds = dto.getOrgIds();
        if (Objects.nonNull(orgIds)){
            for (Long id : orgIds) {
                roleInfoMapper.saveRoleOrg(roleId, id);
            }
        }
        //处理 安防区域ID
        List<Long> areaIds = dto.getAreaIds();
        if (Objects.nonNull(areaIds)){
            for (Long id : areaIds) {
                roleInfoMapper.saveRoleArea(roleId, id);
            }
        }
        // 处理视频通道资源
        List<Long> channelIds = dto.getAreaIds();
        if (Objects.nonNull(channelIds)){
            for (Long id : channelIds) {
                roleInfoMapper.saveRoleChannel(roleId, id);
            }
        }
        // 处理视频通道操作
        List<Long> operationIds = dto.getAreaIds();
        if (Objects.nonNull(operationIds)){
            for (Long id : operationIds) {
                roleInfoMapper.saveRoleChannelOperation(roleId, id);
            }
        }


        return new ResponseResult(200, "操作成功");
    }

    @Override
    public ResponseResult updateRole(SysRoleInfoDTO dto) {
        return null;
    }
}
