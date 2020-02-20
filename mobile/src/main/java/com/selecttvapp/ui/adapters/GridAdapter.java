package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.GridViewListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.GridViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Appsolute dev on 11-Aug-17.
 */

public class GridAdapter extends RecyclerView.Adapter {
    public ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
    private Context context;
    private Activity activity;
    private GridViewListener gridViewListener;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 100;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private int payMode = RabbitTvApplication.getInstance().getPaymode();

    public GridAdapter(ArrayList<CauroselsItemBean> listItems, int payMode, Activity activity, GridViewListener gridViewListener) {
        try {
            this.listItems.clear();
            this.listItems = listItems;
            this.payMode = payMode;
            this.context = activity;
            this.activity = activity;
            this.gridViewListener = gridViewListener;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            vh = new DataObjectHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            if (viewHolder instanceof DataObjectHolder) {
                final DataObjectHolder holder = (DataObjectHolder) viewHolder;
                final CauroselsItemBean item = listItems.get(position);
                final String image_url = item.getCarousel_image();
                final String type = item.getType();

                //   viewAll_list_data.get(position).getName()bvbcvb
                LinearLayout.LayoutParams lp;
                if (type.equalsIgnoreCase("s") || type.equalsIgnoreCase("show")) {
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    if (position % 2 == 0)
                        lp.setMargins(0, 4, 6, 4);
                    else
                        lp.setMargins(6, 4, 0, 4);
                } else {
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.setMargins(4, 4, 4, 4);
                }

                holder.imageView.setLayoutParams(lp);
                holder.gridview_item.setLayoutParams(lp);

                if (type.equalsIgnoreCase("n") || type.equalsIgnoreCase("L")) {
                    holder.gridview_item.setVisibility(View.VISIBLE);
                    holder.imageView.setVisibility(View.GONE);
                    if (image_url != null) {
                        Image.loadGridImage(context, image_url, holder.gridview_item);
                    }

                } else if (type.equalsIgnoreCase("M") || type.equalsIgnoreCase("movie")) {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
                        Image.loadMovieImage(activity, image_url, holder.imageView, true);
                    }
                } else {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
                        Image.loadShowImage(activity, image_url, holder.imageView);
                    }
                }

                Log.d("categoryimage", "===>" + image_url);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equalsIgnoreCase("s") || type.equalsIgnoreCase("show")) {
                            activity.startActivityForResult(ShowDetailsActivity.getIntent(activity, item.getId(), payMode), 9999);
                        } else if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
                            activity.startActivity(MovieDetailsActivity.getIntent(activity, item.getId()));
                        }
                    }
                });

                holder.gridview_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gridViewListener != null)
                            gridViewListener.onClickItem(item.getId() + "", //String id
                                    Constants.LoadNetworkData,   // String recall method
                                    item.getId()); // string recall id
                    }
                });
            } else {
                ProgressViewHolder holder = ((ProgressViewHolder) viewHolder);
                holder.progressBar.setIndeterminate(true);
                holder.progressBar.setPadding(5, 5, 5, 5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final DynamicImageView imageView;
        private GridViewItem gridview_item;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
            gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);


        }
    }

    //set Your Progress ViewHolder
    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public void clearValues() {
        listItems.clear();
        notifyDataSetChanged();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setRecyclerview(RecyclerView recyclerView) {
        if (recyclerView != null)
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = gridLayoutManager.getItemCount() - 1;
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                        if (lastVisibleItem > 0 && totalItemCount > 0)
                            if (!loading && totalItemCount <= lastVisibleItem) {
                                loading = true;
                                if (onLoadMoreListener != null) {
                                    showProgress();
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                    }
                });
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (getItemViewType(position)) {
                            case VIEW_ITEM:
                                return 1;
                            case VIEW_PROG:
                                return gridLayoutManager.getSpanCount(); //number of columns of the grid
                            default:
                                return -1;
                        }
                    }
                });
            }
    }

    //Use This method its Handle all of item to add more data
    public void setMoreData(List moreItemList) {
        try {
            Log.e("listdata", "::" + moreItemList.size());
            if (moreItemList.size() > 0) {
                removeProgress();
                listItems.addAll(moreItemList);
                this.notifyItemInserted(listItems.size());
                this.notifyDataSetChanged();
                setLoaded(false);
            } else {
                setLoaded(true);
                removeProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //its used for Loded Data set.
    public void setLoaded(boolean load) {
        loading = load;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void showProgress() {
        listItems.add(null); //this is Main its for Loader
        notifyItemInserted(listItems.size() - 1); //added one more line
    }

    public void removeProgress() {
        listItems.remove(listItems.size() - 1);
        this.notifyItemRemoved(listItems.size());
    }

}