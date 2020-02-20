package com.selecttvapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.ui.bean.SideMenu;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public class SpinnerGameAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<SideMenu> _list_data;
    private LayoutInflater _inflater;

    public SpinnerGameAdapter(Context ctx, ArrayList<SideMenu> list_data) {
        _context = ctx;
        _list_data = list_data;
        _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _list_data.size();
    }

    @Override
    public Object getItem(int position) {
        try {
            return _list_data.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {
            if (row == null) {
                row = _inflater.inflate(R.layout.spinner_view, parent, false);
                TextView textCategory = (TextView) row.findViewById(R.id.textCategory);

                SideMenu sideMenu = _list_data.get(position);
                String strCategory = sideMenu.getName();
                textCategory.setText(strCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {
            if (row == null) {
                row = _inflater.inflate(R.layout.spinneritem, parent, false);
                TextView textCategory = (TextView) row.findViewById(R.id.textCategory);

                SideMenu sideMenu = _list_data.get(position);
                String strCategory = sideMenu.getName();
                textCategory.setText(strCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
}