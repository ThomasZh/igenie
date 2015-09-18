package com.oct.ga.comm.cmd;

import java.io.UnsupportedEncodingException;

import com.oct.ga.comm.tlv.TlvObject;

public abstract class StpCommand
		implements Command
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5910871267479764857L;
	private short tag;
	protected int sequence; // timestamp as it

	@Override
	public StpCommand execute()
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TlvObject encode()
			throws UnsupportedEncodingException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StpCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		// TODO Auto-generated method stub
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

	public int getSequence()
	{
		return sequence;
	}

	public void setSequence(int sequence)
	{
		this.sequence = sequence;
	}

}
