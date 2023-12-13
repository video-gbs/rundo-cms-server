package com.runjian.cascade.vo.response;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miracle
 * @date 2023/12/11 14:54
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cascade.local")
public class GetPlatformInfoRsp {

    /**
     * 平台名称
     */
    private String name;

    /**
     * 平台ip
     */
    private String ip;

    /**
     * 平台端口
     */
    private String port;

    /**
     * 国标域
     */
    private String gbDomain;

    /**
     * 国标编码
     */
    private String gbCode;
}
