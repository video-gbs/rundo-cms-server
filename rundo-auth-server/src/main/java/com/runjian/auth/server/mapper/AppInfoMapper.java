package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.page.PageSysAppInfoDTO;
import com.runjian.auth.server.domain.entity.AppInfo;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 应用信息 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfo> {

    Page<SysAppInfoVO> MySelectPage(PageSysAppInfoDTO dto);

}