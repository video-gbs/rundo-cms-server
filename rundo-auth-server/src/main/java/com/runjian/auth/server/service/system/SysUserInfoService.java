package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysUserInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysUserInfo;
import com.runjian.auth.server.domain.vo.system.EditSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysUserInfoService extends IService<SysUserInfo> {

    void saveSysUserInfo(AddSysUserInfoDTO dto);

    void updateSysUserInfo(UpdateSysUserInfoDTO dto);

    ResponseResult<ListSysUserInfoVO> getUser(Long id);

    void changeStatus(StatusSysUserInfoDTO dto);

    Page<ListSysUserInfoVO> getSysUserInfoByPage(QuerySysUserInfoDTO dto);

    void removeSysUserInfoById(Long id);

    EditSysUserInfoVO getSysUserInfoById(Long id);
}
