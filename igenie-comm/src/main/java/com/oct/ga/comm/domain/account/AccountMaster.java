package com.oct.ga.comm.domain.account;

public class AccountMaster
		extends AccountBasic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5298873852983564706L;
	private String email;
	private String phone;
	private String lang;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

}
