package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewAppsLinksList;
import com.selecttvapp.ui.bean.AppFormatBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public class PresenterAppsLinksList {
    private Activity activity;
    private ViewAppsLinksList mListener;
    private FontHelper fontHelper;
    private String contentType = "";

    public PresenterAppsLinksList(Activity activity) {
        this.activity = activity;
        fontHelper = new FontHelper();
    }

    public void setListener(ViewAppsLinksList mListener) {
        this.mListener = mListener;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void createAppsLinksLayout(Context context, ArrayList<AppFormatBean> data_list, FlexboxLayout mFlexboxLayout) {
        mFlexboxLayout.removeAllViews();
        if (data_list.size() <= 0)
            mFlexboxLayout.setVisibility(View.GONE);

        for (AppFormatBean appFormatBean : data_list) {
            View itemView = buildView(context, appFormatBean);
            mFlexboxLayout.addView(itemView);
        }
    }

    private View buildView(final Context context, final AppFormatBean appFormatBean) {
        String[] sub_list = PreferenceManager.geSubscribedList();
        List subsList = Utilities.toLowerCase(Arrays.asList(sub_list));
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mInflater.inflate(R.layout.movie_payment_design, null);
        TextView movie_payment_textview = (TextView) itemView.findViewById(R.id.movie_payment_textview);
        ImageView movie_payment_imageview = (ImageView) itemView.findViewById(R.id.movie_payment_imageview);

        String strSourceName = "";
        String price = "";
        try {
            strSourceName = appFormatBean.getSource();
            final String app_name = appFormatBean.getApp_name();
            price = appFormatBean.getFormats_price();
            if (appFormatBean.getFormats_type().equalsIgnoreCase("rent")) {
                movie_payment_textview.setText("RENT " + price);
                movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.free_bg));
            } else if (appFormatBean.getFormats_type().equalsIgnoreCase("purchase")) {
                movie_payment_textview.setText("BUY " + price);
                movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.rent_btn_bg));
            } else if (appFormatBean.getFormats_type().equalsIgnoreCase("subscription")) {
                if (subsList.contains(appFormatBean.getSubscription_code().toLowerCase())) {
                    movie_payment_textview.setText("Subscribed");
                    movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.rent_btn_bg));
                } else {
                    movie_payment_textview.setText("Free Trial");
                    movie_payment_textview.setBackgroundResource(R.drawable.free_trial_bg);
                }
            } else if (appFormatBean.getFormats_type().equalsIgnoreCase("cable")) {
                movie_payment_textview.setText("Free");
                Drawable image = ContextCompat.getDrawable(context, R.drawable.key_icon);
                int h = image.getIntrinsicHeight();
                int w = image.getIntrinsicWidth();
                image.setBounds(0, 0, w, h);
                movie_payment_textview.setCompoundDrawables(image, null, null, null);
                movie_payment_textview.setBackgroundColor(ContextCompat.getColor(context, R.color.free_bg));
            } else {
                movie_payment_textview.setText(price);
            }

