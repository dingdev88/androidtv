package com.selecttvapp.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.selecttvapp.R;
import com.selecttvapp.common.DeveloperKey;
import com.selecttvapp.ui.activities.HomeActivity;

public class OnDemandDialogVideo extends DialogFragment implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerSupportFragment youtubePlayerFragment;
    private YouTubePlayer YPlayer;
    Button butCancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TransparentProgressDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.ondemand_dialog_video, container, false);
        butCancel = (Button) view.findViewById(R.id.butCancel);
        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        prepareYoutubePlayer(view);
        return view;
    }

    private void prepareYoutubePlayer(View view) {
        youtubePlayerFragment = (YouTubePlayerSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.demand_youtube_player_layout);
        if (youtubePlayerFragment == null) {
            youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            getChildFragmentManager().beginTransaction().add(R.id.demand_youtube_player_layout, youtubePlayerFragment).commit();
        }
        youtubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        if (!wasRestored) {
            YPlayer = youTubePlayer;
            youTubePlayer.loadPlaylist("PLNT1r49jsn3kMgZo6GRcBbMIz6NHiZM_9");
            this.YPlayer = youTubePlayer;
      /*  youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.*/
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e(HomeActivity.class.getSimpleName(), "Ruh Roh!");
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
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        Window window = getDialog().getWindow();
        window.setLayout(width, 800);
        window.setGravity(Gravity.CENTER);

    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}