package com.selecttvapp.channels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.adapters.AlertDailogListAdapter;

import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public class ChannelCategoryAdapter extends RecyclerView.Adapter<ChannelCategoryAdapter.DataObjectHolder> {

    ArrayList<ChannelCategoryList> menuList;
    Context context;
    int mSelectedItem=-1, tabwidth;
    SubMenuListener mSubMenuListener;
    int nWidth = 0;
    int nHeight = 0;
    int selectedPos = 0;
    String selectedSlug;
    boolean isDialog;

    public ChannelCategoryAdapter(ArrayList<ChannelCategoryList> menuList, Context context, int sel_pos, SubMenuListener mSubMenuListener, String selectedSlug, boolean isDialog) {
        this.menuList = menuList;
        this.context = context;
        this.mSelectedItem = -1;
        this.mSubMenuListener = mSubMenuListener;
        this.selectedSlug = selectedSlug;
        this.isDialog = isDialog;

        if (context == null)
            return;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            nHeight = displayMetrics.widthPixels;
            nWidth = displayMetrics.heightPixels;

        } else {
            nWidth = displayMetrics.widthPixels;
            nHeight = displayMetrics.heightPixels;
        }
        tabwidth = nWidth / 3;

    }

    @Override
    public ChannelCategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.rest_channel_list_layout, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChannelCategoryAdapter.DataObjectHolder holder, final int position) {
        holder.fragment_ondemandlist_items.setText(menuList.get(holder.getAdapterPosition()).getName());
//        ViewGroup.LayoutParams params = holder.fragment_ondemandlist_items_layout.getLayoutParams();
//        params.width = tabwidth;
//        holder.fragment_ondemandlist_items_layout.setLayoutParams(params);
        if (!TextUtils.isEmpty(selectedSlug) && menuList.get(holder.getAdapterPosition()).getSlug().equalsIgnoreCase(selectedSlug)) {
            mSelectedItem = holder.getAdapterPosition();
            selectedSlug = "";
        }

        if (TextUtils.isEmpty(selectedSlug) && holder.getAdapterPosition() == mSelectedItem) {
            holder.bottom_line.setVisibility(View.GONE);
            holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(context, R.color.text_yellow));
        } else {
            holder.bottom_line.setVisibility(View.GONE);
            holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.fragment_ondemandlist_items_layout.setOnClickListener(v -> OnItemClick(position));
    }

    private void loadChannels(String selected_slug, boolean dialogflag) {
        mSubMenuListener.onSubMenuSelected(selected_slug, dialogflag);
    }
    public void OnItemClick(int position){
        mSelectedItem = position;
        notifyDataSetChanged();
        String selected_slug = menuList.get(position).getSlug();
        ArrayList<ChannelCategoryList> temp_list = menuList.get(position).getSubCategories();
        if (temp_list.size() > 0) {
            loadsubcategories(temp_list, menuList.get(position).getName());
        } else {
            loadChannels(selected_slug, isDialog);
        }
    }
    private void loadsubcategories(final ArrayList<ChannelCategoryList> temp_list, String name) {
        if (temp_list == null) {
            return;
        }
        final AlertDialog.Builder builderMovieList = new AlertDialog.Builder(context);

        LayoutInflater inflater12 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater12.inflate(R.layout.alert_dialog, null);
        View titleLayout = inflater12.inflate(R.layout.alert_dialog_title, null);
        builderMovieList.setCustomTitle(titleLayout);
        builderMovieList.setView(layout);

        TextView txtTitle = (TextView) titleLayout.findViewById(R.id.txtDialogTitle);
        txtTitle.setText(name);

        ListView listContents = (ListView) layout.findViewById(R.id.listContents1);

        ArrayList<String> arrayAdapter = new ArrayList<>();
        for (int i = 0; i < temp_list.size(); i++) {
            try {

                String strName = temp_list.get(i).getName();

                arrayAdapter.add(strName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        AlertDailogListAdapter adapter = new AlertDailogListAdapter(context, arrayAdapter);
        listContents.setAdapter(adapter);
        final AlertDialog dialog = builderMovieList.create();
        Button btnCancel = (Button) layout.findViewById(R.id.btnCancel1);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        listContents.setOnItemClickListener((parent, view, position, id) -> {
            try {
                loadChannels(temp_list.get(position).getSlug(), isDialog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
        dialog.show();
     //   dialog.getWindow().setLayout(nWidth - 50, nHeight - 100);
      //  dialog.getWindow().setGravity(Gravity.CENTER);

    }

   /* public static void onDialogItemClick(int position) {
        try {
            loadChannels(mtemp_list.get(position).getSlug(), isDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }*/
    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        LinearLayout fragment_ondemandlist_items_layout;
        TextView fragment_ondemandlist_items;
        View bottom_line;

        public DataObjectHolder(View itemView) {
            super(itemView);

            fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.fragment_ondemandlist_items_layout1);
            fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.fragment_ondemandlist_items1);
            bottom_line = (View) itemView.findViewById(R.id.bottom_line);
        }
    }
}
