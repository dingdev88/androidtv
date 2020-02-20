package com.selecttvapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.demo.network.common.DebugHelper;
import com.demo.network.util.AppConstants;
import com.demo.network.util.AppPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.InstallationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static boolean checkGooglePlayServices(final Activity activity) {
        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
                activity);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            default:
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck,
                        activity, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                dialog.show();
        }
        return false;
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        return appInstalledOrNot(context, uri, null);
    }


    public static boolean appInstalledOrNot(Context context, String uri, InstallationListener installationListener) {
        if (uri.isEmpty())
            return false;
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            if (installationListener != null)
                installationListener.appInstalled();
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (installationListener != null)
            installationListener.appNotInstalled();

        return false;
    }

    public static void hideKeyBoard(Activity activity) {
        try {
            hideKeyBoard(activity, activity.getCurrentFocus().getWindowToken());
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    public static void hideKeyBoard(Context context, View view) {
        try {
            hideKeyBoard(context, view.getWindowToken());
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }

    private static void hideKeyBoard(Context context, IBinder windowToken) {
        try {
            if (context != null && windowToken != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception exception) {
            DebugHelper.trackException(exception);
        }
    }


    /**
     * Get app package name from play store url.
     */
    public static Map<String, String> getPackageNameFromQuery(String query) {
        Map<String, String> map = new HashMap<>();
        try {
            String[] queryString = query.split("\\?");
            if (queryString.length > 1) {
                String[] params = queryString[1].split("&");
                for (String param : params) {
                    String name = param.split("=")[0];
                    String value = param.split("=")[1];
                    map.put(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void showAlert(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        TextView titleView = (TextView) dialog
                .findViewById(context.getResources()
                        .getIdentifier("alertTitle", "id",
                                "android"));

        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }

    public static String stripHtml(String html) {
        return html.replaceAll("\\<[^>]*>", "");
    }

    public static long GetUnixTime() {

        DateFormat sdfgmt = DateFormat.getDateTimeInstance();
        sdfgmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat sdfmad = DateFormat.getDateTimeInstance();
        TimeZone tz = TimeZone.getTimeZone(TimeZone.getDefault().getID());
        sdfmad.setTimeZone(tz);
        Date inptdate = null;
        String gmtTime = "";
        try {
            gmtTime = sdfgmt.format(new Date());
            inptdate = sdfmad.parse(gmtTime);


            return inptdate.getTime();

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int GetUniqueID() {
        return (int) (Math.random() + SystemClock.currentThreadTimeMillis());
    }

    public static void clearPreferenceData(Context mContext) {
        AppPreference.saveBoolean(mContext, false, AppConstants.KEY_IS_LOGIN);
        PreferenceManager.setAccessToken("");
        PreferenceManager.setUsername("");
        PreferenceManager.setCity("");
        PreferenceManager.setFirstName("");
        PreferenceManager.setLastName("");
        PreferenceManager.setGender("");
        PreferenceManager.setEmail("");
        PreferenceManager.setState("");
        PreferenceManager.setDateOfBirth("");
        PreferenceManager.setLastLogin("");
        PreferenceManager.setAddress1("");
        PreferenceManager.setAddress2("");
        PreferenceManager.setPostalCode("");
        PreferenceManager.setPhoneNumber("");
        PreferenceManager.setId(0);

    }

    public static void setPreferenceData(JSONObject mJSONObject, Context mContext) {
        try {
            if (mJSONObject.has("username")) {
                String username = mJSONObject.getString("username");
                PreferenceManager.setUsername(username);
            }
            if (mJSONObject.has("city")) {
                String city = mJSONObject.getString("city");
                PreferenceManager.setCity(city);
            }
            if (mJSONObject.has("first_name")) {
                String first_name = mJSONObject.getString("first_name");
                PreferenceManager.setFirstName(first_name);
            }
            if (mJSONObject.has("last_name")) {
                String last_name = mJSONObject.getString("last_name");
                PreferenceManager.setLastName(last_name);
            }
            if (mJSONObject.has("gender")) {
                String gender = mJSONObject.getString("gender");
                PreferenceManager.setGender(gender);
            }
            if (mJSONObject.has("email")) {
                String email = mJSONObject.getString("email");
                PreferenceManager.setEmail(email);
            }
            if (mJSONObject.has("state")) {
                String state = mJSONObject.getString("state");
                PreferenceManager.setState(state);
            }
            if (mJSONObject.has("date_of_birth")) {
                String date_of_birth = mJSONObject.getString("date_of_birth");
                PreferenceManager.setDateOfBirth(date_of_birth);
            }
            if (mJSONObject.has("last_login")) {
                String last_login = mJSONObject.getString("last_login");
                PreferenceManager.setLastLogin(last_login);
            }
            if (mJSONObject.has("address_1")) {
                String address_1 = mJSONObject.getString("address_1");
                PreferenceManager.setAddress1(address_1);
            }
            if (mJSONObject.has("address_2")) {
                String address_2 = mJSONObject.getString("address_2");
                PreferenceManager.setAddress2(address_2);

            }
            if (mJSONObject.has("postal_code")) {
                String postal_code = mJSONObject.getString("postal_code");
                PreferenceManager.setPostalCode(postal_code);
            }
            if (mJSONObject.has("phone_number")) {
                String phone_number = mJSONObject.getString("phone_number");
                PreferenceManager.setPhoneNumber(phone_number);
            }
            if (mJSONObject.has("id")) {
                int id = mJSONObject.getInt("id");
                PreferenceManager.setId(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // to set focus on a particular view
    public static void requestfocus(View view){
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}