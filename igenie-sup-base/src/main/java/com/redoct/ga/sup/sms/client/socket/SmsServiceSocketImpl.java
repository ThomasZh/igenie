package com.redoct.ga.sup.sms.client.socket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.GenericSingleton;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.SupSocketException;
import com.oct.ga.comm.domain.gatekeeper.SupServerInfo;
import com.redoct.ga.sup.client.socket.SupSocketClient;
import com.redoct.ga.sup.client.socket.SupSocketConnectionManager;
import com.redoct.ga.sup.sms.SupSmsService;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeReq;
import com.redoct.ga.sup.sms.cmd.SendVerificationCodeResp;

public class SmsServiceSocketImpl
		implements SupSmsService
{
	@Override
	public void sendVerificationCode(String phone, String ekey, String lang)
			throws SupSocketException
	{
		try {
			SupSocketConnectionManager socketConnectionManager = GenericSingleton
					.getInstance(SupSocketConnectionManager.class);
			SupServerInfo supServer = socketConnectionManager.next(GlobalArgs.SUP_TYPE_SMS_SERVER);

			if (supServer == null) {
				throw new SupSocketException("sup account server is not avilable");
			} else {
				InetSocketAddress addr = new InetSocketAddress(supServer.getIp(), supServer.getPort());

				SendVerificationCodeReq reqCmd = new SendVerificationCodeReq(phone, ekey, lang);

				SendVerificationCodeResp respCmd = (SendVerificationCodeResp) socketClient.send(addr, reqCmd);
				if (respCmd != null)
					logger.debug("response state: " + respCmd.getRespState());
			}
		} catch (Exception e) {
			throw new SupSocketException("unknow failure.", e);
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

	private final static Logger logger = LoggerFactory.getLogger(SmsServiceSocketImpl.class);

}
