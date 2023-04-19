package com.runjian.auth.server.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.auth.server.domain.dto.system.VideoAreaDTO;
import com.runjian.auth.server.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.entity.VideoArea;
import com.runjian.auth.server.domain.vo.system.VideoAreaVO;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.feign.ExpansionClient;
import com.runjian.auth.server.mapper.VideoAraeMapper;
import com.runjian.auth.server.service.system.VideoAreaService;
import com.runjian.auth.server.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 安保区域 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class VideoAreaServiceImpl extends ServiceImpl<VideoAraeMapper, VideoArea> implements VideoAreaService {

    @Autowired
    private VideoAraeMapper videoAraeMapper;

    @Autowired
    private ExpansionClient expansionClient;

    @Override
    public VideoAreaVO save(VideoAreaDTO dto) {
        // 确认父级安全区域是否存在
        VideoArea prentInfo = videoAraeMapper.selectById(dto.getAreaPid());
        if (prentInfo == null) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "父级安全区域不存在");
        }
        VideoArea area = new VideoArea();
        area.setAreaName(dto.getAreaName());
        area.setAreaPid(dto.getAreaPid());
        area.setDescription(dto.getDescription());
        area.setLevel(prentInfo.getLevel() + 1);
        log.info("添加安防区域入库数据信息{}", JSONUtil.toJsonStr(area));
        videoAraeMapper.insert(area);
        VideoAreaVO videoAreaVO = videoAraeMapper.mySelectById(area.getId());
        BeanUtils.copyProperties(area, videoAreaVO);
        return videoAreaVO;
    }

    @Override
    public void modifyById(VideoAreaDTO dto) {
        VideoArea videoArea = new VideoArea();
        BeanUtils.copyProperties(dto, videoArea);
        videoAraeMapper.updateById(videoArea);
    }

    @Override
    public VideoAreaVO findById(Long id) {
        return videoAraeMapper.mySelectById(id);
    }

    @Override
    public CommonResponse erasureById(Long id) {
        // 1.判断是否为根节点
        if (id == 1L) {
            return CommonResponse.failure(BusinessErrorEnums.DEFAULT_MEDIA_DELETE_ERROR);
        }
        // 2.确认当前需要删除的安防区域有无下级安防区域
        VideoArea videoArea = videoAraeMapper.selectById(id);
        List<VideoAreaVO> videoAreaChildren = videoAraeMapper.mySelectListById(id);
        int size = videoAreaChildren.size();
        for (int i = size - 1; i >= 0; i--) {
            VideoAreaVO area = videoAreaChildren.get(i);
            if (area.getId().equals(videoArea.getId())) {
                // 2-1.剔除自己之后确认是否还存在子节点
                videoAreaChildren.remove(area);
            }
        }
        // 2-2.剔除自己之后确认是否还存在子节点
        if (CollUtil.isNotEmpty(videoAreaChildren)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_AREA_OPERATION2);
        }
        // 3.调用远端确认是否可以删除
        CommonResponse<Boolean> commonResponse = expansionClient.videoAreaBindCheck(id);
        if (!commonResponse.getData()) {
            return CommonResponse.success(videoAraeMapper.deleteById(id));
        } else {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_AREA_OPERATION);
        }
    }

    @Override
    public void moveVideoArea(MoveVideoAreaDTO dto) {
        //  0-1 禁止本级移动到本级
        if (dto.getId().equals(dto.getAreaPid())) {
            log.info("禁止本级移动到本级");
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "禁止本级移动到本级");
        }
        // 目标位置的信息
        VideoArea targetInfo = videoAraeMapper.selectById(dto.getAreaPid());
        // 被移动项信息
        VideoArea info = videoAraeMapper.selectById(dto.getId());
        // 判断目标是否存在
        Optional.ofNullable(info).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "移动的节点不存在或已删除"));
        Optional.ofNullable(targetInfo).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "目标位置不存在或已删除"));

        // 1.如果目标位置与被移动项的父级ID相同，为同级别移动
        if (info.getAreaPid().equals(targetInfo.getAreaPid())) {
            log.info("同级别移动");
            // 切换排序顺序
            info.setAreaSort(targetInfo.getAreaSort());
            videoAraeMapper.updateById(targetInfo);
            targetInfo.setAreaSort(info.getAreaSort());
            videoAraeMapper.updateById(targetInfo);
            return;
        }
        // 2.如果目标位置与是移动项的子级，禁止移动
        List<VideoAreaVO> childrenList = videoAraeMapper.mySelectListById(dto.getId());
        List<Long> ids = childrenList.stream().map(VideoAreaVO::getId).collect(Collectors.toList());
        if (ids.contains(dto.getAreaPid())) {
            log.info("父级向子级移动");
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "禁止父级向子级移动");
            // return;
        }
        // 3.更新当前节点信息
        info.setAreaPid(dto.getAreaPid());
        info.setLevel(targetInfo.getLevel() + 1);
        videoAraeMapper.updateById(info);
        // 5.根据id，更新子节点层级信息
        updateChildren(info.getId(), info.getLevel());

    }

    private void updateChildren(Long id, Integer level) {
        List<VideoArea> infoList = getChildren(id);
        infoList.stream().peek(obj -> obj.setLevel(level + 1)).collect(Collectors.toList());
        updateBatchById(infoList);
        for (VideoArea videoArea : infoList) {
            updateChildren(videoArea.getId(), videoArea.getLevel());
        }
    }

    private List<VideoArea> getChildren(Long id) {
        LambdaQueryWrapper<VideoArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoArea::getAreaPid, id);
        queryWrapper.orderBy(true, true, VideoArea::getAreaSort, VideoArea::getUpdatedTime);
        return videoAraeMapper.selectList(queryWrapper);
    }

    @Override
    public List<VideoAreaVO> findByList(Long areaId) {
        List<VideoAreaVO> videoAreaList;
        videoAreaList = videoAraeMapper.mySelectListById(Objects.requireNonNullElse(areaId, 1L));
        return videoAreaList.stream().map(
                item -> {
                    VideoAreaVO videoAreaVO = new VideoAreaVO();
                    BeanUtils.copyProperties(item, videoAreaVO);
                    return videoAreaVO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<VideoArea> getVideoAreaByRoleCode(String roleCode) {
        return videoAraeMapper.selectVideoAreaByRoleCode(roleCode);
    }

    @Override
    public List<Long> getAreaIdListByRoleId(Long id) {
        return videoAraeMapper.selectAreaIdListByRoleId(id);
    }

    @Override
    public List<VideoAreaTree> findByTree() {
        List<VideoAreaVO> videoList = videoAraeMapper.mySelectListById(1L);
        List<VideoAreaTree> videoAreaTreeList = videoList.stream().map(
                item -> {
                    VideoAreaTree bean = new VideoAreaTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return DataTreeUtil.buildTree(videoAreaTreeList, 1L);
    }

    @Override
    public List<String> getAreaNameByUserId(Long userId) {
        return videoAraeMapper.selectAreaNameByUserId(userId);
    }
}
