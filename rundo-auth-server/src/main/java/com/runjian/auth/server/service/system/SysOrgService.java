package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;
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

    List<SysOrgTree> getSysOrgTree();

    IPage<SysOrg> getListByPage(Integer pageNum, Integer pageSize);

    SysOrgVO saveSysOrg(AddSysOrgDTO dto);

    String removeSysOrgById(Long id);

    void updateSysOrgById(UpdateSysOrgDTO dto);

    SysOrgVO getSysOrgById(Long id);

    List<SysOrgVO> getSysOrgList();

    void moveSysOrg(MoveSysOrgDTO dto);

    String batchDelete(List<Long> ids);
}
