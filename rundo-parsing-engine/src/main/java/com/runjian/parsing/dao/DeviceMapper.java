package com.runjian.parsing.dao;

import com.runjian.parsing.entity.DeviceInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface DeviceMapper {

    String DEVICE_TABLE_NAME = "rundo_device";

    @Select(" SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE id = #{deviceId} ")
    Optional<DeviceInfo> selectById(Long deviceId);

    @Insert(" INSERT INTO " + DEVICE_TABLE_NAME +
            " (gateway_id, origin_id, update_time, create_time) " +
            " VALUES " +
            " (#{gatewayId}, #{originId}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(DeviceInfo deviceInfo);

    @Delete(" DELETE FROM " + DEVICE_TABLE_NAME +
            " WHERE id = #{deviceId} ")
    void deleteById(Long deviceId);

    @Select(" <script>" +
            " SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE gateway_id = #{gatewayId} AND origin_id = #{originId} " +
            " </script>")
    Optional<DeviceInfo> selectByGatewayIdAndOriginId(Long gatewayId, String originId);

    @Select(" <script> " +
            " SELECT * FROM " + DEVICE_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='deviceIdList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<DeviceInfo> selectByIds(List<Long> deviceIdList);
}
