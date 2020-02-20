package com.selecttvapp.presentation.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.demo.network.util.AppConstants;
import com.demo.network.util.AppPreference;
import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utils;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewLoginListener;
import com.selecttvapp.ui.activities.LoadingActivity;
import com.selecttvapp.ui.activities.LoginActivity;
import com.selecttvapp.ui.activities.WelcomeVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Appsolute dev on 06-Dec-17.
 */

public class PresenterLogin {
    private Activity activity;
    private ViewLoginListener loginListener;
    private FontHelper fontHelper = new FontHelper();
    String brand_slug = "";

    public PresenterLogin(LoginActivity activity) {
        this.activity = activity;
        loginListener = activity;
    }


    public void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }

    public boolean isUserAlreadyLoggedIn(Context context) {
        return AppPreference.getBoolean(context, AppConstants.KEY_IS_LOGIN);
    }

    /*move to next screen*/
    public void moveToNextScreen(Activity context) {
        boolean bSlideNotShow = AppPreference.getBoolean(context, AppConstants.KEY_SLIDE_SHOW);
        if (bSlideNotShow == false) {
            AppPreference.saveBoolean(context, true, AppConstants.KEY_SLIDE_SHOW);
            if (PreferenceManager.isDemandFirstTime()) {
                Intent intent = new Intent(context, WelcomeVideoActivity.class);
                intent.putExtra("mode", 20);
                context.startActivity(intent);
                context.finish();
            } else {
                Intent intent = new Intent(context, LoadingActivity.class);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                context.finish();
            }
        } else {
            Intent intent = new Intent(context, LoadingActivity.class);
            intent.putExtra("mode", 20);
            context.startActivity(intent);
            context.finish();
        }
    }

    /*user login service*/
    public void userLogin(final Activity context, final String username, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "Please wait", "Authenticating...", false);
//        if (activity.getPackageName().equalsIgnoreCase("com.selecttvapp"))
//            brand_slug = "selecttv-freecast-com";
//        if (activity.getPackageName().equalsIgnoreCase(BuildConfig.RABBITTVPLUS_PACKAGE))
//            brand_slug = "rabbittvplus-freecast-com";
        final Thread thread = new Thread(() -> {
            try {
                JSONObject response = JSONRPCAPI.getLogin(username, password, BuildConfig.BRAND_SLUG);
                if (response == null) return;
                if (!response.isNull("error")) {
                    final String message = response.getString("error");
                    onLoginFailed(message);
                } else {
                    onLoginSuccess(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
        thread.start();
    }

    /*user login response*/
    private void onLoginSuccess(JSONObject response) {
        try {
            if (response.getString("expired").equalsIgnoreCase("true")) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, activity.getString(R.string.subscription_expired), Toast.LENGTH_SHORT).show();
                        
                    }
                });
            } else {
                AppPreference.saveBoolean(activity, true, AppConstants.KEY_IS_LOGIN);
                PreferenceManager.setAccessToken(response.getString("access_token"));
                if (response.has("subscriptions")) {
                    try {
                        JSONArray subscriptionsArray = response.getJSONArray("subscriptions");
                        if (subscriptionsArray != null && subscriptionsArray.length() > 0) {
                            String[] list = new String[subscriptionsArray.length()];
                            for (int i = 0; i < subscriptionsArray.length(); i++) {
                                JSONObject sub_object = subscriptionsArray.getJSONObject(i);
                                list[i] = sub_object.getString("code").toLowerCase();
                            }
                            PreferenceManager.setSubscribedList(list);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginListener.onLoginSuccess();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLoginFailed(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginListener.onLoginFailed(message);
            }
        });
    }

    /*get user details*/
    public void loadUserDetails() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject response = JSONRPCAPI.getUserProfile(PreferenceManager.getAccessToken());
                if (response != null) {
                    Utils.setPreferenceData(response, activity);
                }
            }
        });
        thread.start();
    }

    // Get need help apis
    public void getNeedHelpURI(final Activity activity, final String url) {
        Thread thread = new Thread(() -> {
            final JSONObject response = JSONRPCAPI.getNeedHelpURI(url);
            if (response == null) return;
            String appName = activity.getString(R.string.app_name);

            Iterator iterator = response.keys();
            while (iterator.hasNext()) {
                final String key = (String) iterator.next();
                if (appName.toLowerCase().toString().replace(" ", "").equals(key.toLowerCase().replace(" ", ""))) {
                    activity.runOnUiThread(() -> {
                        try {
                            loginListener.showNeedHelp(response.getString(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                }
            }
        });
        thread.start();
    }
}
