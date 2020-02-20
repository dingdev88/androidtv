package com.selecttvapp.personalization;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.GridViewItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalizationItemsAdapter extends RecyclerView.Adapter<PersonalizationItemsAdapter.ViewHolder> {

    private ArrayList<PersonalizationItem> mItems = new ArrayList<>();
    private ArrayList<PersonalizationShows> mPersonalizationShowsArrayList = new ArrayList<>();
    private Activity activity;
    private int mHorizontal;
    private boolean mPager;
    private String TYPE = "";
    private ArrayList<String> FAVORITE_ITEMS = new ArrayList<>();

    //Tv shows
    public PersonalizationItemsAdapter(Activity activity, ArrayList<PersonalizationItem> mItems, String TYPE, ArrayList<String> FAVORITE_ITEMS) {
        this.activity = activity;
        this.TYPE = TYPE;
        this.mItems = mItems;
        this.FAVORITE_ITEMS = FAVORITE_ITEMS;
    }

    //Movies
    public PersonalizationItemsAdapter(Activity activity, String TYPE, ArrayList<PersonalizationShows> mPersonalizationShowsArrayList, ArrayList<String> FAVORITE_ITEMS) {
        Log.e("test8", "working");
        this.activity = activity;
        this.TYPE = TYPE;
        this.mPersonalizationShowsArrayList = mPersonalizationShowsArrayList;
        this.FAVORITE_ITEMS = FAVORITE_ITEMS;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personalization_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PersonalizationItem item;
        PersonalizationShows mPersonalizationShows;
        String image = null;


        if (TYPE.equalsIgnoreCase("tv_shows")) {
            holder.imageView.setVisibility(View.VISIBLE);
            item = mItems.get(position);
            image = item.getImage();
            holder.cardView.setLayoutParams(getTVShowsParams(position));

            if (FAVORITE_ITEMS.contains(item.getId())) {
                holder.cardView.setBackgroundResource(R.drawable.border_yellow);
            }

        } else if (TYPE.equalsIgnoreCase("movies") || TYPE.equalsIgnoreCase("radio")) {
            holder.mSquareImage.setVisibility(View.VISIBLE);
            mPersonalizationShows = mPersonalizationShowsArrayList.get(position);
            image = mPersonalizationShows.getImage();
            holder.cardView.setLayoutParams(getMovieParams(position));

            if (FAVORITE_ITEMS.contains(mPersonalizationShows.getId())) {
                holder.cardView.setBackgroundResource(R.drawable.border_yellow);
            }
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            holder.imageView.getLayoutParams().width = height / 3;
        else holder.imageView.getLayoutParams().width = width / 3;

        if (image != null)
            if (TYPE.equalsIgnoreCase("tv_shows"))
                Image.loadShowImage(holder.imageView.getContext(), image, holder.imageView);
            else
                Image.loadGridImage(holder.mSquareImage.getContext(), image, holder.mSquareImage);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TYPE.equalsIgnoreCase("tv_shows"))
                    addFavorite(holder, mItems.get(position).getId());
            }
        });
        holder.mSquareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TYPE.equalsIgnoreCase("movies") || TYPE.equalsIgnoreCase("radio"))
                    addFavorite(holder, mPersonalizationShowsArrayList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        switch (TYPE) {
            case "tv_shows":
                return mItems.size();
            case "movies":
                return mPersonalizationShowsArrayList.size();
            case "radio":
                return mPersonalizationShowsArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DynamicImageView imageView;
        private GridViewItem mSquareImage;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
            mSquareImage = (GridViewItem) itemView.findViewById(R.id.gridview_item);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }

        @Override
        public void onClick(View v) {
            if (TYPE.equalsIgnoreCase("tv_shows"))
                Toast.makeText(activity, mItems.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
            if (TYPE.equalsIgnoreCase("movies")) {
                Toast.makeText(activity, mPersonalizationShowsArrayList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addFavorite(final ViewHolder holder, final String itemId) {
        if (FAVORITE_ITEMS.contains(itemId)) {
            holder.cardView.setBackgroundResource(0);
            FAVORITE_ITEMS.remove(itemId);
            deleteFavoritePersonalizationItem(holder.imageView.getContext(), TYPE, Integer.parseInt(itemId));
        } else {
            FAVORITE_ITEMS.add(itemId);
            holder.cardView.setBackgroundResource(R.drawable.border_yellow);
            addFavoritePersonalizationItem(holder.imageView.getContext(), TYPE, Integer.parseInt(itemId), holder.cardView);
        }
    }

    private void addFavoritePersonalizationItem(final Context context, final String TYPE, final int showId, final View view) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Object object = null;
                    if (TYPE != null) {
                        if (TYPE == "tv_shows")
                            object = JSONRPCAPI.addFavoritePersonalizationTVShows(PreferenceManager.getAccessToken(), showId);
                        if (TYPE == "movies")
                            object = JSONRPCAPI.addFavoritePersonalizationMovies(PreferenceManager.getAccessToken(), showId);
                        if (TYPE == "radio")
                            object = JSONRPCAPI.addFavoritePersonalizationMusic(PreferenceManager.getAccessToken(), showId);
                    }

                    if (object != null) {
                        if (object instanceof JSONObject) {
                            JSONObject json = (JSONObject) object;
                            if (json.has("name")) {
                                if (json.getString("name").equalsIgnoreCase("JSONRPCError")) {
                                    if (json.has("message")) {
                                        String message = json.getString("message");
                                        if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                                            PresenterMyInterest.getInstance().showSessionExpiredDialog(activity);
                                            view.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (view != null)
                                                        view.setBackgroundResource(0);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteFavoritePersonalizationItem(final Context context, final String TYPE, final int showId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Object object = null;
                    if (TYPE != null) {
                        if (TYPE == "tv_shows")
                            object = JSONRPCAPI.deleteFavoritePersonalizationTVShowItem(PreferenceManager.getAccessToken(), showId);
                        if (TYPE == "movies")
                            object = JSONRPCAPI.deleteFavoritePersonalizationMovieItem(PreferenceManager.getAccessToken(), showId);
                        if (TYPE == "radio")
                            object = JSONRPCAPI.deleteFavoritePersonalizationMusicItem(PreferenceManager.getAccessToken(), showId);
                    }

                    if (object != null) {
                        if (object instanceof JSONObject) {
                            JSONObject json = (JSONObject) object;
                            if (json.has("name")) {
                                if (json.getString("name").equalsIgnoreCase("JSONRPCError")) {
                                    if (json.has("message")) {
                                        String message = json.getString("message");
                                        if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                                            PresenterMyInterest.getInstance().showSessionExpiredDialog(activity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private LinearLayout.LayoutParams getMovieParams(int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        return layoutParams;
    }

    private LinearLayout.LayoutParams getTVShowsParams(int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position != 0)
            layoutParams.setMargins(10, 0, 0, 0);
        return layoutParams;
    }

}

