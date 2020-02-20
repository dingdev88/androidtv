package com.selecttvapp.ui.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.Utilities;


public class VideoViewActivity extends AppCompatActivity {
    public static final String VIDEO_URL = "video_url";

    private WebView webView;
    private String videoUrl;
    private ProgressBar horizProgressbar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        horizProgressbar = (ProgressBar) findViewById(R.id.horizProgressbar);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                horizProgressbar.setVisibility(View.VISIBLE);
                horizProgressbar.setProgress(progress);
                if (progress >= 100)
                    horizProgressbar.setVisibility(View.GONE);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                horizProgressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                horizProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                horizProgressbar.setVisibility(View.GONE);
            }
        });
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setScrollbarFadingEnabled(false);
        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);

        try {
            videoUrl = getIntent().getStringExtra(VIDEO_URL);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            String frameWidth = ((width / 100) * 53) + "";
            String frameHeight = height + "";

            if (InternetConnection.isConnected(this)) {
                String frameVideo = "<html><head> <meta name=\"viewport\" content=\"width=" + frameWidth + ",user-scalable = no \" ></head> <body style=\"margin:0;padding:0;background: #00000000;\"><iframe allowtransparency=\"true\" style=\"background: #00000000;\" width=\"100%\" height=\"100%\" src=" + videoUrl + " frameborder=\"0\" allowfullscreen></iframe></body></html>";
                webView.loadData(frameVideo, "text/html", "utf-8");

            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.VIDEO_VIEW_SCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            webView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
