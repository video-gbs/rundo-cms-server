package com.runjian.parsing.dao;

import com.runjian.parsing.entity.DispatchInfo;
import org.apache.ibatis.annotations.Mapper;
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

    Optional<DispatchInfo> selectBySerialNum(String serialNum);

    void save(DispatchInfo dispatchInfo);

    List<DispatchInfo> selectAll();
}
