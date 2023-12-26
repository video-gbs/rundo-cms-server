package com.runjian.cascade.dao;

import com.runjian.cascade.entity.PlatformInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
}
