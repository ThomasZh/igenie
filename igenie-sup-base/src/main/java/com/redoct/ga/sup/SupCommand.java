package com.redoct.ga.sup;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.tlv.TlvObject;

public interface SupCommand
{
	/**
	 * handling the decoded java object, may be save in DB
	 * 
	 * @return base command
	 * @throws Exception
	 */
	public SupRespCommand execute(ApplicationContext context)
			throws Exception;

	/**
	 * encode command to send...
	 * 
	 * @return TlvObject
	 * @throws UnsupportedEncodingException
	 */
	public TlvObject encode()
			throws UnsupportedEncodingException;

	/**
	 * decode package to handle...
	 * 
	 * @param tlv
	 * @return command
	 * @throws UnsupportedEncodingException
	 */
	public SupCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException;

}
