package com.redoct.ga.sup.sms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oct.ga.comm.LogErrorMessage;
import com.redoct.ga.sup.sms.SupSmsService;

public class SendCloudServiceImpl
		implements SupSmsService
{
	final static private String url = "http://sendcloud.sohu.com/smsapi/send";
	final static private String smsKey = "ALXKb8gBtLKT9QPyE3DQ7n3ZbOJEJctG";

	@Override
	public void sendVerificationCode(String phone, String ekey, String lang)
	{
		// ������
		Map<String, String> params = new HashMap<String, String>();
		params.put("smsUser", "tripc2csupport");
		if (lang.equals("en")) {
			params.put("templateId", "150");
		} else if (lang.equals("cn")) {
			params.put("templateId", "151");
		} else {
			params.put("templateId", "151");
		}
		params.put("phone", phone);
		params.put("vars", "{\"ekey\":\"" + ekey + "\"}");

		// �Բ�����������
		Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>()
		{
			@Override
			public int compare(String arg0, String arg1)
			{
				// ���Դ�Сд
				return arg0.compareToIgnoreCase(arg1);
			}
		});
		sortedMap.putAll(params);

		// ����ǩ��
		StringBuilder sb = new StringBuilder();
		sb.append(smsKey).append("&");
		for (String s : sortedMap.keySet()) {
			sb.append(String.format("%s=%s&", s, sortedMap.get(s)));
		}
		sb.append(smsKey);
		String sig = DigestUtils.md5Hex(sb.toString());

		// �����в�����ǩ����ӵ�post�������������
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();
		for (String s : sortedMap.keySet()) {
			postparams.add(new BasicNameValuePair(s, sortedMap.get(s)));
		}
		postparams.add(new BasicNameValuePair("signature", sig));

		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postparams, "utf8"));
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 100000);
			HttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			logger.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			logger.error(LogErrorMessage.getFullInfo(e));
		} finally {
			httpPost.releaseConnection();
		}
	}

	// ////////////////////////////////////////////////////

	private final static Logger logger = LoggerFactory.getLogger(SendCloudServiceImpl.class);

}
