package com.selecttvapp.personalization;


import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selecttvapp.R;

import java.util.ArrayList;

public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.ViewHolder>/* implements GravitySnapHelper.SnapListener */ {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private Activity activity;
    private ArrayList<String> mFavoritItems = new ArrayList<>();
    private String TYPE;

    private ArrayList<PersonalizationShows> mPersonalizationShowsArrayList = new ArrayList<>();
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };


    public SnapAdapter(Activity activity, String TYPE, ArrayList<PersonalizationShows> mPersonalizationShowsArrayList, ArrayList<String> mFavoritItems) {
        Log.e("test7", "working");
        this.activity = activity;
        this.mPersonalizationShowsArrayList = mPersonalizationShowsArrayList;
        this.mFavoritItems = mFavoritItems;
        this.TYPE = TYPE;
    }


    @Override
    public int getItemViewType(int position) {
        return HORIZONTAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewType == VERTICAL ? LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personalization_adapter_snap, parent, false)
                : LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personalization_adapter_snap, parent, false);

        if (viewType == VERTICAL) {
            view.findViewById(R.id.recyclerView).setOnTouchListener(mTouchListener);
        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonalizationShows personalizationShows = mPersonalizationShowsArrayList.get(position);

        holder.snapTextView.setText(personalizationShows.getTitle());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));


        holder.recyclerView.setAdapter(new PersonalizationItemsAdapter(activity, personalizationShows.getItems(), TYPE, mFavoritItems));

    }


    @Override
    public int getItemCount() {
        return mPersonalizationShowsArrayList.size();
    }

   /* @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView snapTextView;
        public RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            snapTextView = (TextView) itemView.findViewById(R.id.snapTextView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        }

    }
}

