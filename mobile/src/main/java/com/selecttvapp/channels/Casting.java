package com.selecttvapp.channels;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.selecttvapp.cast.Casty;
import com.selecttvapp.cast.MediaData;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by babin on 7/14/2017.
 */

public class Casting  {

    /*chromecast*/
    private static final String APP_ID = CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID;

    public boolean isCastConnected() {
        return castConnected;
    }

    public void setCastConnected(boolean castConnected) {
        this.castConnected = castConnected;
    }

    private boolean castConnected=false;

    private Activity mActivity;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public int getmOffset() {
        return mOffset;
    }

    public void setmOffset(int mOffset) {
        this.mOffset = mOffset;
    }

    private String mTitle;
    private String mData;
    private int mOffset;

    private Casty casty;

    public Casting(Activity mActivity) {
        this.mActivity=mActivity;
    }

    public void initializeCasty(FragmentActivity activity, MediaRouteButtonHoloDark media_route_button, final Casty.OnConnectChangeListener onConnectChangeListener) {
        casty = Casty.create(mActivity);
        casty.setUpMediaRouteButton(media_route_button);
        casty.setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                onConnectChangeListener.onConnected();
               setCastConnected(true);
                extractStream(mTitle, mData, mOffset);
            }

            @Override
            public void onDisconnected() {
                setCastConnected(false);
                onConnectChangeListener.onDisconnected();
            }
        });
    }

    public void extractStream(final String mTitle, final String mData, final int mOffset) {
        try {
            if(isCastConnected()){
                Log.d("ChromeCast::::", "castVideoFromId:::generating stream");


                final String sThumb = "http://img.youtube.com/vi/" + mData + "/default.jpg";

                String yurl="";
                if(mData.contains("http")){
                    yurl= mData;
                }else{
                    yurl="http://www.youtube.com/watch?v=" + mData;
                }

                Log.d("ChromeCast:::",":::"+yurl);


                new YouTubeExtractor(mActivity) {
                    @Override
                    public void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta vMeta) {
                        if (sparseArray != null) {
                            int itag = 22;
                            String downloadUrl = "";
                            if (sparseArray.get(22) != null) {
                                downloadUrl = sparseArray.get(22).getUrl();
                            } else if (sparseArray.get(18) != null) {
                                downloadUrl = sparseArray.get(18).getUrl();
                            } else if (sparseArray.get(17) != null) {
                                downloadUrl = sparseArray.get(17).getUrl();
                            }else if (sparseArray.get(91) != null) {
                                downloadUrl = sparseArray.get(91).getUrl();
                            }
                            if (downloadUrl.length() > 0) {
                                Log.d("ChromeCast:::",":downloadUrl::"+downloadUrl);

                                playMediaCast(downloadUrl,mTitle,sThumb,mOffset);

                                // if( bRunningThread == false )
                                //    startTestThread();
                            }
                        }
                    }
                }.extract(yurl, true, true);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playMediaCast(String url, String sTitle, String sThumbUrl, int nTimeOffset) {

        casty.getPlayer().loadMediaAndPlay(createSampleMediaData(url,sTitle,sThumbUrl,nTimeOffset));
    }

    private static MediaData createSampleMediaData(String url, String sTitle, String sThumbUrl, int nTimeOffset) {
        return new MediaData.Builder(url)
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("video/webM")
                .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
                .setTitle(sTitle)
                .addPhotoUrl(sThumbUrl)
                .setPosition(nTimeOffset)
                .build();
    }
    private static MediaInfo createSampleMediaInfoData() {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        return new MediaInfo.Builder("https://r2---sn-3oxupo-h55e.googlevideo.com/videoplayback?ip=103.219.207.161&key=yt6&ms=au&mt=1502111143&mv=m&id=o-AKQDBXjyUL4fkijC-dTKnPVT8jJx4vENlIoITfdUmBaQ&mm=31&mn=sn-3oxupo-h55e&signature=D1FEAA70E3AFD704AF204989D9488E23C6D6D353.2BCF9B13C3F51E613CA7EBBC0F2F2D96E1D55C03&pcm2cms=yes&ipbits=0&initcwndbps=2617500&dur=652.248&itag=22&pl=24&source=youtube&expire=1502132866&mime=video%2Fmp4&lmt=1470979056783232&ei=ImaIWeR_iOShA-GPj_gF&requiressl=yes&ratebypass=yes&sparams=dur%2Cei%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire")
                .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
                .setContentType("video/webM")
                .setMetadata(movieMetadata)
                .build();
    }

    public void stopCasting() {
        casty.stopCasting();
    }
}
//.setContentType("video/webM")
//setContentType("application/x-mpegURL")
//.setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)


