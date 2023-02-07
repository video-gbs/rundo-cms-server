package com.runjian.auth.server.controller.video;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.video.AddVideoAreaDTO;
import com.runjian.auth.server.domain.dto.video.UpdateVideoAreaDTO;
import com.runjian.auth.server.domain.vo.tree.VideoAreaTree;
import com.runjian.auth.server.domain.vo.video.MoveVideoAreaDTO;
import com.runjian.auth.server.domain.vo.video.VideoAreaVO;
import com.runjian.auth.server.service.area.VideoAreaSaervice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 安防区域 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "安防区域")
@Slf4j
@RestController
@RequestMapping("/videoArae")
public class VideoAreaController {

    @Autowired
    private VideoAreaSaervice videoAreaSaervice;

    @PostMapping("/add")
    @ApiOperation("添加安防区域")
    public ResponseResult<VideoAreaVO> save(@RequestBody AddVideoAreaDTO dto) {
        log.info("添加安防区域前端传参信息{}", JSONUtil.toJsonStr(dto));
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.saveVideoArae(dto));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除安防区域")
    public ResponseResult<String> delete(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.removeVideoAreaById(id));
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除安防区域")
    public ResponseResult<String> batchDelete(@RequestBody List<Long> ids) {
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.batchDelete(ids));
    }

    @PostMapping("/update")
    @ApiOperation("编辑安防区域")
    public ResponseResult<?> update(@RequestBody UpdateVideoAreaDTO dto) {
        videoAreaSaervice.updateVideoAreaById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/move")
    @ApiOperation("移动安防区域")
    public ResponseResult<?> move(@RequestBody MoveVideoAreaDTO dto) {
        videoAreaSaervice.moveVideoArea(dto);
        return new ResponseResult<>(200, "操作成功");
    }


    @GetMapping("/getById/{id}")
    @ApiOperation("获取安防区域信息")
    public ResponseResult<VideoAreaVO> getById(@PathVariable Long id) {
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.getVideoAreaById(id));
    }

    @GetMapping("/tree")
    @ApiOperation("获取安防区域层级树")
    public ResponseResult<List<VideoAreaTree>> getTreeList() {
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.getTreeList());
    }


    @GetMapping("/getList")
    @ApiOperation("获取安防区域列表")
    public ResponseResult<List<VideoAreaVO>> getList() {
        return new ResponseResult<>(200, "操作成功", videoAreaSaervice.getVideoAreaList());
    }

}
