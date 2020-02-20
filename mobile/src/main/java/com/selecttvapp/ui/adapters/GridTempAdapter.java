package com.selecttvapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.selecttvapp.R;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 9/22/2016.
 */

public class GridTempAdapter extends RecyclerView.Adapter<GridTempAdapter.DataObjectHolder> {
    ArrayList<Integer> images;
    Context context;
    public GridTempAdapter(ArrayList<Integer> images,Context context)
    {
        this.images=images;
        this.context=context;

    }

    @Override
    public GridTempAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(GridTempAdapter.DataObjectHolder holder, int position) {
        try
        {
            holder.imageView.setImageResource(images.get(position));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);


            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}

