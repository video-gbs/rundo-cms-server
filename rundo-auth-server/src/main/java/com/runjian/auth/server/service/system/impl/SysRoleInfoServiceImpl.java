package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysRoleInfoDTO;
import com.runjian.auth.server.entity.system.SysRoleInfo;
import com.runjian.auth.server.mapper.system.SysRoleInfoMapper;
import com.runjian.auth.server.service.system.SysRoleInfoService;
import com.runjian.auth.server.util.RundoIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new ResponseResult(200, "操作成功");
    }
    @Override
    public ResponseResult updateRole(SysRoleInfoDTO dto) {
        return null;
    }
}
