package com.oct.ga.comm.tlv;

/**
 * 简化的ASN.1协议解析器. VTLV: Version/Tag/Length/Value <li>
 * |-------|---|------|------------|</li> <li>|Version|Tag|Length|Value-------|</li>
 * <li>|1------|1--|4-----|Length------|</li> <li>
 * |------1|-12|----12|012345678901|</li> <li>|-------|---|------|------------|</li>
 * 
 * @author thomas.h.zhang
 */
public class TlvParser {
	/**
	 * 编码: TLVObject->byte[]
	 * 
	 * @param tlv
	 * @return byte[]
	 */
	public static byte[] encode(TlvObject tlv) {
		byte[] b = new byte[6 + tlv.getLength()];
		return tlv.toBytes(b, 0);
		// return tlv.toBytes();
	}

	// ///////////////////////////////////////////////////

	/**
	 * 解码: byte[]->TLVObject
	 * 
	 * @param b
	 * @return TlvObject
	 */
	public static TlvObject decodeHeader(byte[] b) {
		TlvObject tlv = new TlvObject();

		tlv.setTag(TlvByteUtil.byte2Short(b, 0));
		tlv.setLength(TlvByteUtil.byte2Int(b, TlvObject.TAG_LENGTH));

		return tlv;
	}

	/**
	 * 解码: byte[]->TLVObject
	 * 
	 * @param b
	 * @return TlvObject
	 */
	public static TlvObject decode(byte[] b) {
		TlvObject tlv = decodeHeader(b);
		byte[] value = TlvByteUtil.sub(b, TlvObject.HEADER_LENGTH);
		tlv.setValue(value);

		return tlv;
	}

	/**
	 * 解码: TLVObject.body(byte[])->TLVObject
	 * 
	 * @param tlv
	 * @param childCount child个数
	 */
	public static void decodeChildren(TlvObject tlv, int childCount)
			throws IllegalArgumentException {
		for (int i = 0; i < childCount; i++) {
			int leftLength = getLeftChildLenght(tlv, i);
			byte[] s1 = TlvByteUtil.sub(tlv.getValue(), leftLength
					+ TlvObject.TAG_LENGTH, leftLength
					+ TlvObject.HEADER_LENGTH);

			int childLength = TlvByteUtil.byte2Int(s1, 0);
			if (childLength < 0 || childLength > tlv.getLength())
				throw new IllegalArgumentException("Parse child[" + i
						+ "] length(" + childLength + ") error.");

			byte[] childStr = TlvByteUtil.sub(tlv.getValue(), leftLength,
					leftLength + TlvObject.HEADER_LENGTH + childLength);
			TlvObject childTLV = TlvParser.decode(childStr);
			tlv.add(childTLV);
		}
	}

	/**
	 * 取得第i个子TLVObject左侧所有TLVObject的字符串长度
	 * 
	 * @param tlv
	 * @param i
	 * @return int
	 */
	private static int getLeftChildLenght(TlvObject tlv, int index) {
		int rs = 0;
		for (int i = 0; i < index; i++) {
			TlvObject child = tlv.getChild(i);
			int length = child.getLength();
			rs += TlvObject.HEADER_LENGTH + length;// 6为2位Tag+4位Length
		}
		return rs;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TlvObject tlv1 = new TlvObject((byte) 1, (byte) 14,
				"PM-00-01-00-01".getBytes());
		TlvObject tlv2 = new TlvObject((byte) 2, (byte) 3, "0.5".getBytes());
		TlvObject tlv = new TlvObject((byte) 12, (byte) 23, null);
		tlv.add(tlv1);
		tlv.add(tlv2);

		byte[] b = TlvParser.encode(tlv);
		System.out.println("encode: [" + TlvByteUtilPrinter.getHex(b) + "]");
		TlvObject rs = TlvParser.decode(b);
		TlvParser.decodeChildren(rs, 2);
		rs.debugPrint();
	}

}
