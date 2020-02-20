package com.demo.network.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.demo.network.adapter.MaterialListAdapter;
import com.demo.network.async.InitializeApplicationsTask;
import com.demo.network.listener.ApplicationDataRecievedListener;
import com.demo.network.model.Data;
import com.selecttvapp.R;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class AddsCheckerFragment extends BaseFragment implements ApplicationDataRecievedListener, View.OnClickListener {

    private RecyclerView rcData;
    private MaterialListAdapter mAdapter;
    private AQuery aQuery;
    private List<Data> listPackages;
    private EventBus bus = EventBus.getDefault();
    private Context context;
    private TextView tvInstalled;
    private TextView tvNotInstalled;
    private View viewInstalled;
    private View viewNotInstalled;
    private RelativeLayout rltInstalled;
    private RelativeLayout rltNotInstalled;
    public static BaseFragment newInstance() {
        BaseFragment baseFragment = new AddsCheckerFragment();
        return baseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.ui_app_checker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bus.register(this);
        initComponents(view);
        setListener();
        createAquery();
        checkAppsForStatus();
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();

    }

    private void checkAppsForStatus() {
        new InitializeApplicationsTask(context, this).execute();
    }

    private void createAquery() {
        aQuery = new AQuery(context);
    }

    private void setAdapter() {
        mAdapter = new MaterialListAdapter(listPackages, R.layout.ui_row_app_checker_main, context, aQuery);
        rcData.setAdapter(mAdapter);
    }

    @Override
    public void initComponents(View view) {
        rcData = (RecyclerView) view.findViewById(R.id.ui_main_list);
        rcData.setLayoutManager(new LinearLayoutManager(context));
        tvInstalled = (TextView) view.findViewById(R.id.tv_installed);
        tvNotInstalled = (TextView) view.findViewById(R.id.tv_not_installed);
        viewInstalled = view.findViewById(R.id.view_installed);
        viewNotInstalled = view.findViewById(R.id.view_not_installed);
        rltInstalled = (RelativeLayout) view.findViewById(R.id.rlt_installed);
        rltNotInstalled = (RelativeLayout) view.findViewById(R.id.rlt_not_installed);
    }
    private void setListener() {
        rltNotInstalled.setOnClickListener(this);
        rltInstalled.setOnClickListener(this);
    }
    private void setInstalledTab() {
        tvInstalled.setTextColor(Color.parseColor("#ffffff"));//context.getColor(R.color.white)
        tvNotInstalled.setTextColor(Color.parseColor("#A4A4A4"));//context.getColor(R.color.dark_gray)
        viewInstalled.setVisibility(View.VISIBLE);
        viewNotInstalled.setVisibility(View.GONE);
    }
    private void setNotInstalledTab() {
        tvInstalled.setTextColor(Color.parseColor("#A4A4A4")); //context.getColor(R.color.dark_gray)
        tvNotInstalled.setTextColor(Color.parseColor("#ffffff"));//context.getColor(R.color.white)
        viewInstalled.setVisibility(View.GONE);
        viewNotInstalled.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResultRecived(List<Data> data){
        this.listPackages = data;
        setInstalledAdapter();
    }

    public void onEvent(String event){
        checkAppsForStatus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_installed:
                setInstalledAdapter();

                break;
            case R.id.rlt_not_installed:
                setNotInstalledAdapter();

                break;
            default:
                break;
        }
    }

    private void setNotInstalledAdapter() {
        List<Data> listInsallted = new ArrayList<>();
        setNotInstalledTab();
        for(Data data: listPackages) {
            if(!data.isAppInstalled()) {
                listInsallted.add(data);
            }

        }
        mAdapter = new MaterialListAdapter(listInsallted, R.layout.ui_row_app_checker_main, context, aQuery);
        rcData.setAdapter(mAdapter);
    }

    private void setInstalledAdapter() {
        List<Data> listNotInsallted = new ArrayList<>();
        setInstalledTab();
        for(Data data: listPackages) {
            if(data.isAppInstalled()) {
                listNotInsallted.add(data);
            }

        }
        mAdapter = new MaterialListAdapter(listNotInsallted, R.layout.ui_row_app_checker_main, context, aQuery);
        rcData.setAdapter(mAdapter);
    }
}
