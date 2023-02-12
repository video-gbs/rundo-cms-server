package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddVideoAreaDTO;
import com.runjian.auth.server.domain.dto.system.UpdateVideoAreaDTO;
import com.runjian.auth.server.domain.entity.VideoArea;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.vo.system.VideoAreaVO;

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

    VideoAreaVO save(AddVideoAreaDTO dto);

    void modifyById(UpdateVideoAreaDTO dto);

    VideoAreaVO findById(Long id);

    String erasureById(Long id);

    List<VideoAreaTree> findByTree();

    void moveVideoArea(MoveVideoAreaDTO dto);

    List<VideoAreaVO> findByList(Long areaId);

    String erasureBatch(List<Long> ids);
}
