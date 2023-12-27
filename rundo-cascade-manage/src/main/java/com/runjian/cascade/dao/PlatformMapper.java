package com.runjian.cascade.dao;

import com.runjian.cascade.entity.PlatformInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.runjian.cascade.entity.PlatformInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/12/11 17:51
 */
@Mapper
@Repository
public interface PlatformMapper {

    String PLATFORM_SUP_TABLE_NAME = "rundo_platform";


    Optional<PlatformInfo> selectByIpAndPort(String ip, Integer port);

    void save(PlatformInfo platformInfo);

    Optional<PlatformInfo> selectById(Long platformId);

    void update(PlatformInfo platformInfo);

    List<PlatformInfo> selectAllByIds(Set<Long> platformIds);

    void batchDelete(List<Long> ids);

    @Update(" UPDATE " + PLATFORM_SUP_TABLE_NAME + " set online_state = #{signState}" +
            " WHERE gb_code = #{gbCode} limit 1")
    PlatformInfo updateStatusByGbCode(String gbCode,Integer signState);


    @Select(" SELECT * FROM " + PLATFORM_SUP_TABLE_NAME +
            " WHERE gb_code = #{gbCode} limit 1")
    PlatformInfo selectByGbCode(String gbCode);
}
