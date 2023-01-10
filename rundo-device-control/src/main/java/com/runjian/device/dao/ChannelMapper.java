package com.runjian.device.dao;

import com.runjian.device.entity.ChannelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface ChannelMapper {

    String CHANNEL_TABLE_NAME = "rundo_channel";

    void batchSave(List<ChannelInfo> saveList);

    Optional<ChannelInfo> selectById(Long channelId);

    void updateSignState(ChannelInfo channelInfo);
}
