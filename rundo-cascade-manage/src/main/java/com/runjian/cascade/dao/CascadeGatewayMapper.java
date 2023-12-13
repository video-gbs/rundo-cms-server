package com.runjian.cascade.dao;

import com.runjian.cascade.entity.CascadeGatewayInfo;
import com.runjian.cascade.vo.response.GetCascadeGatewayRsp;
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
public interface CascadeGatewayMapper {

    String CASCADE_GATEWAY_TABLE_NAME = "rundo_cascade_gateway";

    List<GetCascadeGatewayRsp> selectByPage();

    Optional<CascadeGatewayInfo> selectById(Long gatewayId);

    void save(CascadeGatewayInfo cascadeGatewayInfo);

    void update(CascadeGatewayInfo cascadeGatewayInfo);
}
