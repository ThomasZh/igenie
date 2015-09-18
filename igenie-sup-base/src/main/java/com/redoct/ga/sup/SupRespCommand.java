package com.redoct.ga.sup;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.tlv.TlvObject;

public abstract class SupRespCommand
		implements SupCommand
{
	private short tag;
	private long sequence; // timestamp as it
	private short respState;

	@Override
	public SupRespCommand execute(ApplicationContext context)
			throws Exception
	{
		return null;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		return null;
	}

	@Override
	public SupCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		return null;
	}

	public short getTag()
	{
		return tag;
	}

	public void setTag(short tag)
	{
		this.tag = tag;
	}

	public long getSequence()
	{
		return sequence;
	}

	public void setSequence(long sequence)
	{
		this.sequence = sequence;
	}

	public short getRespState()
	{
		return respState;
	}

	public void setRespState(short respState)
	{
		this.respState = respState;
	}

}
