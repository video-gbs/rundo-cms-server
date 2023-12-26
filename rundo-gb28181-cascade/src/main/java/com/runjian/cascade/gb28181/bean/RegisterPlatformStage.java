package com.runjian.cascade.gb28181.bean;


import lombok.Data;

/**
 * @author lin
 */
@Data
public class RegisterPlatformStage {



    /**
     * 平台名称
     */
    private String callId;

    /**
     * 平台Id
     */
    private String platformId;

    /**
     * 是否时注册，false为注销
     */
    private boolean register;





}
