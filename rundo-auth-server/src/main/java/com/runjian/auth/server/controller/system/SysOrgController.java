package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.domain.vo.SysOrgVO;
import com.runjian.auth.server.service.system.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 组织机构表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    @PostMapping("/add")
    @ApiOperation("新建部门")
    public ResponseResult addSysOrg(@RequestBody SysOrgDTO dto) {
        return sysOrgService.addSysOrg(dto);
    }

    @PostMapping("/update")
    @ApiOperation("新建部门")
    public ResponseResult updateSysOrg(@RequestBody SysOrgDTO dto) {
        return sysOrgService.updateSysOrg(dto);
    }


    @GetMapping("getSysOrgById")
    @ApiOperation("查询部门")
    public ResponseResult<SysOrgVO> getSysOrgById(Long id) {
        return new ResponseResult<>(200, "操作成", sysOrgService.getSysOrgById(id));
    }

    public ResponseResult moveSysOrg() {
        return null;
    }

    public ResponseResult deleteSysOrg() {
        return null;
    }
}
