package com.runjian.cascade.dao;

import com.runjian.cascade.entity.GatewayInfo;
import com.runjian.cascade.vo.response.GetGatewayRsp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/12/11 15:18
 */
@Mapper
@Repository
public interface GatewayMapper {

    String CASCADE_GATEWAY_TABLE_NAME = "rundo_gateway";

    List<GetGatewayRsp> selectByPage();

    Optional<GatewayInfo> selectById(Long gatewayId);

    void save(GatewayInfo gatewayInfo);

    void update(GatewayInfo gatewayInfo);
}
