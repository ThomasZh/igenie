package com.oct.ga.comm.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvObject;

public class TlvPackageEncoder
		extends ProtocolEncoderAdapter
{
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception
	{
		TlvObject pkg = (TlvObject) message;
		byte[] b = pkg.toBytes();

		logger.info("from tlv:(tag=" + pkg.getTag() + ", length=" + pkg.getLength() + ") to ioBuffer");
		// TlvByteUtilPrinter.hexDump("tlv payload: ", b);

		IoBuffer buff = IoBuffer.allocate(TlvObject.HEADER_LENGTH + pkg.getLength());
		buff.setAutoExpand(true);
		if (b != null && b.length >= TlvObject.HEADER_LENGTH) {
			buff.put(b);
			buff.flip();
			out.write(buff);
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(TlvPackageEncoder.class);

}