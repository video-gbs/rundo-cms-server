package com.runjian.stream.dao;

import com.runjian.stream.entity.GatewayDispatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/3 10:22
 */
@Mapper
@Repository
public interface GatewayDispatchMapper {
    void saveAll(List<GatewayDispatchInfo> gatewayDispatchInfoList);

    Optional<GatewayDispatchInfo> selectByGatewayId(Long gatewayId);

    void save(GatewayDispatchInfo gatewayDispatchInfo);

    void update(GatewayDispatchInfo gatewayDispatchInfo);

    List<GatewayDispatchInfo> selectByGatewayIds(Set<Long> gatewayIds);

    void updateAll(List<GatewayDispatchInfo> gatewayDispatchInfoList);

    void deleteByGatewayId(Long gatewayId);

    void deleteByDispatchIdAndNotInGatewayIds(Set<Long> gatewayIds);
}
