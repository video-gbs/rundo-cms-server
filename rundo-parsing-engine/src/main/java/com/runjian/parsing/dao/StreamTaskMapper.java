package com.runjian.parsing.dao;

import com.runjian.parsing.entity.StreamTaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/10 11:14
 */
@Repository
@Mapper
public interface StreamTaskMapper {
    void save(StreamTaskInfo streamTaskInfo);

    Optional<StreamTaskInfo> selectById(Long taskId);

    void updateState(Long taskId, Integer taskState, String detail, LocalDateTime updateTime);
}
