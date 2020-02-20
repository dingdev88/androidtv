package com.selecttvapp.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.presentation.fragments.PresenterAppsLinksList;
import com.selecttvapp.presentation.views.ViewAppsLinksList;
import com.selecttvapp.ui.bean.AppFormatBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppLinksFragment extends Fragment implements ViewAppsLinksList {

    private final String ARG_PARAM_JSON_RESPONSE = "json_response";
    private final static String ARG_PARAM_CONTENT_TYPE = "content_type";
    private Object JSON_response = "";
    private String contentType = "";

    private final String PARAM_FREE_SD_LIST = "free_sd_list";
    private final String PARAM_FREE_HD_LIST = "free_hd_list";
    private final String PARAM_FREE_HDX_LIST = "free_hdx_list";
    private final String PARAM_FREE_OTHER = "free_other";
    private final String PARAM_PAID_SD_LIST = "paid_sd_list";
    private final String PARAM_PAID_HD_LIST = "paid_hd_list";
    private final String PARAM_PAID_HDX_LIST = "paid_hdx_list";
    private final String PARAM_PAID_OTHER = "paid_other";

    private Activity activity;
    private Context context;
    private PresenterAppsLinksList presenterAppsLinks;
    private FontHelper fontHelper = new FontHelper();

    private ArrayList<AppFormatBean> free_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> free_other = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_sd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hd_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_hdx_list = new ArrayList<>();
    private ArrayList<AppFormatBean> paid_other = new ArrayList<>();

    private FlexboxLayout mFlexboxLayout;
    private LinearLayout layoutTools;
    private ImageView switchImage;
    private TextView freeLabel, buyLabel, btnSD, btnHD,
            btnHDX;
    private TextView labelNoData;

    private String selected_app_format = "android";
    private boolean switchSelected = false;

    public AppLinksFragment() {
        // Required empty public constructor
    }

    public static AppLinksFragment newInstance(Object jsonResponse, String contentType) {
        AppLinksFragment fragment = new AppLinksFragment();
        fragment.JSON_response = jsonResponse;
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_CONTENT_TYPE, contentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterAppsLinks = new PresenterAppsLinksList(getActivity());
        if (presenterAppsLinks != null)
            presenterAppsLinks.setListener(this);
        activity = getActivity();
        context = getActivity();

        if (getArguments() != null) {
            contentType = getArguments().getString(ARG_PARAM_CONTENT_TYPE, "");
            presenterAppsLinks.setContentType(contentType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_links, container, false);
        mFlexboxLayout = (FlexboxLayout) view.findViewById(R.id.flexbox_layout);
        layoutTools = (LinearLayout) view.findViewById(R.id.layoutTools);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        switchImage = (ImageView) view.findViewById(R.id.switchImage);
        buyLabel = (TextView) view.findViewById(R.id.buyLabel);
        btnSD = (TextView) view.findViewById(R.id.btnSD);
        btnHD = (TextView) view.findViewById(R.id.btnHD);
        btnHDX = (TextView) view.findViewById(R.id.btnHDX);

        if (presenterAppsLinks != null)
            presenterAppsLinks.setFont(FontHelper.MYRIADPRO_REGULAR, freeLabel, buyLabel, btnSD, btnHD, btnHDX);

        switchImage.setOnClickListener(v -> {
            mFlexboxLayout.setVisibility(View.VISIBLE);//flex box layout
            labelNoData.setVisibility(View.GONE);

            if (switchSelected) {
                switchSelected = false;
                switchImage.setImageResource(R.drawable.off);
                showAndroidFreeLinksApps();
            } else {
                switchSelected = true;
                switchImage.setImageResource(R.drawable.on);
                showAndroidBuyRentApps();
            }
        });

        btnSD.setOnClickListener(v -> {
            setActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);
            setInActiveTextView(context, btnHDX);

            if (selected_app_format.equalsIgnoreCase("android"))
                if (switchSelected)
                    presenterAppsLinks.createAppsLinksLayout(context, paid_sd_list, mFlexboxLayout);
                else
                    presenterAppsLinks.createAppsLinksLayout(context, free_sd_list, mFlexboxLayout);

        });

        btnHD.setOnClickListener(v -> {
            setActiveTextView(context, btnHD);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHDX);

            if (selected_app_format.equalsIgnoreCase("android"))
                if (switchSelected)
                    presenterAppsLinks.createAppsLinksLayout(context, paid_hd_list, mFlexboxLayout);
                else
                    presenterAppsLinks.createAppsLinksLayout(context, free_hd_list, mFlexboxLayout);
        });

        btnHDX.setOnClickListener(v -> {
            setActiveTextView(context, btnHDX);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);

            if (selected_app_format.equalsIgnoreCase("android"))
                if (switchSelected)
                    presenterAppsLinks.createAppsLinksLayout(context, paid_hdx_list, mFlexboxLayout);
                else
                    presenterAppsLinks.createAppsLinksLayout(context, free_hdx_list, mFlexboxLayout);
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (JSON_response != null)
            if (presenterAppsLinks != null)
                if (JSON_response instanceof JSONObject)
                    presenterAppsLinks.makelistingsNext((JSONObject) JSON_response);
    }


    @Override
    public void showAppStoreDialog(AppFormatBean app) {
        FragmentManager fm = getChildFragmentManager();
        AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + app.getApp_name(), "" + app.getImage(), app.getApp_download_link());
        dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
    }

    @Override
    public void setAppsList(Map<String, ArrayList<AppFormatBean>> appsLists) {
        if (appsLists.containsKey("free_sd_list"))
            free_sd_list = appsLists.get("free_sd_list");
        if (appsLists.containsKey("free_hd_list"))
            free_hd_list = appsLists.get("free_hd_list");
        if (appsLists.containsKey("free_hdx_list"))
            free_hdx_list = appsLists.get("free_hdx_list");
        if (appsLists.containsKey("free_other"))
            free_other = appsLists.get("free_other");
        if (appsLists.containsKey("paid_sd_list"))
            paid_sd_list = appsLists.get("paid_sd_list");
        if (appsLists.containsKey("paid_hd_list"))
            paid_hd_list = appsLists.get("paid_hd_list");
        if (appsLists.containsKey("paid_hdx_list"))
            paid_hdx_list = appsLists.get("paid_hdx_list");
        if (appsLists.containsKey("paid_other"))
            paid_other = appsLists.get("paid_other");

        if (switchSelected) {
            showAndroidBuyRentApps();
        } else {
            if ((free_sd_list.size() == 0) && (free_hd_list.size() == 0) && (free_hdx_list.size() == 0)) {
                swapSwitch();
            } else {
                showAndroidFreeLinksApps();
            }
        }
    }

    private void swapSwitch() {
        mFlexboxLayout.setVisibility(View.VISIBLE);//flex box layout
        labelNoData.setVisibility(View.GONE);

        if (switchSelected) {
            switchSelected = false;
            switchImage.setImageResource(R.drawable.off);
            showAndroidFreeLinksApps();
        } else {
            switchSelected = true;
            switchImage.setImageResource(R.drawable.on);
            showAndroidBuyRentApps();
        }
    }

    private void showAndroidFreeLinksApps() {
        if (free_sd_list.size() > 0)
            btnSD.setVisibility(View.VISIBLE);
        else
            btnSD.setVisibility(View.GONE);

        if (free_hd_list.size() > 0)
            btnHD.setVisibility(View.VISIBLE);
        else
            btnHD.setVisibility(View.GONE);

        if (free_hdx_list.size() > 0)
            btnHDX.setVisibility(View.VISIBLE);
        else
            btnHDX.setVisibility(View.GONE);

        if ((free_sd_list.size() == 0) && (free_hd_list.size() == 0) && (free_hdx_list.size() == 0)) {
            if (contentType.equalsIgnoreCase("show"))
                labelNoData.setText("No free links currently available for the current episode");
            else
                labelNoData.setText("No free mobile links currently available for this movie");
            labelNoData.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.GONE);
            return;
        }

        if (free_sd_list.size() > 0) {
            setActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);
            setInActiveTextView(context, btnHDX);
            presenterAppsLinks.createAppsLinksLayout(context, free_sd_list, mFlexboxLayout);

        } else if (free_hd_list.size() > 0) {
            setActiveTextView(context, btnHD);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHDX);
            presenterAppsLinks.createAppsLinksLayout(context, free_hd_list, mFlexboxLayout);

        } else if (free_hdx_list.size() > 0) {
            setActiveTextView(context, btnHDX);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);
            presenterAppsLinks.createAppsLinksLayout(context, free_hdx_list, mFlexboxLayout);
        } else if (free_other.size() > 0) {
            presenterAppsLinks.createAppsLinksLayout(context, free_other, mFlexboxLayout);
        } else {
            mFlexboxLayout.setVisibility(View.GONE);
        }
    }

    private void showAndroidBuyRentApps() {
        if (paid_sd_list.size() > 0)
            btnSD.setVisibility(View.VISIBLE);
        else
            btnSD.setVisibility(View.GONE);

        if (paid_hd_list.size() > 0)
            btnHD.setVisibility(View.VISIBLE);
        else
            btnHD.setVisibility(View.GONE);

        if (paid_hdx_list.size() > 0)
            btnHDX.setVisibility(View.VISIBLE);
        else
            btnHDX.setVisibility(View.GONE);

        if ((paid_sd_list.size() == 0) && (paid_hd_list.size() == 0) && (paid_hdx_list.size() == 0)) {
            if (contentType.equalsIgnoreCase("show"))
                labelNoData.setText("No links currently available for the current episode");
            else
                labelNoData.setText("No mobile links currently available for this movie");
            labelNoData.setVisibility(View.VISIBLE);
            mFlexboxLayout.setVisibility(View.GONE);
            return;
        }

        if (paid_sd_list.size() > 0) {
            setActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);
            setInActiveTextView(context, btnHDX);
            presenterAppsLinks.createAppsLinksLayout(context, paid_sd_list, mFlexboxLayout);

        } else if (paid_hd_list.size() > 0) {
            setActiveTextView(context, btnHD);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHDX);
            presenterAppsLinks.createAppsLinksLayout(context, paid_hd_list, mFlexboxLayout);

        } else if (paid_hdx_list.size() > 0) {
            setActiveTextView(context, btnHDX);
            setInActiveTextView(context, btnSD);
            setInActiveTextView(context, btnHD);
            presenterAppsLinks.createAppsLinksLayout(context, paid_hdx_list, mFlexboxLayout);

        } else if (paid_other.size() > 0) {
            presenterAppsLinks.createAppsLinksLayout(context, paid_other, mFlexboxLayout);
        } else {
//                android_details.setVisibility(View.GONE);
            mFlexboxLayout.setVisibility(View.GONE);
        }
    }

    private void setActiveTextView(Context context, TextView activeTextView) {
        activeTextView.setBackgroundResource(R.drawable.app_format_bg_selected);
        activeTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        setFont(FontHelper.MYRIADPRO_SEMIBOLD, activeTextView);
    }

    private void setInActiveTextView(Context context, TextView inActiveTextView) {
        inActiveTextView.setBackgroundResource(R.drawable.app_format_bg);
        inActiveTextView.setTextColor(ContextCompat.getColor(context, R.color.text_lite_blue));
        setFont(FontHelper.MYRIADPRO_REGULAR, inActiveTextView);
    }

    public void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }
}
