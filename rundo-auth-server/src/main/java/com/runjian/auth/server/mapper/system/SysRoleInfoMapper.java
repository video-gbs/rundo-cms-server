package com.runjian.auth.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysRoleInfo;
import com.runjian.auth.server.domain.vo.system.EditUserSysRoleInfoVO;
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
public interface SysRoleInfoMapper extends BaseMapper<SysRoleInfo> {

    /**
     * 根据用户ID查询用户角色
     *
     * @param userId
     * @return
     */
    List<String> selectRoleByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询接口权限
     *
     * @param roleCode
     * @return
     */
    List<String> findApiByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色编码查询菜单权限
     *
     * @param roleCode
     * @return
     */
    List<String> findMenuByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色编码查询应用权限
     *
     * @param roleCode
     * @return
     */
    List<String> findAppByRoleCode(@Param("roleCode") String roleCode);


    Page<EditUserSysRoleInfoVO> selectEditUserSysRoleInfoPage(PageEditUserSysRoleInfoDTO page);
}
