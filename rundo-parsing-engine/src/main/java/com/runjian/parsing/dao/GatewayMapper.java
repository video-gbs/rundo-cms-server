package com.runjian.parsing.dao;

import com.runjian.parsing.entity.GatewayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface GatewayMapper {

    String GATEWAY_TABLE_NAME = "rundo_gateway";

    Optional<GatewayInfo> selectBySerialNum();


    void save(GatewayInfo gatewayInfo);
}
