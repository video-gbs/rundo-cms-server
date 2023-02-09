package com.runjian.stream.dao;

import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.vo.response.GetDispatchRsp;
import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/3 10:21
 */
@Mapper
@Repository
public interface DispatchMapper {

    String DISPATCH_TABLE_NAME = "rundo_dispatch";

    @Select(" SELECT * FROM " + DISPATCH_TABLE_NAME +
            " WHERE id = #{dispatchId} ")
    Optional<DispatchInfo> selectById(Long dispatchId);

    @Update(" UPDATE " + DISPATCH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE id = #{id} ")
    void setAllOnlineState(Integer onlineState, LocalDateTime updateTime);

    @Update(" <script> " +
            " <foreach collection='dispatchIds' item='item' separator=';'> " +
            " UPDATE " + DISPATCH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE id = #{item} "+
            " </foreach> " +
            " </script> ")
    void batchUpdateOnlineState(Set<Long> dispatchIds, Integer onlineState, LocalDateTime updateTime);

    @Insert(" INSERT INTO " + DISPATCH_TABLE_NAME +
            " (id, serial_num, online_state, name, ip, port, url, update_time, create_time) " +
            " VALUES " +
            " (#{id}, #{serialNum}, #{onlineState}, #{name}, #{ip}, #{port}, #{url}, #{updateTime}, #{createTime})")
    void save(DispatchInfo dispatchInfo);

    @Update(" <script> " +
            " UPDATE " + DISPATCH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            "<if test=\"name != null\" >, name = #{name} </if>" +
            "<if test=\"ip != null\" >, ip = #{ip} </if>" +
            "<if test=\"port != null\" >, port = #{port} </if>" +
            "<if test=\"url != null\" >, url = #{url} </if>" +
            " WHERE id = #{id} " +
            " </script> ")
    void update(DispatchInfo dispatchInfo);

    @Update(" UPDATE " + DISPATCH_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , online_state = #{onlineState} " +
            " WHERE id = #{dispatchId} ")
    void updateOnlineState(Long dispatchId, Integer onlineState, LocalDateTime updateTime);

    @Select(" SELECT * FROM " + DISPATCH_TABLE_NAME +
            " WHERE name = #{name} ")
    List<GetDispatchRsp> selectAllByPage(String name);
}
