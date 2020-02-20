package com.selecttvapp.channels;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.selecttvapp.ui.views.CustomScrollView;


/**
 * Created by babin on 7/5/2017.
 */

public interface TiimeLineListListener {
    void loadProgramList(ChannelScheduler mChannelScheduler, RecyclerView fragment_channel_program_list_item, int position, CustomScrollView fragment_channel_program_list_horizontal_scroll);


    void loadStream(ChannelScheduler mChannelScheduler, TextView stream_txtChannelName);
}
