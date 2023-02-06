package com.runjian.device.service.impl;

import com.runjian.device.dao.DetailMapper;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.service.DetailBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/9 11:05
 */
@Slf4j
@Service
public class DetailBaseServiceImpl implements DetailBaseService {

    @Autowired
    private DetailMapper detailMapper;

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
    @Override
    public void saveOrUpdateDetail(Long id, Integer type, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, LocalDateTime nowTime) {
        Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(id, type);
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setIp(ip);
        detailInfo.setPort(port);
        detailInfo.setName(name);
        detailInfo.setManufacturer(manufacturer);
        detailInfo.setModel(model);
        detailInfo.setFirmware(firmware);
        detailInfo.setPtzType(ptzType);
        detailInfo.setUpdateTime(nowTime);
        // 判断数据是否为空，根据情况保存或者更新
        if (detailInfoOp.isEmpty()){
            detailInfo.setDcId(id);
            detailInfo.setType(type);
            detailInfo.setCreateTime(nowTime);
            detailMapper.save(detailInfo);
        } else {
            detailMapper.update(detailInfo);
        }
    }

}
