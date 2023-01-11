package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.entity.system.SysConfig;

/**
 * <p>
 * 系统全局参数配置 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysConfigService extends IService<SysConfig> {

    ResponseResult addSysConfig(SysConfig dto);

    ResponseResult updateSysConfig(SysConfig dto);
}
