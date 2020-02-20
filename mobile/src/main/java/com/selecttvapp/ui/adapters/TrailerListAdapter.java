package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.selecttvapp.R;
import com.selecttvapp.common.DeveloperKey;
import com.selecttvapp.ui.activities.VideoViewActivity;
import com.selecttvapp.ui.dialogs.ProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Appsolute dev on 01-Aug-17.
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.DataObjectHolder> {
    private ArrayList<String> trailers;
    private Activity activity;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private boolean hasVideosIds;

    public TrailerListAdapter(Activity activity, ArrayList<String> trailers, boolean hasVideosIds) {
        this.activity = activity;
        this.trailers = trailers;
        this.hasVideosIds = hasVideosIds;
        thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
    }

    @Override
    public TrailerListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.video_view, parent, false);
        return new TrailerListAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerListAdapter.DataObjectHolder holder, int position) {
        final String video = trailers.get(position);
        Log.e("video ", position + "--> " + video);

        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            String frameWidth = ((width / 100) * 90.9) + "";

            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                frameWidth = ((width / 100) * 53) + "";
            else frameWidth = ((width / 100) * 90.9) + "";

            final Uri uri = Uri.parse(video);
            if (hasVideosIds) {
                holder.thumbnail.setVisibility(View.VISIBLE);
                holder.thumbnail.setTag(video);

            } else if ("www.youtube.com".equals(uri.getHost())) {
                if (getVideoIdFromYoutube(video).find()) {
                    holder.thumbnail.setVisibility(View.VISIBLE);
                    holder.thumbnail.setTag(video.replace("https://www.youtube.com/watch?v=", ""));
                } else {
                    holder.webView.setVisibility(View.VISIBLE);
                    loadWebViewVideo(holder, video, frameWidth);
                }
            } else {
                holder.webView.setVisibility(View.VISIBLE);
                loadWebViewVideo(holder, video, frameWidth);
            }


            holder.playIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hasVideosIds) {
                        loadYoutubeVideo(video);
                    } else if ("www.youtube.com".equals(uri.getHost())) {
                        Matcher matcher = getVideoIdFromYoutube(video);
                        if (matcher.find()) {
                            loadYoutubeVideo(matcher.group());
                        }
                    } else {
                        Intent newIntent = new Intent(activity, VideoViewActivity.class);
                        newIntent.putExtra(VideoViewActivity.VIDEO_URL, video);
                        activity.startActivity(newIntent);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumb,
                playIcon;
        private WebView webView;
        private YouTubeThumbnailView thumbnail;
        private FrameLayout layoutViewsContainer;
        private FrameLayout layoutPlayIcon;
        private ProgressHUD mProgressHUD;
        private TrailerListAdapter.ThumbnailListener thumbnailListener = new TrailerListAdapter.ThumbnailListener();

        public DataObjectHolder(View view) {
            super(view);

            //webview
            webView = (WebView) view.findViewById(R.id.webView);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Return the app name after finish loading
                    try {
//                        if (progress == 100)
//                            mProgressHUD.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            webView.setWebViewClient(new WebViewClient());
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setScrollbarFadingEnabled(false);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            playIcon = (ImageView) view.findViewById(R.id.playIcon);
            layoutViewsContainer = (FrameLayout) view.findViewById(R.id.viewsContainer);
            layoutPlayIcon = (FrameLayout) view.findViewById(R.id.layoutPlayIcon);
            layoutPlayIcon.setEnabled(false);

            // videoThumb = itemView;
//            videoThumb = (ImageView) view.findViewById(R.id.imageView);
//            videoThumb.setAdjustViewBounds(true);
//            videoThumb.setScaleType(ImageView.ScaleType.FIT_XY);


            thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(TrailerListAdapter.DataObjectHolder holder) {
        super.onViewDetachedFromWindow(holder);
        try {
            if (holder.mProgressHUD != null)
                holder.mProgressHUD.dismiss();
            if (holder.playIcon != null)
                holder.playIcon.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrameLayout.LayoutParams getDefaultLayoutParams(int height) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, 0, 0, 10);
        return layoutParams;
    }

    private Matcher getVideoIdFromYoutube(String video) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        return compiledPattern.matcher(video);
    }

    private void loadWebViewVideo(TrailerListAdapter.DataObjectHolder holder, String video, String frameWidth) {
//        if (!video.isEmpty())
//            holder.mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, true, null);
//        String frameVideo = "<html><head> <meta name=\"viewport\" content=\"width=" + frameWidth + ",user-scalable = no \" ></head> <body style=\"margin:0;padding:0;background: #00000000;\"><iframe allowtransparency=\"true\" style=\"background: #00000000;\" width=\"100%\" height=\"100%\" src=" + video + " frameborder=\"0\" allowfullscreen></iframe></body></html>";
        String frameVideo = "<html><head> <meta name=\"viewport\" content=\"user-scalable = no \" ></head> <body style=\"margin:0;padding:0;background: #00000000;\"><iframe allowtransparency=\"true\" style=\"background: #00000000;\" width=\"100%\" height=\"100%\" src=" + video + " frameborder=\"0\" allowfullscreen></iframe></body></html>";
        holder.webView.loadData(frameVideo, "text/html", "utf-8");
    }

    private void loadYoutubeVideo(String videoId) {
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity).equals(YouTubeInitializationResult.SUCCESS)) {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) activity,
                    DeveloperKey.DEVELOPER_KEY,
                    videoId,//video id
                    100,     //after this time, video will start automatically
                    true,               //autoplay or not
                    false);             //lightbox mode or not; show the video in a small box
            activity.startActivity(intent);
        }
    }

    private class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
        }
    }
}
