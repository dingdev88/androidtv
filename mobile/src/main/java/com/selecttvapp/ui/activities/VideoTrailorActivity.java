package com.selecttvapp.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.DeveloperKey;
import com.selecttvapp.common.Utilities;


public class VideoTrailorActivity extends YouTubeFailureRecoveryActivity implements YouTubePlayer.PlaybackEventListener {

    YouTubePlayer player;
    String strUrl;

    private LinearLayout frameYoutubeContainer;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.selecttvapp.R.layout.activity_video_trailor);

        strUrl = getIntent().getStringExtra("videoid");

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(com.selecttvapp.R.id.youtube_view);
        if (youTubeView != null)
            youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        frameYoutubeContainer = (LinearLayout) findViewById(com.selecttvapp.R.id.frameYoutubeContainer);
        btnClose = (Button) findViewById(com.selecttvapp.R.id.btnClose);
        btnClose.setOnClickListener(v -> finish());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLandscapeLayout();
        } else {
            setPortraitLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.VIDEO_TRAILER_SCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.selecttvapp.R.menu.menu_video_trailor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.selecttvapp.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return null;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;
        this.player.setPlaybackEventListener(this);
        this.player.loadVideo(strUrl);
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
    public void onPrevious() {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPlaylistEnded() {

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLandscapeLayout();
        } else {
            setPortraitLayout();
        }
    }

    private void setLandscapeLayout() {
        int margin = getApplication().getResources().getDimensionPixelSize(com.selecttvapp.R.dimen.videotrailor_landscape_margin);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameYoutubeContainer.getLayoutParams();
        params.setMargins(margin, 0, margin, 0);
        frameYoutubeContainer.setLayoutParams(params);
    }

    private void setPortraitLayout() {
        int margin = getApplication().getResources().getDimensionPixelSize(com.selecttvapp.R.dimen.videotrailor_portrait_margin);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameYoutubeContainer.getLayoutParams();
        params.setMargins(margin, 0, margin, 0);
        frameYoutubeContainer.setLayoutParams(params);
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }
}
