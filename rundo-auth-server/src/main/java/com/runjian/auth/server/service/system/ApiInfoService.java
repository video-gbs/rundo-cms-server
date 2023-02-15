package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.entity.ApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.ApiInfoTree;

import java.util.List;

/**
 * <p>
 * 接口信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface ApiInfoService extends IService<ApiInfo> {

    void save(AddSysApiInfoDTO dto);

    void modifyById(UpdateSysApiInfoDTO dto);

    SysApiInfoVO findById(Long id);

    Page<SysApiInfoVO> findByPage(Integer pageNum, Integer pageSize);

    void modifyByStatus(StatusSysApiInfoDTO dto);

    List<ApiInfoTree> findByTree(QuerySysApiInfoDTO dto);

    List<SysApiInfoVO> findByList(QuerySysApiInfoDTO dto);
}
