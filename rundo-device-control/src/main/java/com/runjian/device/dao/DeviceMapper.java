package com.runjian.device.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

}
