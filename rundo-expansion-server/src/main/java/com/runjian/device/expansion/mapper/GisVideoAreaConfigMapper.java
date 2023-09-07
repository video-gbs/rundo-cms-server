package com.runjian.device.expansion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.device.expansion.entity.GisConfig;
import com.runjian.device.expansion.entity.GisVideoAreaConfig;

/**
 * @author chenjialing
 */
public interface GisVideoAreaConfigMapper extends BaseMapper<GisVideoAreaConfig> {


    GisVideoAreaConfig listPage(Long videoAreaId);
}
