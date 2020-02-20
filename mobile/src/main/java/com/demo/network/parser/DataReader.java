package com.demo.network.parser;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;


public class DataReader {
    public static String readDataFromFile(Context context) throws IOException {
        String json = null;
        try {

            InputStream is = context.getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
