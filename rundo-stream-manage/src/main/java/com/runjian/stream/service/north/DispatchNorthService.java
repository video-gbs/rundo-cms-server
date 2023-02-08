package com.runjian.stream.service.north;

import com.github.pagehelper.PageInfo;
import com.runjian.stream.vo.response.GetDispatchRsp;

/**
 * @author Miracle
 * @date 2023/2/7 19:49
 */
public interface DispatchNorthService {

    /**
     * 获取调度服务信息
     * @param page 页码
     * @param num 每页数量
     * @param name 名称
     * @return
     */
    PageInfo<GetDispatchRsp> getDispatchByPage(int page, int num, String name);

    /**
     * 修改额外的名字与可访问URL
     * @param dispatchId 分配id
     * @param name 名字
     * @param url 可访问url
     */
    void updateExtraData(Long dispatchId, String name, String url);
}
