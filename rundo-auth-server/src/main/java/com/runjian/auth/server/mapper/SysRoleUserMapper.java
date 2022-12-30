package com.runjian.auth.server.mapper;

import com.runjian.auth.server.entity.SysRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:39:49
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

}
