package com.runjian.cascade.dao;

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
public interface PlatformMapper {

    String PLATFORM_SUP_TABLE_NAME = "rundo_platform";

    @Select(" SELECT * FROM " + PLATFORM_SUP_TABLE_NAME +
            " WHERE gb_code = #{gbCode} limit 1")
    PlatformInfo selectByGbCode(String gbCode);


    @Update(" UPDATE " + PLATFORM_SUP_TABLE_NAME + " set online_state = #{signState}" +
            " WHERE gb_code = #{gbCode} limit 1")
    PlatformInfo updateStatusByGbCode(String gbCode,Integer signState);
}
