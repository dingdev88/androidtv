package com.selecttvapp.channels;


import java.util.ArrayList;


/**
 * Created by babin on 7/5/2017.
 */

public interface StreamApiListener {
    void onStreamLoaded(String categorySlug, String slug, ArrayList<Streams> mStream);
    void onLoadingFailed();

    void onNetworkError();
}

