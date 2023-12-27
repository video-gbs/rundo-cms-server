package com.runjian.cascade.gb28181Module.gb28181.bean;

import lombok.Data;

/**
 * @author chenjialing
 */
@Data
public class DeviceChannel {

	/**
	 * 通道国标编号
	 */
	private String channelId;

	/**
	 * 通道名
	 */
	private String channelName;
	
	/**
	 * 生产厂商
	 */
	private String manufacturer;


	/**
	 * 父级id
	 */
	private String parentId;


	/**
	 * 云台类型
	 */
	private int ptzType;

	/**
	 * 在线/离线
	 * 1在线,0离线
	 * 默认在线
	 * 信令:
	 * <Status>ON</Status>
	 * <Status>OFF</Status>
	 * 遇到过NVR下的IPC下发信令可以推流， 但是 Status 响应 OFF
	 */
	private int status;

	/**
	 * 经度
	 */
	private double longitude;

	/**
	 * 纬度
	 */
	private double latitude;


}
