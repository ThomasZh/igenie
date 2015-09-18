package com.redoct.ga.sup;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.IoSession;
import org.springframework.context.ApplicationContext;

import com.oct.ga.comm.tlv.TlvObject;

public abstract class SupReqCommand
		implements SupCommand
{
	private static final AtomicInteger NEXT_SEQUENCE = new AtomicInteger();
	private short tag;
	private long sequence; // timestamp as it
	private int currentTimestamp;
	private IoSession ioSession;

	public SupReqCommand()
	{
		long timestamp = System.currentTimeMillis();
		this.setSequence(NEXT_SEQUENCE.incrementAndGet());
		this.setCurrentTimestamp((int) (timestamp / 1000));
	}

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

	public int getCurrentTimestamp()
	{
		return currentTimestamp;
	}

	public void setCurrentTimestamp(int currentTimestamp)
	{
		this.currentTimestamp = currentTimestamp;
	}

	public IoSession getIoSession()
	{
		return ioSession;
	}

	public void setIoSession(IoSession ioSession)
	{
		this.ioSession = ioSession;
	}
}
