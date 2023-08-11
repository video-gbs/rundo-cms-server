package com.runjian.parsing.dao;

import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * 网关数据库操作类
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Mapper
@Repository
public interface GatewayMapper {

    String GATEWAY_TABLE_NAME = "rundo_gateway";


    @Select(" <script>" +
            " SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE serial_num = #{serialNum} " +
            " </script>")
    Optional<GatewayInfo> selectBySerialNum(String serialNum);

    @Insert(" INSERT INTO " + GATEWAY_TABLE_NAME +
            " (serial_num, sign_type, gateway_type, protocol, ip, port, update_time, create_time) " +
            " VALUES " +
            " (#{serialNum}, #{signType}, #{gatewayType}, #{protocol}, #{ip}, #{port}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(GatewayInfo gatewayInfo);

    @Select(" SELECT * FROM " + GATEWAY_TABLE_NAME +
            " WHERE id = #{gatewayId} ")
    Optional<GatewayInfo> selectById(Long id);

    @Select(" SELECT * FROM " + GATEWAY_TABLE_NAME)
    List<GatewayInfo> selectAll();
    @Update(value = {" <script> " +
            " UPDATE " + GATEWAY_TABLE_NAME +
            " SET update_time = #{updateTime} " +
            " <if test='ip != null'>, ip = #{ip} </if> " +
            " <if test='port != null'>, port = #{port} </if> " +
            " WHERE id = #{id} "+
            " </script> "})
    void update(GatewayInfo gatewayInfo);
}
