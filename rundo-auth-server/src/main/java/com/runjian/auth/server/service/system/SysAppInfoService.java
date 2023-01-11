package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysAppInfoDTO;
import com.runjian.auth.server.entity.SysAppInfo;

/**
 * <p>
 * 应用信息 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysAppInfoService extends IService<SysAppInfo> {

    /**
     * 添加应用
     *
     * @param dto
     * @return
     */
    ResponseResult addSysAppInfo(SysAppInfoDTO dto);

    ResponseResult updateSysAppInfo(SysAppInfoDTO dto);
}
