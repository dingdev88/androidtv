package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.network.common.AppFonts;
import com.selecttvapp.R;
import com.selecttvapp.common.GlideApp;
import com.selecttvapp.common.MyGlideApp;
import com.selecttvapp.model.Actor;
import com.selecttvapp.ui.views.DynamicImageView;

import java.util.ArrayList;

/**
 * Created by Lenova on 5/8/2017.
 */

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.DataObjectHolder> {
    private ArrayList<Actor> actorList;
    private Activity activity;
    private Typeface MYRIADPRO_REGULAR;

    public ActorsAdapter(Activity activity, ArrayList<Actor> actorList) {
        this.activity = activity;
        this.actorList = actorList;
        MYRIADPRO_REGULAR = Typeface.createFromAsset(activity.getAssets(), AppFonts.MYRIADPRO_REGULAR);
    }

    @Override
    public ActorsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        DynamicImageView view = new DynamicImageView(activity);
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_actors, parent, false);
        return new ActorsAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActorsAdapter.DataObjectHolder holder, int position) {

        Actor actor = actorList.get(holder.getAdapterPosition());
        Log.e("actor_image ", position + "--> " + actor.getImage());

        try {
//            if (!actor.getImage().isEmpty() && actor.getImage() != null && !actor.getImage().equalsIgnoreCase("null")) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                holder.actorImage.getLayoutParams().width = (width / 4) + ((width / 100) * 4);
                holder.actorImage.getLayoutParams().height = (height / 100) * 25;
            } else {
                holder.actorImage.getLayoutParams().width = (height / 4) + ((height / 100) * 4);
                holder.actorImage.getLayoutParams().height = (width / 100) * 25;
            }

            holder.itemView.setPadding(5, 0, 5, 0);
            holder.actorName.getLayoutParams().width = holder.actorImage.getLayoutParams().width;


            if (actor.getImage().isEmpty()) {
                holder.actorImage.setAdjustViewBounds(false);
                holder.actorImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                holder.actorImage.setAdjustViewBounds(true);
                holder.actorImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            holder.actorName.setText(actor.getName());
            GlideApp.with(activity).load(actor.getImage())
                    .placeholder(R.drawable.actor_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.actorImage);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private DynamicImageView actorImage;
        private TextView actorName;

        public DataObjectHolder(View itemView) {
            super(itemView);
            actorImage = (DynamicImageView) itemView.findViewById(R.id.imageView);
            actorName = (TextView) itemView.findViewById(R.id.actorName);
            actorName.setTypeface(MYRIADPRO_REGULAR);
        }
    }
}
