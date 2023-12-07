package com.runjian.stream.dao;

import com.runjian.stream.entity.StreamPushInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/12/7 9:21
 */

@Mapper
@Repository
public interface StreamPushMapper {

    String STREAM_PUSH_TABLE_NAME = "rundo_stream_mapper";

    List<StreamPushInfo> selectByState(Integer state);

    Optional<StreamPushInfo> selectById(Long streamPushId);

    void deleteById(Long streamPushId);

    void update(StreamPushInfo streamPushInfo);

    List<StreamPushInfo> selectAll();
}
