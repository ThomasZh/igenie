package com.oct.ga.comm.tlv;

/**
 * 网络传输过程中经常使用的byte util
 * 
 * Copyright 1999-2001 by Java Service Network Community, KOREA. All rights
 * reserved. http://www.javaservice.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author WonYoung Lee, javaservice@hanmail.net
 */
public class TlvByteUtil {

	/**
	 * char to 1bytes=8bits
	 * 
	 * @param s
	 * @return bytes
	 */
	public static final byte[] char2Byte(char s) {
		byte[] dest = new byte[1];
		dest[0] = (byte) ((s >>> 8) & 0xff);
		return dest;
	}

	/**
	 * 1bytes=8bits to char
	 * 
	 * @param src
	 * @return char
	 */
	public static final char byte2Char(byte[] src)
			throws IllegalArgumentException {
		return byte2Char(src, 0);
	}

	/**
	 * 1bytes=8bits to char
	 * 
	 * @param src
	 * @param offset
	 * @return char
	 */
	public static final char byte2Char(byte[] src, int offset)
			throws IllegalArgumentException {
		if (src.length < offset + 1)
			throw new IllegalArgumentException("Illegal byte length: "
					+ src.length);

		return (char) ((src[offset] & 0xff) << 8);
	}

	/**
	 * short to 2bytes=16bits
	 * 
	 * @param s
	 * @return bytes
	 */
	public static final byte[] short2Byte(short s) {
		byte[] dest = new byte[2];
		dest[1] = (byte) (s & 0xff);
		dest[0] = (byte) ((s >>> 8) & 0xff);
		return dest;
	}

	/**
	 * short to 2bytes=16bits
	 * 
	 * @param dest
	 * @param offset
	 * @param s
	 * @return bytes
	 */
	public static final byte[] short2Byte(byte[] dest, int offset, short s)
			throws IllegalArgumentException {
		if (dest.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ dest.length);

		dest[offset] = (byte) ((s >>> 8) & 0xff);
		dest[offset + 1] = (byte) (s & 0xff);
		return dest;
	}

	/**
	 * 2bytes=16bits to short
	 * 
	 * @param src
	 * @return short
	 */
	public static final short byte2Short(byte[] src)
			throws IllegalArgumentException {
		return byte2Short(src, 0);
	}

	/**
	 * 2bytes=16bits to short
	 * 
	 * @param src
	 * @param offset
	 * @return short
	 */
	public static final short byte2Short(byte[] src, int offset)
			throws IllegalArgumentException {
		if (src.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ src.length);

		return (short) (((src[offset] & 0xff) << 8) | (src[offset + 1] & 0xff));
	}

	/**
	 * int to 4bytes=32bits
	 * 
	 * @param i
	 * @return bytes
	 */
	public static final byte[] int2Byte(int i) {
		byte[] dest = new byte[4];
		dest[3] = (byte) (i & 0xff);
		dest[2] = (byte) ((i >>> 8) & 0xff);
		dest[1] = (byte) ((i >>> 16) & 0xff);
		dest[0] = (byte) ((i >>> 24) & 0xff);
		return dest;
	}

	/**
	 * int to 4bytes=32bits
	 * 
	 * @param dest
	 * @param offset
	 * @param i
	 * @return bytes
	 */
	public static final byte[] int2Byte(byte[] dest, int offset, int i)
			throws IllegalArgumentException {
		if (dest.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ dest.length);

		dest[offset] = (byte) ((i >>> 24) & 0xff);
		dest[offset + 1] = (byte) ((i >>> 16) & 0xff);
		dest[offset + 2] = (byte) ((i >>> 8) & 0xff);
		dest[offset + 3] = (byte) (i & 0xff);
		return dest;
	}

	/**
	 * 4bytes=32bits to int
	 * 
	 * @param src
	 * @return int
	 */
	public static final int byte2Int(byte[] src)
			throws IllegalArgumentException {
		return byte2Int(src, 0);
	}

	/**
	 * 4bytes=32bits to int
	 * 
	 * @param src
	 * @param offset
	 * @return int
	 */
	public static final int byte2Int(byte[] src, int offset)
			throws IllegalArgumentException {
		if (src.length < offset + 4)
			throw new IllegalArgumentException("Illegal byte length: "
					+ src.length);

		return ((src[offset] & 0xff) << 24) | ((src[offset + 1] & 0xff) << 16)
				| ((src[offset + 2] & 0xff) << 8) | (src[offset + 3] & 0xff);
	}

	/**
	 * long to 8bytes=64bits
	 * 
	 * @param l
	 * @return bytes
	 */
	public static final byte[] long2Byte(long l) {
		byte[] dest = new byte[8];
		dest[7] = (byte) (l & 0xff);
		dest[6] = (byte) ((l >>> 8) & 0xff);
		dest[5] = (byte) ((l >>> 16) & 0xff);
		dest[4] = (byte) ((l >>> 24) & 0xff);
		dest[3] = (byte) ((l >>> 32) & 0xff);
		dest[2] = (byte) ((l >>> 40) & 0xff);
		dest[1] = (byte) ((l >>> 48) & 0xff);
		dest[0] = (byte) ((l >>> 56) & 0xff);
		return dest;
	}

