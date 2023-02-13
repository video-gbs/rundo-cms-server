package com.runjian.parsing.dao;

import com.runjian.parsing.entity.DispatchInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/7 18:07
 */
@Mapper
@Repository
public interface DispatchMapper {

    String GATEWAY_DISPATCH_TABLE_NAME = "rundo_dispatch";

    @Select(" SELECT * FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE serial_num = #{serialNum} ")
    Optional<DispatchInfo> selectBySerialNum(String serialNum);

    @Insert(" INSERT INTO " + GATEWAY_DISPATCH_TABLE_NAME +
            " (serial_num, sign_type, ip, port, update_time, create_time) " +
            " VALUES " +
            " (#{serialNum}, #{signType}, #{ip}, #{port}, #{updateTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(DispatchInfo dispatchInfo);

    @Select(" SELECT * FROM " + GATEWAY_DISPATCH_TABLE_NAME)
    List<DispatchInfo> selectAll();

    @Select(" SELECT * FROM " + GATEWAY_DISPATCH_TABLE_NAME +
            " WHERE id = #{dispatchId} ")
    Optional<DispatchInfo> selectById(Long dispatchId);
}
