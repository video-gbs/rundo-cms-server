package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.entity.SysOrg;
import com.runjian.common.config.response.CommonResponse;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysOrgService extends IService<SysOrg> {

    /**
     * 新建部门
     *
     * @param dto
     * @return
     */
    CommonResponse addSysOrg(SysOrgDTO dto);

    /**
     * 删除部门
     *
     * @param dto
     * @return
     */
    CommonResponse deleteSysOrg();

    /**
     * 查看部门详情
     *
     * @param dto
     * @return
     */
    CommonResponse getSysOrg();

    /**
     * 移动部门
     *
     * @param dto
     * @return
     */
    CommonResponse moveSysOrg();

}
