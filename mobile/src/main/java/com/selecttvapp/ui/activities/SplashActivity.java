package com.selecttvapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.demo.network.util.AppConstants;
import com.demo.network.util.AppPreference;
import com.newrelic.agent.android.NewRelic;
import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.channels.ClosingService;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.forceupdate.ForceUpdate;
import com.selecttvapp.presentation.fragments.PresenterForceUpdate;
import com.selecttvapp.presentation.views.ViewForceUpdate;
import com.selecttvapp.ui.adapters.TestFragmentAdapter;
import com.selecttvapp.ui.base.BaseActivity;
import com.selecttvapp.ui.splash.PresenterSplash;
import com.selecttvapp.ui.splash.ViewSplash;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import io.fabric.sdk.android.Fabric;


public class SplashActivity extends BaseActivity implements ViewSplash, ViewForceUpdate {
    public static boolean isAllreadyCheckedAppUpdateAvailable = false;
    private PresenterSplash presenter = new PresenterSplash();
    private PresenterForceUpdate presenterForceUpdate = new PresenterForceUpdate();
    private VideoView videoBackground;
    private AutoScrollViewPager autoScrollViewPager;
    private CirclePageIndicator pageIndicator;
    private TestFragmentAdapter adapter;
    private Button btnStartWatching;
    private View.OnClickListener m_btnStartClickListener = v -> {
        AppPreference.saveBoolean(getApplicationContext(), true, AppConstants.KEY_FIRST_TIME);
        showLoginScreen();
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.ui_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteCache(SplashActivity.this);
        Fabric.with(this, new Crashlytics());
        startService(new Intent(getBaseContext(), ClosingService.class));
        NewRelic.withApplicationToken("AA36987a145eeed62194e6d66252e63fd3a3815ee9").start(this.getApplication());
        presenter.onAttach(this);
        presenterForceUpdate.onAttach(this);
        presenter.getBrands();
        if (isUserAlreadyLoggedIn()) {
            openMainScreen();
            return;
        }

        try {
            videoBackground = (VideoView) findViewById(R.id.videoViewSplash);
            videoBackground.setOnErrorListener((mp, what, extra) -> true);
            String urll = "http://rabbittv-resp.s3.amazonaws.com/app/540x960.mp4";
            videoBackground.setVideoPath(urll);
            videoBackground.setOnCompletionListener(mp -> videoBackground.seekTo(0));
            videoBackground.start();


            adapter = new TestFragmentAdapter(getSupportFragmentManager());
            autoScrollViewPager = (AutoScrollViewPager) findViewById(R.id.autoScrollViewpager);
            autoScrollViewPager.setAdapter(adapter);
            autoScrollViewPager.setInterval(5000);
            autoScrollViewPager.startAutoScroll();

            pageIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
            pageIndicator.setViewPager(autoScrollViewPager);

            btnStartWatching = (Button) findViewById(R.id.btnStartWatching);
            btnStartWatching.setOnClickListener(m_btnStartClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (presenterForceUpdate != null && !isUserAlreadyLoggedIn() && InternetConnection.isConnected(getApplicationContext()))
            presenterForceUpdate.checkVersionUpdate(true);
        else callNextActivity();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.SPLASH_SCREEN);
        isAllreadyCheckedAppUpdateAvailable = false;
    }


    private void callNextActivity() {
        if (isUserAlreadyLoggedIn())
            openMainScreen();
        else {
            btnStartWatching.setVisibility(View.VISIBLE);
            //used to give focus to the button
            Utils.requestfocus(btnStartWatching);
        }
    }

    private void showLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isUserAlreadyLoggedIn() {
        return AppPreference.getBoolean(this, AppConstants.KEY_IS_LOGIN);
    }

    private void openMainScreen() {
        Intent intent = new Intent(this, HomeSplash.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenterForceUpdate != null)
            presenterForceUpdate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (presenterForceUpdate != null)
            presenterForceUpdate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        if (presenterForceUpdate != null)
            presenterForceUpdate.stopProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onGotGoogleAnalyticsTrackingId(String trackingId) {
        Utilities.googleAnalytics(Constants.SPLASH_SCREEN);
    }

    @Override
    public void onAppUpdateAvailable(boolean isAvailable, ForceUpdate update) {
        if (!isAvailable) {
            callNextActivity();
            return;
        }
        if (!update.version.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
            isAllreadyCheckedAppUpdateAvailable = true;
            presenterForceUpdate.showUpdateAppDialog(update.releaseText, update.downloadPath, update.forceUpdate);
        }
    }

    @Override
    public void onAppUpdateSkipped() {
        callNextActivity();
    }

    //deletes the cache folder present in our app
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
