package com.selecttvapp.channels;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */

public class ExoPlayerFragmentFullScreen extends Fragment {
    private static final String PARAM_VIDEO_URI = "video_uri";
    private static final String PARAM_VIDEO_CTYPE = "video_ctype";
    private static final String PARAM_VIDEO_MRSSOffset = "video_mrssoffset";
    private static final String PARAM_VIDEO_MODE = "video_mode";

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ExoPlayer.EventListener exoPlayerEventListener;

    public ExoPlayerFragmentFullScreen() {
    }

    public static ExoPlayerFragmentFullScreen newInstance(String videoURI, String ContentType, Integer Offset, String VideoMode) {
        ExoPlayerFragmentFullScreen playerFragment = new ExoPlayerFragmentFullScreen();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_VIDEO_URI, videoURI);
        bundle.putString(PARAM_VIDEO_CTYPE, ContentType);
        bundle.putInt(PARAM_VIDEO_MRSSOffset, Offset);
        bundle.putString(PARAM_VIDEO_MODE, VideoMode);
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String videoURI = "";
        String Ctype = "";
        Integer MRSSOffset = 0;
        String VideoMode = "";
        VideoMode = getArguments().getString(PARAM_VIDEO_MODE);


        View view = inflater.inflate(R.layout.fragment_exo_player_fullscreen, container, false);


        if (getArguments() != null) {
            videoURI = getArguments().getString(PARAM_VIDEO_URI);
            Ctype = getArguments().getString(PARAM_VIDEO_CTYPE);

        }

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(getContext());

        simpleExoPlayerView = view.findViewById(R.id.player_view_fullscreen);

        simpleExoPlayerView.setUseController(false);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        //Uri mp4url = Uri.parse("https://i7b5v2p4.ssl.hwcdn.net/files/company/55fb29fc97f815171abb6e62/assets/videos/5b8918f598f815b00dc5415b/vod/5b8918f598f815b00dc5415b.mp4");

        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeterA);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource videoSource = new ExtractorMediaSource(Uri.parse("") ,dataSourceFactory,extractorsFactory,null,null);

        if(Ctype=="MRSS") {
            Uri mp4url = Uri.parse(videoURI);
            videoSource = new ExtractorMediaSource(mp4url ,dataSourceFactory,extractorsFactory,null,null);

        }
        else {
            Uri mp4VideoUri = Uri.parse(videoURI);
            videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        }

        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        if(Ctype=="MRSS") {
            Uri mp4url = Uri.parse(videoURI);
            videoSource = new ExtractorMediaSource(mp4url ,dataSourceFactory,extractorsFactory,null,null);
            player.seekTo(getArguments().getInt(PARAM_VIDEO_MRSSOffset));
        }
        player.prepare(loopingSource);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("Error:::::",":::"+error.getCause()+"::::"+error.type);
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
            }
        });

        player.setPlayWhenReady(true); //run file/link when ready to play.

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.EXO_PLAYER_SCREEN);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.release();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null)
            player.release();
    }

    public void stopExoPlayer(){
        if(player !=null){
            player.setPlayWhenReady(false);
        }
    }
    public void resumeExoPlayer(){
        if(player !=null){
            player.setPlayWhenReady(true);
        }
    }
}
