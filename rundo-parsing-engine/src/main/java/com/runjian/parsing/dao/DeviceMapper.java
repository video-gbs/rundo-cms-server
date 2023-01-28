package com.runjian.parsing.dao;

import com.runjian.parsing.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

    Optional<DeviceInfo> selectById(Long deviceId);

    void save(DeviceInfo deviceInfo);

    void deleteById(Long deviceId);
}
