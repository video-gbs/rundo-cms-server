package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysUserInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysUserInfoVO;
import com.runjian.auth.server.domain.entity.system.SysUserInfo;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysUserInfoService extends IService<SysUserInfo> {

    ResponseResult addUser(AddSysUserInfoDTO dto);

    ResponseResult updateUser(AddSysUserInfoDTO dto);

    ResponseResult<SysUserInfoVO> getUser(Long id);

    ResponseResult<List<SysUserInfoVO>> getUserList();
}
