package test.unit;

import com.oct.ga.comm.EcryptUtil;

public class Md5Test
{
	public static void main(String[] args)
	{
		String pwd = "1";
		String md5pwd = EcryptUtil.md5(pwd);
		System.out.println("md5pwd: " + md5pwd);

		String salt = "pdRYtInSk/";
		String ecryptedPwd = EcryptUtil.md5(md5pwd + EcryptUtil.md5(salt));
		System.out.println("ecryptedPwd: " + ecryptedPwd);
	}
}
