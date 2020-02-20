package com.selecttvapp.channels;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.selecttvapp.R;
import com.selecttvapp.WebView.AdvancedWebView;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebViewPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebViewPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AdvancedWebView player_webView;
    int nWidth = 0;

    public WebViewPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebViewPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebViewPlayerFragment newInstance(String param1, String param2) {
        WebViewPlayerFragment fragment = new WebViewPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.rest_fragment_web_view_player, container, false);
        player_webView = (AdvancedWebView) rootView.findViewById(R.id.player_webView);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        player_webView.removeAllViews();
        player_webView.getSettings().setJavaScriptEnabled(true);
        player_webView.getSettings().setAppCacheEnabled(true);
        player_webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        player_webView.getSettings().setDomStorageEnabled(true);
        player_webView.getSettings().setDatabaseEnabled(true);
        player_webView.getSettings().setLoadWithOverviewMode(true);
        player_webView.getSettings().setUseWideViewPort(false);
        player_webView.getSettings().setGeolocationEnabled(true);
        player_webView.getSettings().setSupportZoom(true);
        player_webView.getSettings().setBuiltInZoomControls(false);
        player_webView.setCookiesEnabled(true);
        player_webView.setMixedContentAllowed(true);
        player_webView.setThirdPartyCookiesEnabled(true);
        player_webView.setLayoutParams(new FrameLayout.LayoutParams(nWidth, 900));
        if (mParam1.contains(".js")) {
            playWebViewJSUrl(mParam1);
        } else {
            playWebViewVideo(mParam1);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.WEBVIEW_PLAYER_SCREEN);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        nWidth = displayMetrics.widthPixels;
        player_webView.setLayoutParams(new FrameLayout.LayoutParams(nWidth, 900));
//        if (mParam1.contains(".js")) {
//            playWebViewJSUrl(mParam1);
//        } else {
//            playWebViewVideo(mParam1);
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onResume();
    }

    private void playWebViewVideo(String playVideo) {
        try {
            String htmlContentInStringFormat = "<!DOCTYPE html><html><body style='margin:0;padding:0;'>" + playVideo + "</body></html>";

            int heig = (9 * (nWidth / 16));
            Log.d("data:::::", ":::calculated" + playVideo);
            if (htmlContentInStringFormat.trim().contains("width=\"100%\"")) {
                htmlContentInStringFormat = htmlContentInStringFormat.replaceAll("height=\"100%\"", "height=\"" + heig + "\"");
                if (htmlContentInStringFormat.trim().contains("width=100%")) {
                    htmlContentInStringFormat = htmlContentInStringFormat.replaceAll("width=100%", "width=\"" + nWidth + "\"");
                }

            }
            String[] ss = htmlContentInStringFormat.split("width=100%");
            player_webView.loadDataWithBaseURL("http://localhost/", htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void playWebViewJSUrl(String playVideo) {
        try {
            String data = playVideo.replace("//content", "http://content");
            data = data.replace("http", "https");
            data = data.replace("httpss", "https");
            String htmlContentInStringFormat = "<html><body style=\"margin:0;padding:0;background: #00000000;\">" + data + "</body></html>";
            player_webView.loadDataWithBaseURL("http://localhost/", htmlContentInStringFormat, "text/html", "utf-8", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
