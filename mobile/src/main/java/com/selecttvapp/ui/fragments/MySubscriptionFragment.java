package com.selecttvapp.ui.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.GlideApp;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.databinding.HorizontalListItemBinding;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.GridSpacingItemDecoration;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.Slider;
import com.selecttvapp.model.UserSubscriptionBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.bean.UserAllSubscriptionBean;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.GridViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySubscriptionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static double list_width = 0;
    View lastselected;
    private String mParam1 = "";
    private String mParam2 = "";

    private Context mContext;
    private Handler handler = new Handler();

    private ArrayList<UserSubscriptionBean> subscriptionList = new ArrayList<>();
    private HashMap<String, UserSubscriptionBean> user_map = new HashMap<String, UserSubscriptionBean>();
    private ArrayList<UserAllSubscriptionBean> all_subscriptionList = new ArrayList<>();
    private RecyclerView subscription_list, subscription_show_list, subscription_Movie_list;
    private AQuery aq;
    private Button set_button, skip_button;
    private ImageView switch_image;
    private ArrayList<HorizontalListitemBean> horizontal_list_date = new ArrayList<>();
    private boolean iscableSelected = false;
    private boolean mSessionExpired = false;
    private int slider_image_width = 0;
    private LayoutInflater mlayoutinflater;
    private RelativeLayout relativelayout_select_subscriptions;
    private LinearLayout linearlayout_showsbySubscription, linearlayout_dynamic_image, linearLayout_shows_tab, linearLayout_movies_tab;
    OnBackPressedListener onBackPressedListener = new OnBackPressedListener() {
        @Override
        public void onBackPressed() {
            HomeActivity.setOnBackPressedListener(null);
            linearlayout_showsbySubscription.setVisibility(View.GONE);
            relativelayout_select_subscriptions.setVisibility(View.VISIBLE);
        }
    };
    private TextView textview_manage, shows_text_tab, movies_text_tab;
    private TextView labelNoData;
    private View view_shows_left, view_shows_top, view_shows_bottom, view_shows_right, view_movies_top, view_movies_bottom, view_movies_right;
    private HorizontalScrollView horizontal_listview;

    public MySubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySubscriptionFragment newInstance(String param1, String param2) {
        MySubscriptionFragment fragment = new MySubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        //v.setAlpha(128);   // 128 = 0.5
    }

    public static void setUnlocked(ImageView v) {
        v.setColorFilter(null);
        //v.setAlpha(255);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_layout, container, false);
        mContext = getActivity();
        PreferenceManager.setFirstLogin(false);
        aq = new AQuery(getActivity());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int listwidth = (width / 5) * 3;
        mlayoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_width = (0.9 * height * 0.8);
        } else {
            list_width = (0.9 * width * 0.8);
        }
        slider_image_width = (int) (list_width);


        subscription_list = (RecyclerView) view.findViewById(R.id.subscription_list);
        subscription_show_list = (RecyclerView) view.findViewById(R.id.subscription_show_list);
        subscription_Movie_list = (RecyclerView) view.findViewById(R.id.subscription_Movie_list);
        set_button = (Button) view.findViewById(R.id.set_button);
        skip_button = (Button) view.findViewById(R.id.skip_button);
        switch_image = (ImageView) view.findViewById(R.id.switchImage);

        relativelayout_select_subscriptions = (RelativeLayout) view.findViewById(R.id.relativelayout_select_subscriptions);
        linearlayout_showsbySubscription = (LinearLayout) view.findViewById(R.id.linearlayout_showsbySubscription);
        linearlayout_dynamic_image = (LinearLayout) view.findViewById(R.id.linearlayout_dynamic_image);
        textview_manage = (TextView) view.findViewById(R.id.textview_manage);
        shows_text_tab = (TextView) view.findViewById(R.id.shows_text_tab);
        movies_text_tab = (TextView) view.findViewById(R.id.movies_text_tab);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        horizontal_listview = (HorizontalScrollView) view.findViewById(R.id.horizontalListview);

        view_shows_left = view.findViewById(R.id.view_shows_left);
        view_shows_top = view.findViewById(R.id.view_shows_top);
        view_shows_bottom = view.findViewById(R.id.view_shows_bottom);
        view_movies_bottom = view.findViewById(R.id.view_movies_bottom);
        view_movies_right = view.findViewById(R.id.view_movies_right);
        view_shows_right = view.findViewById(R.id.view_shows_right);
        view_movies_top = view.findViewById(R.id.view_movies_top);
        linearLayout_shows_tab = (LinearLayout) view.findViewById(R.id.linearLayout_shows_tab);
        linearLayout_movies_tab = (LinearLayout) view.findViewById(R.id.linearLayout_movies_tab);

        linearLayout_shows_tab.setOnClickListener(v -> displayShowsTab());

        linearLayout_movies_tab.setOnClickListener(v -> displayMoviesTab());


        skip_button.setVisibility(View.GONE);

        subscription_list.setNestedScrollingEnabled(false);

        /*ViewGroup.LayoutParams params=subscription_list.getLayoutParams();
        params.width=listwidth;
        subscription_list.setLayoutParams(params);*/
        GridLayoutManager manager = new GridLayoutManager(mContext, 5);


        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        subscription_show_list.setLayoutManager(layoutManager);

        LinearLayoutManager linear_layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        subscription_list.setLayoutManager(linear_layoutManager1);
        subscription_list.hasFixedSize();

        GridLayoutManager mlayoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        subscription_Movie_list.setLayoutManager(mlayoutManager);


        int spanCount = 3; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        subscription_show_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        subscription_Movie_list.addItemDecoration(new GridSpacingItemDecoration(4, spacing, includeEdge));

        set_button.setOnClickListener(v -> {
            updateSubscriptions();
//                new updateSubscriptions().execute();
            /*Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            finish();*/
        });

        labelNoData.setOnClickListener(v -> {
            if (!mSessionExpired)
                loadSubscriptionAPI();
//                    new LoadSubscriptionAPI().execute();
        });

        textview_manage.setOnClickListener(v -> {
            try {
                HomeActivity.setOnBackPressedListener(null);
                linearlayout_showsbySubscription.setVisibility(View.GONE);
                relativelayout_select_subscriptions.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        switch_image.setOnClickListener(v -> swap_switch());

        if (!mParam1.isEmpty()) {
            relativelayout_select_subscriptions.setVisibility(View.GONE);
        }

        loadSubscriptionAPI();
//        new LoadSubscriptionAPI().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.SUBSCRIPTIONS_SCREEN);
    }


    @Override
    public void onDestroyView() {
        PresenterMyInterest.dismisDialog();
        super.onDestroyView();
    }

    private void swap_switch() {
        if (iscableSelected) {
            iscableSelected = false;
            switch_image.setImageResource(R.drawable.off);


        } else {
            iscableSelected = true;
            switch_image.setImageResource(R.drawable.on);
        }
    }

    private void updateSubscriptions() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
            try {
                String value = "";
                try {
                    if (all_subscriptionList != null && all_subscriptionList.size() > 0)
                        for (int i = 0; i < all_subscriptionList.size(); i++)
                            if (all_subscriptionList.get(i).isSelected()) {
                                if (!TextUtils.isEmpty(value))
                                    value += ",";
                                value += all_subscriptionList.get(i).getCode();
                            }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (iscableSelected) {
                    if (!TextUtils.isEmpty(value))
                        value = "CABLE," + value;
                    else value += "CABLE";
                }
                if (!TextUtils.isEmpty(value)) {
                    String list[] = value.split(",");
                    PreferenceManager.setSubscribedList(list);
                } else {
                    String list[] = new String[1];
                    PreferenceManager.setSubscribedList(list);
                }

                final JSONObject response = JSONRPCAPI.setUserSubscription(PreferenceManager.getAccessToken(), value);
                handler.post(() -> {
                    if (response == null)
                        Toast.makeText(getActivity(), "Subscriptions Updated", Toast.LENGTH_SHORT).show();

                    if (all_subscriptionList != null && all_subscriptionList.size() > 0)
                        displayShowsbysubscriptions();
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mProgressHUD.dismiss();
            }
        });
        thread.start();
    }

    private void displayShowsbysubscriptions() {
        HomeActivity.setOnBackPressedListener(onBackPressedListener);
        linearlayout_showsbySubscription.setVisibility(View.VISIBLE);
        relativelayout_select_subscriptions.setVisibility(View.GONE);
        final ArrayList<UserAllSubscriptionBean> selectedList = new ArrayList<>();
        for (int i = 0; i < all_subscriptionList.size(); i++) {
            if (all_subscriptionList.get(i).isSelected()) {
                selectedList.add(all_subscriptionList.get(i));
            }
        }

        if (!mParam1.isEmpty()) {
            if (mParam1.trim().equalsIgnoreCase("subscriptions-movies")) {
                displayMoviesTab();
            } else if (mParam1.equalsIgnoreCase("subscription-tv")) {
                displayShowsTab();
            }
        }

        if (selectedList != null) {

            linearlayout_dynamic_image.removeAllViews();

            ArrayList<CauroselsItemBean> emptyList = new ArrayList<>();
            GridShowAdapter emptyAdapter = new GridShowAdapter(emptyList, getActivity());
            subscription_show_list.setAdapter(emptyAdapter);
            subscription_Movie_list.setAdapter(emptyAdapter);

            for (int i = 0; i < selectedList.size(); i++) {
                final View itemmlayout = mlayoutinflater.inflate(R.layout.subscription_network_item, null);
                final GridViewItem gridview_item = (GridViewItem) itemmlayout.findViewById(R.id.gridview_item);
                if (i == 0) {
                    lastselected = gridview_item;
                    gridview_item.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.subscription_bg));
                    new LoadNetworkData().execute("" + selectedList.get(0).getCode());

                }

                aq.id(gridview_item).image(selectedList.get(i).getImage_url());
              gridview_item.setTag(i);

                gridview_item.setOnClickListener(v -> {
                    String tag = gridview_item.getTag().toString();
                    if (lastselected != null) {
                        lastselected.setBackground(null);
                    }
                    gridview_item.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.subscription_bg));
                    lastselected = gridview_item;

                    String t = selectedList.get(Integer.parseInt(tag)).getCode();

                    new LoadNetworkData().execute("" + t);

                });

                linearlayout_dynamic_image.addView(itemmlayout);


            }

        } else {

        }

    }

    private void loadSubscriptionAPI() {
        labelNoData.setVisibility(View.GONE);
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
            try {
                final Object response = JSONRPCAPI.getUserSubscription(PreferenceManager.getAccessToken());
                handler.post(() -> {
                    if (response == null) {
                        if (InternetConnection.isConnected(getActivity()))
                            loadSubscriptionAPI();
                        else {
                            labelNoData.setText("No internet connection, click here to reload");
                            labelNoData.setVisibility(View.VISIBLE);
                        }
                    } else
                        onLoadedSubscriptionAPI(response);
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }

        });
        thread.start();
    }

    private void onLoadedSubscriptionAPI(Object response) {
        try {
            if (response != null) {
                if (response instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) response;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject result_object = jsonArray.getJSONObject(i);
                            final UserSubscriptionBean userSubscriptionBean = new UserSubscriptionBean(result_object);
                            if (userSubscriptionBean.getCode().equalsIgnoreCase("CABLE"))
                                iscableSelected = userSubscriptionBean.isSelected();
                            else
                                subscriptionList.add(userSubscriptionBean);
                            user_map.put(userSubscriptionBean.getCode(), userSubscriptionBean);

                            handler.post(() -> {
//                                aq.id(new ImageView(mContext)).image(userSubscriptionBean.getImage_url());
//                                aq.id(new ImageView(mContext)).image(userSubscriptionBean.getGray_image_url());
                            });
                        }

                        labelNoData.setVisibility(View.GONE);
                        loadAllSubscriptionAPI();
                        if (iscableSelected)
                            switch_image.setImageResource(R.drawable.on);
                    }
                } else {
                    JSONObject json = (JSONObject) response;
                    if (PresenterMyInterest.getInstance().isTokenExpired(json)) {
                        PresenterMyInterest.getInstance().showSessionExpiredDialog(getActivity());
                        set_button.setVisibility(View.GONE);
                        labelNoData.setVisibility(View.VISIBLE);
                        labelNoData.setText("Your session has been expired because you are logged into multiple devices.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllSubscriptionAPI() {
        labelNoData.setVisibility(View.GONE);
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
            try {
                final JSONArray response;
                if (RabbitTvApplication.getAll_Subscription() != null && RabbitTvApplication.getAll_Subscription().length() > 0)
                    response = RabbitTvApplication.getAll_Subscription();
                else
                    response = JSONRPCAPI.getAllUserSubscription();
                handler.post(() -> onLoadedAllSubscriptionAPI(response));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }

        });
        thread.start();
    }

    private void onLoadedAllSubscriptionAPI(JSONArray response) {
        try {
            if (response == null) {
                labelNoData.setText("No data found, Click here to reload");
                labelNoData.setVisibility(View.VISIBLE);
                return;
            } else labelNoData.setVisibility(View.GONE);

            if (response != null && response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
//                    horizontal_list_date = new ArrayList<>();
                    JSONObject json = response.getJSONObject(i);
                    UserSubscriptionBean item = new UserSubscriptionBean(json);
                    if (user_map != null && user_map.containsKey(item.getCode())) {
                        item = user_map.get(item.getCode());
                    }

                    UserAllSubscriptionBean allSubscriptionBean = new UserAllSubscriptionBean(json, item);
                    if (allSubscriptionBean.getData_list().size() > 0)
                        all_subscriptionList.add(allSubscriptionBean);
                }

                if (iscableSelected) {
                    switch_image.setImageResource(R.drawable.on);
                }

                if (all_subscriptionList != null && all_subscriptionList.size() > 0) {
                    OnDemandContentAdapter mOnDemandContentAdapter = new OnDemandContentAdapter(all_subscriptionList, getActivity());
                    subscription_list.setAdapter(mOnDemandContentAdapter);
                }
                if (!mParam1.isEmpty()) {
                    updateSubscriptions();
//                    new updateSubscriptions().execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIsTablet() {

        if (getActivity() != null) {
            try {
                String inputSystem;
                inputSystem = android.os.Build.ID;
                Log.d(":::", inputSystem);
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated
                Log.d("::::", width + "");
                Log.d("::::", height + "");
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(width / dm.xdpi, 2);
                double y = Math.pow(height / dm.ydpi, 2);
                double screenInches = Math.sqrt(x + y);
                Log.d(":::::", "Screen inches : " + screenInches + "");
                return screenInches > 6.0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        return false;
    }

    private ArrayList<CauroselsItemBean> parseArray(JSONObject carousel_array, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();
        JSONArray show_array;

        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (type.equalsIgnoreCase("s")) {
                    show_array = carousel_array.getJSONArray("shows");
                } else {
                    show_array = carousel_array.getJSONArray("movies");
                }


                if (show_array != null && show_array.length() > 0) {
                    for (int i = 0; i < show_array.length(); i++) {
                        int viewAll_id = 0;
                        String viewAll_title = "", viewAll_type = "", viewAll_image = "";
                        JSONObject object = show_array.getJSONObject(i);
                        if (object.has("id")) {
                            viewAll_id = object.getInt("id");
                        }

                        if (object.has("title")) {
                            viewAll_title = object.getString("title");
                            Log.d("title title::", ":::" + viewAll_title);
                        }
                        if (TextUtils.isEmpty(viewAll_title)) {
                            if (object.has("name")) {
                                viewAll_title = object.getString("name");
                                Log.d("name ::", ":::" + viewAll_title);
                            }
                        }
                        if (object.has("type")) {
                            viewAll_type = object.getString("type");
                        }
                        if (object.has("image")) {
                            viewAll_image = object.getString("image");
                        } else if (object.has("carousel_image")) {
                            viewAll_image = object.getString("carousel_image");
                        }

                        parse_list.add(new CauroselsItemBean(viewAll_image, type, viewAll_id, viewAll_title));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }

    private void displayShowsTab() {
        view_shows_left.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_shows_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        view_movies_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_500));
        view_movies_right.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_500));

        shows_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        movies_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_500));

        view_shows_bottom.setVisibility(View.GONE);
        view_movies_bottom.setVisibility(View.VISIBLE);

        subscription_Movie_list.setVisibility(View.GONE);
        subscription_show_list.setVisibility(View.VISIBLE);
    }

    private void displayMoviesTab() {
        view_shows_left.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_500));
        view_shows_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_grey_500));

        view_movies_top.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        view_movies_right.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        shows_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_500));
        movies_text_tab.setTextColor(ContextCompat.getColor(mContext, R.color.white));

        view_shows_bottom.setVisibility(View.VISIBLE);
        view_movies_bottom.setVisibility(View.GONE);
        subscription_Movie_list.setVisibility(View.VISIBLE);
        subscription_show_list.setVisibility(View.GONE);
    }



    class OnDemandContentAdapter extends RecyclerView.Adapter<OnDemandContentAdapter.DataObjectHolder> {
        ArrayList<UserAllSubscriptionBean> list_data;
        Context context;
        private int mSelectedItem = 0;

        public OnDemandContentAdapter(ArrayList<UserAllSubscriptionBean> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
        }


        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_item, parent, false);
            return new OnDemandContentAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(DataObjectHolder holder, int position, List<Object> payloads) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder,position,payloads);
            }else {
                Bundle bundle = (Bundle) payloads.get(0);
                final UserAllSubscriptionBean item = list_data.get(position);

                if (bundle.containsKey("subscribe")) {
                    String image_url = "";
                    if (item.isSelected()) {
                        image_url = item.getImage_url();
                    } else {
                        image_url = item.getGray_image_url();
                    }
                    Image.loadGridImage(image_url, holder.network_imageView);

                    if (item.isSelected()) {
                        holder.installed_textView.setVisibility(View.VISIBLE);
                        holder.installed_textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.subscribed_selected));
                        holder.installed_textView.setText("Subscribed");
                        holder.tick_imageButton.setVisibility(View.VISIBLE);
                    } else {
                        holder.installed_textView.setVisibility(View.VISIBLE);
                        holder.installed_textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_installed_app));
                        holder.installed_textView.setText("UnSubscribed");
                        holder.tick_imageButton.setVisibility(View.GONE);
                    }
                }
            }

        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            try {
                slider_image_width = (int) (list_width);
                final UserAllSubscriptionBean item = list_data.get(position);

                holder.horizontal_listview_title.setText(Utils.stripHtml(item.getName()));
//                holder.network_imageView.setTag(position);
                holder.installed_textView.setTag(position);

                String image_url = "";
                if (item.isSelected()) {
                    image_url = item.getImage_url();
                } else {
                    image_url = item.getGray_image_url();
                }

                Log.d("network image:::", ":::url:::" + image_url);
//                if (!TextUtils.isEmpty(image_url)) {
                 Image.loadGridImage(image_url, holder.network_imageView);
//                    aq.id(holder.network_imageView).image(context.getResources().getDrawable(R.drawable.abc));
//                }

                try {
                    holder.dynamic_image_layout.removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (item.isSelected()) {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.subscribed_selected));
                    holder.installed_textView.setText("Subscribed");
                    holder.tick_imageButton.setVisibility(View.VISIBLE);
                } else {
                    holder.installed_textView.setVisibility(View.VISIBLE);
                    holder.installed_textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_installed_app));
                    holder.installed_textView.setText("UnSubscribed");
                    holder.tick_imageButton.setVisibility(View.GONE);
                }



                LayoutInflater inflater = LayoutInflater.from(context);
                for (int l = 0; l < item.getData_list().size(); l++) {
                    HorizontalListItemBinding binding = HorizontalListItemBinding.inflate(inflater);

                    LinearLayout.LayoutParams vp;
                    if (checkIsTablet()) {
                        vp = new LinearLayout.LayoutParams((slider_image_width - 40) / 3, 500);
                        if (l != 0)
                            vp.setMargins(20, 0, 0, 0);
                    } else {
                        if (item.getData_list().get(l).getType().equalsIgnoreCase("m")) {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (l != 0)
                                vp.setMargins(20, 0, 0, 0);
                        } else {
                            vp = new LinearLayout.LayoutParams((slider_image_width - 20) / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (l != 0)
                                vp.setMargins(20, 0, 0, 0);
                        }
                    }

                    String image_url_scroll = item.getData_list().get(l).getImage();
//                    binding.dynamicImageView.loadImage(image_url_scroll);
                    Image.loadShowImage(image_url_scroll, binding.dynamicImageView);
                    binding.dynamicImageView.setLayoutParams(vp);

                    int finalL = l;
                    binding.getRoot().setOnClickListener(v -> {
                        int pos = holder.getAdapterPosition();
                        if (!list_data.get(pos).isSelected()) {
                           /* FragmentManager fm = getActivity().getSupportFragmentManager();
                            AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance(""+item.getNetwork_name(),""+item.getNetwork_image(),item.getNetwork_link());

                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");*/
                        } else {
                            if (item.getData_list().get(finalL).getType().equalsIgnoreCase("M") || item.getData_list().get(finalL).getType().equalsIgnoreCase("movie")) {
                                startActivity(MovieDetailsActivity.getIntent(getActivity(), item.getData_list().get(finalL).getId()));
                            } else {
                                int payMode = RabbitTvApplication.getPaymode();
                                startActivity(ShowDetailsActivity.getIntent(getActivity(), item.getData_list().get(finalL).getId(), payMode));
                            }
                        }

                    });
                    holder.dynamic_image_layout.addView(binding.getRoot());

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            HorizontalScrollView horizontal_listview;
            TextView horizontal_listview_title, view_all_text, installed_textView;
            LinearLayout recycler_layout, image_view, dynamic_image_layout;
            ImageView left_slide, right_slide, tick_imageButton;
            GridViewItem network_imageView;
            LinearLayout network_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                network_imageView = (GridViewItem) itemView.findViewById(R.id.network_imageViewv);
                left_slide = (ImageView) itemView.findViewById(R.id.left_slide);
                right_slide = (ImageView) itemView.findViewById(R.id.right_slide);
                tick_imageButton = (ImageView) itemView.findViewById(R.id.tick_imageButton);

                installed_textView = (TextView) itemView.findViewById(R.id.txtAppSize);
                view_all_text = (TextView) itemView.findViewById(R.id.btnViewAll);
                horizontal_listview_title = (TextView) itemView.findViewById(R.id.gameCategoryName);

                dynamic_image_layout = (LinearLayout) itemView.findViewById(R.id.dynamic_image_layout);
                recycler_layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);
                network_layout = (LinearLayout) itemView.findViewById(R.id.network_layout);
                horizontal_listview = (HorizontalScrollView) itemView.findViewById(R.id.horizontalListview);

                network_imageView.setOnClickListener(v->onChanged());
                installed_textView.setOnClickListener(v->onChanged());

            }

            private void onChanged() {
                list_data.get(getAdapterPosition()).setSelected(!list_data.get(getAdapterPosition()).isSelected());
//                notifyItemChanged(getAdapterPosition());
                Bundle bundle = new Bundle();
                bundle.putBoolean("subscribe", true);
                notifyItemChanged(getAdapterPosition(),bundle);
            }
        }
    }

    private class LoadNetworkData extends AsyncTask<String, Object, Object> {

        ArrayList<Slider> slider_list = new ArrayList<>();
        ArrayList<HorizontalListitemBean> horizontalListBeans;
        int id;
        ProgressHUD mProgressHUD;
        private ArrayList<CauroselsItemBean> listdataShow = new ArrayList<>();
        private ArrayList<CauroselsItemBean> listdataMovies = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                linearLayout_shows_tab.setEnabled(true);
                linearLayout_movies_tab.setEnabled(true);
                subscription_show_list.setVisibility(View.GONE);
                subscription_Movie_list.setVisibility(View.GONE);
                mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                String code = params[0];

                //JSONArray carousel_array = JSONRPCAPI.getTVNetworkList(g_id, 100, 0, 0);
                JSONObject response_object = JSONRPCAPI.getUserSubscriptionbyCode(code);
                listdataShow = parseArray(response_object, "S");
                listdataMovies = parseArray(response_object, "M");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mProgressHUD.dismiss();
            try {

                if (listdataShow.size() > 0) {
                    subscription_show_list.setVisibility(View.VISIBLE);
                    GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataShow, getActivity());
                    subscription_show_list.setAdapter(viewAll_adpater);
                    if (mParam1.isEmpty())
                        displayShowsTab();
                } else {
                    try {
                        GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataShow, getActivity());
                        subscription_show_list.setAdapter(viewAll_adpater);
                        linearLayout_shows_tab.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (listdataMovies.size() > 0) {
                    subscription_Movie_list.setVisibility(View.GONE);
                    GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataMovies, getActivity());
                    subscription_Movie_list.setAdapter(viewAll_adpater);
                    if (listdataShow.size() == 0) {
                        linearLayout_shows_tab.setEnabled(false);
                        if (mParam1.isEmpty())
                            displayMoviesTab();
                    }
                } else {
                    try {
                        GridShowAdapter viewAll_adpater = new GridShowAdapter(listdataMovies, getActivity());
                        subscription_Movie_list.setAdapter(viewAll_adpater);
                        linearLayout_movies_tab.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!mParam1.isEmpty()) {
                    if (mParam1.trim().equalsIgnoreCase("subscriptions-movies")) {
                        subscription_Movie_list.setVisibility(View.VISIBLE);
                        subscription_show_list.setVisibility(View.GONE);
                    } else if (mParam1.equalsIgnoreCase("subscription-tv")) {
                        subscription_Movie_list.setVisibility(View.GONE);
                        subscription_show_list.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    mParam1 = "";
                    mProgressHUD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class GridShowAdapter extends RecyclerView.Adapter<GridShowAdapter.DataObjectHolder> {
        ArrayList<CauroselsItemBean> viewAll_list_data;
        Context context;


        public GridShowAdapter(ArrayList<CauroselsItemBean> viewAll_list_data, Context context) {
            try {
                this.viewAll_list_data = viewAll_list_data;
                this.context = context;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public GridShowAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new GridShowAdapter.DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final GridShowAdapter.DataObjectHolder holder, final int position) {
            try {
                final CauroselsItemBean item = viewAll_list_data.get(position);

                final String image_url = item.getCarousel_image();
                final String type = item.getType();

                if (type.equalsIgnoreCase("n")) {
                    holder.gridview_item.setVisibility(View.VISIBLE);
                    holder.imageView.setVisibility(View.GONE);
                    if (image_url != null) {
                        Image.loadGridImage(image_url,holder.gridview_item);
//                        Picasso.with(context
//                                .getApplicationContext())
//                                .load(image_url)
//                                .fit()
//                                .placeholder(R.drawable.loader_network).into(holder.gridview_item);
                    }

                } else if (type.equalsIgnoreCase("M")) {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
                        Image.loadMovieImage(image_url, holder.imageView);
//                        holder.imageView.loadMovieImage(image_url);
                    }
                } else {
                    holder.gridview_item.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    if (image_url != null) {
//                        holder.imageView.loadImage(image_url);
                        Image.loadShowImage(image_url, holder.imageView);
                    }
                }

                Log.d("categoryimage", "===>" + image_url);
                holder.imageView.setOnClickListener(v -> {

                    if (type.equalsIgnoreCase("M") || type.equalsIgnoreCase("movie")) {
                        startActivity(MovieDetailsActivity.getIntent(getActivity(), item.getId()));
                    } else {
                        startActivity(ShowDetailsActivity.getIntent(getActivity(), item.getId(), RabbitTvApplication.getPaymode()));
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return viewAll_list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private final DynamicImageView imageView;
            GridViewItem gridview_item;

            public DataObjectHolder(View itemView) {
                super(itemView);


                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
                gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);

            }
        }
    }

}
