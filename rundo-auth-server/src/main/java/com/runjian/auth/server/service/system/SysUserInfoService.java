package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysUserInfoDTO;
import com.runjian.auth.server.domain.vo.UserInfoVo;
import com.runjian.auth.server.entity.system.SysUserInfo;

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
    ResponseResult addUser(SysUserInfoDTO dto);


    /**
     * 编辑用户
     *
     * @return
     */
    ResponseResult updateUser();

    /**
     * 删除用户
     *
     * @return
     */
    ResponseResult deleteUser();

    /**
     * 批量删除用户
     *
     * @return
     */
    ResponseResult batchDeleteUsers();

    /**
     * 查询用户信息
     *
     * @return
     */
    ResponseResult<UserInfoVo> getUser(Long userId);

    /**
     * 用户列表
     *
     * @return
     */
    ResponseResult getUserList();

    ResponseResult getUserListByPage();
}
