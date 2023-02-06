package com.runjian.device.expansion.expansion.dao;

import com.runjian.device.expansion.entity.DeviceInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 设备数据库操作类
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

    @Select(" SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<DeviceInfo> selectById(Long id);

    @Insert(" INSERT INTO " + DEVICE_TABLE_NAME +
            " (id, gateway_id, sign_state, device_type, online_state, update_time, create_time) " +
            " VALUES " +
            " (#{id}, #{gatewayId}, #{signState}, #{deviceType}, #{onlineState}, #{updateTime}, #{createTime})")
    void save(DeviceInfo deviceInfo);

    @Update({" <script> " +
            " UPDATE "  + DEVICE_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='signState != null'>, sign_state = #{signState}</if> " +
            " <if test='deviceType != null'>, device_type = #{deviceType}</if> " +
            " <if test='onlineState != null'>, online_state = #{onlineState}</if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(DeviceInfo deviceInfo);

    @Delete(" DELETE FROM " + DEVICE_TABLE_NAME +
            " WHERE id = #{id} ")
    void deleteById(Long id);

    @Update(" UPDATE "  + DEVICE_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " sign_state = #{signState} " +
            " WHERE id = #{id} ")
    void updateSignState(DeviceInfo deviceInfo);
}
