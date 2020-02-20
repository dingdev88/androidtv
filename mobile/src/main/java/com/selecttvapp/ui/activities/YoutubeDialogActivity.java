package com.selecttvapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.selecttvapp.R;
import com.selecttvapp.common.DeveloperKey;

/**
 * Created by babin on 1/4/2017.
 */

public class YoutubeDialogActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    public static final String API_KEY = "your api kery from google";
    public static  String VIDEO_ID ;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerFragment youTubePlayerFragment;
    private static final int RQS_ErrorDialog = 1;
    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaybackEventListener myPlaybackEventListener;
    //YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayerView youTubeView;
    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    YouTubePlayer.PlaybackEventListener playbackEventListener;
    String log = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.youtube_player_dialog_activity);
        Bundle bundle = getIntent().getExtras();
        VIDEO_ID = bundle.getString("video_id");

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        try {
            if (result.isUserRecoverableError()) {
                result.getErrorDialog(this, RQS_ErrorDialog).show();
            } else {
                String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", result.toString());
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        try {
            if (!wasRestored) {

                player.loadVideo(VIDEO_ID);

                // Hiding player controls
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                player.setPlayerStateChangeListener(playerStateChangeListener);
                player.setPlaybackEventListener(playbackEventListener);
                youTubePlayer.setFullscreen(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final class MyPlayerStateChangeListener implements
            YouTubePlayer.PlayerStateChangeListener {
        @Override
        public void onAdStarted() {
        }
        @Override
        public void onError(
                com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
        }
        @Override
        public void onLoaded(String arg0) {
        }
        @Override
        public void onLoading() {
        }
        @Override
        public void onVideoEnded() {
            finish();
        }
        @Override
        public void onVideoStarted() {
        }
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
        }

        @Override
        public void onPaused() {
            Log.d("youtube:::", "::::::paused");
            // Called when playback is paused, either due to user action or call to pause().
        }

        @Override
        public void onStopped() {
            Log.d("youtube:::", "::::::stopped");
            // Called when playback stops for a reason other than being paused.
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }
}
