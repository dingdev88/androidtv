package com.selecttvapp.channels;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.DeveloperKey;
import com.selecttvapp.common.FileUtils;
import com.selecttvapp.common.Utilities;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by babin on 7/8/2017.
 */

public class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {
    private String currentVideoID = "video_id";
    private static YouTubePlayer activePlayer;
    private static int currentPlayingIndex = 0;
    private static int offset = 0;
    private static ArrayList<ProgramList> newProgramList = new ArrayList<>();
    private static Runnable runable;
    private static Handler handler;
    private String currentId = "";
    private static YoutubeDataListener mYoutubeDataListener;
    private static String selectedSlug = "";
    private static boolean isPaused = false;
    private static boolean playerInitialized = false;

    public static void setPlayerYouTubeFrag(PlayerYouTubeFrag playerYouTubeFrag) {
        PlayerYouTubeFrag.playerYouTubeFrag = playerYouTubeFrag;
    }

    static PlayerYouTubeFrag playerYouTubeFrag;

    public static PlayerYouTubeFrag getPlayerYouTubeFrag() {
        return playerYouTubeFrag;
    }

    public static PlayerYouTubeFrag newInstance(ArrayList<ProgramList> programlist, YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;
        newProgramList.clear();
        newProgramList.addAll(programlist);
        playerYouTubeFrag = new PlayerYouTubeFrag();
        PlayerYouTubeFrag.currentPlayingIndex = getCurrentplaylistIndex(programlist);
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentPlaylist(programlist);
        offset = getCurrentPlaylistOffset(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", offset);
        bundle.putInt("pos", 0);
        bundle.putInt("type", 1);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        /*playerYouTubeFrag.init();

        setBackgroundtask();*/

        return playerYouTubeFrag;
    }

    public static PlayerYouTubeFrag newInstance(ArrayList<ProgramList> programlist, int pos, YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;
        newProgramList.clear();
        newProgramList.addAll(programlist);
        playerYouTubeFrag = new PlayerYouTubeFrag();
        PlayerYouTubeFrag.currentPlayingIndex = pos;
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentAllPlaylist(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", 0);
        bundle.putInt("pos", pos);
        bundle.putInt("type", 2);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        //playerYouTubeFrag.init();


        return playerYouTubeFrag;
    }

    public static PlayerYouTubeFrag newInstance(String title, String mData, YoutubeDataListener mYoutubeDataListener, String mSlug) {
        PlayerYouTubeFrag.mYoutubeDataListener = mYoutubeDataListener;
        playerYouTubeFrag = new PlayerYouTubeFrag();
        Bundle bundle = new Bundle();
        bundle.putString("data", mData);
        bundle.putString("title", title);
        bundle.putInt("type", 3);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.setArguments(bundle);
        // playerYouTubeFrag.initEmbedStream();

        return playerYouTubeFrag;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initEmbedStream() {
        try {
            initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                }

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    playerInitialized = true;
                    activePlayer = player;
                    activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    activePlayer.setShowFullscreenButton(true);
                    activePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                    activePlayer.setManageAudioFocus(true);
                    activePlayer.setFullscreen(false);
                    activePlayer.setPlayerStateChangeListener(new MyPlayerStateChangeListener());
                    activePlayer.setPlaybackEventListener(new MyPlayerEventChangeListener());
                    if (!wasRestored) {
                        String data = getArguments().getString("data");
                        String title = getArguments().getString("title");
                        if (data.contains("videoseries")) {
                            String id = ChannelUtils.getYoutubePlaylistId(data);
                            activePlayer.loadPlaylist(id);
                            mYoutubeDataListener.loadYoutubeVideo(id, title, 0);
                            currentId = id;
                        } else {
                            String id = ChannelUtils.getYoutubeId(data);
                            activePlayer.loadVideo(id);
                            mYoutubeDataListener.loadYoutubeVideo(id, title, 0);
                            currentId = id;
                        }
                        playPausePlayer();


                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null && runable != null) {
            handler.removeCallbacks(runable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.YOUTUBE_PLAYER_SCREEN);
        selectedSlug = getArguments().getString("slug");
        int type = getArguments().getInt("type");
        if (type == 1) {
            init();
            setBackgroundtask();
        } else if (type == 2) {
            init();
        }
        if (type == 3) {
            initEmbedStream();
        }
        Log.d("test:::", "::type==" + type);
    }

    private static void setBackgroundtask() {
        handler = new Handler();
        runable = () -> {
            if (runable != null && playerInitialized) {
                runbgTask();
            } else if (runable != null)
                handler.postDelayed(runable, 0);

        };
        if (runable != null) {
            handler.postDelayed(runable, 0);
        }

    }

    private static void runbgTask() {
        try {
            int newIndex = VideoFilter.getCurentVideoPosition(newProgramList);
            //seekoffset!=0 to check whether its called first time or not
            //to check current video playing for the time
            //Log.d("test:::","::CurrentIndex=="+currentPlayingIndex+":::NewIndes=="+newIndex);

            if (newIndex > 0 && newIndex != currentPlayingIndex) {
                currentPlayingIndex = newIndex;
                if (activePlayer != null) {
                    activePlayer.loadVideos(getCurrentPlaylist(newProgramList), 0, 0);
                    playPausePlayer();
                    mYoutubeDataListener.loadYoutubeVideo(getCurrentPlaylist(newProgramList).get(0), newProgramList.get(newIndex).getName(), 0);
                }

                if (handler != null)
                    handler.postDelayed(runable, 1000);
            } else if (newIndex < 0 && currentPlayingIndex >= newProgramList.size() - 1) {
                ArrayList<ProgramList> updatedList = mYoutubeDataListener.getProgramList(selectedSlug);
                newProgramList.clear();
                newProgramList.addAll(updatedList);
                if (handler != null)
                    handler.postDelayed(runable, 10);
            } else {
                if (handler != null)
                    handler.postDelayed(runable, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        if (getActivity() == null)
            return;

        try {
            initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                }

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    try {
                        playerInitialized = true;
                        activePlayer = player;
                        activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        activePlayer.setShowFullscreenButton(true);
                        activePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
                        activePlayer.setManageAudioFocus(true);
                        activePlayer.setFullscreen(false);
                        activePlayer.setPlayerStateChangeListener(new MyPlayerStateChangeListener());
                        activePlayer.setPlaybackEventListener(new MyPlayerEventChangeListener());
                        if (!wasRestored) {
                            String data = getArguments().getString("url");
                            int off = getArguments().getInt("offset");
                            int pos = getArguments().getInt("pos");
                            ArrayList<String> youtube_playlis = new Gson().fromJson(data, new TypeToken<List<String>>() {
                            }.getType());
                            activePlayer.loadVideos(youtube_playlis, pos, off);
                            playPausePlayer();
                            mYoutubeDataListener.loadYoutubeVideo(youtube_playlis.get(0), newProgramList.get(pos).getName(), off);
                            currentId = youtube_playlis.get(0);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getCurrentPlaylist(ArrayList<ProgramList> programlist) {
        ArrayList<String> youtube_data = new ArrayList<>();
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        if (currentPlayingIndex > 0) {
            ArrayList<PlayList> playList = programlist.get(currentPlayingIndex).getPlaylist();
            for (int j = 0; j < playList.size(); j++) {
                youtube_data.add(playList.get(j).getData());
            }
        }

        return youtube_data;
    }

    private static ArrayList<String> getCurrentAllPlaylist(ArrayList<ProgramList> programlist) {
        ArrayList<String> youtube_data = new ArrayList<>();
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        for (int i = 0; i < programlist.size(); i++) {
            ArrayList<PlayList> playList = programlist.get(i).getPlaylist();
            for (int j = 0; j < playList.size(); j++) {
                youtube_data.add(playList.get(j).getData());
            }
        }

        return youtube_data;
    }

    private static int getCurrentPlaylistOffset(ArrayList<ProgramList> programlist) {
        try {
            return (int) (ChannelUtils.GetUnixTime() - ChannelUtils.getDurationFromDate(programlist.get(currentPlayingIndex).getStart_at()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getCurrentplaylistIndex(ArrayList<ProgramList> programlist) {
        AsyncTask.execute(() -> {
            String data=new Gson().toJson(programlist);
            FileUtils.writeToFile("Logs :: ",
                    data, "selectTvLogs");
        });
        //Logs for channel menu
        return VideoFilter.getCurentVideoPosition(programlist);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayerandHandler();
    }

    private void releasePlayerandHandler() {
        if (activePlayer != null) {
            playerInitialized = false;
            activePlayer.release();
            activePlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runable);
            handler = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayerandHandler();
        playerYouTubeFrag = null;
    }

    public void updatePlayer(int position, ArrayList<ProgramList> mProgramList) {
        if (activePlayer != null) {
            activePlayer.loadVideos(getCurrentAllPlaylist(mProgramList), position, 0);
            playPausePlayer();
        }

    }

    public static void releasePlayer() {
        try {
            if (activePlayer != null) {
                playerInitialized = false;
                activePlayer.release();
                activePlayer = null;
            }
            if (handler != null) {
                handler.removeCallbacks(runable);
                handler = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void playPlayer(boolean isPlay) {
        if (activePlayer != null) {
            if (isPlay)
                activePlayer.play();
            else
                activePlayer.pause();
        }

    }

    public boolean isPlayerPaused() {
        return isPaused;
    }


    public static void setNewData(ArrayList<ProgramList> programlist, String mSlug) {
        releasePlayer();
        newProgramList.clear();
        newProgramList.addAll(programlist);
        PlayerYouTubeFrag.currentPlayingIndex = getCurrentplaylistIndex(programlist);
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentPlaylist(programlist);
        offset = getCurrentPlaylistOffset(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", offset);
        bundle.putInt("pos", 0);
        bundle.putInt("type", 1);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.getArguments().putAll(bundle);
        selectedSlug = mSlug;
        playerYouTubeFrag.init();
        playerYouTubeFrag.setBackgroundtask();
    }

    public static void setNewData(ArrayList<ProgramList> programlist, int pos, String mSlug) {
        releasePlayer();
        newProgramList.clear();
        newProgramList.addAll(programlist);
        PlayerYouTubeFrag.currentPlayingIndex = pos;
        ArrayList<String> youtube_playlist = new ArrayList<>();
        youtube_playlist = getCurrentAllPlaylist(programlist);

        Bundle bundle = new Bundle();
        bundle.putString("url", new Gson().toJson(youtube_playlist));
        bundle.putInt("offset", 0);
        bundle.putInt("pos", pos);
        bundle.putInt("type", 2);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.getArguments().putAll(bundle);
        selectedSlug = mSlug;
        playerYouTubeFrag.init();
    }

    public static void setNewData(String title, String mData, String mSlug) {
        releasePlayer();
        newProgramList.clear();
        Bundle bundle = new Bundle();
        bundle.putString("data", mData);
        bundle.putString("title", title);
        bundle.putInt("type", 3);
        bundle.putString("slug", mSlug);

        playerYouTubeFrag.getArguments().putAll(bundle);
        selectedSlug = mSlug;
        playerYouTubeFrag.initEmbedStream();
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {
            try {
                Log.d("youtube:::", "::::::loaded" + s);
                if (!currentId.equalsIgnoreCase(s)) {
                    mYoutubeDataListener.loadYoutubeVideo(s, "", 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
            if (handler != null && playerInitialized) {
                runbgTask();
            }
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
            Log.d("youtube:::", "::::::error" + errorReason.name());
        }
    }


    private class MyPlayerEventChangeListener implements YouTubePlayer.PlaybackEventListener {
        @Override
        public void onPlaying() {
            isPaused = false;
        }

        @Override
        public void onPaused() {
            isPaused = true;

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

    private static void playPausePlayer() {
        if (isPaused)
            activePlayer.pause();
    }
}