package com.runjian.parsing.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

}
