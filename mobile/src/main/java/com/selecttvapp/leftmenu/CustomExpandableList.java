package com.selecttvapp.leftmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by babin on 10/27/2017.
 */

public class CustomExpandableList extends ExpandableListView {
    public CustomExpandableList(Context context) {
        super(context);
    }

    public CustomExpandableList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpandableList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean expandGroup(int groupPos) {
        return super.expandGroup(groupPos,false);
    }
}
