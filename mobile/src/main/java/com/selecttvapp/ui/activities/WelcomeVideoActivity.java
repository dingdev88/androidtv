package com.selecttvapp.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.DeveloperKey;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.fragments.AppDownloadFragment;
import com.selecttvapp.ui.fragments.AppManagerFragment;
import com.selecttvapp.ui.fragments.FastDownloadFragment;
import com.selecttvapp.ui.fragments.OnDemandYoutubeFragment.OndemandyoutubeFragmentInteractionListener;

public class WelcomeVideoActivity extends AppCompatActivity implements YouTubePlayer.PlayerStateChangeListener, OndemandyoutubeFragmentInteractionListener,
        FastDownloadFragment.OnAppDownloadFragmentInteractionListener, AppManagerFragment.OnAppFragmentInteractionListener {
    Button done_button;
    private YouTubePlayer YPlayer;

    private FrameLayout layoutContainer;
    private FrameLayout layoutWelcomeVideo;
    private TextView txtHeader;

    private int getContainerLayoutId() {
        return R.id.layoutContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_video);
        done_button = (Button) findViewById(R.id.done_button);
        layoutContainer = (FrameLayout) findViewById(getContainerLayoutId());
        layoutWelcomeVideo = (FrameLayout) findViewById(R.id.layoutWelcomeVideo);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        Utils.requestfocus(done_button);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        /*YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.demand_youtube_player_layout1);
        youtubeFragment.initialize(DeveloperKey.DEVELOPER_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        try {
                            if (!b) {

                                YPlayer = youTubePlayer;
                                YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                                YPlayer.loadPlaylist("PLNT1r49jsn3ke-nQ3NRSTnFCi5AlMkpyB");
                                YPlayer.play();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });*/

        final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.demand_youtube_player_layout1, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                try {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                        YPlayer.setFullscreen(false);
                        YPlayer.setShowFullscreenButton(false);
                        YPlayer.setManageAudioFocus(true);
                        YPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                        YPlayer.loadPlaylist("PLNT1r49jsn3ke-nQ3NRSTnFCi5AlMkpyB");
                        YPlayer.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                ErrorDialog();
                // TODO Auto-generated method stub
                Log.d("youtube::::", ":::failed");
            }
        });
        done_button.setOnClickListener(v -> {
            try {
                if (PreferenceManager.isDemandFirstTime()) {
                    Fragment fragment = null;
                    try {
                        fragment = getSupportFragmentManager().findFragmentById(R.id.demand_youtube_player_layout1);
                        if (fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    fragment = new AppManagerFragment();
                    if (fragment != null) {
                        layoutWelcomeVideo.setVisibility(View.GONE);
                        txtHeader.setText("App Manager");
                        txtHeader.setVisibility(View.VISIBLE);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(getContainerLayoutId(), fragment);
                        fragmentTransaction.commit();
                        layoutContainer.setVisibility(View.VISIBLE);
                    }

//                        fragment = new AppDownloadFragment();
//                        if (fragment != null) {
//                            layoutWelcomeVideo.setVisibility(View.GONE);
//                            txtHeader.setText("Fast Download");
//                            txtHeader.setVisibility(View.VISIBLE);
//                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(getContainerLayoutId(), fragment);
//                            fragmentTransaction.commit();
//                            layoutContainer.setVisibility(View.VISIBLE);
//                        }
                    return;
                }


                Intent intent = new Intent(WelcomeVideoActivity.this, LoadingActivity.class);
                intent.putExtra("mode", 20);
                startActivity(intent);
                WelcomeVideoActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.WELCOME_SCREEN);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (YPlayer != null) {
                YPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void ondemandyoutubeFragmentInteraction() {
        Fragment fragment = new AppDownloadFragment();
        if (fragment != null) {
            txtHeader.setText("Fast Download");
            txtHeader.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(getContainerLayoutId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onAppDownloadFragmentInteraction() {
        Fragment fragment = new AppManagerFragment();
        if (fragment != null) {
            txtHeader.setText("App Manager");
            txtHeader.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(getContainerLayoutId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onAppFragmentInteraction() {
        Intent intent = new Intent(WelcomeVideoActivity.this, LoadingActivity.class);
        intent.putExtra("mode", 20);
        startActivity(intent);
        WelcomeVideoActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager().findFragmentById(getContainerLayoutId()) != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerLayoutId());
            if (fragment instanceof AppDownloadFragment)
                fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getSupportFragmentManager().findFragmentById(getContainerLayoutId()) != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerLayoutId());
            if (fragment instanceof AppDownloadFragment)
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void ErrorDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeVideoActivity.this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle(R.string.warning);
        // Icon Of Alert Dialog
        // alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage(R.string.yt_not_aval);
        alertDialogBuilder.setCancelable(false);

       /* alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                getActivity().finishAffinity();
                //HomeActivity.super.onBackPressed();
                finishAffinity();
            }
        });*/

        alertDialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(HomeActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
