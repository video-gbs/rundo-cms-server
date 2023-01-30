package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.system.AddSysConfigDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysConfigDTO;
import com.runjian.auth.server.domain.vo.system.SysConfigVO;
import com.runjian.auth.server.service.system.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统全局参数配置 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "系统参数配置")
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @PostMapping("/add")
    @ApiOperation("添加系统参数配置")
    public ResponseResult<?> add(@RequestBody AddSysConfigDTO dto) {
        sysConfigService.saveSysConfig(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @PostMapping("/update")
    @ApiOperation("编辑系统参数配置")
    public ResponseResult<?> update(@RequestBody UpdateSysConfigDTO dto) {
        sysConfigService.updateSysConfigById(dto);
        return new ResponseResult<>(200, "操作成功");
    }

    @GetMapping("/getById")
    @ApiOperation("获取编辑系统参数配置信息")
    public ResponseResult<SysConfigVO> getById(@RequestBody Long id) {
        return new ResponseResult<>(200, "操作成功", sysConfigService.getSysConfigById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取系统参数配置列表 无分页")
    public ResponseResult<List<SysConfigVO>> getList() {
        return new ResponseResult<>(200, "操作成功", sysConfigService.getSysConfigList());
    }

    @GetMapping("/getListByPage")
    @ApiOperation("获取系统参数配置列表 含分页")
    public ResponseResult getListByPage(@RequestBody Long id) {
        // TODO 分页获取系统参数配置列表
        return new ResponseResult<>(200, "操作成功", sysConfigService.list());
    }

}
