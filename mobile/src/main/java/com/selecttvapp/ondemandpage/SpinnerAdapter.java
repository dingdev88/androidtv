package com.selecttvapp.ondemandpage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.model.CategoryBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Aug-17.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context m_context;
    private ArrayList<CategoryBean> mdata;
    private LayoutInflater m_inflater;

    public SpinnerAdapter(Context ctx, ArrayList<CategoryBean> data) {
        m_context = ctx;
        mdata = data;
        if (m_context != null)
            m_inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mdata.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = m_inflater.inflate(R.layout.spinneritem, parent, false);
        }
        String strCategory = "", slug = "";
        int strId = 0;

        try {
            strCategory = mdata.get(position).getName();
            slug = mdata.get(position).getSlug();
            strId = mdata.get(position).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textCategory = (TextView) row.findViewById(R.id.textCategory);
        textCategory.setText(strCategory);
        return row;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = m_inflater.inflate(R.layout.spinner_view, parent, false);
        }

        String strCategory = "", slug = "";
        int strId = 0;

        try {
            strCategory = mdata.get(position).getName();
            slug = mdata.get(position).getSlug();
            strId = mdata.get(position).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textCategory = (TextView) row.findViewById(R.id.textCategory);
        textCategory.setText(strCategory);

        Log.e("ERRORLOG", strCategory);
        return row;
    }
}
