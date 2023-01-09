package com.runjian.device.dao;

import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

    Optional<DeviceInfo> selectById(Long id);

    void save(DeviceInfo deviceInfo);

    void update(DeviceInfo deviceInfo);

    Optional<DeviceInfo> selectByOriginIdAndGatewayId(String deviceId, Long gatewayId);

    void deleteById(Long id);

    void updateSignState(DeviceInfo deviceInfo);
}
