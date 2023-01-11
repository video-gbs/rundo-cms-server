package com.runjian.auth.server.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.auth.server.entity.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    /**
     * 根据userID查询用户角色
     *
     * @param userId
     * @return
     */
    List<String> selectRoleByUserId(@Param("userId") Long userId);
}