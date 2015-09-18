package com.oct.ga.cscart;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.ContentEncodingHttpClient;

public class CscartHttpCommand
{
	public static void main(String[] args)
			throws ClientProtocolException, IOException
	{
//		CloseableHttpClient httpclient = new ContentEncodingHttpClient();

		HttpHost targetHost = new HttpHost("182.92.71.66", 80, "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials("lwz7512@gmail.com", "bc131wPq6GYS148l21k1Ao81I89BafsR"));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);

		HttpGet httpget = new HttpGet("http://182.92.71.66/cscart/api/users/");
//		for (int i = 0; i < 3; i++) {
//			CloseableHttpResponse response = httpclient.execute(targetHost, httpget, context);
//			try {
//				HttpEntity entity = response.getEntity();
//				byte[] b = new byte[4096];
//				entity.getContent().read(b);
//				System.out.println(new String(b));
//			} finally {
//				response.close();
//			}
//		}
	}
}
