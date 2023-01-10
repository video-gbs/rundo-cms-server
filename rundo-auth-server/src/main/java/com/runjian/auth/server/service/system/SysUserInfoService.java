package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.UserInfoVo;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.common.config.response.CommonResponse;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysUserInfoService extends IService<SysUserInfo> {

    /**
     * 新建用户
     *
     * @param dto
     * @return
     */
    CommonResponse addUser(SysUserInfoDTO dto);


    /**
     * 编辑用户
     *
     * @return
     */
    CommonResponse updateUser();

    /**
     * 删除用户
     *
     * @return
     */
    CommonResponse deleteUser();

    /**
     * 批量删除用户
     *
     * @return
     */
    CommonResponse batchDeleteUsers();

    /**
     * 查询用户信息
     *
     * @return
     */
    CommonResponse<UserInfoVo> getUser(Long userId);

    /**
     * 用户列表
     *
     * @return
     */
    CommonResponse getUserList();

    CommonResponse getUserListByPage();
}
