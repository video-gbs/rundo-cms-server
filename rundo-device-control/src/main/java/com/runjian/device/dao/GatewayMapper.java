package com.runjian.device.dao;

import com.runjian.device.entity.GatewayInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayMapper {

    String GATEWAY_TABLE_NAME = "rundo_gateway";

    @Insert("INSERT INTO " + GATEWAY_TABLE_NAME +
            " (id, serial_num, name, sign_type, state, gateway_type, protocol, ip, port, update_time, create_time) " +
            " VALUES " +
            " (#{id}, #{serialNum}, #{name}, #{signType}, #{online}, #{gatewayType}, #{protocol}, #{ip}, #{port}, #{updateTime}, #{createTime})")
    void save(GatewayInfo gatewayInfo);

    @Update(value = {" <script> " +
            " UPDATE "  + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime}, " +
            " <if test='name != null'>, name = #{name}</if> " +
            " <if test='signType != null'>, signType = #{signType}</if> " +
            " <if test='online != null'>, online = #{online}</if> " +
            " <if test='ip != null'>, ip = #{ip}</if> " +
            " <if test='port != null'>, port = #{port}</if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(GatewayInfo gatewayInfo);
}
