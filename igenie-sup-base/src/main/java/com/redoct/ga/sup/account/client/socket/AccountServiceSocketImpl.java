package com.redoct.ga.sup.account.client.socket;

import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.account.AccountBasic;
import com.oct.ga.comm.domain.account.AccountDetail;
import com.oct.ga.comm.domain.account.AccountMaster;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.redoct.ga.sup.account.SupAccountService;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.ApplyVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.CreateAccountReq;
import com.redoct.ga.sup.account.cmd.CreateAccountResp;
import com.redoct.ga.sup.account.cmd.CreateLoginReq;
import com.redoct.ga.sup.account.cmd.CreateLoginResp;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyReq;
import com.redoct.ga.sup.account.cmd.CreateLostPwdEkeyResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginReq;
import com.redoct.ga.sup.account.cmd.ModifyAccountId4LoginResp;
import com.redoct.ga.sup.account.cmd.ModifyPwdReq;
import com.redoct.ga.sup.account.cmd.ModifyPwdResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoByLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountBasicInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsReq;
import com.redoct.ga.sup.account.cmd.QueryAccountDetailsResp;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoReq;
import com.redoct.ga.sup.account.cmd.QueryAccountMasterInfoResp;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicReq;
import com.redoct.ga.sup.account.cmd.QueryAllAccountBasicResp;
import com.redoct.ga.sup.account.cmd.QueryLoginNameReq;
import com.redoct.ga.sup.account.cmd.QueryLoginNameResp;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoReq;
import com.redoct.ga.sup.account.cmd.QueryLostPwdEkeyInfoResp;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeReq;
import com.redoct.ga.sup.account.cmd.QueryVerificationCodeResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginExistResp;
import com.redoct.ga.sup.account.cmd.VerifyLoginReq;
import com.redoct.ga.sup.account.cmd.VerifyLoginResp;
import com.redoct.ga.sup.account.domain.LostPwdEkey;
import com.redoct.ga.sup.account.domain.VerificationCode;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;

