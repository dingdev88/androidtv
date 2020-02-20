package com.selecttvapp.ui.samplevideoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.selecttvapp.R;
import com.selecttvapp.channels.ChannelsRestFragment;
import com.selecttvapp.network.JSONRPCAPI;

import org.json.JSONObject;


public class VideoFragment extends Fragment implements AdEvent.AdEventListener, AdErrorEvent.AdErrorListener {

    private SampleVideoPlayer mVideoPlayer;
    private ViewGroup mAdUiContainer;
    private ImaSdkFactory mSdkFactory;
    private AdsLoader mAdsLoader;
    private AdsManager mAdsManager;
    public boolean mIsAdDisplayed;
    private View mPlayButton;
    private ProgressBar mProgressView;

    private String mAdUrl;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        if (mAdsLoader != null) {
            mAdsManager.destroy();
            mAdsManager = null;
            mAdsLoader.contentComplete();
        }


        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(this.getContext());
        // Add listeners for when ads are loaded and for errors.
        mAdsLoader.addAdErrorListener(this);

        mAdsLoader.addAdsLoadedListener(adsManagerLoadedEvent -> {
            // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
            // events for ad playback and errors.
            mAdsManager = adsManagerLoadedEvent.getAdsManager();

            // Attach event and error event listeners.
            mAdsManager.addAdErrorListener(VideoFragment.this);
            mAdsManager.addAdEventListener(VideoFragment.this);
            mAdsManager.init();
        });


        mVideoPlayer.addVideoCompletedListener(() -> {
            // Handle completed event for playing post-rolls.
            if (mAdsLoader != null) {
                mAdsLoader.contentComplete();
            }
        });


        /*Demo Ads*/

        // requestAds(getString(R.string.ad_tag_url));
        //mProgressView.setVisibility(View.VISIBLE);


        /*Client Url*/
        String videoAddUrl = "https://mobile2.freecast.com/advertisement/android/channels/?brand_slug=" + com.selecttvapp.BuildConfig.BRAND_SLUG;
        final Thread thread = new Thread(() -> {
            try {
                final JSONObject response = JSONRPCAPI.getVideoAd(videoAddUrl, JSONRPCAPI.REQUEST_GET);
                Log.d(getClass().getSimpleName(), response.toString());

                JSONObject adObject = response.getJSONObject("ad");
                mAdUrl = adObject.getString("vast_tag");

                requestAds(mAdUrl);
                mProgressView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                if (getParentFragment() instanceof ChannelsRestFragment) {
                    ((ChannelsRestFragment) getParentFragment()).removeVideoPlayer(true);
                }
            }
        });
        thread.start();

        mHandler = new Handler();

        mRunnable = this::rePlayAds;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        mVideoPlayer = (SampleVideoPlayer) rootView.findViewById(R.id.sampleVideoPlayer);
        mAdUiContainer = (ViewGroup) rootView.findViewById(R.id.videoPlayerWithAdPlayback);
        mPlayButton = rootView.findViewById(R.id.playButton);
        mProgressView = (ProgressBar) rootView.findViewById(R.id.progress_view);

        return rootView;
    }

    /**
     * Request video ads from the given VAST ad tag.
     *
     * @param adTagUrl URL of the ad's VAST XML
     */
    private void requestAds(String adTagUrl) {
        AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
        adDisplayContainer.setAdContainer(mAdUiContainer);

        // Create the ads request.
        AdsRequest request = mSdkFactory.createAdsRequest();
        request.setAdTagUrl(adTagUrl);
        request.setAdDisplayContainer(adDisplayContainer);

        request.setContentProgressProvider(() -> {
            if (mIsAdDisplayed || mVideoPlayer == null || mVideoPlayer.getDuration() <= 0) {
                return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
            }
            return new VideoProgressUpdate(mVideoPlayer.getCurrentPosition(),
                    mVideoPlayer.getDuration());
        });

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);

    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.i("Video Ads", "Event: " + adEvent.getType());

        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be played.
                // AdsManager.start() begins ad playback. This method is ignored for VMAP or
                // ad rules playlists, as the SDK will automatically start executing the
                // playlist.
                removeHandler();
                mAdsManager.start();
                mProgressView.setVisibility(View.GONE);

                break;

            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a video
                // ad is played.
                mIsAdDisplayed = true;
                mVideoPlayer.pause();

                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed
                // and you should start playing your content.
                mIsAdDisplayed = false;
                mVideoPlayer.play();
                break;
            case ALL_ADS_COMPLETED:
                if (getParentFragment() instanceof ChannelsRestFragment) {
                    ((ChannelsRestFragment) getParentFragment()).removeVideoPlayer(true);
                } else {
                    mPlayButton.setVisibility(View.VISIBLE);
                }
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }

                rePlayAds();

                break;
            default:
                break;
        }
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e("Video ads", "Ad Error: " + adErrorEvent.getError().getMessage());

        if (getParentFragment() instanceof ChannelsRestFragment) {
            ((ChannelsRestFragment) getParentFragment()).removeVideoPlayer(true);
        } else {

            mVideoPlayer.play();
            mProgressView.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.VISIBLE);

            postHandler();
        }
    }

    @Override
    public void onResume() {

        //System.out.println("on Resume player");
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.resume();
        } else {
            mVideoPlayer.play();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        // System.out.println("on pause player");
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.pause();
        } else {
            mVideoPlayer.pause();
        }
        super.onPause();
    }


    /*Method to start ads after 5 sec...*/
    private void postHandler() {
        int REPEAT_TIME_INTERVAL = 10000;
        if (mHandler != null && mRunnable != null)
            mHandler.postDelayed(mRunnable, REPEAT_TIME_INTERVAL);
    }

    private void removeHandler() {
        if (mHandler != null && mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
    }

    /*Method to play ads repeatedly...*/
    private void rePlayAds() {
        try {
            if (getView() != null && !(getParentFragment() instanceof ChannelsRestFragment)
                    && !mIsAdDisplayed)
                mPlayButton.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

