package com.demo.network.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.network.listener.ApplicationDataRecievedListener;
import com.demo.network.model.AppInfo;
import com.demo.network.model.Data;
import com.demo.network.parser.DataReader;
import com.demo.network.parser.JsonParser;
import com.selecttvapp.network.JSONRPCAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private ApplicationDataRecievedListener applicationDataRecievedListener;
    private ProgressDialog dialog;
    private List<Data> data;

    public InitializeApplicationsTask(Context context, ApplicationDataRecievedListener applicationDataRecievedListener) {
        this.context = context;
        this.applicationDataRecievedListener = applicationDataRecievedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setTitle("Please wait");
        dialog.setMessage("Checking...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<Data> listData = new ArrayList<Data>();
        HashMap<String, Data> hash = new HashMap<String, Data>();
        try {
            JSONArray array = JSONRPCAPI.getAppsList();
            String data = array == null ? null : array.toString();
            if(data == null || data.length() == 0 || data.equals("null")){
                data = DataReader.readDataFromFile(context);
            }
            listData = JsonParser.parseData(data);
            if (listData != null) {
                for (Data temp : listData) {
                    JSONObject obj = JSONRPCAPI.getPackageName(temp.getName());
                    if( obj != null ){
                        temp.setAppLink(obj.getString("link"));
                        temp.setAppPakage(obj.getString("package"));
                    }else
                        temp.parsePackage();
                    hash.put(temp.getAppPakage(), temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Query the applications
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo ri : ril) {
            Log.e("Info", "" + ri.activityInfo.parentActivityName);
            if (!isSystemPackage(ri)) {
                String key = ri.activityInfo.packageName;
                if (hash.containsKey(key)) {
                    Data dd = hash.get(key);
                    dd.setIsAppInstalled(true);
                    dd.setAppInfo(new AppInfo(context, ri));
                    Log.e("Info1", "" + dd);
                }
            }
        }
        if (listData != null) {
            Collections.sort(listData);
        }
        this.data = listData;
        return null;
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    @Override
    protected void onPostExecute(Void appInfos) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (applicationDataRecievedListener != null) {
            applicationDataRecievedListener.onResultRecived(data);
        }
    }
}

