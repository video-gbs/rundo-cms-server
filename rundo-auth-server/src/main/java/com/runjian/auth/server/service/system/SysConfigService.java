package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.entity.system.SysConfig;
import com.runjian.auth.server.domain.dto.system.AddSysConfigDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysConfigDTO;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;

import java.util.List;

/**
 * <p>
 * 系统全局参数配置 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysConfigService extends IService<SysConfig> {
    void saveSysConfig(AddSysConfigDTO dto);

    void updateSysConfigById(UpdateSysConfigDTO dto);

    SysConfigVO getSysConfigById(Long id);

    List<SysConfigVO> getSysConfigList();
}
