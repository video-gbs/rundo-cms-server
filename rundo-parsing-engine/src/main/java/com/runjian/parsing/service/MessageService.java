package com.runjian.parsing.service;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.vo.CommonMqDto;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MessageService {



    /**
     * 消息分配
     * @param request
     */
    void msgDispatch(CommonMqDto<?> request);

}
