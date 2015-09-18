package com.oct.ga.stp.http.club.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.stp.http.account.LoginRequest;
import com.oct.ga.stp.http.account.LoginResponse;
import com.oct.ga.stp.http.common.Header;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItineraryTest {

	private static String sessionId = "5d8d6a93-404a-4adb-b3e6-f60b039a9597";
	private static String baseUri = "http://123.56.105.78:8080";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private String activityId = "abc";

	// @BeforeClass
	public static void login() throws ClientProtocolException, IOException {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("18625219583");
		loginRequest.setMyDeviceId("357458042353671");
		loginRequest.setGateToken("3d82737b-7354-4137-a19c-d75a32b7e8d4");
		loginRequest.setApnsToken("1a4cc92a52ac3c4e5d0f851603e01a72");
		loginRequest.setOsVersion("android:HUAWEI P7-L00,REL,4.4.2");
		loginRequest.setPassword(EcryptUtil.md5("qaz"));// XXX
		String requestBody = OBJECT_MAPPER.writeValueAsString(loginRequest);
		StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);

		HttpPost httpPost = new HttpPost(baseUri + "/account/login");
		httpPost.setEntity(entity);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			LoginResponse loginResponse = OBJECT_MAPPER.readValue(getResponseBody(httpResponse), LoginResponse.class);
			sessionId = loginResponse.getSessionToken();
		} else {
			Assert.fail();
		}
	}

	@Test
	public void test1Create() throws ClientProtocolException, IOException {
		String requestBody = OBJECT_MAPPER.writeValueAsString(getTestObjects());

		HttpPost httpPost = new HttpPost(baseUri + "/activities/" + activityId + "/itineraries");
		httpPost.addHeader(Header.HTTP_HEADER_SESSION_ID, sessionId);
		HttpEntity httpEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		httpPost.setEntity(httpEntity);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		Assert.assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}

	@Test
	public void test2Read() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(baseUri + "/activities/" + activityId + "/itineraries");
		httpGet.addHeader(Header.HTTP_HEADER_SESSION_ID, sessionId);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		String body = getResponseBody(httpResponse);
		@SuppressWarnings("unchecked")
		Map<String, Object>[] testItineraries = getTestObjects();
		for (int i = 0; i < testItineraries.length; i++) {
			Map<String, Object> map = testItineraries[i];
			// the 'desc' field doesn't store empty string
			if ("".equals(map.get("desc"))) {
				map.remove("desc");
			}
		}
		JsonNode expected = OBJECT_MAPPER.valueToTree(testItineraries);
		Assert.assertEquals(expected, OBJECT_MAPPER.readTree(body));
	}

	@Test
	public void test3Delete() throws ClientProtocolException, IOException {
		HttpDelete httpDelete = new HttpDelete(baseUri + "/activities/" + activityId + "/itineraries");
		httpDelete.addHeader(Header.HTTP_HEADER_SESSION_ID, sessionId);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpDelete);
		Assert.assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}

	@Test
	public void test4Email() throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(baseUri + "/test/email");
		String email = "abc@qq.com";
		byte[] value = email.getBytes(Charset.forName("UTF-8"));
		ByteBuffer byteBuffer = ByteBuffer.allocate(value.length + 6);
		byteBuffer.putShort((short) 1);
		byteBuffer.putInt(value.length);
		byteBuffer.put(value);
		HttpEntity httpEntity = new ByteArrayEntity(byteBuffer.array(), ContentType.APPLICATION_OCTET_STREAM);
		httpPost.setEntity(httpEntity);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		System.out.println(getResponseBody(httpResponse));
	}

	@AfterClass
	public static void logout() {

	}

	@SuppressWarnings("rawtypes")
	private Map[] getTestObjects() {
		Map<String, Object> itineraryRequest1 = new HashMap<>();
		itineraryRequest1.put("beginTime", 1441095193);
		itineraryRequest1.put("endTime", 1441095293);
		itineraryRequest1.put("title", "第一天的行程");
		itineraryRequest1.put("location", "北京");
		itineraryRequest1.put("desc", "故宫");
		itineraryRequest1.put("imageUrls", new String[] { "http://image-url/image1", "http://image-url/image2" });
		itineraryRequest1.put("geoX", "100.0");
		itineraryRequest1.put("geoY", "120.0");

		Map<String, Object> itineraryRequest2 = new HashMap<>();
		itineraryRequest2.put("beginTime", 1441095193 + 24 * 60);
		itineraryRequest2.put("endTime", 1441095293 + 24 * 60);
		itineraryRequest2.put("title", "第二天的行程");
		itineraryRequest2.put("location", "上海");
		itineraryRequest2.put("desc", "");
		itineraryRequest2.put("imageUrls", new String[] { "http://image-url/image3", "http://image-url/image4" });
		itineraryRequest2.put("geoX", "150.0");
		itineraryRequest2.put("geoY", "300.0");

		Map<String, Object> itineraryRequest3 = new HashMap<>();
		itineraryRequest3.put("beginTime", 1441095193 + 24 * 60 * 2);
		itineraryRequest3.put("endTime", 1441095293 + 24 * 60 * 2);
		itineraryRequest3.put("title", "第三天的行程");
		itineraryRequest3.put("location", "香港");
		// itineraryRequest3.put("desc", null);
		itineraryRequest3.put("imageUrls", new String[] { "http://image-url/image5", "http://image-url/image6" });
		itineraryRequest3.put("geoX", "200.0");
		itineraryRequest3.put("geoY", "350.0");

		return new Map[] { itineraryRequest1, itineraryRequest2, itineraryRequest3 };
	}

	private static String getResponseBody(HttpResponse httpResponse) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		httpResponse.getEntity().writeTo(baos);
		byte[] bytes = baos.toByteArray();
		return new String(bytes, "UTF-8");
	}
}
