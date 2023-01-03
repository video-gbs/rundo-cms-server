package com.runjian.parsing.dao;

import com.runjian.parsing.entity.GatewayInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface GatewayMapper {

    String GATEWAY_TABLE_NAME = "rundo_gateway";


    @Select(value = {" <script>" +
            " SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE serial_num = #{serialNum} " +
            " </script>"})
    Optional<GatewayInfo> selectBySerialNum(String serialNum);


    @Insert(" INSERT INTO " + GATEWAY_TABLE_NAME +
            " (serial_num, sign_type, gateway_type, protocol, ip, port, update_time, create_time) " +
            " VALUES " +
            " (#{serialNum}, #{signType}, #{gatewayType}, #{protocol}, #{ip}, #{port}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(GatewayInfo gatewayInfo);
}
