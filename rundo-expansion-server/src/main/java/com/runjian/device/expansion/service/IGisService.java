package com.runjian.device.expansion.service;

import com.runjian.device.expansion.entity.GisConfig;
import com.runjian.device.expansion.entity.GisVideoAreaConfig;
import com.runjian.device.expansion.vo.request.GisConfigReq;
import com.runjian.device.expansion.vo.request.GisConfigStatusReq;
import com.runjian.device.expansion.vo.request.GisVideoAreaConfigReq;

/**
 * @author chenjialing
 */
public interface IGisService {

    /**
     * 保存
     * @param req
     */
    void save(GisConfigReq req);

    /**
     * 状态修改
     */
    void statusChange(GisConfigStatusReq req);

    /**
     * 查找开启得地图配置
     * @return
     */
    GisConfig findOneStatusOn();


    void gisConfigVideoAreaSave(GisVideoAreaConfigReq req);

    /**
     * 查询节点相关的gis配置
     * @param videoAreaId
     * @return
     */
    GisVideoAreaConfig findVideoAreaOne(Long videoAreaId);
}