//            Picasso.with(context)
//                    .load(appFormatBean.getImage())
//                    .placeholder(R.drawable.loader_network).into(movie_payment_imageview);
            Image.loadGridImage(appFormatBean.getImage(), movie_payment_imageview);


            movie_payment_imageview.setFocusableInTouchMode(true);
            movie_payment_imageview.setFocusable(true);
            movie_payment_imageview.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            movie_payment_imageview.setNextFocusUpId(R.id.switchImage_tv_apps);
            movie_payment_imageview.setOnClickListener(v -> {
                if (appFormatBean.isApp_required()) {
                    loadPackageNameList(app_name, appFormatBean);
                } else {
                    String url_link = appFormatBean.getLink();
                    if (!TextUtils.isEmpty(url_link)) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_link));
                        if (myIntent != null) {
                            activity.startActivity(myIntent);
                        } else {
                            Toast.makeText(context, "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }

    private void loadPackageNameList(final String appName, final AppFormatBean appFormatBean) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject response = JSONRPCAPI.getPackageName(appName);
                    if (response == null) return;

                    jsonObject.put("app_package", response.getString("package"));
                    final String package_name = response.getString("package");
                    jsonObject.put("app_image", response.getString("image"));
                    jsonArray.put(jsonObject);
                    activity.runOnUiThread(() -> makeSourceDialog(package_name, appFormatBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void makeSourceDialog(String packageName, AppFormatBean obj) {
        try {
            if (Utils.appInstalledOrNot(activity, packageName)) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (myIntent != null) {
                    activity.startActivity(myIntent);
                } else {
                    Toast.makeText(activity, "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (obj.getApp_name().toLowerCase().equals("google play")) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getLink()));
                    activity.startActivity(myIntent);
                } else {
                    mListener.showAppStoreDialog(obj);
//                    FragmentManager fm = activity.getSupportFragmentManager();
//                    AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + obj.getApp_name(), "" + obj.getImage(), obj.getApp_download_link());
//                    dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end of makeSourceDialog method


    public void makelistingsNext(JSONObject m_jsonMovieLinks) {
        Map<String, ArrayList<AppFormatBean>> mapList = new HashMap<>();

        JSONArray m_jsonArrayFreeItems = null,
                m_jsonArrayPaidItems = null,
                m_jsonArrayAuthenticItems = null;

        ArrayList<AppFormatBean> free_sd_list = new ArrayList<>();
        ArrayList<AppFormatBean> free_hd_list = new ArrayList<>();
        ArrayList<AppFormatBean> free_hdx_list = new ArrayList<>();
        ArrayList<AppFormatBean> free_other = new ArrayList<>();
        ArrayList<AppFormatBean> paid_sd_list = new ArrayList<>();
        ArrayList<AppFormatBean> paid_hd_list = new ArrayList<>();
        ArrayList<AppFormatBean> paid_hdx_list = new ArrayList<>();
        ArrayList<AppFormatBean> paid_other = new ArrayList<>();

        try {
            try {
                if (contentType.equalsIgnoreCase("show")) {
                    m_jsonArrayFreeItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("android").getJSONArray("free");
                    m_jsonArrayPaidItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("android").getJSONArray("paid");
                    try {
                        m_jsonArrayAuthenticItems = m_jsonMovieLinks.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (m_jsonMovieLinks.has("sources"))
                        if (m_jsonMovieLinks.getJSONObject("sources").has("mobile")) {
                            m_jsonArrayFreeItems = m_jsonMovieLinks.getJSONObject("sources").getJSONObject("mobile").getJSONObject("android").getJSONArray("free");

                            m_jsonArrayPaidItems = m_jsonMovieLinks.getJSONObject("sources").getJSONObject("mobile").getJSONObject("android").getJSONArray("paid");
                            try {
                                if (!m_jsonMovieLinks.getJSONObject("sources").getJSONObject("mobile").getJSONObject("android").isNull("authenticated"))
                                    m_jsonArrayAuthenticItems = m_jsonMovieLinks.getJSONObject("sources").getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                }

                try {
                    if (m_jsonArrayAuthenticItems != null)
                        if (m_jsonArrayAuthenticItems.length() > 0) {
                            for (int i = 0; i < m_jsonArrayAuthenticItems.length(); i++) {
                                final JSONObject jsonObject = m_jsonArrayAuthenticItems.getJSONObject(i);
                                String source = jsonObject.getString("source");
                                String link = jsonObject.getString("link");
                                final String app_name = jsonObject.getString("app_name");
                                final String app_download_link = jsonObject.getString("app_download_link");
                                final String display_name = jsonObject.getString("display_name");
                                final boolean app_required = jsonObject.getBoolean("app_required");
                                final boolean app_link = jsonObject.getBoolean("app_link");
                                final String image = jsonObject.getString("image");
                                String subscription_code = "";
                                if (jsonObject.has("subscription_code")) {
                                    subscription_code = jsonObject.getString("subscription_code");
                                }
                                Log.d("Source::", "Source::" + source);
                                String[] sub_list = PreferenceManager.geSubscribedList();
                                Log.d("sublist:::", ":::start");
                                if (Arrays.asList(sub_list).contains("cable") || Arrays.asList(sub_list).contains("CABLE")) {
                                    free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "cable", "", display_name, app_required, app_link, image, subscription_code));
                                }
                            }
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (m_jsonArrayFreeItems != null)
                        if (m_jsonArrayFreeItems.length() > 0) {
                            for (int i = 0; i < m_jsonArrayFreeItems.length(); i++) {
                                final JSONObject jsonObject = m_jsonArrayFreeItems.getJSONObject(i);
                                String source = jsonObject.getString("source");
                                String link = jsonObject.getString("link");
                                final String app_name = jsonObject.getString("app_name");
                                final String app_download_link = jsonObject.getString("app_download_link");
                                final String display_name = jsonObject.getString("display_name");
                                final String image = jsonObject.getString("image");
                                final boolean app_required = jsonObject.getBoolean("app_required");
                                final boolean app_link = jsonObject.getBoolean("app_link");
                                String subscription_code = "";
                                if (jsonObject.has("subscription_code")) {
                                    subscription_code = jsonObject.getString("subscription_code");
                                }
                                Log.d("Source::", "Source::" + source);
                                if (jsonObject.has("formats")) {
                                    Log.d("Source::", "has formats::");
                                    Object o = jsonObject.get("formats");
                                    if (o instanceof JSONArray) {
                                        JSONArray formats = jsonObject.getJSONArray("formats");
                                        if (formats.length() > 0) {
                                            for (int j = 0; j < formats.length(); j++) {
                                                String formats_price = formats.getJSONObject(j).getString("price");
                                                String formats_type = formats.getJSONObject(j).getString("type");
                                                String formats_format = formats.getJSONObject(j).getString("format");
                                                formats_price = "$" + formats_price;

                                                if (formats_format.equalsIgnoreCase("sd")) {
                                                    free_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                } else if (formats_format.equalsIgnoreCase("hd")) {
                                                    free_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                } else if (formats_format.equalsIgnoreCase("hdx")) {
                                                    free_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                }
                                            }
                                        } else {
                                            free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link, image, subscription_code));
                                        }
                                    } else {
                                        free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "", "", display_name, app_required, app_link, image, subscription_code));
                                    }
                                }
                            }
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (m_jsonArrayPaidItems != null)
                        if (m_jsonArrayPaidItems != null && m_jsonArrayPaidItems.length() > 0) {
                            for (int i = 0; i < m_jsonArrayPaidItems.length(); i++) {
                                final JSONObject jsonObject = m_jsonArrayPaidItems.getJSONObject(i);
                                String source = jsonObject.getString("source");
                                String link = jsonObject.getString("link");
                                final String app_name = jsonObject.getString("app_name");
                                final String app_download_link = jsonObject.getString("app_download_link");
                                final String display_name = jsonObject.getString("display_name");
                                final boolean app_required = jsonObject.getBoolean("app_required");
                                final boolean app_link = jsonObject.getBoolean("app_link");
                                final String image = jsonObject.getString("image");
                                String subscription_code = "";
                                if (jsonObject.has("subscription_code")) {
                                    subscription_code = jsonObject.getString("subscription_code");
                                }
                                Log.d("Source::", "Source::" + source);
                                if (jsonObject.has("formats")) {
                                    Log.d("Source::", "has formats::");
                                    Object o = jsonObject.get("formats");
                                    if (o instanceof JSONArray) {
                                        JSONArray formats = jsonObject.getJSONArray("formats");
                                        if (formats.length() > 0) {
                                            for (int j = 0; j < formats.length(); j++) {
                                                String formats_price = formats.getJSONObject(j).getString("price");
                                                String formats_type = formats.getJSONObject(j).getString("type");
                                                String formats_format = formats.getJSONObject(j).getString("format");
                                                formats_price = "$" + formats_price;

                                                if (formats_format.equalsIgnoreCase("sd")) {
                                                    paid_sd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                } else if (formats_format.equalsIgnoreCase("hd")) {
                                                    paid_hd_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                } else if (formats_format.equalsIgnoreCase("hdx")) {
                                                    paid_hdx_list.add(new AppFormatBean(source, link, app_name, app_download_link, formats_price, formats_type, formats_format, display_name, app_required, app_link, image, subscription_code));
                                                }
                                            }
                                        }

                                    } else {
                                        free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "Subscription", "", display_name, app_required, app_link, image, subscription_code));
                                    }
                                } else {
                                    free_other.add(new AppFormatBean(source, link, app_name, app_download_link, "Free", "Subscription", "", display_name, app_required, app_link, image, subscription_code));
                                }
                            }

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            free_sd_list.addAll(free_other);
            paid_sd_list.addAll(paid_other);

            mapList.put("free_sd_list", free_sd_list);
            mapList.put("free_hd_list", free_hd_list);
            mapList.put("free_hdx_list", free_hdx_list);
            mapList.put("free_other", free_other);
            mapList.put("paid_sd_list", paid_sd_list);
            mapList.put("paid_hd_list", paid_hd_list);
            mapList.put("paid_hdx_list", paid_hdx_list);
            mapList.put("paid_other", paid_other);

            if (mListener != null)
                mListener.setAppsList(mapList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
