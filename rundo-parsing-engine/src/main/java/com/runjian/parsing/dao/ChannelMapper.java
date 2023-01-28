package com.runjian.parsing.dao;

import com.runjian.parsing.entity.ChannelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/13 14:18
 */
@Mapper
@Repository
public interface ChannelMapper {

    String CHANNEL_TABLE_NAME = "rundo_channel";

    Optional<ChannelInfo> selectById(Long id);

    void deleteByDeviceId(Long deviceId);
}