	/**
	 * long to 8bytes=64bits
	 * 
	 * @param dest
	 * @param offset
	 * @param l
	 * @return bytes
	 */
	public static final byte[] long2Byte(byte[] dest, int offset, long l)
			throws IllegalArgumentException {
		if (dest.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ dest.length);

		int2Byte(dest, offset, (int) (l >>> 32));
		int2Byte(dest, offset + 4, (int) (l & 0xffffffffL));
		return dest;
	}

	/**
	 * 8bytes=64bits to long
	 * 
	 * @param src
	 * @return long
	 */
	public static final long byte2Long(byte[] src)
			throws IllegalArgumentException {
		return byte2Long(src, 0);
	}

	/**
	 * 8bytes=64bits to long
	 * 
	 * @param src
	 * @param offset
	 * @return long
	 */
	public static final long byte2Long(byte[] src, int offset)
			throws IllegalArgumentException {
		if (src.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ src.length);

		return ((long) byte2Int(src, offset) << 32)
				| ((long) byte2Int(src, offset + 4) & 0xffffffffL);
	}

	/**
	 * float to 8bytes=64bits
	 * 
	 * @param dest
	 * @param offset
	 * @param i
	 * @return bytes
	 */
	public static final byte[] float2Byte(byte[] dest, int offset, int i)
			throws IllegalArgumentException {
		if (dest.length < offset + 2)
			throw new IllegalArgumentException("Illegal byte length: "
					+ dest.length);

		dest[offset] = (byte) ((i >>> 24) & 0xff);
		dest[offset + 1] = (byte) ((i >>> 16) & 0xff);
		dest[offset + 2] = (byte) ((i >>> 8) & 0xff);
		dest[offset + 3] = (byte) (i & 0xff);
		return dest;
	}

	/**
	 * 判断一个字节数组与一个字符串的内容是否相同.
	 * 
	 * @param b
	 * @param s
	 * @return boolean
	 */
	public static final boolean isEquals(byte[] b, String s)
			throws IllegalArgumentException {
		if (b == null)
			throw new IllegalArgumentException(
					"Can not compares null bytes with a string.");
		if (s == null)
			throw new IllegalArgumentException(
					"Can not compares a bytes with null string.");

		int slen = s.length();
		if (b.length != slen)
			return false;
		for (int i = slen; i-- > 0;)
			if (b[i] != s.charAt(i))
				return false;
		return true;
	}

	/**
	 * 判断两个字节数组的内容是否相同.
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static final boolean isEquals(byte[] a, byte[] b)
			throws IllegalArgumentException {
		if (a == null)
			throw new IllegalArgumentException(
					"Can not compares null bytes with a bytes.");
		if (b == null)
			throw new IllegalArgumentException(
					"Can not compares a bytes with null bytes.");

		if (a.length != b.length)
			return false;
		for (int i = a.length; i-- > 0;)
			if (a[i] != b[i])
				return false;
		return true;
	}

	/**
	 * 合并两个字节数组.
	 * 
	 * @param a
	 * @param b
	 * @return bytes
	 */
	public static final byte[] merge(byte[] a, byte[] b) {
		int aLen = 0;
		int bLen = 0;
		if (a == null)
			aLen = 0;
		else
			aLen = a.length;
		if (b == null)
			bLen = 0;
		else
			bLen = b.length;

		int length = aLen + bLen;
		byte[] rs = new byte[length];

		copy(rs, a, 0);
		copy(rs, b, aLen);

		return rs;
	}

	/**
	 * 复制字节数组.
	 * 
	 * @param a
	 * @param b
	 * @param offset
	 */
	public static final void copy(byte[] a, byte[] b, int offset) {
		if (a == null)
			throw new IllegalArgumentException(
					"Can not copy bytes to null bytes.");

		if (b != null)
			// System.arrayCopy()方法不是用循环实现的，速度比用循环拷贝要快得多。
			System.arraycopy(b, 0, a, offset, b.length);
		// for (int i = offset, j = 0; j < b.length; i++, j++)
		// a[i] = b[j];
	}

	/**
	 * 截取字节数组.
	 * 
	 * @param b
	 * @param offset
	 * @return bytes
	 */
	public static final byte[] sub(byte[] b, int offset)
			throws IllegalArgumentException {
		if (b == null)
			throw new IllegalArgumentException(
					"Can not get sub bytes from null bytes.");

		return sub(b, offset, b.length);
	}

	/**
	 * 截取字节数组.
	 * 
	 * @param b
	 * @param start
	 * @param end
	 * @return bytes
	 */
	public static final byte[] sub(byte[] b, int start, int end)
			throws IllegalArgumentException {
		if (end > b.length)
			end = b.length;

		int length = end - start;
		if (length < 0)
			throw new IllegalArgumentException("Can not get(" + start + ", "
					+ end + ") sub bytes from a bytes.");

		if (b == null)
			throw new IllegalArgumentException(
					"Can not get sub bytes from null bytes.");

		byte[] rs = new byte[length];
		for (int i = 0; i < length; i++) {
			int index = i + start;
			if (index < b.length)
				rs[i] = b[index];
			else
				break;
		}
		return rs;
	}
}
