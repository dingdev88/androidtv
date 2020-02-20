package com.selecttvapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class TestFragment extends Fragment {
    private static final String KEY_TITLE = "TestFragment:Title";
    private static final String KEY_DESCRIPTION = "TestFragment:Descriptioin";

    private static int fragment_resource[] = new int[]{com.selecttvapp.R.layout.splash_fragment_1, com.selecttvapp.R.layout.splash_fragment_2, com.selecttvapp.R.layout.splash_fragment_4};

    public static TestFragment newInstance(int nFragmentID/*String strTitle, String strDescription*/) {
        TestFragment fragment = new TestFragment();

        fragment.mID = nFragmentID;

        return fragment;
    }

    private int mID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(fragment_resource[mID], null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
