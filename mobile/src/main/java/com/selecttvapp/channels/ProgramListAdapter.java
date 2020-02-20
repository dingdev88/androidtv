package com.selecttvapp.channels;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.ui.views.CustomScrollView;

import java.util.ArrayList;


/**
 * Created by babin on 7/5/2017.
 */

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.DataObjectHolder> {
    Context context;
    int mlistPosition = 0;
    int mListenMainSubSize;
    ArrayList<ProgramList> video_list=new ArrayList<>();
    ArrayList<ProgramList> displayVideo_list=new ArrayList<>();
    CustomScrollView customScrollView;
    String type;
    int cellwidth;
    TImelineItemListener mTImelineItemListener;
    int nWidth=0;
    int nHeight=0;

    public ProgramListAdapter(ArrayList<ProgramList> displayVideo_list, Context context, CustomScrollView customScrollView, String type, int mListenMainSubSize, TImelineItemListener mTImelineItemListener) {
        this.context = context;
        this.displayVideo_list = displayVideo_list;
        this.customScrollView = customScrollView;
        this.type = type;
        this.mListenMainSubSize = mListenMainSubSize;
        this.mTImelineItemListener=mTImelineItemListener;
        if(displayVideo_list.size()<=4){
            video_list.addAll(displayVideo_list);
        }else{
            for(int i=0;i<4;i++){
                video_list.add(displayVideo_list.get(i));
            }
        }


        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            nHeight= displayMetrics.widthPixels;
            nWidth = displayMetrics.heightPixels;

        }else{
            nWidth = displayMetrics.widthPixels;
            nHeight = displayMetrics.heightPixels;
        }
        cellwidth= (nWidth - ChannelUtils.convertDpToPixels(context)) / 3;

        //video_list.add(0,new ProgramList());


    }

    @Override
    public ProgramListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_channel_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgramListAdapter.DataObjectHolder holder, int position) {
      //  if(position!=0){
            ProgramList mProgramList=video_list.get(position);
            holder.fragment_ondemandlist_items.setText(mProgramList.getName());
            String strTextTime = ChannelUtils.parseDateToddMMyyyy(video_list.get(position).getStart_at());
            holder.txtChannel_time.setText(String.valueOf("Start Time: " + strTextTime));
       // }
        holder.cell_layout.setTag(video_list.get(position).getUuid());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = cellwidth;
        holder.item_grid.setLayoutParams(layoutParams);
        holder.cell_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cell_layout.getTag()!=null){
                    mTImelineItemListener.onTimelineItemSelected(holder.cell_layout.getTag().toString());

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }

    public void setVideoData(ArrayList<ProgramList> allProgramList) {
        video_list.clear();
        displayVideo_list.clear();
        video_list.addAll(allProgramList);
        displayVideo_list.addAll(allProgramList);
        notifyDataSetChanged();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        LinearLayout fragment_ondemandlist_items_layout,cell_layout;
        TextView fragment_ondemandlist_items, txtChannel_time;
        RelativeLayout item_grid;

        public DataObjectHolder(View itemView) {
            super(itemView);
            fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
            txtChannel_time = (TextView) itemView.findViewById(R.id.txtChannel_time);
            item_grid = (RelativeLayout) itemView.findViewById(R.id.item_grid);
            cell_layout = (LinearLayout) itemView.findViewById(R.id.cell_layout);

        }
    }
    public void addVideoData(ArrayList<ProgramList> mProgramList){
        int startPos=mProgramList.size();
        video_list.addAll(mProgramList);
        notifyItemChanged(startPos,mProgramList.size());
    }
    public void addVideoItem(ProgramList mProgramList){
        video_list.add(mProgramList);
        notifyDataSetChanged();
    }
    public void newIndex(int index){
        if(displayVideo_list.size()>video_list.size()){
            video_list.add(displayVideo_list.get(video_list.size()));
            notifyDataSetChanged();
        }

    }


}
