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

		HttpClient httpClient = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("http://localhost/cscart/");

		System.out.println(request.getRequestLine());
		try {
			HttpResponse response = httpClient.execute(request);

			System.out.println(response.getStatusLine());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
