package com.oct.ga.comm.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class TlvPackageCodecFactory
		implements ProtocolCodecFactory
{
	public TlvPackageCodecFactory()
	{
		encoder = new TlvPackageEncoder();
		decoder = new TlvPackageDecoder();
	}

	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;

	@Override
	public ProtocolEncoder getEncoder(IoSession session)
			throws Exception
	{
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session)
			throws Exception
	{
		return decoder;
	}
}
