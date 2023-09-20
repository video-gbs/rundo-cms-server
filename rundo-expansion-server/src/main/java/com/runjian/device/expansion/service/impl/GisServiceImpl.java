package com.runjian.device.expansion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.device.expansion.entity.GisConfig;
import com.runjian.device.expansion.entity.GisVideoAreaConfig;
import com.runjian.device.expansion.mapper.GisConfigMapper;
import com.runjian.device.expansion.mapper.GisVideoAreaConfigMapper;
import com.runjian.device.expansion.service.IGisService;
import com.runjian.device.expansion.vo.request.GisConfigReq;
import com.runjian.device.expansion.vo.request.GisConfigStatusReq;
import com.runjian.device.expansion.vo.request.GisVideoAreaConfigReq;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * gis地图服务控制
 * @author chenjialing
 */
@Service
@Slf4j
public class GisServiceImpl implements IGisService {

    @Autowired
    GisConfigMapper gisConfigMapper;

    @Autowired
    GisVideoAreaConfigMapper gisVideoAreaConfigMapper;
    @Override
    public void save(GisConfigReq req) {
        GisConfig gisConfig = new GisConfig();
        BeanUtil.copyProperties(req,gisConfig);

        if(ObjectUtils.isEmpty(req.getId())){
            //添加

            gisConfigMapper.insert(gisConfig);
        }else {
            //编辑
            gisConfigMapper.updateById(gisConfig);
        }
    }

    @Override
    public List<GisConfig> list() {
        return gisConfigMapper.selectList(null);
    }

    @Override
    public synchronized void statusChange(GisConfigStatusReq req) {
        GisConfig gisConfig = new GisConfig();
        BeanUtil.copyProperties(req,gisConfig);
        LambdaQueryWrapper<GisConfig> gisConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gisConfigLambdaQueryWrapper.eq(GisConfig::getOnStatus,1);
        gisConfigLambdaQueryWrapper.last("limit 1");
        GisConfig gisConfigDb = gisConfigMapper.selectOne(gisConfigLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(gisConfigDb)){
            if(gisConfigDb.getId() != req.getId()){
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR,"已有其他的gis配置开启,请勿重复开启");
            }
        }
        gisConfigMapper.updateById(gisConfig);
    }

    @Override
    public GisConfig findOneStatusOn() {
        LambdaQueryWrapper<GisConfig> gisConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gisConfigLambdaQueryWrapper.eq(GisConfig::getOnStatus,1);
        gisConfigLambdaQueryWrapper.last("limit 1");
        GisConfig gisConfigDb = gisConfigMapper.selectOne(gisConfigLambdaQueryWrapper);
        if(ObjectUtils.isEmpty(gisConfigDb)){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION,"请先开启gis地图配置");

        }

        return gisConfigDb;
    }

    @Override
    public Long gisConfigVideoAreaSave(GisVideoAreaConfigReq req) {
        GisVideoAreaConfig gisVideoAreaConfig = new GisVideoAreaConfig();
        BeanUtil.copyProperties(req,gisVideoAreaConfig);
        if(ObjectUtils.isEmpty(req.getId())){
            gisVideoAreaConfigMapper.insert(gisVideoAreaConfig);

        }else {
            gisVideoAreaConfigMapper.updateById(gisVideoAreaConfig);

        }
        return gisVideoAreaConfig.getId();

    }

    @Override
    public GisVideoAreaConfig findVideoAreaOne(Long videoAreaId) {

        GisVideoAreaConfig gisVideoAreaConfig = gisVideoAreaConfigMapper.listPage(videoAreaId);
        if(ObjectUtils.isEmpty(gisVideoAreaConfig)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR,"该节点暂未配置gis地图配置，或则未开启gis配置启用");
        }

        return gisVideoAreaConfig;
    }
}
