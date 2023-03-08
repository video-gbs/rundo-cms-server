package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.MoveSysOrgDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysOrgDTO;
import com.runjian.auth.server.domain.entity.OrgInfo;
import com.runjian.auth.server.domain.vo.system.SysOrgVO;
import com.runjian.auth.server.domain.vo.tree.SysOrgTree;

import java.util.List;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface OrgInfoService extends IService<OrgInfo> {

    SysOrgVO save(AddSysOrgDTO dto);

    List<SysOrgTree> findByTree();

    IPage<OrgInfo> findByPage(Integer pageNum, Integer pageSize);

    String erasureById(Long id);

    void modifyById(UpdateSysOrgDTO dto);

    SysOrgVO findById(Long id);

    List<SysOrgVO> findByList();

    void moveSysOrg(MoveSysOrgDTO dto);

    String erasureBatch(List<Long> ids);
}