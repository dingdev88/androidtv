package com.selecttvapp.RPC;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Implementation of JSON-RPC over HTTP/POST
 */
public class JSONRPCHttpClient extends JSONRPCClient {
    private String serviceUri;

    public JSONRPCHttpClient(String uri) {
        this.serviceUri = uri;
    }

    protected JSONObject doJSONRequest(JSONObject jsonRequest) throws JSONRPCException {
        URL url;

        try {
            long e = System.currentTimeMillis();
            String strresponse = "";
            try {
                url = new URL(this.serviceUri);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(25000);
                conn.setConnectTimeout(25000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                Log.d("json-rpc", "params:" + jsonRequest.toString());
                writer.write(jsonRequest.toString());

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("json-rpc", "Http connection ok :");
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    Log.d("json-rpc", "reading::::");
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    strresponse = sb.toString();
                } else {
                    strresponse = "";

                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }


            e = System.currentTimeMillis() - e;
            Log.d("json-rpc", "Request time :" + e);
            String responseString = strresponse.trim();
            Log.d("json-rpc", "responseString:::" + responseString);

            Object json = new JSONTokener(responseString).nextValue();
            if (json instanceof JSONArray) {
                //you have an json array
                JSONArray jsonArray = new JSONArray(responseString);
                return jsonArray.getJSONObject(0);
            }

            JSONObject jsonResponse = new JSONObject(responseString);
            if (jsonResponse.has("error")) {
                Object jsonError = jsonResponse.get("error");
                if (!jsonError.equals((Object) null)) {
                    throw new JSONRPCException(jsonResponse.get("error"));
                } else {
                    return jsonResponse;
                }
            } else {
                return jsonResponse;
            }
        } catch (JSONException var13) {
            throw new JSONRPCException("Invalid JSON response", var13);
        }
    }

    protected JSONObject doJSONGetRequest(JSONObject jsonRequest) throws JSONRPCException {
        URL url;

        try {
            long e = System.currentTimeMillis();
            String strresponse = "";
            try {
                url = new URL(this.serviceUri);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                //con.setRequestProperty("User-Agent", USER_AGENT);
                int responseCode = con.getResponseCode();
                Log.e("GET Response Code :: ", "_" + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    strresponse = response.toString();
                } else {
                    strresponse = "";
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }


            e = System.currentTimeMillis() - e;
            Log.d("json-rpc", "Request time :" + e);
            String responseString = strresponse.trim();
            Log.d("json-rpc", "responseString:::" + responseString);

            Object json = new JSONTokener(responseString).nextValue();
            if (json instanceof JSONArray) {
                //you have an json array
                JSONArray jsonArray = new JSONArray(responseString);
                return jsonArray.getJSONObject(0);
            }

            JSONObject jsonResponse = new JSONObject(responseString);
            if (jsonResponse.has("error")) {
                Object jsonError = jsonResponse.get("error");
                if (!jsonError.equals((Object) null)) {
                    throw new JSONRPCException(jsonResponse.get("error"));
                } else {
                    return jsonResponse;
                }
            } else {
                return jsonResponse;
            }
        } catch (JSONException var13) {
            throw new JSONRPCException("Invalid JSON response", var13);
        }
    }

    @Override
    protected JSONObject doJSONGetRequest() throws JSONRPCException {
        return null;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
