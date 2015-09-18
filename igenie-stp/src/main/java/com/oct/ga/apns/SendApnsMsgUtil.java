package com.oct.ga.apns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.oct.ga.stp.ApplicationContextUtil;
import com.oct.ga.stp.GlobalConfigurationVariables;

/**
 * Admin monitor use it.
 * 
 * @author thomas
 * 
 */
public class SendApnsMsgUtil
{
	public static void main(String argv[])
	{
		String messageTxt = argv[0]; // "192.168.1.101:13107";
		GlobalConfigurationVariables gcv = ApplicationContextUtil.getGlobalConfigurationVariables();
		String apnsCertificateDestination = gcv.getApnsCertificateDestination();
		String apnsCertificatePath = gcv.getApnsCertificatePath();
		String apnsCertificateCode = gcv.getApnsCertificateCode();

		ApnsService service;
		service = APNS.newService().withCert(apnsCertificatePath, apnsCertificateCode).withAppleDestination(true)
				.build();

		PayloadBuilder payloadBuilder = APNS.newPayload();
		payloadBuilder.badge(1);
		payloadBuilder.sound("default");
		String txt = "Server (" + messageTxt + ") was down!";

		payloadBuilder.localizedKey("MESSAGE_SENDTO_YOU");
		payloadBuilder.localizedArguments("Monitor", "Admin", txt);
		// payloadBuilder.customField("type", "101");
		// payloadBuilder.customField("id", "204");

		String payload = payloadBuilder.toString();
		logger.debug("Payload json: " + payload);

		// String token =
		// "6850be16 49690dfb 1ccabe2e f072db42 3771b8f9 21824245 6772371f 1f22afcc";
		// // ipad
		String token = "7b145767 733c8a76 43b7161f 0c70d153 5bb915c3 7abba04e 449feb7b e1370efc"; // iphone
		// String token =
		// "9f49ed12 6a9b1f1e 9726a8f4 df805404 916ee780 164db1cb 7b127b23 429860c8";
		// String token =
		// "f6f8c77c b26de349 de760b1d fe1c9f82 b5a53927 f66747e7 ed33f142 736173c2";
		logger.info("A message sent to (" + token + ") through by apns.");
		service.push(token, payload);
	}

	private final static Logger logger = LoggerFactory.getLogger(SendApnsMsgUtil.class);
}
