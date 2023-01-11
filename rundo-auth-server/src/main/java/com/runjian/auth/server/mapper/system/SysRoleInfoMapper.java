package com.runjian.auth.server.mapper.system;

import com.runjian.auth.server.entity.system.SysRoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 根据userID查询用户角色
     *
     * @param userId
     * @return
     */
    List<Long> selectRoleByUserId(@Param("userId") Long userId);

    Integer saveUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Integer saveRoleApp(@Param("roleId") Long roleId, @Param("id") Long id);

    Integer saveRoleMenu(@Param("roleId") Long roleId, @Param("id") Long id);

    Integer saveRoleOrg(@Param("roleId") Long roleId, @Param("id") Long id);

    Integer saveRoleArea(@Param("roleId") Long roleId, @Param("id") Long id);

    Integer saveRoleChannel(@Param("roleId") Long roleId, @Param("id") Long id);

    Integer saveRoleChannelOperation(@Param("roleId") Long roleId, @Param("id") Long id);

}
