package com.selecttvapp.ui.fragments;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.selecttvapp.ui.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class FragmentHome extends Fragment {


    protected View rootView;

    /**
     * Gets Layout resource ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * Gets root fragment view
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * Gets Base Activity from fragment
     *
     * @return
     */
    public HomeActivity getHomeActivity() {
        return (HomeActivity) getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
