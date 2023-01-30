package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.system.SysDict;
import com.runjian.auth.server.mapper.system.SysDictMapper;
import com.runjian.auth.server.domain.dto.system.AddSysDictDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysDictDTO;
import com.runjian.auth.server.domain.vo.system.SysDictVO;
import com.runjian.auth.server.service.system.SysDictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public void saveSysDict(AddSysDictDTO dto) {

    }

    @Override
    public void updateSysDictById(UpdateSysDictDTO dto) {

    }

    @Override
    public SysDictVO getSysDictById(Long id) {
        return null;
    }

    @Override
    public List<SysDictVO> getSysDictList() {
        return null;
    }
}
