package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.VideoAreaDTO;
import com.runjian.auth.server.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.entity.VideoArea;
import com.runjian.auth.server.domain.vo.system.VideoAreaVO;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.common.config.response.CommonResponse;

import java.util.List;

/**
 * <p>
 * 安保区域 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface VideoAreaService extends IService<VideoArea> {

    /**
     * 获取安防区域层级树
     *
     * @return List
     */
    List<VideoAreaTree> findByTree();

    /**
     * 新建安防区域，并返回新增后的信息
     *
     * @param dto
     * @return VideoAreaVO
     */
    VideoAreaVO save(VideoAreaDTO dto);

    /**
     * 编辑安防区域
     *
     * @param dto
     */
    void modifyById(VideoAreaDTO dto);

    /**
     * 根据ID 查询安防区域信息
     *
     * @param id
     * @return
     */
    VideoAreaVO findById(Long id);

    /**
     * 删除安防区域
     *
     * @param id
     * @return
     */
    CommonResponse erasureById(Long id);

    /**
     * 将安防区域移动到目标节点下
     *
     * @param dto
     */
    void moveVideoArea(MoveVideoAreaDTO dto);

    /**
     * 获取安防区域及其子节点
     *
     * @param areaId
     * @return
     */
    List<VideoAreaVO> findByList(Long areaId);

    List<VideoArea> getVideoAreaByRoleCode(String roleCode);
}
