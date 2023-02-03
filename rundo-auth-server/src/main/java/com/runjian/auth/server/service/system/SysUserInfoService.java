package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.system.SysUserInfo;
import com.runjian.auth.server.domain.vo.system.EditSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;

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

    void saveSysUserInfo(AddSysUserInfoDTO dto);

    void updateSysUserInfo(UpdateSysUserInfoDTO dto);

    void changeStatus(StatusSysUserInfoDTO dto);

    Page<ListSysUserInfoVO> getSysUserInfoByPage(QuerySysUserInfoDTO dto);

    void removeSysUserInfoById(Long id);

    void batchRemoveSysUserInfo(List<Long> ids);

    EditSysUserInfoVO getSysUserInfoById(Long id);

    RelationSysUserInfoVO getRelationSysUserInfoById(Long id);

    Page<RelationSysUserInfoVO> getRelationSysUserInfoList(RelationSysUserInfoDTO dto);
}
