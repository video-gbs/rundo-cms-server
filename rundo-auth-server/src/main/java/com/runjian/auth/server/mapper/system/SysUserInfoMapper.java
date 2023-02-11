package com.runjian.auth.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.page.PageRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysUserInfoDTO;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.domain.vo.system.ListSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.OrgInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-29 20:01:25
 */
@Mapper
public interface SysUserInfoMapper extends BaseMapper<UserInfo> {

    void insertUserOrg(@Param("userId") Long userId, @Param("orgId") Long orgId);

    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    List<Long> selectRoleByUserId(@Param("userId") Long userId);

    void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Page<ListSysUserInfoVO> MySelectPage(PageSysUserInfoDTO page);

    OrgInfoVO selectOrgInfoByUserId(@Param("userId") Long userId);

    RelationSysUserInfoVO selectRelationSysUserInfoById(@Param("userId") Long userId);

    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRelationSysUserInfoDTO page);
}
