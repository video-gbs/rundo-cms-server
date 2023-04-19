package com.runjian.auth.server.mapper;

import com.runjian.auth.server.domain.entity.VideoArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.runjian.auth.server.domain.vo.system.VideoAreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 安保区域 Mapper 接口
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Mapper
public interface VideoAraeMapper extends BaseMapper<VideoArea> {

    /**
     * 自定义获取节点信息
     *
     * @param id
     * @return
     */
    VideoAreaVO mySelectById(@Param("id") Long id);

    /**
     * 自定义获取节点及子节点列表
     *
     * @param areaId
     * @return
     */
    List<VideoAreaVO> mySelectListById(@Param("areaId") Long areaId);

    /**
     * 通过角色编码，查取角色已有的安防区域信息
     *
     * @param roleCode
     * @return
     */
    List<VideoArea> selectVideoAreaByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色ID，获取角色已有的安防区域ID列表
     *
     * @param roleId
     * @return
     */
    List<Long> selectAreaIdListByRoleId(@Param("roleId") Long roleId);
}
