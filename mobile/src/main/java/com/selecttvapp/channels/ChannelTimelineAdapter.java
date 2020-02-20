package com.selecttvapp.channels;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.selecttvapp.R;

import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public class ChannelTimelineAdapter extends RecyclerView.Adapter<ChannelTimelineAdapter.DataObjectHolder> {
    private static int PROGRAM = 1;
    private static int LIVE = 0;
    Context context;
    int mlistPosition = 0;
    ArrayList<ChannelScheduler> channel_list;
    private TimelineListListener mTimelineListListener;
    private Handler handler = new Handler();

    public ChannelTimelineAdapter(ArrayList<ChannelScheduler> channel_list, Context context, TimelineListListener mTimelineListListener) {
        this.context = context;
        this.channel_list = channel_list;
        this.mTimelineListListener = mTimelineListListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_channeltimelineitem, parent, false);
        return new DataObjectHolder(view);
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        ChannelScheduler mChannelScheduler = channel_list.get(position);
        holder.bind(mChannelScheduler);
    }

    @Override
    public int getItemCount() {
        return channel_list.size();
    }

    public void swapChannelTimeline(int adapterPosition) {
        ChannelScheduler mChannelScheduler = channel_list.get(adapterPosition);
        channel_list.remove(adapterPosition);
        channel_list.add(0, mChannelScheduler);
        notifyItemMoved(adapterPosition, 0);
    }

    public void addTimeLineData(ArrayList<ChannelScheduler> filteredList) {
        final int positionStart = channel_list.size() + 1;
        int previousSize = getItemCount() - 1;
//        channel_list.addAll(filteredList);
        for (ChannelScheduler item : filteredList) {
            channel_list.add(item);
            this.notifyItemChanged(previousSize++);
        }
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        FrameLayout frame_layout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            frame_layout = (FrameLayout) itemView.findViewById(R.id.frame_layout);
        }

        void bind(ChannelScheduler mChannelScheduler) {
            Log.e("posiiton", getAdapterPosition() + "");
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            try {
                int containerId = frame_layout.getId();// Get container id
                Fragment oldFragment = fragmentManager.findFragmentById(containerId);
                if (oldFragment != null) {
                    fragmentManager.beginTransaction().remove(oldFragment).commitNow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//

            try {
                int newContainerId = ChannelUtils.GetUniqueID();// My method
                frame_layout.setId(newContainerId);// Set container id

                ScrollingFragment fragment = ScrollingFragment.newInstance(mChannelScheduler, getAdapterPosition());
                mTimelineListListener.onFragmentQueued(fragmentManager, newContainerId, mChannelScheduler, getAdapterPosition(), fragment);
                fragmentManager.beginTransaction().replace(newContainerId, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame_layout.setVisibility(View.VISIBLE);
//            frame_layout.setTag(channel_list.get(getAdapterPosition()).getSlug());

            frame_layout.setOnClickListener(v -> {
//                String slug = frame_layout.getTag().toString();
                mTimelineListListener.onTimelineListSelected(getAdapterPosition(), mChannelScheduler.getSlug());
            });
        }
    }


}
