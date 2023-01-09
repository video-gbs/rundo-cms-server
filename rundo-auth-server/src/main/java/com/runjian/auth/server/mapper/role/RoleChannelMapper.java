package com.runjian.auth.server.mapper.role;

import com.runjian.auth.server.entity.RoleChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色通道关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface RoleChannelMapper extends BaseMapper<RoleChannel> {

    /**
     * 根据用户角色查询用户通道权限
     *
     * @return
     */
    List<String> selectChannelAuthByroleCodes(@Param("roleCodes") List<String> roleCodes);

}
