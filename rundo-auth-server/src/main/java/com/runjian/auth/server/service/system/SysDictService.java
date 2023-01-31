package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.QureySysDictDTO;
import com.runjian.auth.server.domain.entity.system.SysDict;
import com.runjian.auth.server.domain.dto.system.AddSysDictDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysDictDTO;
import com.runjian.auth.server.domain.vo.system.SysDictVO;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysDictService extends IService<SysDict> {

    void saveSysDict(AddSysDictDTO dto);

    void updateSysDictById(UpdateSysDictDTO dto);

    SysDictVO getSysDictById(Long id);

    List<SysDictVO> getSysDictList();

    Page<SysDictVO> getSysDictByPage(QureySysDictDTO dto);
}
