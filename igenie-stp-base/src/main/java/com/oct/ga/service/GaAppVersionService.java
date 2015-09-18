package com.oct.ga.service;

public interface GaAppVersionService
{
	/**
	 * @return NO,ADVICE,MUST
	 */
	public short queryUpgradePriority(String clientVersion);
}
