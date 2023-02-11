package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.page.PageSysDictDTO;
import com.runjian.auth.server.domain.dto.system.AddSysDictDTO;
import com.runjian.auth.server.domain.dto.system.QureySysDictDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysDictDTO;
import com.runjian.auth.server.domain.entity.DictInfo;
import com.runjian.auth.server.domain.vo.system.SysDictVO;
import com.runjian.auth.server.mapper.DictInfoMapper;
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
public class SysDictServiceImpl extends ServiceImpl<DictInfoMapper, DictInfo> implements SysDictService {

    @Autowired
    private DictInfoMapper dictInfoMapper;

    @Override
    public void saveSysDict(AddSysDictDTO dto) {
        DictInfo dictInfo = new DictInfo();
        BeanUtils.copyProperties(dto, dictInfo);
        dictInfoMapper.insert(dictInfo);
    }

    @Override
    public void updateSysDictById(UpdateSysDictDTO dto) {
        DictInfo dictInfo = dictInfoMapper.selectById(dto.getId());
        BeanUtils.copyProperties(dto, dictInfo);
        dictInfoMapper.updateById(dictInfo);
    }

    @Override
    public SysDictVO getSysDictById(Long id) {
        DictInfo dictInfo = dictInfoMapper.selectById(id);
        SysDictVO sysDictVO = new SysDictVO();
        BeanUtils.copyProperties(dictInfo, sysDictVO);
        return sysDictVO;
    }

    @Override
    public List<SysDictVO> getSysDictList() {
        List<DictInfo> dictInfoList = dictInfoMapper.selectList(null);
        List<SysDictVO> sysDictVOList = new ArrayList<>();
        for (DictInfo dictInfo : dictInfoList) {
            SysDictVO sysDictVO = new SysDictVO();
            BeanUtils.copyProperties(dictInfo, sysDictVO);
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
        return dictInfoMapper.MySelectPage(page);
    }

    @Override
    public void removeSysDictById(Long id) {
        dictInfoMapper.deleteById(id);
    }

    @Override
    public List<SysDictVO> getByGroupCode(String groupCode) {
        LambdaQueryWrapper<DictInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictInfo::getGroupCode, groupCode);
        List<DictInfo> dictInfoList = dictInfoMapper.selectList(queryWrapper);
        return dictInfoList.stream().map(
                item -> {
                    SysDictVO sysDictVO = new SysDictVO();
                    BeanUtils.copyProperties(item, sysDictVO);
                    return sysDictVO;
                }
        ).collect(Collectors.toList());
    }
}
