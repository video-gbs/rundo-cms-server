package com.runjian.auth.server.service.area;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.video.AddVideoAreaDTO;
import com.runjian.auth.server.domain.dto.video.UpdateVideoAreaDTO;
import com.runjian.auth.server.domain.entity.video.VideoArea;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.domain.vo.video.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.vo.video.VideoAreaVO;

import java.util.List;

/**
 * <p>
 * 安保区域 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface VideoAreaSaervice extends IService<VideoArea> {

    VideoAreaVO saveVideoArae(AddVideoAreaDTO dto);

    void updateVideoAreaById(UpdateVideoAreaDTO dto);

    VideoAreaVO getVideoAreaById(Long id);

    String removeVideoAreaById(Long id);

    List<VideoAreaTree> getTreeList();

    void moveVideoArea(MoveVideoAreaDTO dto);

    List<VideoAreaVO> getVideoAreaList();

    String batchDelete(List<Long> ids);
}
