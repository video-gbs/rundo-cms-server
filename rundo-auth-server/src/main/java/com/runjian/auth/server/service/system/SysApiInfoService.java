package com.runjian.auth.server.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.runjian.auth.server.domain.dto.system.AddSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.auth.server.domain.dto.system.UpdateSysApiInfoDTO;
import com.runjian.auth.server.domain.entity.system.SysApiInfo;
import com.runjian.auth.server.domain.vo.system.SysApiInfoVO;
import com.runjian.auth.server.domain.vo.tree.SysApiInfoTree;

import java.util.List;

/**
 * <p>
 * 接口信息表 服务类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
public interface SysApiInfoService extends IService<SysApiInfo> {

    void saveSysApiInfo(AddSysApiInfoDTO dto);

    void updateSysApiInfoById(UpdateSysApiInfoDTO dto);

    SysApiInfoVO getSysApiInfoById(Long id);

    List<SysApiInfoVO> getSysApiInfoList();

    Page<SysApiInfoVO> getSysApiInfoByPage(Integer pageNum, Integer pageSize);

    void changeStatus(StatusSysApiInfoDTO dto);

    List<SysApiInfoTree> getSysApiInfoTree(QuerySysApiInfoDTO dto);
}
