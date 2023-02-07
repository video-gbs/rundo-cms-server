package com.runjian.auth.server.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.auth.server.domain.dto.system.AddSysDictDTO;
import com.runjian.auth.server.domain.dto.system.QureySysDictDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysDictDTO;
import com.runjian.auth.server.domain.vo.system.SysDictVO;
import com.runjian.auth.server.service.system.SysDictService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "数据字典")
@Slf4j
@RestController
@RequestMapping("/sysDict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @PostMapping("/add")
    @ApiOperation("添加数据字典")
    public CommonResponse<?> save(@RequestBody @Valid AddSysDictDTO dto) {
        log.info("添加数据字典前端传参{}", JSONUtil.toJsonStr(dto));
        sysDictService.saveSysDict(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除数据字典")
    public CommonResponse<?> remove(@PathVariable Long id) {
        log.info("删除数据字典前端传参{}", id);
        sysDictService.removeSysDictById(id);
        return CommonResponse.create(200, "操作成功", null);
    }

    @PostMapping("/update")
    @ApiOperation("修改数据字典")
    public CommonResponse<?> updateSysDict(@RequestBody UpdateSysDictDTO dto) {
        sysDictService.updateSysDictById(dto);
        return CommonResponse.create(200, "操作成功", null);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取数据字典信息")
    public CommonResponse<SysDictVO> getById(@PathVariable Long id) {
        return CommonResponse.create(200, "操作成功", sysDictService.getSysDictById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取数据字典列表 无分页")
    public CommonResponse<List<SysDictVO>> getList() {
        return CommonResponse.create(200, "操作成功", sysDictService.getSysDictList());
    }

    @PostMapping("/getListByPage")
    @ApiOperation("分页获取数据字典列表")
    public CommonResponse<IPage<SysDictVO>> getListByPage(@RequestBody @Valid QureySysDictDTO dto) {
        // TODO 分页获取数据字典列表
        return CommonResponse.create(200, "操作成功", sysDictService.getSysDictByPage(dto));
    }
}
