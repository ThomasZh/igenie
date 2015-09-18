package com.oct.ga.comm.codec;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.tlv.TlvObject;

public class TlvPackageDecoder
		extends CumulativeProtocolDecoder
{
	/**
	 * 杩�涓���规�����杩����锟�?锟芥�������癸拷? 1���褰����瀹瑰��濂芥�讹��杩����false锛������ョ�剁被��ユ�朵��锟�?��瑰��锟�?
	 * 2������瀹逛��澶���讹拷?瑕�涓�锟�?��瑰��杩���ョ�����瀹癸��姝ゆ�惰�����false锛�杩���风�讹拷? CumulativeProtocolDecoder
	 * 浼�灏����瀹规�捐��IoSession涓�锛�绛�涓�娆℃�ユ�版�����灏辫����ㄦ�艰�����浜ょ�����绫荤��doDecode
	 * 3���褰����瀹瑰����讹��杩����true锛����涓洪��瑕����灏������规�版��杩�琛�璇诲��锛���剁被浼�灏���╀�������版�����娆℃�ㄩ�����绫荤��doDecode
	 */
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception
	{
		if (in.remaining() > 0) {// �����版����讹��璇伙拷?6瀛������ゆ��娑������垮害
			in.mark();// ���璁板�����浣�缃�锛�浠ヤ究reset

			short tag = in.getShort();
			int length = in.getInt();
			logger.debug("from ioBuffer to tlv:(tag=" + tag + ", length=" + length + ")");

			// ���娉���垮害
			if (length < 0 || length > 65535) {
				logger.warn("Package out of length:" + length);
//				TlvByteUtilPrinter.hexDump("Package out of length:" + length, in.array());
				throw new UnsupportedEncodingException("Package out of length:" + length);
			}

			// 濡����娑�������瀹圭����垮害涓�澶������存�ヨ�����true
			if (length > in.remaining()) {// 濡����娑�������瀹逛��澶�锛�������缃�锛���稿��浜�涓�璇诲��size
				in.reset();
				return false;// ��ユ�舵�版�版��锛�浠ユ�煎�����瀹���存�版��
			} else {
				TlvObject pkg = new TlvObject();
				byte[] body = new byte[length];
				in.get(body, 0, length);
				// TlvByteUtilPrinter.hexDump("ioBuffer payload: ", body,
				// length);

				pkg.setTag(tag);
				pkg.setLength(length);
				pkg.setValue(body);

				out.write(pkg);

				if (in.remaining() > 0) {// 濡����璇诲�����瀹瑰��杩�绮�浜����锛�灏辫�╃�剁被���缁�淇轰��娆★��杩�琛�涓�涓�娆¤В锟�?
					return true;
				}
			}
		}
		return false;// 澶����������锛�璁╃�剁被杩�琛���ユ�朵��涓�锟�?
	}

	private final static Logger logger = LoggerFactory.getLogger(TlvPackageDecoder.class);

}