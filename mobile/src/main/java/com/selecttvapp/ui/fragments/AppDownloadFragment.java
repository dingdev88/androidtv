package com.selecttvapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvapp.R;
import com.selecttvapp.databinding.FragmentAppmanagerNewlayoutBinding;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.presentation.fragments.PresenterFastDownload;
import com.selecttvapp.presentation.views.ViewFastDownload;
import com.selecttvapp.ui.base.BaseFragment;

import java.util.ArrayList;


public class AppDownloadFragment extends BaseFragment<FragmentAppmanagerNewlayoutBinding> implements ViewFastDownload {
    public static AppDownloadFragment instance = null;
    public FragmentAppmanagerNewlayoutBinding binding;
    protected PresenterFastDownload presenter = new PresenterFastDownload();

    public AppDownloadFragment() {
        // Required empty public constructor
    }

    public static AppDownloadFragment getInstance() {
        return instance;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_appmanager_newlayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        presenter.onAttach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getBinding();
        return getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadAppsList(presenter.getCategoryList());
        presenter.getCategoryList();
    }

    @Override
    public void onDestroyView() {
        instance = null;
        stopProgressDialog();
        super.onDestroyView();
    }

    @Override
    public void stopProgressDialog() {
        super.stopProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isAdded() && getChildFragmentManager() != null)
            if (getChildFragmentManager().getFragments().size() > 0)
                for (Fragment fragment : getChildFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isAdded() && getChildFragmentManager() != null)
            if (getChildFragmentManager().getFragments().size() > 0)
                for (Fragment fragment : getChildFragmentManager().getFragments()) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
    }

    @Override
    public void loadAppsList(ArrayList<CategoryBean> categoryList) {
        binding.nonSwipeableViewPager.setPagingEnabled(false);
        binding.nonSwipeableViewPager.setOffscreenPageLimit(categoryList.size());
        PresenterFastDownload.SectionPagerAdapter sectionPagerAdapter = presenter.getSectionPagerAdapter(getChildFragmentManager(), categoryList);
        binding.nonSwipeableViewPager.setAdapter(sectionPagerAdapter);
        binding.smartTabLayout.setViewPager(binding.nonSwipeableViewPager);
        presenter.setOnPageChangeListener(binding.smartTabLayout);
        binding.nonSwipeableViewPager.setVisibility(View.VISIBLE);
    }
}
