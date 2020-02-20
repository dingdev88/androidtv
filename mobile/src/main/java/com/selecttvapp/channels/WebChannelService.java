package com.selecttvapp.channels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.selecttvapp.prefrence.AppPrefrence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by babin on 7/3/2017.
 */

public class WebChannelService {
    private Context context;
    OkHttpClient client;
    private static WebChannelService INSTANCE;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    int cacheSize = 10 * 1024 * 1024;


    public WebChannelService(Context context) {
        this.context = context;
        Cache cache = new Cache(new File(context.getApplicationContext().getCacheDir(),"Channels"), cacheSize);
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .writeTimeout(60, TimeUnit.SECONDS).build();

    }

    public static WebChannelService getInstance(@NonNull Context context) {
        if(context == null) {
            throw new IllegalArgumentException("getInstance() requires a valid context");
        } else {
            if(INSTANCE == null) {
                Class var1 = WebChannelService.class;
                synchronized(WebChannelService.class) {
                    if(INSTANCE == null) {
                        INSTANCE = new WebChannelService(context);
                    }
                }
            }

            return INSTANCE;
        }
    }
    public static WebChannelService getInstance(@NonNull Context context, String url) {
        if(context == null) {
            throw new IllegalArgumentException("getInstance() requires a valid context");
        } else {
            if(INSTANCE == null) {
                Class var1 = WebChannelService.class;
                synchronized(WebChannelService.class) {
                    if(INSTANCE == null) {
                        INSTANCE = new WebChannelService(context);
                    }
                }
            }

            return INSTANCE;
        }
    }
    public void clearCache(){
        try {
            if(client!=null){
                client.cache().delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadChannelsCategories(CategoryListener mCategoryListener){

        String baseUrl=(AppPrefrence.getInstance().getAndroidChannel());
        String url = baseUrl+WebserviceChannelAPI.LIVE_CATEGORIES;
        Log.e("--ChannelsCategories--","URl----"+url);
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                JSONArray jsonResponse = new JSONArray(responseBody.string());
                Log.e("--ChannelsCategories--","Response----"+jsonResponse);
                ArrayList<ChannelCategoryList> categorylist= ChannelDataparser.parseCategories(jsonResponse);
                mCategoryListener.onCategoriesLoaded(categorylist);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error_code", 400);
                mCategoryListener.onLoadingFailed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mCategoryListener.onLoadingFailed();
        }



    }

    public void loadChannelsDtaa(String categorySlug, boolean isDialog, ChannelApiListener mChannelApiListener){
        if(client.dispatcher()!=null){
            client.dispatcher().cancelAll();
        }
        String baseUrl=(AppPrefrence.getInstance().getAndroidChannel());
        String url = baseUrl+WebserviceChannelAPI.LIVE_SELECTED_CATEGORIES + categorySlug+ WebserviceChannelAPI.NESTED_CHANNELS_METHOD;
        Log.e("--loadChannelsDtaa--","URl----"+url);
        Response response = null;
        ResponseBody responseBody=null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .cacheControl(new CacheControl.Builder().build())
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                if(response.cacheResponse()!=null){
                    responseBody=response.body();
                    Log.e("--cached--","Response----"+response.cacheResponse().toString());
                }else{
                    responseBody=response.body();
                    Log.e("--network--","Response----");
                }
                JSONArray jsonResponse = new JSONArray(responseBody.string());
                Log.e("--loadChannelsDtaa--","Response----"+jsonResponse);
                ArrayList<ChannelScheduler> channelList= ChannelDataparser.parseChannels(categorySlug,jsonResponse);
                mChannelApiListener.onChannelListLoaded(categorySlug,channelList,isDialog);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error_code", 400);
            }
        } catch (UnknownHostException e){
            mChannelApiListener.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadProgramData(String categorySlug, String slug, ProgramApiListener mProgramApiListener){
        for(Call call : client.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(slug))
                return;
        }
        String baseUrl=(AppPrefrence.getInstance().getAndroidChannel());
        String url = baseUrl+WebserviceChannelAPI.PROGRAMS  + slug+ WebserviceChannelAPI.PROGRAMS_METHOD;
        Log.e("--loadProgramData--","URl----"+url);
        Response response = null;
        ResponseBody responseBody=null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .tag(slug)
                    .cacheControl(new CacheControl.Builder().build())
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                if(response.cacheResponse()!=null){
                    responseBody=response.body();
                    Log.e("--cached--","Response----"+response.cacheResponse());
                }else{
                    responseBody=response.body();
                    Log.e("--network--","Response----"+response.networkResponse());
                }
                String jsonResponse = responseBody.string();
                //Log.e("--loadProgramData--","Response----"+jsonResponse);
                Programs mPrograms= ChannelDataparser.parseProgram(jsonResponse);
                mProgramApiListener.onProgramsLoaded(categorySlug,slug,mPrograms);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error_code", 400);
                mProgramApiListener.onLoadingFailed();
            }
        } catch (UnknownHostException e){
            mProgramApiListener.onNetworkError();
        }
        catch (Exception e) {
            mProgramApiListener.onLoadingFailed();
            e.printStackTrace();
        }
    }
    public void loadProgramData(String categorySlug, String slug, int noOfMoves,ProgramApiListener mProgramApiListener){
        for(Call call : client.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(slug))
                return;
        }
        String baseUrl=(AppPrefrence.getInstance().getAndroidChannel());
        String url = baseUrl+WebserviceChannelAPI.PROGRAMS  + slug+ WebserviceChannelAPI.PROGRAMS_METHOD;
        Log.e("--loadProgramData--","URl----"+url);
        Response response = null;
        ResponseBody responseBody=null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .tag(slug)
                    .cacheControl(new CacheControl.Builder().build())
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                if(response.cacheResponse()!=null){
                    responseBody=response.body();
                    Log.e("--cached--","Response----"+response.cacheResponse());
                }else{
                    responseBody=response.body();
                    Log.e("--network--","Response----"+response.networkResponse());
                }
                String jsonResponse = responseBody.string();
                //Log.e("--loadProgramData--","Response----"+jsonResponse);
                Programs mPrograms= ChannelDataparser.parseProgram(jsonResponse);
                if (mPrograms!=null) {
                    long duration = 0L;
                    for (int i = 0; i < mPrograms.getProgramlist().size(); i++) {
                        long video_duration = ChannelUtils.getDuration(mPrograms.getProgramlist().get(i).getDuration());
                        duration = duration + video_duration;
                    }
                    long first_playlist_starting_point = 0L;
                    long first_playlist_duration = 0L;
                    for (int i = 0; i < mPrograms.getProgramlist().size(); i++) {
                        ProgramList mProgramList=mPrograms.getProgramlist().get(i);

                        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                        long created_date = 0L;
                        DateFormat formatter = new SimpleDateFormat(inputPattern, Locale.getDefault());
                        try {
                            Date date = formatter.parse(mPrograms.getStarted());
                            created_date = date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long nowAsPerDeviceTimeZone = 0;
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
                        long video_duration = ChannelUtils.getDuration(mPrograms.getProgramlist().get(i).getDuration());
                        nowAsPerDeviceTimeZone= ChannelUtils.GetUnixTime();
                        if (i == 0) {
                            first_playlist_starting_point = created_date + duration * ((nowAsPerDeviceTimeZone - created_date) / duration);
                            mProgramList.setStart_at(ChannelUtils.getDate(first_playlist_starting_point));
                            mProgramList.setEnd_at(ChannelUtils.getDate(first_playlist_starting_point + video_duration));
                            first_playlist_duration = video_duration;

                        } else {

                            long second_playlist_item_starting_point = first_playlist_starting_point + first_playlist_duration;
                            mProgramList.setStart_at(ChannelUtils.getDate(second_playlist_item_starting_point));
                            mProgramList.setEnd_at(ChannelUtils.getDate(second_playlist_item_starting_point + video_duration));
                            first_playlist_duration = video_duration;
                            first_playlist_starting_point = second_playlist_item_starting_point;

                        }
                        mProgramList.setScroll_dealay((int) video_duration / noOfMoves);

                    }
                }
                mProgramApiListener.onProgramsLoaded(categorySlug,slug,mPrograms);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error_code", 400);
                mProgramApiListener.onLoadingFailed();
            }
        } catch (UnknownHostException e){
            mProgramApiListener.onNetworkError();
        }
        catch (Exception e) {
            mProgramApiListener.onLoadingFailed();
            e.printStackTrace();
        }
    }
    public void loadSteamData(String categorySlug, String slug, StreamApiListener mStreamApiListener){
        String baseUrl=(AppPrefrence.getInstance().getAndroidChannel());
        String url = baseUrl+WebserviceChannelAPI.PROGRAMS  + slug+ WebserviceChannelAPI.STREAM_METHOD;
        Log.e("--loadSteamData--","URl----"+url);
        Response response = null;
        ResponseBody responseBody=null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .cacheControl(new CacheControl.Builder().build())
                    .addHeader(WebserviceChannelAPI.CONTENT_TYPE, WebserviceChannelAPI.CONTENT_JSON)
                    .build();
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                if(response.cacheResponse()!=null){
                    responseBody=response.body();
                    Log.e("--cached--","Response----");
                }else{
                    responseBody=response.body();
                    Log.e("--network--","Response----");
                }
                String jsonResponse = responseBody.string();
                Log.e("--loadSteamData--","Response----"+jsonResponse);
                ArrayList<Streams> mStream= ChannelDataparser.parseStream(jsonResponse);
                mStreamApiListener.onStreamLoaded(categorySlug,slug,mStream);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error_code", 400);
                mStreamApiListener.onLoadingFailed();
            }
        } catch (UnknownHostException e){
            mStreamApiListener.onNetworkError();
        }
        catch (Exception e) {
            e.printStackTrace();
            mStreamApiListener.onLoadingFailed();
        }
    }
    public void cancelAllRequests(){
        if(client.dispatcher()!=null){
            client.dispatcher().cancelAll();
        }
    }


}
