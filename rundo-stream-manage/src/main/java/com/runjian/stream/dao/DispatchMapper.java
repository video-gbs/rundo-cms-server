package com.runjian.stream.dao;

import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.vo.response.GetDispatchRsp;
import org.apache.ibatis.annotations.Mapper;
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

    Optional<DispatchInfo> selectById(Long dispatchId);

    void setAllOnlineState(Integer onlineState, LocalDateTime now);

    void batchUpdateOnlineState(Set<Long> dispatchIds, Integer onlineState, LocalDateTime updateTime);

    void save(DispatchInfo dispatchInfo);

    void update(DispatchInfo dispatchInfo);

    void updateOnlineState(Long dispatchId, Integer onlineState, LocalDateTime updateTime);

    List<GetDispatchRsp> selectAllByPage(String name);
}
