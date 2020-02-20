package com.selecttvapp.common;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.selecttvapp.R;
import com.selecttvapp.ui.helper.MyApplication;

/**
 * Created by Appsolute dev on 13-Dec-17.
 */

public class Permission {
    public static String TAG = "Permission";
    public static String WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static String SYSTEM_ALERT_WINDOW = Manifest.permission.SYSTEM_ALERT_WINDOW;

    public static int CODE_UNKNOWNSOURCE_INSTALLATION = 190;
    public static int CODE_WRITE_EXTERNAL_STORAGE = 191;

    public static Permission getInstance() {
        return new Permission();
    }


    public static boolean checkPermission(Activity activity, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (permission.isEmpty())
                return true;

            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean checkUnknownSourceInstallation() {
        try {
            boolean isNonPlayAppAllowed = Settings.Secure.getInt(MyApplication.getInstance().getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
            return isNonPlayAppAllowed;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkUnknownSourceInstallation(Activity activity) {
        return checkUnknownSourceInstallation(activity, false);
    }

    public static boolean checkUnknownSourceInstallation(Activity activity, boolean isAlertRequired) {
        return checkUnknownSourceInstallation(activity, isAlertRequired, false);
    }

    public static boolean checkUnknownSourceInstallation(Activity activity, boolean isAlertRequired, boolean allowOpenSecurity) {
        try {
            boolean isNonPlayAppAllowed = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
            if (!isNonPlayAppAllowed) {
                if (isAlertRequired)
                    alertUnknownSourceInstallation(activity,true);
                else if (allowOpenSecurity)
                    activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), CODE_UNKNOWNSOURCE_INSTALLATION);
                return false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void alertUnknownSourceInstallation(final Activity activity, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(cancelable);
        builder.setMessage(activity.getResources().getString(R.string.appmanager_note_string));
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS), CODE_UNKNOWNSOURCE_INSTALLATION);
            }
        });
        builder.show();
    }
}
