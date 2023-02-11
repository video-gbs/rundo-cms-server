package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.auth.server.domain.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {


    int saveAppMenu(@Param("appId") Long appId, @Param("menuId") Long menuId);

    List<MenuInfo> getMenuInfoByAppId(@Param("appId") Long appId);
}
