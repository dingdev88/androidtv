package com.selecttvapp.channels;

import android.net.Uri;

/**
 * Created by babin on 8/10/2017.
 */

public interface OnChannelFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
    void showToolbar(boolean option);
    void showCastIcon(boolean option);
}