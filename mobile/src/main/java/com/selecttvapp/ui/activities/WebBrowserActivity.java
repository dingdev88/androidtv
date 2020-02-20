package com.selecttvapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.WebView.AdvancedWebView;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;


public class WebBrowserActivity extends Activity implements View.OnClickListener {

    ImageButton imageBack;
    AdvancedWebView webView;
    TextView txtTitle;
    ProgressBar horizProgressbar;

    String url;
    //    String ua = "Mozilla/5.0 (Android;) Gecko/20.0 Firefox/20.0";
    String ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";
//    String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);

        imageBack = (ImageButton) findViewById(R.id.imageBack);
        horizProgressbar = (ProgressBar) findViewById(R.id.horizProgressbar);
        horizProgressbar.setProgress(0);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        webView = (AdvancedWebView) findViewById(R.id.webView);
        webView.getSettings().setUserAgentString(ua);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //webView.setInitialScale(200);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels;
        final int width = displaymetrics.widthPixels;


        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                webView.scrollTo(width / 2, 0);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                horizProgressbar.setVisibility(View.VISIBLE);
                horizProgressbar.setProgress(0);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                horizProgressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    // Hide the progressbar
                    horizProgressbar.setVisibility(View.GONE);
                }
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
//        url = "http://stv9.neatsoft.info/offers/antenna-range-finder/?mode=mobile";
        String name = bundle.getString("name");


        txtTitle.setText(name);
        webView.loadUrl(url);
        webView.requestFocus();

        imageBack.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else super.onBackPressed();
    }

    private String extractePackageName(String appLink) {
        if (appLink.equalsIgnoreCase("none")) {
            return null;
        }
        String[] packageName = appLink.split("=");
        String[] refinedPackgNameList;
        String refinedPackgName = null;
        if (packageName.length > 1) {
            refinedPackgNameList = packageName[1].split("&");
            refinedPackgName = refinedPackgNameList[0];
        } else {
            /*if( packageName.length > 0 ) {
                refinedPackgName = packageName[0];
            }*/
            return null;
        }
        return refinedPackgName;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.WEB_BROWSER_SCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (v == imageBack) {
            finish();
        }
    }


}
