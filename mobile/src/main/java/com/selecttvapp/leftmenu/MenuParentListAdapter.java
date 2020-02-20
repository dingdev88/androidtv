package com.selecttvapp.leftmenu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.model.SideMenu;
import com.selecttvapp.model.SideMenuChild;
import com.selecttvapp.ui.adapters.FragmentDrawer;
import com.selecttvapp.ui.views.CustExpListview;

import java.util.ArrayList;

/**
 * Created by babin on 10/26/2017.
 */

public class MenuParentListAdapter extends BaseExpandableListAdapter {

    ExpandableListView.OnGroupExpandListener subMenuExpandListner;
    LeftMenuInterface mLeftMenuInterface;
    private Context context;
    private ArrayList<SideMenu> deptList;
    private ExpandableListView.OnGroupClickListener subMenuGroupClickLst;
    private ExpandableListView.OnChildClickListener subMenuchildClickLst;
    private int prevSubList = -1;
    private CustExpListview prevList = null;

    public MenuParentListAdapter(Context context, ArrayList<SideMenu> deptList, ExpandableListView.OnGroupClickListener subMenuGroupClickLst, ExpandableListView.OnChildClickListener subMenuchildClickLst, LeftMenuInterface mLeftMenuInterface) {
        this.context = context;
        this.deptList = deptList;
        this.subMenuGroupClickLst = subMenuGroupClickLst;
        this.subMenuchildClickLst = subMenuchildClickLst;
        this.subMenuExpandListner = subMenuExpandListner;
        this.mLeftMenuInterface = mLeftMenuInterface;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<SideMenuChild> productList =
                deptList.get(groupPosition).getSideMenuChild();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        SideMenuChild detailInfo = (SideMenuChild) getChild(groupPosition, childPosition);


        if (detailInfo.getSideMenuChild().size() > 0) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.custom_expandable_list_layout, null);
            final CustExpListview mySubList = (CustExpListview) view.findViewById(R.id.mySubList);
            mySubList.setTag(getGroup(groupPosition));
            mySubList.setVisibility(View.VISIBLE);

            MenuChildListAdapter rootAdapter = new MenuChildListAdapter(context, detailInfo);
            mySubList.setAdapter(rootAdapter);

            mySubList.setOnGroupClickListener(subMenuGroupClickLst);
            mySubList.setOnChildClickListener(subMenuchildClickLst);
            mySubList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int i) {
                    if (prevSubList != -1 && prevSubList != i) {
                        prevList.collapseGroup(prevSubList);
                    }
                    prevSubList = i;
                    prevList = mySubList;
                }
            });

            view.setTag(detailInfo);
        } else {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_parent, null);
            TextView itemParentTitle = (TextView) view.findViewById(R.id.itemParentTitle);
            ImageView itemParentImage = (ImageView) view.findViewById(R.id.itemParentImage);
          //  itemParentTitle.setVisibility(View.VISIBLE);
           // itemParentImage.setVisibility(View.INVISIBLE);
            itemParentTitle.setText(detailInfo.getName().trim());
            if(detailInfo.getName().trim().equals("Channels") && detailInfo.getSlug().trim().equals("kids-family")){
                itemParentTitle.setVisibility(View.GONE);
                itemParentImage.setVisibility(View.GONE);
            }else {
                itemParentTitle.setVisibility(View.VISIBLE);
                itemParentImage.setVisibility(View.INVISIBLE);
            }
            if (detailInfo.getSlug() != null && detailInfo.isClick() && (detailInfo.getSlug().equalsIgnoreCase(FragmentDrawer.SelectedSlug))) {
                itemParentTitle.setTextColor(ContextCompat.getColor(context, R.color.leftmenu_selected));
                mLeftMenuInterface.onSelectedTextView(itemParentTitle);
            } else {
                itemParentTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            view.setTag(detailInfo);
        }
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<SideMenuChild> productList = deptList.get(groupPosition).getSideMenuChild();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {

        SideMenu headerInfo = (SideMenu) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_root, null);
        }
        TextView heading = (TextView) view.findViewById(R.id.itemRootTitle);
        ImageView itemParentImage = (ImageView) view.findViewById(R.id.itemParentImage);
        if (headerInfo.getSideMenuChild() != null && headerInfo.getSideMenuChild().size() > 0) {
            itemParentImage.setVisibility(View.VISIBLE);
        } else {
            itemParentImage.setVisibility(View.INVISIBLE);
        }
        String name = headerInfo.getName().trim();
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
