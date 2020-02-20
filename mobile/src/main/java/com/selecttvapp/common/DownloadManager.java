package com.selecttvapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.callbacks.DownloadListener;

import java.io.File;

/**
 * Created by Appsolute dev on 11-Dec-17.
 */

public class DownloadManager {
    public static final int CODE_APK_INSTALLATION = 189;

    public String lastVisibleApkName = "";
    public String lastApkUrl = "";
    public DownloadListener downloadListener = null;
    private Activity activity;

    public DownloadManager(Activity activity) {
        this.activity = activity;
    }

    public void onAttach(Activity activity) {
        this.activity = activity;
    }

    public void downloadAPK(String url, Context context, final DownloadListener downloadListener) {
        lastApkUrl = url;
        this.downloadListener = downloadListener;
        if (Build.VERSION.SDK_INT < 9) {
            throw new RuntimeException("Method requires API level 9 or above");
        }


        if (!Permission.getInstance().checkPermission(activity, Permission.getInstance().WRITE_EXTERNAL_STORAGE, Permission.CODE_WRITE_EXTERNAL_STORAGE))
            return;

        final android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 11) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setTitle(URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, "", Intent.ACTION_VIEW));

        final android.app.DownloadManager dm = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            try {
                dm.enqueue(request);
                downloadListener.onStart("Downloading File");
            } catch (SecurityException e) {
                if (Build.VERSION.SDK_INT >= 11) {
                    request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE);
                }
                dm.enqueue(request);
                downloadListener.onStart("Downloading File");
            }

            final android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
            if (query != null) {
                query.setFilterByStatus(android.app.DownloadManager.STATUS_FAILED | android.app.DownloadManager.STATUS_PAUSED | android.app.DownloadManager.STATUS_SUCCESSFUL | android.app.DownloadManager.STATUS_RUNNING | android.app.DownloadManager.STATUS_PENDING);
            }

            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void run() {
                    boolean downloading = true;
                    try {
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int status = c.getInt(c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
                            if (status == android.app.DownloadManager.STATUS_RUNNING) {
                                downloadListener.onLoading("Downloading..");
                                c.close();
                            } else if (status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                                downloading = false;
                                String apkName = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_TITLE));
                                String title = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_URI));
                                if (!apkName.isEmpty()) {
                                    lastVisibleApkName = apkName;
                                    downloadListener.onSuccess(apkName);
//                                    installApk(apkName, true);
                                }
                                c.close();
                            } else if (status == android.app.DownloadManager.STATUS_FAILED) {
                                downloadListener.onError("");
                                downloading = false;
                            } else {
                                c.close();
                            }
                        } else c.close();
                    } catch (Exception e) {
                        downloadListener.onError(e.getMessage());
                        e.printStackTrace();
                    } finally {
                        if (downloading && !activity.isDestroyed())
                            handler.postDelayed(this, 1000);
                        else handler.removeCallbacks(this);
                    }
                }
            });
        }
        // if the download manager app has been disabled on the device
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void installLastSelectedApk() {
        if (!lastVisibleApkName.isEmpty())
            installApk(lastVisibleApkName);
    }


    public void installApk(String apkName) {
        lastVisibleApkName = apkName;

        File toInstall = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + apkName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String provider = activity.getResources().getString(R.string.providers);
            Uri apkUri = FileProvider.getUriForFile(activity, provider, toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(intent, CODE_APK_INSTALLATION);
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent,CODE_APK_INSTALLATION);

      /*      File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + apkName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, CODE_APK_INSTALLATION);*/
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public void alertInstallAPK(final String apkName, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(cancelable);
        builder.setMessage("Install " + apkName);
        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!apkName.isEmpty()) {
                    installApk(apkName);
                }
            }
        });
        builder.show();
    }
}
