package com.runjian.device.expansion.config;

import com.runjian.device.expansion.vo.feign.response.MessageSubRsp;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class MessageSubMapConf {

    private HashMap<String,MessageSubRsp> messageSubRspMap;
}
