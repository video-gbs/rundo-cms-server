package com.runjian.auth.server.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.SysConfigDTO;
import com.runjian.auth.server.domain.entity.ConfigInfo;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;
import com.runjian.auth.server.mapper.ConfigInfoMapper;
import com.runjian.auth.server.service.system.ConfigInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public void save(SysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        configInfoMapper.insert(configInfo);
    }

    @Override
    public void modifyById(SysConfigDTO dto) {
        ConfigInfo configInfo = new ConfigInfo();
        BeanUtils.copyProperties(dto, configInfo);
        configInfoMapper.updateById(configInfo);
    }

    @Override
    public SysConfigVO findById(Long id) {
        SysConfigVO sysConfigVO = new SysConfigVO();
        ConfigInfo configInfo = configInfoMapper.selectById(id);
        BeanUtils.copyProperties(configInfo, sysConfigVO);
        return sysConfigVO;
    }

    @Override
    public List<SysConfigVO> findByList() {

        return configInfoMapper.selectList(null).stream().map(
                item -> {
                    SysConfigVO sysConfigVO = new SysConfigVO();
                    BeanUtils.copyProperties(item, sysConfigVO);
                    return sysConfigVO;
                }
        ).collect(Collectors.toList());
    }
}
