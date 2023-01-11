package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.SysOrgDTO;
import com.runjian.auth.server.domain.vo.SysOrgVO;
import com.runjian.auth.server.entity.SysOrg;

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
    ResponseResult addSysOrg(SysOrgDTO dto);

    /**
     * 查看部门详情
     *
     * @param dto
     * @param id
     * @return
     */
    SysOrgVO getSysOrgById(Long id);

    /**
     * 删除部门
     *
     * @param dto
     * @return
     */
    ResponseResult deleteSysOrg();

    /**
     * 移动部门
     *
     * @param dto
     * @return
     */
    ResponseResult moveSysOrg();

}