public class AccountServiceSocketImpl
		implements SupAccountService
{
	@Override
	public boolean verifyExist(short loginType, String loginName)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				VerifyLoginExistReq reqCmd = new VerifyLoginExistReq(loginType, loginName);
				logger.info("request cmd: " + reqCmd.getTag());

				VerifyLoginExistResp respCmd = (VerifyLoginExistResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return true;
					else
						return false;
				} else
					return false;
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public String verifyLogin(short loginType, String loginName, String md5pwd)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				VerifyLoginReq reqCmd = new VerifyLoginReq(loginType, loginName, md5pwd);
				logger.info("request cmd: " + reqCmd.getTag());

				VerifyLoginResp respCmd = (VerifyLoginResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					if (respCmd.getRespState() == ErrorCode.SUCCESS) {
						return respCmd.getAccountId();
					} else if (respCmd.getRespState() == ErrorCode.LOGIN_WRONG_PWD) {
						logger.warn("logintype(" + loginType + ") loginname(" + loginName
								+ ") & password pairs are wrong.");
						return null;
						// throw new
						// SupSocketException("loginname & password pairs are wrong.");
					} else if (respCmd.getRespState() == ErrorCode.UNKNOWN_FAILURE) {
						throw new SupSocketException("unknow failure.");
					} else {
						throw new SupSocketException("unknow failure.");
					}
				} else {
					throw new SupSocketException("unknow failure.");
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void resetPwd(short loginType, String loginName, String md5pwd, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ModifyPwdReq reqCmd = new ModifyPwdReq(loginType, loginName, md5pwd);
				logger.info("request cmd: " + reqCmd.getTag());

				ModifyPwdResp respCmd = (ModifyPwdResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public String createAccount(String nickname, String avatarUrl, String desc, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				CreateAccountReq reqCmd = new CreateAccountReq(nickname, avatarUrl, desc);
				logger.info("request cmd: " + reqCmd.getTag());

				CreateAccountResp respCmd = (CreateAccountResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					String accountId = respCmd.getAccountId();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return accountId;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void modifyAccountBasicInfo(AccountBasic account, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ModifyAccountBasicInfoReq reqCmd = new ModifyAccountBasicInfoReq(account);
				logger.info("request cmd: " + reqCmd.getTag());

				ModifyAccountBasicInfoResp respCmd = (ModifyAccountBasicInfoResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void createLogin(String accountId, short loginType, String loginName, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				CreateLoginReq reqCmd = new CreateLoginReq(accountId, loginType, loginName);
				logger.info("request cmd: " + reqCmd.getTag());

				CreateLoginResp respCmd = (CreateLoginResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public void modifyAccountId4Login(String accountId, short loginType, String loginName, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ModifyAccountId4LoginReq reqCmd = new ModifyAccountId4LoginReq(accountId, loginType, loginName);
				logger.info("request cmd: " + reqCmd.getTag());

				ModifyAccountId4LoginResp respCmd = (ModifyAccountId4LoginResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
				}
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public String createEkey(short loginType, String loginName, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				CreateLostPwdEkeyReq reqCmd = new CreateLostPwdEkeyReq(loginType, loginName);
				logger.info("request cmd: " + reqCmd.getTag());

				CreateLostPwdEkeyResp respCmd = (CreateLostPwdEkeyResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					String ekey = respCmd.getEkey();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return ekey;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public LostPwdEkey queryEkey(String ekey)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryLostPwdEkeyInfoReq reqCmd = new QueryLostPwdEkeyInfoReq(ekey);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryLostPwdEkeyInfoResp respCmd = (QueryLostPwdEkeyInfoResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					LostPwdEkey ekeyInfo = respCmd.getEkeyInfo();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return ekeyInfo;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public AccountBasic queryAccount(String accountId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryAccountBasicInfoReq reqCmd = new QueryAccountBasicInfoReq(accountId);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryAccountBasicInfoResp respCmd = (QueryAccountBasicInfoResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					AccountBasic account = respCmd.getAccount();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return account;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public AccountBasic queryAccount(short loginType, String loginName)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryAccountBasicInfoByLoginNameReq reqCmd = new QueryAccountBasicInfoByLoginNameReq(loginType,
						loginName);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryAccountBasicInfoByLoginNameResp respCmd = (QueryAccountBasicInfoByLoginNameResp) socketClient
						.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					AccountBasic account = respCmd.getAccount();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return account;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public AccountMaster queryAccountMaster(String accountId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryAccountMasterInfoReq reqCmd = new QueryAccountMasterInfoReq(accountId);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryAccountMasterInfoResp respCmd = (QueryAccountMasterInfoResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					AccountMaster account = respCmd.getAccount();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return account;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
		}
	}

	@Override
	public String queryLoginName(String accountId, short loginType)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryLoginNameReq reqCmd = new QueryLoginNameReq(accountId, loginType);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryLoginNameResp respCmd = (QueryLoginNameResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					String loginName = respCmd.getLoginName();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return loginName;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public String applyVerificationCode(short verificationType, String deviceId, String phone, int timestamp)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				ApplyVerificationCodeReq reqCmd = new ApplyVerificationCodeReq(verificationType, deviceId, phone);
				logger.info("request cmd: " + reqCmd.getTag());

				ApplyVerificationCodeResp respCmd = (ApplyVerificationCodeResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					String ekey = respCmd.getEkey();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return ekey;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public VerificationCode queryVerificationCode(short verificationType, String deviceId)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryVerificationCodeReq reqCmd = new QueryVerificationCodeReq(verificationType, deviceId);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryVerificationCodeResp respCmd = (QueryVerificationCodeResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					VerificationCode code = respCmd.getCode();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return code;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public List<AccountBasic> queryAllAccountBasic()
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryAllAccountBasicReq reqCmd = new QueryAllAccountBasicReq();
				logger.info("request cmd: " + reqCmd.getTag());

				QueryAllAccountBasicResp respCmd = (QueryAllAccountBasicResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					List<AccountBasic> array = respCmd.getArray();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return array;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	@Override
	public List<AccountDetail> queryAccountDetails(List<String> ids)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_ACCOUNT_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				QueryAccountDetailsReq reqCmd = new QueryAccountDetailsReq(ids);
				logger.info("request cmd: " + reqCmd.getTag());

				QueryAccountDetailsResp respCmd = (QueryAccountDetailsResp) socketClient.send(addr, reqCmd);
				if (respCmd != null) {
					logger.info("response cmd: " + respCmd.getTag());
					logger.debug("response state: " + respCmd.getRespState());
					List<AccountDetail> array = respCmd.getAccounts();
					if (respCmd.getRespState() == ErrorCode.SUCCESS)
						return array;
					else
						throw new SupSocketException("unknow failure.");
				} else
					throw new SupSocketException("unknow failure.");
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure: ", e);
		}
	}

	// /////////////////////////////////////////////////////

	private SupSocketClient socketClient;

	public SupSocketClient getSocketClient()
	{
		return socketClient;
	}

	public void setSocketClient(SupSocketClient socketClient)
	{
		this.socketClient = socketClient;
	}

	private final static Logger logger = LoggerFactory.getLogger(AccountServiceSocketImpl.class);

}
