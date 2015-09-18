package com.oct.ga.comm.domain.jqgrid;

import com.oct.ga.comm.domain.JsonBeanAdapter;

public class JqDevice
		extends JsonBeanAdapter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8029938693217511049L;
	private String device_id;
	private String os_version;
	private String apns_token;
	private int create_time;
	private int last_update_time;
	private short state;

	public String getDevice_id()
	{
		return device_id;
	}

	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}

	public String getOs_version()
	{
		return os_version;
	}

	public void setOs_version(String os_version)
	{
		this.os_version = os_version;
	}

	public String getApns_token()
	{
		return apns_token;
	}

	public void setApns_token(String apns_token)
	{
		this.apns_token = apns_token;
	}

	public int getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(int create_time)
	{
		this.create_time = create_time;
	}

	public int getLast_update_time()
	{
		return last_update_time;
	}

	public void setLast_update_time(int last_update_time)
	{
		this.last_update_time = last_update_time;
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}

}
