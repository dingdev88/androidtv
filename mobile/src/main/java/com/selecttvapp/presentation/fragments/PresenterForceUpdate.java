package com.selecttvapp.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.DownloadListener;
import com.selecttvapp.common.DownloadManager;
import com.selecttvapp.common.Permission;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.forceupdate.ForceUpdate;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewForceUpdate;
import com.selecttvapp.ui.base.BasePresenter;

import org.json.JSONObject;

/**
 * Created by ocspl-72 on 10/1/18.
 */

public class PresenterForceUpdate extends BasePresenter<ViewForceUpdate> {
    private static String availableUpdateVersion = "";
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private DownloadManager downloadManager;
    private AlertDialog dialog = null;
    private boolean forceUpdate = false;

    public PresenterForceUpdate() {
    }

    @Override
    public void onAttach(ViewForceUpdate view) {
        super.onAttach(view);
        downloadManager = new DownloadManager(getActivity());
    }

    public void checkVersionUpdate(boolean requiredLoadingAlert) {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                return;
        if (dialog != null)
            if (dialog.isShowing())
                return;

        if (requiredLoadingAlert)
            showProgressDialog();
        Thread thread = new Thread(() -> {
            try {
//                String versionUrl = "http://dev7stv.freecast.com/mobile/apps/selecttv/android/latest";
//                String versionUrl = "http://stagingstv.freecast.com/mobile/apps/selecttv/Android/latest/";  // development
                String appName=BuildConfig.App_Name_Version_Check;
                String versionUrl = "http://mobile2.freecast.com/mobile/apps/"+appName+"/android/";  // live
                final JSONObject response = JSONRPCAPI.getResponseByURL(versionUrl, JSONRPCAPI.REQUEST_GET);
                handler.post(() -> {
                    stopProgressDialog();
                    if (response != null)
                        onVersionUpdateResponse(Utilities.fromJson(response.toString(), ForceUpdate.class));
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stopProgressDialog();
            }
        });
        thread.start();
    }

    private void onVersionUpdateResponse(ForceUpdate response) {
        double versionFromServer=Double.parseDouble(response.version);
        double versionOfApp=Double.parseDouble(BuildConfig.VERSION_NAME);
        if (versionFromServer>versionOfApp) {
            this.forceUpdate = response.forceUpdate;
            this.availableUpdateVersion = response.version;
            getViewState().onAppUpdateAvailable(true, response);
        }else getViewState().onAppUpdateAvailable(false, response);
    }

    public void showUpdateAppDialog(String releaseText, final String appLink, final boolean forceUpdate) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(releaseText);
        builder.setCancelable(false);
        builder.setPositiveButton("Update", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            try {
                if (appLink.toLowerCase().startsWith("https://play.google.com/store/apps/details?id=")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                    getActivity().startActivity(myIntent);
                } else if (appLink.toLowerCase().endsWith(".apk")) {
                    downloadManager.downloadAPK(appLink, getContext(), new DownloadListener() {
                        @Override
                        public void onStart(String message) {
                            showProgressDialog("Please wait", "Downloading...", false);
                        }

                        @Override
                        public void onLoading(String message) {
                        }

                        @Override
                        public void onSuccess(String apkName) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            if (!Permission.checkUnknownSourceInstallation()) {
                                Permission.alertUnknownSourceInstallation(getActivity(), !forceUpdate);
                                return;
                            }
                            downloadManager.installApk(apkName);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (!forceUpdate) {
            builder.setNegativeButton("Skip Now", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                getViewState().onAppUpdateSkipped();
            });
        }
        dialog = builder.create();
        dialog.show();
    }

    private void showProgressDialog() {
        showProgressDialog("Please wait", "Checking Version...", false);
    }

    private void showProgressDialog(String title, String message, boolean cancelable) {
        stopProgressDialog();
        try {
            progressDialog = ProgressDialog.show(getActivity(), title, message, false);
            progressDialog.setCanceledOnTouchOutside(cancelable);
            progressDialog.setCancelable(cancelable);
            progressDialog.show();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void stopProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Permission.CODE_UNKNOWNSOURCE_INSTALLATION) {
            if (!Permission.checkUnknownSourceInstallation()) {
                Permission.alertUnknownSourceInstallation(getActivity(), !forceUpdate);
                return;
            }
            downloadManager.installLastSelectedApk();
        }
        if (requestCode == DownloadManager.CODE_APK_INSTALLATION)
            if (!forceUpdate || availableUpdateVersion.equalsIgnoreCase(BuildConfig.VERSION_NAME))
                getViewState().onAppUpdateSkipped();
            else if (forceUpdate) getViewState().finishActivity();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Permission.CODE_WRITE_EXTERNAL_STORAGE)
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (Permission.getInstance().checkPermission(getActivity(), Permission.getInstance().WRITE_EXTERNAL_STORAGE, Permission.CODE_WRITE_EXTERNAL_STORAGE)) {
                        downloadManager.downloadAPK(downloadManager.lastApkUrl, getContext(), downloadManager.downloadListener);
                    }
    }

}
