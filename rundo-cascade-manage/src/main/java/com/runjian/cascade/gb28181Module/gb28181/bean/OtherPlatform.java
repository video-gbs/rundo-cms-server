package com.runjian.cascade.gb28181Module.gb28181.bean;


import lombok.Data;

/**
 * @author lin
 */
@Data
public class OtherPlatform {



    /**
     * 平台名称
     */
    private String name;

    /**
     * 上级平台--国标编码
     */
    private String serverGbId;

    /**
     * 上级平台--国标域
     */
    private String serverGbDomain;

    /**
     * 上级平台--服务ip
     */
    private String serverIp;

    /**
     * 上级平台--服务端口
     */
    private int serverPort;

    /**
     * SIP认证用户名(默认使用设备国标编号)
     */
    private String username;

    /**
     * SIP认证密码
     */
    private String password;


    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport;




}
