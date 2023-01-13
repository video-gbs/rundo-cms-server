package com.runjian.auth.server.service.area;

import com.runjian.auth.server.model.vo.video.AreaNode;
import com.runjian.auth.server.entity.video.VideoArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 安保区域 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface VideoAraeService extends IService<VideoArea> {

    List<AreaNode> getTreeList(Long id, String areaName);

    void saveVideoArae(VideoArea dto);
}
