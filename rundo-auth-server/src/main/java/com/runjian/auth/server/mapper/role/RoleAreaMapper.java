package com.runjian.auth.server.mapper.role;

import com.runjian.auth.server.entity.RoleArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色安防区域关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface RoleAreaMapper extends BaseMapper<RoleArea> {
    /**
     * 根据用户角色查询用户安全区划权限
     *
     * @return
     */
    List<String> selectAreaAuthByroleCodes(@Param("roleCodes") List<String> roleCodes);
}
