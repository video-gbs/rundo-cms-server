package com.runjian.auth.server.service.login;

import com.runjian.auth.server.domain.vo.system.HomeVO;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeSevice
 * @Description 门户首页
 * @date 2023-02-08 周三 11:51
 */
public interface HomeSevice {
    HomeVO getIndex();
}
