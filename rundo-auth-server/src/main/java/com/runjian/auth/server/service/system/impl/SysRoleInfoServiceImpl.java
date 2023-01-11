package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysRoleInfoDTO;
import com.runjian.auth.server.entity.system.SysRoleInfo;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.auth.server.util.SnowflakeUtil;
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
    private SnowflakeUtil idUtil;
    @Autowired
    private SysRoleInfoMapper roleInfoMapper;

    @Override
    public ResponseResult addRole(SysRoleInfoDTO dto) {
        SysRoleInfo sysRoleInfo = new SysRoleInfo();
        Long roleId = idUtil.nextId();
        sysRoleInfo.setId(roleId);
        sysRoleInfo.setRoleName(dto.getRoleName());
        // sysRoleInfo.setRoleCode();
        // sysRoleInfo.setRoleSort();
        sysRoleInfo.setRoleDesc(dto.getRoleDesc());
        // sysRoleInfo.setParentRoleId();
        // sysRoleInfo.setParentRoleIds();
        // sysRoleInfo.setTenantId();
        // sysRoleInfo.setDeleteFlag();
        // sysRoleInfo.setCreatedBy();
        // sysRoleInfo.setUpdatedBy();
        // sysRoleInfo.setCreatedTime();
        // sysRoleInfo.setUpdatedTime();
        roleInfoMapper.insert(sysRoleInfo);

        // 添加角色应用映射关系
        List<Long> appIds = dto.getAppIds();
        // 添加角色菜单映射关系
        List<Long> menuIds = dto.getMenuIds();
        // 添加角色接口映射关系
        List<Long> apiIds = dto.getApiIds();
        // 添加角色部门映射关系
        List<Long> orgIds = dto.getOrgIds();
        for (Long orgId : orgIds) {
            // TODO 待处理 添加角色部门映射关系
        }

        List<Long> areaIds = dto.getAreaIds();
        for (Long areaId : areaIds) {
            // TODO 待处理 添加角色部门映射关系
        }

        List<Long> channelIds = dto.getChannelIds();
        for (Long channelId : channelIds) {
            // TODO 待处理 添加角色通道映射关系
        }

        List<Long> operationIds = dto.getOperationIds();
        for (Long operationId : operationIds) {
            // TODO 待处理 添加角色通道映射关系
        }
        // 其他
        return new ResponseResult(200, "操作成功");
    }
}
