package com.selecttvapp.channels;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.selecttvapp.R;
import com.selecttvapp.WebView.AdvancedWebView;
import com.selecttvapp.common.DeveloperKey;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PlayerFragment extends Fragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaylistEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ChannelScheduler mChannelScheduler;

    private FrameLayout youtube_fragment;
    VideoView channel_video_player;
    AdvancedWebView channel_video_player_webview;
    LinearLayout channel_video_player_webview_layout;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlaylistEventListener mPlaylistEventListener;
    private YouTubePlayer youtube_player;

    private OnFragmentInteractionListener mListener;
    List<String> youtube_data=new ArrayList<>();
    int moffset=0;


    public PlayerFragment() {
        // Required empty public constructor
    }


    public static PlayerFragment newInstance(ChannelScheduler data, String param2) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(data));
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
        mChannelScheduler = new Gson().fromJson(mParam1, ChannelScheduler.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.rest_fragment_player, container, false);
        initializeViews(rootView);
        setVideoType(mChannelScheduler);


        return rootView;
    }

    private void setVideoType(ChannelScheduler mPrograms) {
        if(mPrograms!=null){
            String program_type=mPrograms.getType();
            String video_type="";
            if(program_type.equalsIgnoreCase("live/video")||program_type.equalsIgnoreCase("live/radio")){
                video_type=mPrograms.getStreams().get(0).getType();
            }else{
                video_type=mPrograms.getPrograms().getProgramlist().get(0).getPlaylist().get(0).getType();
            }
            switch (video_type) {
                case "embed":
                    playVideoInWebView(mPrograms.getStreams().get(0).getData());
                    break;
                case "stream":
                    playVideoInVideoView(mPrograms.getStreams().get(0).getData());
                    break;
                case "custom":
                    playVideoInYoutubeView(mPrograms.getPrograms().getProgramlist());
                    break;
                case "src":
                    extractStreamUrl(mPrograms.getStreams().get(0).getData());
                    break;
                case "":
                    break;
            }
        }
    }


    private void playVideoInYoutubeView(ArrayList<ProgramList> programlist) {
        youtube_fragment.setVisibility(View.VISIBLE);
        channel_video_player_webview_layout.setVisibility(View.GONE);
        channel_video_player.setVisibility(View.GONE);
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        int newIndex= VideoFilter.getCurentVideoPosition(programlist);
        for(int i=newIndex;i<programlist.size();i++){
            ArrayList<PlayList> playList=programlist.get(i).getPlaylist();
            for(int j=0;j<playList.size();j++){
                youtube_data.add(playList.get(j).getData());
            }

        }

        moffset = (int) (nowAsPerDeviceTimeZone - ChannelUtils.getDurationFromDate(programlist.get(newIndex).getStart_at()));
        loadYoutubevideos(youtube_data, moffset);

    }
    public void loadYoutubevideos(List<String> list, int offset) {
        try {

            if (youtube_player != null) {
                youtube_player .loadVideos(list, 0, offset);
            }
        } catch (Exception e) {
            youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
            e.printStackTrace();
        }
    }

    private void playVideoInVideoView(String data) {
        if (youtube_player != null) {
            try {
                youtube_player .setFullscreen(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtube_player .release();
        }
        youtube_fragment.setVisibility(View.GONE);
        channel_video_player_webview_layout.setVisibility(View.GONE);
        channel_video_player.setVisibility(View.VISIBLE);
        final int[] i = {0};
        Log.d("Videoplayer", data);
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(channel_video_player);
        channel_video_player.setVideoURI(Uri.parse(data));
        channel_video_player.requestFocus();
        channel_video_player.start();


    }

    private void playVideoInWebView(String data) {

    }

    private void initializeViews(View rootView) {
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);



        youtube_fragment = (FrameLayout) rootView.findViewById(R.id.youtube_fragment);
        channel_video_player = (VideoView) rootView.findViewById(R.id.channel_video_player);
        channel_video_player_webview_layout = (LinearLayout) rootView.findViewById(R.id.channel_video_player_webview_layout);
        channel_video_player_webview = (AdvancedWebView) rootView.findViewById(R.id.channel_video_player_webview);

        channel_video_player_webview.setWebChromeClient(new myWebChromeClient());
        channel_video_player_webview.getSettings().setJavaScriptEnabled(true);
        channel_video_player_webview.getSettings().setAppCacheEnabled(true);
        channel_video_player_webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        channel_video_player_webview.getSettings().setDomStorageEnabled(true);
        channel_video_player_webview.getSettings().setDatabaseEnabled(true);
        channel_video_player_webview.getSettings().setGeolocationEnabled(true);
        channel_video_player_webview.getSettings().setSupportZoom(true);
        channel_video_player_webview.getSettings().setBuiltInZoomControls(false);
        channel_video_player_webview.getSettings().setLoadWithOverviewMode(true);
        channel_video_player_webview.getSettings().setUseWideViewPort(true);
        channel_video_player_webview.setCookiesEnabled(true);
        channel_video_player_webview.setMixedContentAllowed(true);
        channel_video_player_webview.setThirdPartyCookiesEnabled(true);


        //Event Listeneres for youtube
        /*playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
        mPlaylistEventListener = new MyPlaylistEventListener();*/

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youtube_player= youTubePlayer;
            youtube_player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youtube_player.setPlayerStateChangeListener(playerStateChangeListener);
            youtube_player.setPlaybackEventListener(playbackEventListener);
            youtube_player.setPlaylistEventListener(mPlaylistEventListener);
            youtube_player.setShowFullscreenButton(true);
            youtube_player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
            youtube_player.setManageAudioFocus(true);
            youtube_player.setFullscreen(false);

            if(youtube_data.size()>0&&youtube_fragment.getVisibility()== View.VISIBLE){
                loadYoutubevideos(youtube_data, moffset);
            }
        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

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

    class myWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return false;
        }

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChannelFragmentInteractionListener) {
            mListener = (OnChannelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChannelFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void extractStreamUrl(final String data) {
        final List<String> cue_list = new ArrayList<>();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String json_string = "";
                int timeout = 100 * 10000;

                // Extract video URL
                try {
                    Document doc = Jsoup.connect(data).timeout(timeout).get();
                    Element script = doc.getElementsByTag("script").first();
                    String content = doc.data();


                    Pattern pattern = Pattern.compile("\\window.__data=(.*?)\\;");
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        System.out.println(matcher.group(1));
                        json_string = matcher.group(1);
                    }

                    int i = content.indexOf("url");

                    if (!TextUtils.isEmpty(json_string)) {
                        JSONObject string_Object = new JSONObject(json_string);

                        JSONObject video_object = (string_Object.getJSONObject("video")).getJSONObject("byId");

                        if (video_object != null && video_object.length() > 0) {
                            Iterator x = video_object.keys();
                            if (x.hasNext()) {
                                String key = (String) x.next();
                                JSONObject url_object = video_object.getJSONObject(key);
                                String video_url = url_object.getString("url");
                                if (!video_url.startsWith("https:")) {
                                    video_url = "https:" + video_url;
                                }
                                Log.d("video url:::", ":::" + video_url);
                                final String play_url = video_url;


                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cue_list.add(play_url);
                                        playVideoInVideoView(play_url);
                                    }
                                });


                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
