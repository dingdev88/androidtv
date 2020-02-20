package com.selecttvapp.push;


import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class Server {

    final int DEFAULT_TIMEOUT = 20 * 1000;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public Server() {
        client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);

    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);

        Log.e("url", (getAbsoluteUrl(url)));
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            client.post(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
            Log.d("url", (getAbsoluteUrl(url)));
            Log.d("Post Data :", params.toString());

        } catch (Exception e) {

        }
    }

    public static void postSync(String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            SyncHttpClient client = new SyncHttpClient();
            client.post(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
            Log.d("url", (getAbsoluteUrl(url)));
            Log.d("Post Data :", params.toString());

        } catch (Exception e) {

        }
    }

    public static void cancel(Context c) {
        client.cancelRequests(c, true);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return ServerConfig.BASE_URL + relativeUrl;
    }
}
