package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.entity.system.SysAppInfo;
import com.runjian.auth.server.domain.dto.system.AddSysAppInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysAppInfoDTO;
import com.runjian.auth.server.domain.vo.system.SysAppInfoVO;

import java.util.List;

/**
 * <p>
 * 应用信息 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysAppInfoService extends IService<SysAppInfo> {

    void saveSysAppInfo(AddSysAppInfoDTO dto);

    void updateSysAppInfoById(UpdateSysAppInfoDTO dto);

    SysAppInfoVO getSysAppInfoById(Long id);

    List<SysAppInfoVO> getSysAppInfoList();
}
