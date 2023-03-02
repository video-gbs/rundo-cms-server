package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.QureySysDictDTO;
import com.runjian.auth.server.domain.entity.DictInfo;
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
public interface DictInfoService extends IService<DictInfo> {

    void save(AddSysDictDTO dto);

    void modifyById(UpdateSysDictDTO dto);

    SysDictVO findById(Long id);

    List<SysDictVO> findByList();

    Page<SysDictVO> findByPage(QureySysDictDTO dto);

    void erasureById(Long id);

    List<SysDictVO> findByGroupCode(String groupCode);
}
