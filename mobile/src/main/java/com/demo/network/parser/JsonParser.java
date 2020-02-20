package com.demo.network.parser;

import com.demo.network.model.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonParser {
    public static List<Data> parseData(String json) {
        ArrayList<Data> data= new ArrayList<Data>();
        try {
            JSONArray temp = new JSONArray(json);
            for (int i = 0; i < temp.length(); i++) {
                JSONObject obj = temp.getJSONObject(i);
                Data d = new Data();
                d.setName(obj.getString("name"));
                d.setAppLink(obj.getString("link"));
                d.setImageLink(obj.getString("image"));
                data.add(d);
            }
//            Type listType = new TypeToken<List<Data>>(){}.getType();
//            List<Data> posts = (List<Data>) new Gson().fromJson(temp.toString(), listType);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
