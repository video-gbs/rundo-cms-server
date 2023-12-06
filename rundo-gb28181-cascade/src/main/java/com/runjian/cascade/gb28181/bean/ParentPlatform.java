package com.runjian.cascade.gb28181.bean;


import lombok.Data;

/**
 * @author lin
 */
@Data
public class ParentPlatform {

    /**
     * id
     */
    private Integer id;

    /**
     * 是否启用
     */
    private boolean enable;

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
    private String devicePort;

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
     * 心跳周期(秒)
     */
    private int keepTimeout;

    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport;

    /**
     * 字符集
     */
    private String characterSet;

    /**
     * 允许云台控制
     */
    private boolean ptz;

    /**
     * RTCP流保活
     */
    private boolean rtcp;

    /**
     * 在线状态
     */
    private boolean status;

    /**
     * 在线状态
     */
    private int channelCount;

    /**
     * 默认目录Id,自动添加的通道多放在这个目录下
     */
    private String catalogId;

    /**
     * 已被订阅目录信息
     */
    private boolean catalogSubscribe;

    /**
     * 已被订阅报警信息
     */
    private boolean alarmSubscribe;

    /**
     * 已被订阅移动位置信息
     */
    private boolean mobilePositionSubscribe;

    /**
     * 点播未推流的设备时是否使用redis通知拉起
     */
    private boolean startOfflinePush;

    /**
     * 目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8
     */
    private int catalogGroup;

    /**
     * 行政区划
     */
    private String administrativeDivision;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 树类型 国标规定了两种树的展现方式 行政区划 CivilCode 和业务分组:BusinessGroup
     */
    private String treeType = "BusinessGroup";

}
