package com.selecttvapp.youtube.http;

import android.util.Log;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;

public class Http implements HttpClient {
	public static InputStream get(URL url) throws IOException{
		return instance.getInputStream(url, HttpMethod.GET);
	}
    
	public static InputStream get(String requestURL) throws MalformedURLException, IOException {
		return get(new URL(requestURL));
	}

	private static URL logAndThen(URL url, HttpMethod method){
		Log.i("jp.mumoshu.http.Http", method.toString() + ": " + url.toString());
		return url;
	}

	private static HttpClient instance = new Http();

	public static void setHttpClient(HttpClient client){
		instance = client;
	}

	public InputStream getInputStream(URL url, HttpMethod method) throws IOException{
       	InputStream in;
    	HttpURLConnection http;

    	logAndThen(url, method);
    	http = (HttpURLConnection)url.openConnection();


		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient();

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

	//	http.setDefaultHostnameVerifier(hostnameVerifier);
		method.setTo(http);

		http.connect();
		in = http.getInputStream();
		return in;
	}
}
