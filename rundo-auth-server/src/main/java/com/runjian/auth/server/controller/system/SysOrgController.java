package com.runjian.auth.server.controller.system;

import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.service.system.SysOrgService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 组织机构表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "组织机管理接口")
@RestController
@RequestMapping("/sysOrg")
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    @PostMapping("add")
    @ApiOperation("新建部门")
    public CommonResponse addSysOrg(@RequestBody SysOrgDTO dto) {
        return CommonResponse.success(sysOrgService.addSysOrg(dto));
    }


    public CommonResponse deleteSysOrg() {
        return CommonResponse.success();
    }

    public CommonResponse<SysOrgDTO> getSysOrg() {
        return CommonResponse.success();
    }

    public CommonResponse moveSysOrg() {
        return CommonResponse.success();
    }

}
