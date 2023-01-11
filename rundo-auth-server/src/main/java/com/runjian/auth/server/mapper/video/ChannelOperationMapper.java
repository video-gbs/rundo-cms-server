package com.runjian.auth.server.mapper.video;

import com.runjian.auth.server.entity.video.ChannelOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 通道操作 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface ChannelOperationMapper extends BaseMapper<ChannelOperation> {

    List<Long> selectByRoleId(Long roleId);
}
