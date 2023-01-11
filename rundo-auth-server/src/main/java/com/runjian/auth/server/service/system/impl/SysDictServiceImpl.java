package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysDictDTO;
import com.runjian.auth.server.entity.SysDict;
import com.runjian.auth.server.mapper.system.SysDictMapper;
import com.runjian.auth.server.service.system.SysDictService;
import org.springframework.stereotype.Service;

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
    public ResponseResult addSysDict(SysDictDTO dto) {
        return null;
    }
}
