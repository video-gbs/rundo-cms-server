package com.runjian.auth.server.mapper.role;

import com.runjian.auth.server.entity.SysRoleApi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色接口关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysRoleApiMapper extends BaseMapper<SysRoleApi> {

    List<Long> selectApiByRoleId(@Param("roleId")Long roleId);
}
