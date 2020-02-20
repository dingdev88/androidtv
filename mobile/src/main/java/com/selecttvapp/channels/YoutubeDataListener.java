package com.selecttvapp.channels;

import java.util.ArrayList;


/**
 * Created by babin on 7/17/2017.
 */

public interface YoutubeDataListener {
    public void loadYoutubeVideo(String url, String title, int offset);
    public ArrayList<ProgramList> getProgramList(String mSlug);
}
