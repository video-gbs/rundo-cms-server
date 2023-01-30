package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.vo.system.SysOrgNode;
import com.runjian.auth.server.domain.entity.system.SysOrg;

import java.util.List;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysOrgService extends IService<SysOrg> {

    List<SysOrgNode> getSysOrgTree(Long id, String orgName);

    IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize);
}
