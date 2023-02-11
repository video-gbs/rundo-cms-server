package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.ConfigInfo;
import com.runjian.auth.server.mapper.system.SysConfigMapper;
import com.runjian.auth.server.domain.dto.system.AddSysConfigDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysConfigDTO;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;
import com.runjian.auth.server.service.system.SysConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统全局参数配置 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, ConfigInfo> implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public void saveSysConfig(AddSysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        sysConfigMapper.insert(configInfo);
    }

    @Override
    public void updateSysConfigById(UpdateSysConfigDTO dto) {
        ConfigInfo configInfo = sysConfigMapper.selectById(dto.getId());


    }

    @Override
    public SysConfigVO getSysConfigById(Long id) {
        return null;
    }

    @Override
    public List<SysConfigVO> getSysConfigList() {
        return null;
    }
}
