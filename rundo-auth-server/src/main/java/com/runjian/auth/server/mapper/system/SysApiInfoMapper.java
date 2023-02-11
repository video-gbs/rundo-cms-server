package com.runjian.auth.server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.entity.SysApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 接口信息表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface SysApiInfoMapper extends BaseMapper<SysApiInfo> {

    void insertAppApi(@Param("appId") Long appId, @Param("apiId") Long apiId);

    Page<SysApiInfoVO> MySelectPage(Page<SysApiInfoVO> page);

}
