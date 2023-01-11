package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysDictDTO;
import com.runjian.auth.server.entity.system.SysDict;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysDictService extends IService<SysDict> {

    ResponseResult addSysDict(SysDictDTO dto);
}
