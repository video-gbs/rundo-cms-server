package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.*;
import com.runjian.auth.server.domain.entity.UserInfo;
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
public interface UserInfoService extends IService<UserInfo> {

    void save(AddSysUserInfoDTO dto);

    void modifyById(UpdateSysUserInfoDTO dto);

    void modifyByStatus(StatusSysUserInfoDTO dto);

    Page<ListSysUserInfoVO> findByPage(QuerySysUserInfoDTO dto);

    void erasureById(Long id);

    void erasureBatch(List<Long> ids);

    EditSysUserInfoVO findById(Long id);

    RelationSysUserInfoVO findRelationById(Long id);

    Page<RelationSysUserInfoVO> findRelationList(QueryRelationSysUserInfoDTO dto);
}
