package com.runjian.device.service;

import java.time.LocalDateTime;

/**
 * 设备或通道详情基础服务
 * @author Miracle
 * @date 2023/1/9 11:03
 */
public interface DetailBaseService {

    /**
     * 保存设备或者通道的详细信息
     * @param id 设备id
     * @param type 信息类型
     * @param ip ip地址
     * @param port 端口
     * @param name 设备名称
     * @param manufacturer 厂商
     * @param model 型号
     * @param firmware 固件版本
     * @param ptzType 云台类型
     * @param nowTime 更新时间
     */
    void saveOrUpdateDetail(Long id, Integer type, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, LocalDateTime nowTime);
}
