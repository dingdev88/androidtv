package com.selecttvapp.channels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Image;
import com.selecttvapp.ui.views.DynamicImageView;

import java.util.ArrayList;


/**
 * Altered by Elias on 7/11/2017.
 */

public class ShowListAdapter extends RecyclerView.Adapter<ShowListAdapter.DataObjectHolder> {
    Context context;
    int mlistPosition = 0;
    ArrayList<ProgramList> list_data;
    ShowListListener mShowListListener;
    String StreamType;
    String strThumbUrl = "";
    public ShowListAdapter(ArrayList<ProgramList> list_data, Context context, ShowListListener mShowListListener) {
        this.context = context;
        this.list_data = list_data;
        this.mShowListListener = mShowListListener;
        StreamType = list_data.get(1).getPlaylist().get(0).getType();
    }

    @Override
    public ShowListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_row_channel_details, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowListAdapter.DataObjectHolder holder, int position) {

        try {
            holder.txtVideoName.setText(list_data.get(position).getName());
            String strTextTime = ChannelUtils.parseDateToddMMyyyy(list_data.get(position).getStart_at());

            holder.txtStartTime.setText(String.valueOf(strTextTime));
//                holder.txtStartTime.setText(list_data.get(position).getStart_at());
            if(StreamType.equals("url"))
            {
                strThumbUrl = list_data.get(position).getPlaylist().get(0).getThumbnail();
            }
            else {
                strThumbUrl = "http://img.youtube.com/vi/" + list_data.get(position).getPlaylist().get(0).getData() + "/default.jpg";
            }
//            holder.imgVideoThumbnail.loadImage(strThumbUrl);
            Image.loadShowImage(strThumbUrl, holder.imgVideoThumbnail);


            holder.item_layout.setOnClickListener(v -> mShowListListener.onShowListItemSelected(holder.getAdapterPosition(), list_data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txtVideoName;
        TextView txtStartTime;
        DynamicImageView imgVideoThumbnail;
        RelativeLayout item_layout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txtVideoName = (TextView) itemView.findViewById(R.id.ui_video_row_txt_tittle);
            txtStartTime = (TextView) itemView.findViewById(R.id.ui_video_row_txt_time);
            imgVideoThumbnail = (DynamicImageView) itemView.findViewById(R.id.ui_video_row_image);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
        }
    }
}