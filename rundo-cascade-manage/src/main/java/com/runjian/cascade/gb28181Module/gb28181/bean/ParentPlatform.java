package com.runjian.cascade.gb28181Module.gb28181.bean;


import lombok.Data;

/**
 * @author lin
 */
@Data
public class ParentPlatform {

    /**
     * 名称
     */
    private String name;

    /**
     * SIP服务国标编码
     */
    private String serverGbId;

    /**
     * SIP服务国标域
     */
    private String serverGbDomain;

    /**
     * SIP服务IP
     */
    private String serverIp;

    /**
     * SIP服务端口
     */
    private int serverPort;

    /**
     * 设备国标编号
     */
    private String deviceGbId;

    /**
     * 设备ip
     */
    private String deviceIp;

    /**
     * 设备端口
     */
    private Integer devicePort;

    /**
     * SIP认证用户名(默认使用设备国标编号)
     */
    private String username;

    /**
     * SIP认证密码
     */
    private String password;

    /**
     * 注册周期 (秒)
     */
    private int expires;


    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport = "UDP";

    /**
     * 字符集
     */
    private String characterSet;




}
