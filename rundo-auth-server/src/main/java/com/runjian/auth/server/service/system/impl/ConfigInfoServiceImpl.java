package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.entity.ConfigInfo;
import com.runjian.auth.server.mapper.ConfigInfoMapper;
import com.runjian.auth.server.domain.dto.system.AddSysConfigDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysConfigDTO;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;
import com.runjian.auth.server.service.system.ConfigInfoService;
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
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements ConfigInfoService {

    @Autowired
    private ConfigInfoMapper configInfoMapper;

    @Override
    public void save(AddSysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        configInfoMapper.insert(configInfo);
    }

    @Override
    public void modifyById(UpdateSysConfigDTO dto) {
        ConfigInfo configInfo = configInfoMapper.selectById(dto.getId());


    }

    @Override
    public SysConfigVO findById(Long id) {
        return null;
    }

    @Override
    public List<SysConfigVO> findByList() {
        return null;
    }
}
