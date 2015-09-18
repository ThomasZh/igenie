package com.oct.ga.stp.utility;

/**
 * 鍗佸叚杩涘埗涓庡崄杩涘埗涔嬮棿鐨勮浆鎹�
 * @author 宕旂礌寮�
 * @version 1.0
 * @since
 */
public class HexStringUtil {

	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	// 浠庡瓧鑺傛暟缁勫埌鍗佸叚杩涘埗瀛楃涓茶浆鎹�
	public static String Bytes2HexString(byte[] b) {
		byte[] buff = new byte[3 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[3 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[3 * i + 1] = hex[b[i] & 0x0f];
			buff[3 * i + 2] = 45;
		}
		String re = new String(buff);
		return re.replace("-", " ");
	}

	// 浠庡崄鍏繘鍒跺瓧绗︿覆鍒板瓧鑺傛暟缁勮浆鎹�
	public static byte[] HexString2Bytes(String hexstr) {
		hexstr = hexstr.replace(" ", "");
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

}
