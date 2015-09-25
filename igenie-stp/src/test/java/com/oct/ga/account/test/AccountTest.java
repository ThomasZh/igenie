package com.oct.ga.account.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.oct.ga.comm.cmd.account.SyncAccountBaseInfoReq;
import com.oct.ga.comm.cmd.account.SyncAccountBaseInfoResp;
import com.oct.ga.comm.tlv.TlvObject;

public class AccountTest {
	private static String sessionId = "5d8d6a93-404a-4adb-b3e6-f60b039a9597";
	private static String baseUri = "http://123.56.105.78:8080/legacy-api";
	private static final String HTTP_HEADER_SESSION_ID = "X-Session-Id";

	@Test
	public void testSyncAccountBase() throws ClientProtocolException, IOException {
		int sequence = 1;
		String accountId = "ecb87b58-a1bf-44c3-80b0-4c17d4375625";
		SyncAccountBaseInfoReq syncAccountBaseInfoReq = new SyncAccountBaseInfoReq(sequence, accountId);
		TlvObject requestTlvObject = syncAccountBaseInfoReq.encode();

		HttpPost httpPost = new HttpPost(baseUri);
		httpPost.addHeader(HTTP_HEADER_SESSION_ID, sessionId);
		HttpEntity httpEntity = new ByteArrayEntity(requestTlvObject.toBytes(), ContentType.APPLICATION_OCTET_STREAM);
		httpPost.setEntity(httpEntity);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		Assert.assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

		HttpEntity responseEntity = httpResponse.getEntity();
		Assert.assertEquals(ContentType.APPLICATION_OCTET_STREAM.toString(),
				responseEntity.getContentType().getValue());
		ByteBuffer headerBuffer = ByteBuffer.allocate(6);
		InputStream inputStream = responseEntity.getContent();
		int b;
		while (headerBuffer.hasRemaining()) {
			b = inputStream.read();
			Assert.assertTrue(b != -1);
			headerBuffer.put((byte) b);
		}

		headerBuffer.flip();
		TlvObject responseTlvObject = new TlvObject();
		responseTlvObject.setTag(headerBuffer.getShort());
		responseTlvObject.setLength(headerBuffer.getInt());

		ByteBuffer valueBuffer = ByteBuffer.allocate(responseTlvObject.getLength());
		while (valueBuffer.hasRemaining()) {
			b = inputStream.read();
			Assert.assertTrue(b != -1);
			valueBuffer.put((byte) b);
		}
		valueBuffer.flip();
		responseTlvObject.setValue(valueBuffer.array());
		SyncAccountBaseInfoResp syncAccountBaseInfoResp = new SyncAccountBaseInfoResp();
		syncAccountBaseInfoResp.decode(responseTlvObject);
		Assert.assertEquals(syncAccountBaseInfoReq.getSequence(), syncAccountBaseInfoResp.getSequence());
		Assert.assertEquals(syncAccountBaseInfoReq.getAccountId(), syncAccountBaseInfoResp.getAccountId());
		Assert.assertNotNull(syncAccountBaseInfoResp.getName());
		Assert.assertNotNull(syncAccountBaseInfoResp.getAvatarUrl());
	}
}
