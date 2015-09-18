package com.oct.ga.comm;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;

public class EcryptUtil
{
	public static String md5ChatId(String accountId, String accountId2)
	{
		if (accountId.compareTo(accountId2) > 0) {
			return md5(accountId2 + accountId);
		} else {
			return md5(accountId + accountId2);
		}
	}

	public static String salt()
	{
		byte[] salt = new byte[10];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt); // 涓�浜轰��绮����������
		// FIXME, use apache base64 encoder instead of sun base64encoder
		// lwz7512@2014/09/15
		String outp = Base64.encodeBase64String(salt);
		return outp.substring(0, 10);
	}

	public static String md5(String str)
	{
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5cscart(String unecryptedPassword, String salt)
	{
		if (salt == null) {
			return EcryptUtil.md5(unecryptedPassword);
		} else {
			return EcryptUtil.md5(EcryptUtil.md5(unecryptedPassword) + EcryptUtil.md5(salt));
		}
	}

	public static String encryptionWithSalt(String unecrypted, String salt)
	{
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update((unecrypted + salt).getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String encryptedPassword = (new BigInteger(messageDigest.digest())).toString(16);
		return encryptedPassword;
	}

	public static String hsPassword(String oPassword, int intsalt)
	{
		byte[] salt = intToByteArray(intsalt);
		byte[] bPassword = oPassword.getBytes();
		String hsPassword = hashSaltPassword(bPassword, salt);
		return hsPassword;
	}

	public static String hashSaltPassword(byte[] bPassword, byte[] salt)
	{
		String hsPassword = "";
		byte[] saltedPassword = new byte[salt.length + bPassword.length];
		try {
			System.arraycopy(salt, 0, saltedPassword, salt.length, 0);
			System.arraycopy(bPassword, 0, saltedPassword, salt.length, bPassword.length);
			MessageDigest m = MessageDigest.getInstance("SHA-1");
			byte[] hashedAndSaltedPassword = m.digest(saltedPassword);
			hsPassword = hashedAndSaltedPassword.toString();
		} catch (Exception ex) {
			// logger.error("HashSaltPassword() " + ex);
			ex.printStackTrace();
		}
		return hsPassword;
	}

	private static byte[] intToByteArray(int value)
	{
		BigInteger big = new BigInteger(Integer.toString(value));
		return big.toByteArray();
	}

	private static String getSecurePassword(String passwordToHash, String salt)
	{
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(salt.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	// Add salt
	private static String getSalt()
			throws NoSuchAlgorithmException, NoSuchProviderException
	{
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

		// Create array for salt
		byte[] salt = new byte[16];
		// Get a random salt
		sr.nextBytes(salt);
		// return salt
		return salt.toString();
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b)
	{
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	private static final String HEX_NUMS_STR = "0123456789ABCDEF";
	private static final Integer SALT_LENGTH = 10;

	/**
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptedPwd(String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		byte[] pwd = null;
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		salt = "&$5c~Rpljn".getBytes();

		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5");
		md.update(salt);
		md.update(password.getBytes("UTF-8"));
		byte[] digest = md.digest();

		pwd = new byte[digest.length + SALT_LENGTH];
		System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
		System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
		return byteToHexString(pwd);
		// return new String(pwd);
	}

	public static void main(String arg[])
			throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException
	{
		System.out.println(md5("i8aegg"));

		System.out.println(salt());
		String unecrypted = "i8aegg";
		String salt = "&$5c~Rpljn";
		String encrypted = null;
		System.out.println(EcryptUtil.md5(unecrypted + salt));

		System.out.println(EcryptUtil.md5(EcryptUtil.md5(unecrypted) + EcryptUtil.md5(salt)));

		// '078884af667cb44fa32890c27fbe82ce'
		encrypted = EcryptUtil.encryptionWithSalt(unecrypted, salt);
		System.out.println(encrypted);

		encrypted = EcryptUtil.hashSaltPassword(unecrypted.getBytes(), salt.getBytes());
		System.out.println(encrypted);

		String securePassword = EcryptUtil.getSecurePassword(unecrypted, salt);
		System.out.println(securePassword); // Prints
											// 83ee5baeea20b6c21635e4ea67847f66

		String regeneratedPassowrdToVerify = EcryptUtil.getSecurePassword(unecrypted, salt);
		System.out.println(regeneratedPassowrdToVerify); // Prints
															// 83ee5baeea20b6c21635e4ea67847f66

		// validPassword
		encrypted = EcryptUtil.getEncryptedPwd(unecrypted);
		System.out.println(encrypted);
	}
}
