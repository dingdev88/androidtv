package com.selecttvapp.leftmenu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.model.SideMenuChild;
import com.selecttvapp.ui.adapters.FragmentDrawer;

import java.util.ArrayList;

/**
 * Created by babin on 10/26/2017.
 */

public class MenuChildListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private SideMenuChild deptList;

    public MenuChildListAdapter(Context context, SideMenuChild deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<SideMenuChild> productList = deptList.getSideMenuChild();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        SideMenuChild detailInfo = (SideMenuChild) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_child, null);
        }
        TextView sequence = (TextView) view.findViewById(R.id.itemChildTitle);
        ImageView itemParentImage = (ImageView) view.findViewById(R.id.itemParentImage);
        itemParentImage.setVisibility(View.INVISIBLE);
        sequence.setText(detailInfo.getName().trim());

        if(detailInfo.getSlug().equalsIgnoreCase(FragmentDrawer.SelectedSlug)){
            sequence.setTextColor(ContextCompat.getColor(context, R.color.leftmenu_selected));
        }else{
            sequence.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        view.setTag(R.string.PARENT,getGroup(groupPosition));
        view.setTag(R.string.CHILD,detailInfo);

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

       ArrayList<SideMenuChild> productList =deptList.getSideMenuChild();
        return deptList.getSideMenuChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {

        SideMenuChild headerInfo = (SideMenuChild) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_parent, null);
        }
        TextView heading = (TextView) view.findViewById(R.id.itemParentTitle);
        ImageView itemParentImage = (ImageView) view.findViewById(R.id.itemParentImage);
        if(headerInfo.getSideMenuChild()!=null&&headerInfo.getSideMenuChild().size()>0){
            itemParentImage.setVisibility(View.VISIBLE);
        }else{
            itemParentImage.setVisibility(View.INVISIBLE);
        }
        heading.setText(headerInfo.getName().trim());
        view.setTag(headerInfo);

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

