package com.selecttvapp.channels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.selecttvapp.RPC.JSONRPCException;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.network.LoaderWebserviceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by babin on 8/19/2017.
 */

public class LoaderWebServices {
    Context context;
    OkHttpClient client;
    private static LoaderWebServices INSTANCE;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public LoaderWebServices(Context context) {
        this.context = context;
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();
    }

    public static LoaderWebServices getInstance(@NonNull Context context) {
        if(context == null) {
            throw new IllegalArgumentException("getInstance() requires a valid context");
        } else {
            if(INSTANCE == null) {
                Class var1 = LoaderWebServices.class;
                synchronized(LoaderWebServices.class) {
                    if(INSTANCE == null) {
                        INSTANCE = new LoaderWebServices(context);
                    }
                }
            }

            return INSTANCE;
        }
    }
    public static LoaderWebServices getInstance(@NonNull Context context, String url) {
        if(context == null) {
            throw new IllegalArgumentException("getInstance() requires a valid context");
        } else {
            if(INSTANCE == null) {
                Class var1 = LoaderWebServices.class;
                synchronized(LoaderWebServices.class) {
                    if(INSTANCE == null) {
                        INSTANCE = new LoaderWebServices(context);
                    }
                }
            }
            return INSTANCE;
        }
    }

    public void getDemandMenuList(LoaderWebserviceInterface mLoaderWebServices, Object... params){
        Response response = null;
        try {
            JSONObject var7 = new JSONObject();
            JSONArray jsonParams = new JSONArray();
            for (int jsonRequest = 0; jsonRequest < params.length; ++jsonRequest) {
                jsonParams.put(params[jsonRequest]);
            }
            try {
                var7.put("id", UUID.randomUUID().hashCode());
                var7.put("method", "selecttvbox.ondemandSideMenu");
                var7.put("params", jsonParams);
            } catch (JSONException var6) {
                throw new JSONRPCException("Invalid JSON request", var6);
            }
            RequestBody body = RequestBody.create(JSON, var7.toString());
            Request request = new Request.Builder()
                    .url(JSONRPCAPI.JSON_URL)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String stringResponse=responseBody.string();
                Object json = new JSONTokener(stringResponse).nextValue();
                if (json instanceof JSONArray) {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }
                JSONObject jsonResponse = new JSONObject(stringResponse);
                if (jsonResponse.has("error")) {
                    Object jsonError = jsonResponse.get("error");
                    if (!jsonError.equals((Object) null)) {
                        throw new JSONRPCException(jsonResponse.get("error"));
                    } else {
                        mLoaderWebServices.ondataLoadingFailed();
                    }
                } else {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }

            } else {
                mLoaderWebServices.ondataLoadingFailed();
            }
        } catch (UnknownHostException e){
            mLoaderWebServices.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
            mLoaderWebServices.ondataLoadingFailed();
        }
    }

    public void getDemandSliderList(LoaderWebserviceInterface mLoaderWebServices, Object... params){
        Response response = null;
        try {
            JSONObject var7 = new JSONObject();
            JSONArray jsonParams = new JSONArray();
            for (int jsonRequest = 0; jsonRequest < params.length; ++jsonRequest) {
                jsonParams.put(params[jsonRequest]);
            }
            try {
                var7.put("id", UUID.randomUUID().hashCode());
                var7.put("method", "selecttvbox.ondemandSlider");
                var7.put("params", jsonParams);
            } catch (JSONException var6) {
                throw new JSONRPCException("Invalid JSON request", var6);
            }
            RequestBody body = RequestBody.create(JSON, var7.toString());
            Request request = new Request.Builder()
                    .url(JSONRPCAPI.JSON_URL)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String stringResponse=responseBody.string();
                Object json = new JSONTokener(stringResponse).nextValue();
                if (json instanceof JSONArray) {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }
                JSONObject jsonResponse = new JSONObject(stringResponse);
                if (jsonResponse.has("error")) {
                    Object jsonError = jsonResponse.get("error");
                    if (!jsonError.equals((Object) null)) {
                        throw new JSONRPCException(jsonResponse.get("error"));
                    } else {
                        mLoaderWebServices.ondataLoadingFailed();
                    }
                } else {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }

            } else {
                mLoaderWebServices.ondataLoadingFailed();
            }
        } catch (UnknownHostException e){
            mLoaderWebServices.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
            mLoaderWebServices.ondataLoadingFailed();
        }
    }

    public void getDemandCarousels(LoaderWebserviceInterface mLoaderWebServices, Object... params){
        Response response = null;
        try {
            JSONObject var7 = new JSONObject();
            JSONArray jsonParams = new JSONArray();
            for (int jsonRequest = 0; jsonRequest < params.length; ++jsonRequest) {
                jsonParams.put(params[jsonRequest]);
            }
            try {
                var7.put("id", UUID.randomUUID().hashCode());
                var7.put("method", "selecttvbox.ondemandSlider");
                var7.put("params", jsonParams);
            } catch (JSONException var6) {
                throw new JSONRPCException("Invalid JSON request", var6);
            }
            RequestBody body = RequestBody.create(JSON, var7.toString());
            Request request = new Request.Builder()
                    .url(JSONRPCAPI.JSON_URL)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String stringResponse=responseBody.string();
                Object json = new JSONTokener(stringResponse).nextValue();
                if (json instanceof JSONArray) {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }
                JSONObject jsonResponse = new JSONObject(stringResponse);
                if (jsonResponse.has("error")) {
                    Object jsonError = jsonResponse.get("error");
                    if (!jsonError.equals((Object) null)) {
                        throw new JSONRPCException(jsonResponse.get("error"));
                    } else {
                        mLoaderWebServices.ondataLoadingFailed();
                    }
                } else {
                    mLoaderWebServices.onresponseLoaded(stringResponse);
                }

            } else {
                mLoaderWebServices.ondataLoadingFailed();
            }
        } catch (UnknownHostException e){
            mLoaderWebServices.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
            mLoaderWebServices.ondataLoadingFailed();
        }
    }


    public void getResults(String method,LoaderWebserviceInterface mLoaderWebServices, Object... params){
        Response response = null;
        try {
            JSONObject var7 = new JSONObject();
            JSONArray jsonParams = new JSONArray();
            for (int jsonRequest = 0; jsonRequest < params.length; ++jsonRequest) {
                if(params!=null&&!TextUtils.isEmpty(params.toString()))
                jsonParams.put(params[jsonRequest]);
            }
            try {
                var7.put("id", UUID.randomUUID().hashCode());
                var7.put("method", method);
                if(params!=null&&!TextUtils.isEmpty(params.toString())&&params.length>0)
                var7.put("params", jsonParams);
            } catch (JSONException var6) {
                throw new JSONRPCException("Invalid JSON request", var6);
            }
            Log.d("query:::",":::"+var7.toString());
            RequestBody body = RequestBody.create(JSON, var7.toString());
            Request request = new Request.Builder()
                    .url(JSONRPCAPI.JSON_URL)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String stringResponse=responseBody.string();
                JSONObject objResponse=new JSONObject(stringResponse);
                if(objResponse.has("result")){
                    Object json =objResponse.get("result");
                    if (json instanceof JSONArray) {
                        mLoaderWebServices.onresponseLoaded(json.toString());
                    }else{
                        JSONObject jsonResponse = new JSONObject(json.toString());
                        if (jsonResponse.has("error")) {
                            Object jsonError = jsonResponse.get("error");
                            if (!jsonError.equals((Object) null)) {
                                throw new JSONRPCException(jsonResponse.get("error"));
                            } else {
                                mLoaderWebServices.ondataLoadingFailed();
                            }
                        } else {
                            mLoaderWebServices.onresponseLoaded(json.toString());
                        }

                    }
                }



            } else {
                mLoaderWebServices.ondataLoadingFailed();
            }
        } catch (UnknownHostException e){
            mLoaderWebServices.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
            mLoaderWebServices.ondataLoadingFailed();
        }
    }
    public void cancelAllRequests(){
        if(client.dispatcher()!=null){
            client.dispatcher().cancelAll();
        }
    }




}
