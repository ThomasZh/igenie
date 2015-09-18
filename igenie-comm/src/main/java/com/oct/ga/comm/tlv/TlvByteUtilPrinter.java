package com.oct.ga.comm.tlv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TlvByteUtilPrinter {
	private static final Logger logger = LoggerFactory.getLogger(TlvByteUtilPrinter.class);

	private final static char[] HEX = "0123456789abcdef".toCharArray();
	private final static char[] OCT = "01234567".toCharArray();

	public static String getHex(byte b) {
		return HEX[(b >> 4) & 0x0f] + "" + HEX[b & 0x0f];
	}

	/**
	 * Encode a byte array into a hexidecimal String
	 * 
	 * @param b
	 * @return hex string
	 */
	public static String getHex(byte b[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(HEX[(b[i] >> 4) & 0x0f] + "" + HEX[b[i] & 0x0f]);
		return sb.toString();
	}

	public static void printToHex(byte b) {
		System.out.print("0x" + getHex(b));
	}

	public static String getOct(byte b) {
		return "0" + OCT[(b >> 6) & 03] + OCT[(b >> 3) & 07] + OCT[b & 07] + " ";
	}

	public static void printToOctal(byte b) {
		System.out.print(getOct(b));
	}

	/**
	 * Convert a byte[] array to readable string format. This makes the "hex"
	 * readable!
	 * 
	 * @return result String buffer in String format
	 * 
	 * @param in
	 *            byte[] buffer to convert to string format
	 */
	public static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;

		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4);
			// shift the bits down
			ch = (byte) (ch & 0x0F);
			// must do this is high order bit is on!
			out.append(HEX[(int) ch]); // convert the nibble to a String
			// Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(HEX[(int) ch]); // convert the nibble to a String
			// Character
			i++;
		}
		String rslt = new String(out);
		return rslt;
	}

	public static void hexDump(String title, byte in[]) {
		hexDump(title, in, in.length);
	}

	/**
	 * dumps size bytes of *data to stdout. Looks like: [0000] 75 6E 6B 6E 6F 77
	 * 6E 20 30 FF 00 00 00 00 39 00 unknown 0.....9. (in a single line of
	 * course)
	 */
	public static void hexDump(String title, byte buf[], int size) {
		StringBuffer sbTitle = new StringBuffer();
		sbTitle.append(title);
		sbTitle.append(" - HEXDUMP ");
		sbTitle.append(size);
		sbTitle.append(" bytes");
		logger.debug(sbTitle.toString());

		int i, j;
		for (i = 0; i < size; i += 16) {
			StringBuffer sb = new StringBuffer();
			sb.append(getHex(TlvByteUtil.int2Byte(i)) + "  ");
			for (j = 0; j < 16; j++)
				if (i + j < size)
					sb.append(getHex(buf[i + j]) + " ");
				else
					sb.append("   ");
			sb.append(" ");
			for (j = 0; j < 16; j++)
				if (i + j < size) {
					char ch = (char) buf[i + j];
					// The character is unprintable
					if (ch < 0x20 || ch >= 0x7F && ch < 0xA0 || ch > 0xFF)
						ch = '.';
					sb.append(ch);
				}
			logger.debug(sb.toString());
		}
	}
}
