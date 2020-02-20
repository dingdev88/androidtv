package com.selecttvapp.ui.fragments;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvapp.R;

/**
 * Created by babin on 8/18/2017.
 */

public class DialogBinder {
    private Dialog mView;
    public RecyclerView restHorizontalListview,restHorizontalChannelList;
    public ImageView restLeftSlide,restRightSlide,restHorizontalListviewChannelClose;
    public ProgressBar restHorizontalChannelListProgressBar;
    public TextView restHorizontalListviewItemCount;

    public DialogBinder(Dialog mView) {
        this.mView=mView;
    }
    public void initializeview(){
        restHorizontalListview=(RecyclerView)mView.findViewById(R.id.rest_horizontal_listview);
        restHorizontalChannelList=(RecyclerView)mView.findViewById(R.id.rest_horizontal_channel_list);
        restLeftSlide=(ImageView)mView.findViewById(R.id.rest_left_slide);
        restRightSlide=(ImageView)mView.findViewById(R.id.rest_right_slide);
        restHorizontalListviewChannelClose=(ImageView)mView.findViewById(R.id.rest_horizontal_listview_channel_close);
        restHorizontalChannelListProgressBar=(ProgressBar)mView.findViewById(R.id.rest_horizontal_channel_list_progressBar);
        restHorizontalListviewItemCount=(TextView)mView.findViewById(R.id.rest_horizontal_listview_item_count);

    }
}
