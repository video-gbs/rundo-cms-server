package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.common.BatchDTO;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.auth.server.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.auth.server.domain.entity.RoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.auth.server.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.auth.server.domain.vo.system.SysRoleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /**
     * 根据用户ID查询,用户角色编码
     *
     * @param userId
     * @return
     */
    List<String> selectRoleCodeByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询,用户角色id
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleByUserId(@Param("userId") Long userId);

    Page<SysRoleInfoVO> MySelectPage(PageSysRoleInfoDto page);

    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);

    Page<RelationSysUserInfoVO> relationSysUserInfoPage(PageRoleRelationSysUserInfoDTO page);

    /**** 角色用户相关 ****/
    void insertRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

    void batchInsertRoleUser(@Param("userList") List<BatchDTO> userList);

    void removeRoleUser(@Param("roleId") Long roleId, @Param("userId") Long userId);

}
