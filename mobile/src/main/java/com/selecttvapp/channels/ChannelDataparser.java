package com.selecttvapp.channels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by babin on 7/3/2017.
 */

public class ChannelDataparser {
    private static ArrayList<ChannelCategoryList> allMenuList;
    private static ArrayList<ChannelScheduler> allchannelList;


    public static ArrayList<ChannelCategoryList> parseCategories(JSONArray m_jsonArrayCategories) {
        try {

            if (m_jsonArrayCategories != null && m_jsonArrayCategories.length() > 0) {
                allMenuList = new ArrayList<>();
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                allMenuList = mGson.fromJson(String.valueOf(m_jsonArrayCategories), new TypeToken<ArrayList<ChannelCategoryList>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allMenuList;
    }

    public static ArrayList<ChannelScheduler> parseChannels(String parentcategorySlug,JSONArray m_jsonArrayChannels) {
        try {

            if (m_jsonArrayChannels != null && m_jsonArrayChannels.length() > 0) {
                allchannelList = new ArrayList<>();
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                allchannelList = mGson.fromJson(String.valueOf(m_jsonArrayChannels), new TypeToken<ArrayList<ChannelScheduler>>() {
                }.getType());
                for(int i=0;i<allchannelList.size();i++){
                    allchannelList.get(i).setParentcategorySlug(parentcategorySlug);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return allchannelList;
    }
    public static Programs parseProgram(String m_programData) {
        Programs mPrograms=null;
        try {

            if (m_programData!= null ) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                mPrograms = mGson.fromJson(String.valueOf(m_programData), new TypeToken<Programs>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mPrograms;
    }

    public static ArrayList<Streams> parseStream(String jsonResponse) {
        ArrayList<Streams> mStream=new ArrayList<>();
        try {

            if (jsonResponse!= null ) {
                JSONObject response_object=new JSONObject(jsonResponse);
                if(response_object.has("streams")){
                    JSONArray streams_array=response_object.getJSONArray("streams");
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();

                    mStream= mGson.fromJson(String.valueOf(streams_array), new TypeToken<ArrayList<Streams>>() {
                    }.getType());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mStream;
    }
}
