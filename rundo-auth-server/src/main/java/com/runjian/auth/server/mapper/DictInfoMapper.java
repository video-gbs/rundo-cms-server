package com.runjian.auth.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.auth.server.domain.dto.page.PageSysDictDTO;
import com.runjian.auth.server.domain.entity.DictInfo;
import com.runjian.auth.server.domain.vo.system.SysDictVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface DictInfoMapper extends BaseMapper<DictInfo> {

    /**
     * 数据字典分页查询
     *
     * @param page
     * @return
     */
    Page<SysDictVO> MySelectPage(PageSysDictDTO page);
}
