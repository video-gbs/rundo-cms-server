package com.runjian.device.dao;

import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.vo.response.GetDevicePageRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            " SELECT de.id AS deviceId, ga.id AS gatewayId, dt.name AS deviceName, ga.name AS gatewayName, de.sign_state, de.online_state, de.create_time, de.device_type," +
            " dt.origin_id, dt.ip, dt.port, dt.manufacturer, dt.model, dt.firmware, dt.ptz_type, dt.username, dt.password  FROM " + DEVICE_TABLE_NAME + " de " +
            " LEFT JOIN " + DetailMapper.DETAIL_TABLE_NAME + " dt ON de.id = dt.dc_id AND type = 1 " +
            " LEFT JOIN " + GatewayMapper.GATEWAY_TABLE_NAME + " ga ON ga.id = de.gateway_id " +
            " WHERE de.sign_state != 0 "  +
            " <if test=\"signState != null\" >  AND de.sign_state = #{signState} </if> " +
            " <if test=\"deviceName != null\" >  AND dt.name LIKE CONCAT('%', #{deviceName}, '%')  </if> " +
            " <if test=\"ip != null\" >  AND dt.ip LIKE CONCAT('%', #{ip}, '%')  </if> " +
            " ORDER BY de.online_state desc, de.create_time desc  " +
            " </script> "})
    List<GetDevicePageRsp> selectByPage( Integer signState, String deviceName, String ip);

    @Select(" SELECT id FROM " + DEVICE_TABLE_NAME +
            " WHERE sign_state = #{signState} ")
    List<Long> selectIdBySignState(Integer signState);

    @Select(value = {" <script> " +
            " SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE gateway_id IN" +
            " <foreach collection='gatewayIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " AND online_state = #{onlineState} " +
            " </script> "})
    List<DeviceInfo> selectByGatewayIdsAndOnlineState(Set<Long> gatewayIds, Integer onlineState);

    @Update(" <script> " +
            " <foreach collection='deviceInfoList' item='item' separator=';'> " +
            " UPDATE " + DEVICE_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , online_state = #{item.onlineState} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateOnlineState(List<DeviceInfo> deviceInfoList);

    @Select(" <script> " +
            " SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='deviceIds'  item='item'  open='(' separator=',' close=')' > #{item} </foreach> " +
            " </script> ")
    List<DeviceInfo> selectByIds(Set<Long> deviceIds);

    @Insert({" <script> " +
            " INSERT INTO " + DEVICE_TABLE_NAME + "(id, gateway_id, sign_state, device_type, online_state, update_time, create_time) values " +
            " <foreach collection='saveList' item='item' separator=','>(#{item.id}, #{item.gatewayId}, #{item.signState}, #{item.deviceType}, #{item.onlineState}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<DeviceInfo> saveList);
}
