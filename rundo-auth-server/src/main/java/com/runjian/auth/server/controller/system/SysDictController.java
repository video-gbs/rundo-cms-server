package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysDictDTO;
import com.runjian.auth.server.service.system.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("/sysDict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @PostMapping("/addSysDict")
    @ApiOperation("添加接口")
    public ResponseResult addSysDict(@RequestBody SysDictDTO dto) {
        return sysDictService.addSysDict(dto);
    }
}
