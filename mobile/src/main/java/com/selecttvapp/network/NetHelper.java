package com.selecttvapp.network;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class NetHelper {
	private static final String TAG = "NetHelper";

	public static JSONObject queryRESTURL(String url) {
		BufferedReader in = null;
		String page = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null)
				sb.append(line);
			page = sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "queryRESTurl", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e(TAG, "queryRESTurl", e);
				}
			}
		}
		try {
			return new JSONObject(page);
		} catch (Exception e) {
			Log.e(TAG, "NetHelper#JSONArray", e);
			return null;
		}
	}
}
