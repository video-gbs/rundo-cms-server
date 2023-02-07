package com.runjian.stream.dao;

import com.runjian.stream.entity.StreamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/2/3 10:22
 */
@Mapper
@Repository
public interface StreamMapper {
    Optional<StreamInfo> selectByStreamId(String streamId);

    void deleteByStreamId(String streamId);

    void updateStreamState(StreamInfo streamInfo);

    Optional<StreamInfo> selectByChannelIdAndPlayType(Long channelId, Integer playType);

    void save(StreamInfo streamInfo);

    List<StreamInfo> selectByChannelId(Long channelId);
}
