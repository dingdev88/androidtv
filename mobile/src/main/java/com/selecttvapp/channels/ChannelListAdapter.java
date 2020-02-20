package com.selecttvapp.channels;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.views.DynamicImageView;

import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.DataObjectHolder> {
    Context context;
    int mlistPosition = 0;

    public ArrayList<ChannelScheduler> getChannel_list_data() {
        return channel_list_data;
    }

    ArrayList<ChannelScheduler> channel_list_data;
    ChannelListListener mChannelListListener;
    View selectedView = null;
    private int mSelectedPosition = 0;

    public ChannelListAdapter(ArrayList<ChannelScheduler> channel_list_data, Context context, ChannelListListener mChannelListListener) {
        this.context = context;
        this.channel_list_data = channel_list_data;
        this.mChannelListListener = mChannelListListener;
    }

    @Override
    public ChannelListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_channel_grid_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChannelListAdapter.DataObjectHolder holder, final int position) {
        ChannelScheduler scheduler = channel_list_data.get(position);
        if (scheduler == null)
            return;

        Log.d(":::name::::", ":::" + channel_list_data.get(position).getName());
        holder.fragment_ondemandlist_items.setText(channel_list_data.get(position).getName());
        if (scheduler.getLogo() != null && !scheduler.getLogo().isEmpty())
            if (!channel_list_data.get(position).getLogo().startsWith("http://") && !channel_list_data.get(position).getLogo().startsWith("https://")) {
                Image.loadGridImage(WebserviceChannelAPI.BASE_URL1 + "/" + channel_list_data.get(position).getLogo(), holder.imageView);
            } else {
                Image.loadGridImage(channel_list_data.get(position).getLogo(), holder.imageView);
            }
        holder.fragment_ondemandlist_items_layout.setTag(channel_list_data.get(position).getSlug());

        holder.fragment_ondemandlist_items_layout.setOnClickListener(v -> {
            if (mSelectedPosition == holder.getAdapterPosition())
                return;
            holder.fragment_ondemandlist_items_layout.setBackgroundColor(Color.parseColor("#0D76BC"));
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.parseColor("#111111"));
            }
            selectedView = holder.fragment_ondemandlist_items_layout;

            mChannelListListener.onChannelListSelected(holder.getAdapterPosition(), holder.fragment_ondemandlist_items_layout.getTag().toString());
        });
        if (position == 0) {
            holder.fragment_ondemandlist_items_layout.setBackgroundColor(Color.parseColor("#0D76BC"));
            selectedView = holder.fragment_ondemandlist_items_layout;
        } /*else {
            holder.fragment_ondemandlist_items_layout.setBackgroundColor(Color.parseColor("#111111"));
        }*/

        holder.fragment_ondemandlist_items_layout.setOnTouchListener((v, event) -> {
            try {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.fragment_ondemandlist_items_layout.setAlpha(0.5f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.fragment_ondemandlist_items_layout.setAlpha(1f);
                } else {
                    holder.fragment_ondemandlist_items_layout.setAlpha(1f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return false;
        });
    }

    @Override
    public int getItemCount() {
        return channel_list_data.size();
    }

    public void addChannelListData(ArrayList<ChannelScheduler> newChannelSchedulerList) {
        //int end=getItemCount();
        //channel_list_data.addAll(newChannelSchedulerList);
        notifyDataSetChanged();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        LinearLayout fragment_ondemandlist_items_layout;
        TextView fragment_ondemandlist_items;
        DynamicImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.item_grid1);
            fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
            imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);

        }
    }

    public void swapChannelList(int pos) {
        int itemCount = getItemCount();
        if (itemCount == 0 && pos < 0 && pos >= itemCount)
            return;
        ChannelScheduler mChannelScheduler = channel_list_data.get(pos);
        channel_list_data.remove(pos);
        channel_list_data.add(0, mChannelScheduler);
        notifyItemMoved(pos, 0);
    }

}
