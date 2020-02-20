package com.demo.network.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.demo.network.model.Data;
import com.demo.network.util.AppUtil;
import com.demo.network.viewholder.PackageListViewHolder;

import java.util.List;

public class MaterialListAdapter extends RecyclerView.Adapter<PackageListViewHolder> {

    private List<Data> data;
    private int rowLayout;
    private Context mAct;
    private AQuery aQuery;

    public MaterialListAdapter(List<Data> data, int rowLayout, Context act, AQuery aQuery) {
        this.data = data;
        this.rowLayout = rowLayout;
        this.aQuery = aQuery;
        this.mAct = act;
    }


    @Override
    public PackageListViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new PackageListViewHolder(v, mAct);
    }

    @Override
    public void onBindViewHolder(final PackageListViewHolder viewHolder, int i) {
        final Data appInfo = data.get(i);
        viewHolder.name.setText(appInfo.getName());
        AppUtil.loadImageFromAQuery(aQuery, mAct, viewHolder.image, appInfo.getImageLink(), viewHolder.progressBar);
        boolean isPackageInstall = appInfo.isAppInstalled();
        if (isPackageInstall) {
            appInfo.setIsAppInstalled(true);
            viewHolder.btnGoToApp.setVisibility(View.VISIBLE);
            viewHolder.btnInstall.setVisibility(View.GONE);
        } else {
            viewHolder.btnInstall.setVisibility(View.VISIBLE);
            viewHolder.btnGoToApp.setVisibility(View.GONE);

        }
        viewHolder.btnInstall.setTag(appInfo);
        viewHolder.btnGoToApp.setTag(appInfo);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


}
