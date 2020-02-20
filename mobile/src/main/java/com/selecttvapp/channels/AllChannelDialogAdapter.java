package com.selecttvapp.channels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Image;
import com.selecttvapp.ui.views.DynamicImageView;

import java.util.ArrayList;


/**
 * Created by babin on 7/15/2017.
 */

public class AllChannelDialogAdapter extends RecyclerView.Adapter<AllChannelDialogAdapter.DataObjectHolder> {
    ArrayList<ChannelScheduler> channelModels;
    Context context;
    ChannelDialogGridListener mChannelDialogGridListener;

    public AllChannelDialogAdapter(ArrayList<ChannelScheduler> channelModels, Context context, ChannelDialogGridListener mChannelDialogGridListener) {
        this.channelModels = channelModels;
        this.context = context;
        this.mChannelDialogGridListener=mChannelDialogGridListener;
    }
    @Override
    public AllChannelDialogAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_allchannel_grid, parent, false);
        return new AllChannelDialogAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllChannelDialogAdapter.DataObjectHolder holder, final int position) {
        try {
            holder.txtChannelName.setText(channelModels.get(position).getName());
            if (!channelModels.get(position).getLogo().startsWith("http://") && !channelModels.get(position).getLogo().startsWith("https://")) {
//                holder.imageView.loadImage("http://qtv3.neatsoft.org/" + channelModels.get(position).getLogo());
                Image.loadShowImage("http://qtv3.neatsoft.org/" + channelModels.get(position).getLogo(), holder.imageView);
            } else {
//                holder.imageView.loadImage(channelModels.get(position).getLogo());
                Image.loadShowImage(channelModels.get(position).getLogo(), holder.imageView);

            }
            holder.item_grid.setOnClickListener(v -> {
                ArrayList<ChannelScheduler> listChannels=new ArrayList<ChannelScheduler>();
                listChannels.addAll(channelModels);
                ChannelScheduler selected_ChannelScheduler=listChannels.get(holder.getAdapterPosition());
                listChannels.remove(holder.getAdapterPosition());
                listChannels.add(0,selected_ChannelScheduler);
                mChannelDialogGridListener.onChannelSelected(listChannels);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return channelModels.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        DynamicImageView imageView;
        TextView txtChannelName;
        LinearLayout item_grid;

        public DataObjectHolder(View itemView) {
            super(itemView);
            item_grid = (LinearLayout) itemView.findViewById(R.id.item_grid);
            imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
            txtChannelName = (TextView) itemView.findViewById(R.id.txtChannelName);
        }
    }
}
