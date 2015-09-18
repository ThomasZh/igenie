package com.oct.ga.cscart;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientDemo {
	public static void main(String[] args) {

		// ����Ӧ����
		HttpClient httpClient = new DefaultHttpClient();

		// HTTP����
		HttpUriRequest request = new HttpGet("http://localhost/cscart/");

		// ��ӡ������Ϣ
		System.out.println(request.getRequestLine());
		try {
			// �������󣬷�����Ӧ
			HttpResponse response = httpClient.execute(request);

			// ��ӡ��Ӧ��Ϣ
			System.out.println(response.getStatusLine());
		} catch (ClientProtocolException e) {
			// Э�����
			e.printStackTrace();
		} catch (IOException e) {
			// �����쳣
			e.printStackTrace();
		}
	}
}
