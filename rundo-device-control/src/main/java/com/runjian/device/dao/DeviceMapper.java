package com.runjian.device.dao;

import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.vo.response.GetDevicePageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Select(value = {" <script> " +
            " SELECT de.id AS deviceId, dt.id AS gatewayId, de.name AS deviceName, dt.name AS gatewayName, de.sign_state, de.online_state, de.create_time  FROM " + DEVICE_TABLE_NAME + " de " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON de.id = dt.dc_id AND type = 1 " +
            " LEFT JOIN " + GatewayMapper.GATEWAY_TABLE_NAME + " ga ON ga.id = de.gateway_id " +
            " WHERE 1=1 " +
            " <if test=\"signState != null\" >  AND de.sign_state = #{signState} </if> " +
            " <if test=\"deviceName != null\" >  AND de.name LIKE CONCAT('%', #{deviceName}, '%')  </if> " +
            " <if test=\"ip != null\" >  AND de.ip LIKE CONCAT('%', #{ip}, '%')  </if> " +
            " ORDER BY de.create_time desc " +
            " </script> "})
    List<GetDevicePageRsp> selectByPage(Integer signState, String deviceName, String ip);
}
