package com.runjian.auth.server.controller.video;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.vo.AreaNode;
import com.runjian.auth.server.entity.video.VideoArae;
import com.runjian.auth.server.service.area.VideoAraeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 安保区域 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "安防区域")
@RestController
@RequestMapping("/videoArae")
public class VideoAraeController {

    @Autowired
    private VideoAraeService videoAraeService;

    @PostMapping("/add")
    @ApiOperation("添加安保区域")
    public ResponseResult save(@RequestBody VideoArae dto) {
        videoAraeService.save(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑安保区域")
    public ResponseResult updateSysDict(@RequestBody VideoArae dto) {
        videoAraeService.updateById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取安保区域信息")
    public ResponseResult getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", videoAraeService.getById(id));
    }

    @GetMapping("/getTree")
    @ApiOperation("获取安保区域树")
    public ResponseResult<List<AreaNode>> getTreeList(@Param("id") Long id, @Param("areaName") String areaName) {
        return new ResponseResult<>(200, "操作成功", videoAraeService.getTreeList(id, areaName));
    }

    @GetMapping("/getList")
    @ApiOperation("获取安保区域列表")
    public ResponseResult getList() {
        return new ResponseResult<>(200, "操作成功", videoAraeService.list());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("分页获取安保区域列表")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取应用列表
        return new ResponseResult<>(200, "操作成功", videoAraeService.list());
    }
}
