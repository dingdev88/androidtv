package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Dimension;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.common.Image;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.GridViewItem;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Aug-17.
 */

public class CarouselsAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private ArrayList<HorizontalListitemBean> listItems;
    private ArrayList<AsyncTask> mLoadImageAsyncList = new ArrayList<>();
    private CarouselsListener callback;
    private int carouselId = -1;
    private int payMode = RabbitTvApplication.getInstance().getPaymode();
    private final int VIEW_ITEM = 1;
    private final int VIEW_VIEWMORE = 2;

    public CarouselsAdapter(Activity activity, int carouselId, ArrayList<HorizontalListitemBean> listItems, int payMode, CarouselsListener callback) {
        this.carouselId = carouselId;
        this.listItems = listItems;
        this.payMode = payMode;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_grid_item, parent, false);
            vh = new CarouselsAdapter.MyViewHolder(itemView);
        } else {
            FrameLayout frameLayout = new FrameLayout(activity);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            frameLayout.setPadding(10, 10, 10, 10);
            vh = new ViewMoreHolder(frameLayout);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position) != null ? VIEW_ITEM : VIEW_VIEWMORE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;
            final HorizontalListitemBean item = listItems.get(position);
            LinearLayout.LayoutParams vp;
            final String image_url = item.getImage();

            if (item.getType().equalsIgnoreCase("n") || item.getType().equalsIgnoreCase("L")) {
                vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                vp.setMargins(2, 2, 2, 2);
                holder.gridview_item.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);

                try {
                    Image.loadGridImage(activity, image_url, holder.gridview_item);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.gridview_item.setLayoutParams(vp);
                holder.gridview_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onClickItem(item);
                    }
                });

            } else if (item.getType().equalsIgnoreCase("m")) {
                vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                vp.setMargins(2, 2, 2, 2);
                holder.gridview_item.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);

                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Image.loadMovieImage(activity, image_url, holder.imageView, false);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.imageView.setLayoutParams(vp);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, MovieDetailsActivity.class);
                        intent.putExtra("showid", item.getId());
                        intent.putExtra("type", item.getType());
                        intent.putExtra("name", item.getName());
                        intent.putExtra("paymode", "" + payMode);
                        activity.startActivity(intent);
                    }
                });
            } else {
                vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                vp.setMargins(5, 5, 5, 5);
                holder.gridview_item.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                try {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Image.loadShowImage(activity, image_url, holder.imageView);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.imageView.setLayoutParams(vp);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(ShowDetailsActivity.getIntent(activity,item.getId(),payMode));
                    }
                });
            }
        } else if (viewHolder instanceof ViewMoreHolder) {
            ViewMoreHolder holder = (ViewMoreHolder) viewHolder;
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null && carouselId != -1)
                        callback.viewMore(carouselId + "");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private DynamicImageView imageView;
        private GridViewItem gridview_item;

        public MyViewHolder(View view) {
            super(view);
            try {
                imageView = (DynamicImageView) view.findViewById(R.id.imageView);
                gridview_item = (GridViewItem) view.findViewById(R.id.gridview_item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class ViewMoreHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewMoreHolder(FrameLayout view) {
            super(view);
            textView = new TextView(activity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,0,20);
            params.gravity=Gravity.RIGHT;
            textView.setLayoutParams(params);
            textView.setText("VIEW MORE");
            textView.setTextColor(activity.getResources().getColor(R.color.white));
            textView.setGravity(Gravity.RIGHT);
            textView.setTextSize(Dimension.SP, 12);
            textView.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            textView.setPadding(40, 20, 40, 20);
            view.addView(textView);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
            textView.setNextFocusUpId(R.id.imageView);
            textView.setNextFocusLeftId(R.id.slide_next);
        }
    }

    public void setSpanSizeLookup(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (getItemViewType(position)) {
                    case VIEW_ITEM:
                        return 1;
                    case VIEW_VIEWMORE:
                        return gridLayoutManager.getSpanCount(); //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
    }

}
