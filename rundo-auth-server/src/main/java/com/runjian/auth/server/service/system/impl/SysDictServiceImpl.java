package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageSysDictDTO;
import com.runjian.auth.server.domain.dto.system.AddSysDictDTO;
import com.runjian.auth.server.domain.dto.system.QureySysDictDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysDictDTO;
import com.runjian.auth.server.domain.entity.system.SysDict;
import com.runjian.auth.server.domain.vo.system.SysDictVO;
import com.runjian.auth.server.mapper.system.SysDictMapper;
import com.runjian.auth.server.service.system.SysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public void saveSysDict(AddSysDictDTO dto) {
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(dto, sysDict);
        sysDictMapper.insert(sysDict);
    }

    @Override
    public void updateSysDictById(UpdateSysDictDTO dto) {
        SysDict sysDict = sysDictMapper.selectById(dto.getId());
        BeanUtils.copyProperties(dto, sysDict);
        sysDictMapper.updateById(sysDict);
    }

    @Override
    public SysDictVO getSysDictById(Long id) {
        SysDict sysDict = sysDictMapper.selectById(id);
        SysDictVO sysDictVO = new SysDictVO();
        BeanUtils.copyProperties(sysDict, sysDictVO);
        return sysDictVO;
    }

    @Override
    public List<SysDictVO> getSysDictList() {
        List<SysDict> sysDictList = sysDictMapper.selectList(null);
        List<SysDictVO> sysDictVOList = new ArrayList<>();
        for (SysDict sysDict : sysDictList) {
            SysDictVO sysDictVO = new SysDictVO();
            BeanUtils.copyProperties(sysDict, sysDictVO);
            sysDictVOList.add(sysDictVO);
        }
        return sysDictVOList;
    }

    @Override
    public Page<SysDictVO> getSysDictByPage(QureySysDictDTO dto) {
        PageSysDictDTO page = new PageSysDictDTO();
        page.setItemName(dto.getItemName());
        page.setItemValue(dto.getItemValue());
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return sysDictMapper.MySelectPage(page);
    }

    @Override
    public void removeSysDictById(Long id) {
        sysDictMapper.deleteById(id);
    }

    @Override
    public List<SysDictVO> getByGroupCode(String groupCode) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDict::getGroupCode, groupCode);
        List<SysDict> sysDictList = sysDictMapper.selectList(queryWrapper);
        return sysDictList.stream().map(
                item -> {
                    SysDictVO sysDictVO = new SysDictVO();
                    BeanUtils.copyProperties(item, sysDictVO);
                    return sysDictVO;
                }
        ).collect(Collectors.toList());
    }
}
