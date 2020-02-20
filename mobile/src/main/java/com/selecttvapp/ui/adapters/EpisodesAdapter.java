package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.network.common.AppFonts;
import com.google.android.flexbox.FlexboxLayout;
import com.selecttvapp.R;
import com.selecttvapp.SortedListHashMap.SortedListHashMap;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Image;
import com.selecttvapp.episodeDetails.EpisodesListFragment;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterAppsLinksList;
import com.selecttvapp.presentation.fragments.PresenterEpisodesList;
import com.selecttvapp.presentation.views.ViewAppsLinksList;
import com.selecttvapp.ui.bean.AppFormatBean;
import com.selecttvapp.ui.bean.SeasonsBean;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.AppStoreDialogFragment;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class EpisodesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewAppsLinksList {

    private Typeface MYRIADPRO_BOLD, MYRIADPRO_ITALIC, MYRIADPRO_REGULAR, MYRIADPRO_SEMIBOLD;

    private SortedListHashMap<Integer, SeasonsBean> episodeBeansList = new SortedListHashMap<>(null);
    private Context context;
    private Activity activity;
    private PresenterEpisodesList presenter;
    private String mAirtime = "";
    private boolean[] listDescDownload;
    private FragmentManager fragmentManager;
    private EpisodesListFragment fragment;
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
    private TextView btnSD, btnHD, btnHDX, labelNoData2;
    private FlexboxLayout mFlexboxLayout;
    private ImageView switchImage;
    private ProgressHUD mProgressHUD;
    private boolean switchSelected = false;
    private String contentType = "";
    private Object JSON_response = "";
    int mLastClickedPosition = -1;


    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public SortedListHashMap<Integer, SeasonsBean> getEpisodeBeansList() {
        return episodeBeansList;
    }


    public EpisodesAdapter(Context context, SortedListHashMap<Integer, SeasonsBean> episodeBeansList, EpisodesListFragment frgment) {
        this.context = context;
        this.activity = (Activity) context;
        this.episodeBeansList.clear();
        this.episodeBeansList = episodeBeansList;
        this.fragment = frgment;
        listDescDownload = new boolean[episodeBeansList.size()];
        for (int i = 0; i < listDescDownload.length; i++) {
            listDescDownload[i] = false;
        }

        MYRIADPRO_BOLD = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_BOLD);
        MYRIADPRO_ITALIC = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_ITALIC);
        MYRIADPRO_REGULAR = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_REGULAR);
        MYRIADPRO_SEMIBOLD = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_SEMIBOLD);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.season_episode_layout1, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        MyViewHolder holder = (MyViewHolder) holder1;

        holder.episodeName.setTypeface(MYRIADPRO_SEMIBOLD);
        holder.episodeDate.setTypeface(MYRIADPRO_ITALIC);
        holder.freeLabel.setTypeface(MYRIADPRO_BOLD);
        holder.episodeDescription.setTypeface(MYRIADPRO_REGULAR);
        holder.txtMore.setTypeface(MYRIADPRO_REGULAR);

        final SeasonsBean seasonsBean = episodeBeansList.get(position);
        Image.loadShowImage(context, seasonsBean.getPoster_url(), holder.episodeImage);// display image
        holder.episodeName.setText(seasonsBean.getName());

        if (seasonsBean.hasAuthenticatedItems()) {
            holder.freeLabel.setVisibility(View.GONE);
        } else if (seasonsBean.getFreeLinks()) {
            holder.freeLabel.setVisibility(View.VISIBLE);
        }

        try {
            if (seasonsBean.getDescription() == null || seasonsBean.getDescription().isEmpty())
                getDescription(holder, seasonsBean);
//                            new GetDescription(holder, seasonsBean).execute();
            else {
                holder.layoutDescriptionHolder.setVisibility(View.VISIBLE);
                holder.episodeDescription.setText(seasonsBean.getDescription());
                holder.episodeDescription.post(new Runnable() {
                    public void run() {
                        int lineCount = holder.episodeDescription.getLineCount();
                        if (lineCount > 2) {
                            holder.episodeDescription.setPadding(20, 20, 20, 0);
                            holder.txtMore.setVisibility(View.VISIBLE);
                            holder.episodeDescription.setMaxLines(2);
                            holder.txtMore.setText("more..");
                        } else {
                            holder.episodeDescription.setPadding(20, 20, 20, 20);
                            holder.txtMore.setVisibility(View.GONE);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  try {
            if (fragmentManager != null) {
                int layoutId = Utils.GetUniqueID();
                holder.layoutAppsList.setId(layoutId);
                if (fragmentManager.findFragmentById(layoutId) != null)
                    fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(layoutId));

                JSONObject jsonObject = new JSONObject(seasonsBean.getShowLinks());
                fragmentManager.beginTransaction().replace(layoutId, AppLinksFragment.newInstance(jsonObject, "show")).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            mAirtime = seasonsBean.getAir_date();
            if (!mAirtime.equals("null") && !mAirtime.equals("")) {
                String[] separated = mAirtime.split("-");
                String part1 = separated[0];
                String part2 = separated[1];
                String part3 = separated[2];
                switch (part2) {
                    case "01":
                        holder.episodeDate.setText("(" + "Jan. " + part3 + ", " + part1 + ")");
                        break;
                    case "02":
                        holder.episodeDate.setText("(" + "Feb. " + part3 + ", " + part1 + ")");
                        break;
                    case "03":
                        holder.episodeDate.setText("(" + "Mar. " + part3 + ", " + part1 + ")");
                        break;
                    case "04":
                        holder.episodeDate.setText("(" + "Apr. " + part3 + ", " + part1 + ")");
                        break;
                    case "05":
                        holder.episodeDate.setText("(" + "May. " + part3 + ", " + part1 + ")");
                        break;
                    case "06":
                        holder.episodeDate.setText("(" + "Jun. " + part3 + ", " + part1 + ")");
                        break;
                    case "07":
                        holder.episodeDate.setText("(" + "Jul. " + part3 + ", " + part1 + ")");
                        break;
                    case "08":
                        holder.episodeDate.setText("(" + "Aug. " + part3 + ", " + part1 + ")");
                        break;
                    case "09":
                        holder.episodeDate.setText("(" + "Sep. " + part3 + ", " + part1 + ")");
                        break;
                    case "10":
                        holder.episodeDate.setText("(" + "Oct. " + part3 + ", " + part1 + ")");
                        break;
                    case "11":
                        holder.episodeDate.setText("(" + "Nov. " + part3 + ", " + part1 + ")");
                        break;
                    case "12":
                        holder.episodeDate.setText("(" + "Dec. " + part3 + ", " + part1 + ")");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocusView(holder.episodeDescription, false);
                if (holder.txtMore.getText().equals("less..")) {
                    holder.episodeDescription.setMaxLines(2);
                    holder.txtMore.setText("more..");
                } else {
                    holder.episodeDescription.setMaxLines(100);
                    holder.txtMore.setText("less..");
                }
            }
        });
//&& TextUtils.isEmpty(episodeBeansList.get(position).getShowLinks())
        System.out.println("App Link  current position ::" + mLastClickedPosition + "::::: adapterPositiom ::" + holder.getAdapterPosition());
        if (mLastClickedPosition == holder.getAdapterPosition()) {
            switchSelected = false;
            free_sd_list.clear();
            free_hd_list.clear();
            free_hdx_list.clear();
            free_other.clear();
            paid_sd_list.clear();
            paid_hd_list.clear();
            paid_hdx_list.clear();
            paid_other.clear();

            btnSD = holder.btnSD;
            btnHD = holder.btnHD;
            btnHDX = holder.btnHDX;
            labelNoData2 = holder.labelNoData2;
            mFlexboxLayout = holder.mFlexboxLayout;
            switchImage = holder.switchImage;
            presenterAppsLinks = new PresenterAppsLinksList(activity);
            if (presenterAppsLinks != null)
                presenterAppsLinks.setListener(this);

            contentType = "show";
            presenterAppsLinks.setContentType(contentType);

            if (presenterAppsLinks != null)
                presenterAppsLinks.setFont(FontHelper.MYRIADPRO_REGULAR, holder.freeLabel, holder.buyLabel, holder.btnSD, holder.btnHD, holder.btnHDX);

            try {
                JSONObject jsonObject = new JSONObject(episodeBeansList.get(mLastClickedPosition).getShowLinks());
                JSON_response = jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (JSON_response != null)
                if (presenterAppsLinks != null)
                    if (JSON_response instanceof JSONObject)
                        presenterAppsLinks.makelistingsNext((JSONObject) JSON_response);

            holder.layoutAppsList.setVisibility(View.VISIBLE);
            holder.switchImage.requestFocus();
            mProgressHUD.dismiss();

        } else {
            holder.layoutAppsList.setVisibility(View.GONE);
            btnSD = null;
            btnHD = null;
            btnHDX = null;
            labelNoData2 = null;
            mFlexboxLayout = null;
            switchImage = null;
            JSON_response = "";
            System.out.println("App Link  Condition False :: View InVisible");
        }


        holder.episodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //  if (holder.layoutAppsList.getVisibility() != View.VISIBLE) {
                    if (TextUtils.isEmpty(episodeBeansList.get(position).getShowLinks())) {
                        mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
                        mLastClickedPosition = holder.getAdapterPosition();
                        setFocusView(holder.layoutAppsList, false);

                        int pos = holder.getAdapterPosition();
                        int id = episodeBeansList.get(pos).getId();

                        Log.d("ID", "onClick: Id" + id);
                        SeasonsBean seasonsBean1 = episodeBeansList.getByKey(id);
                        PresenterEpisodesList presenterEpisodesList = new PresenterEpisodesList(fragment);
                        presenterEpisodesList.loadEpisodesData(seasonsBean1);


                        //  holder.layoutAppsList.setVisibility(View.VISIBLE);
                        System.out.println("visibility onClick");


                    } else {
                        mLastClickedPosition = -1;
                        episodeBeansList.get(position).setShowLinks("");
                        holder.layoutAppsList.setVisibility(View.GONE);
                        System.out.println("visibility gone2");
                        JSON_response = "";

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return episodeBeansList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DynamicImageView episodeImage;
        private DynamicImageView playIcon;
        private TextView episodeName;
        private TextView episodeDate;
        private TextView freeLabel;
        private TextView txtMore;
        private TextView episodeDescription;
        private TextView labelNoData;
        //        private RelativeLayout layoutEpisodeContents;
        private RelativeLayout layoutDescriptionHolder;

        private FrameLayout layoutAppsList;
        private FlexboxLayout mFlexboxLayout;
        private LinearLayout layoutTools;
        private ImageView switchImage;
        private TextView buyLabel, btnSD, btnHD,
                btnHDX;
        private TextView labelNoData2;

        private String selected_app_format = "android";
        private boolean switchSelected = false;


        public MyViewHolder(View itemView) {
            super(itemView);
            //            layoutEpisodeContents = (RelativeLayout) itemView.findViewById(R.id.layoutEpisodeContents);
            episodeImage = (DynamicImageView) itemView.findViewById(R.id.episodeImage1);
            playIcon = (DynamicImageView) itemView.findViewById(R.id.playIcon1);
            episodeName = (TextView) itemView.findViewById(R.id.episodeName);
            episodeDate = (TextView) itemView.findViewById(R.id.episodeDate);
            freeLabel = (TextView) itemView.findViewById(R.id.freeLabel);
            episodeDescription = (TextView) itemView.findViewById(R.id.showDescription);
            txtMore = (TextView) itemView.findViewById(R.id.txtMore);
            labelNoData = (TextView) itemView.findViewById(R.id.labelNoData);
            layoutDescriptionHolder = (RelativeLayout) itemView.findViewById(R.id.layoutDescriptionHolder);
            layoutAppsList = (FrameLayout) itemView.findViewById(R.id.layoutAppsList);

            mFlexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.flexbox_layout1);
            layoutTools = (LinearLayout) itemView.findViewById(R.id.layoutTools);
            labelNoData2 = (TextView) itemView.findViewById(R.id.labelNoData);
            switchImage = (ImageView) itemView.findViewById(R.id.switchImage_tv_apps);
            buyLabel = (TextView) itemView.findViewById(R.id.buyLabel);
            btnSD = (TextView) itemView.findViewById(R.id.btnSD1);
            btnHD = (TextView) itemView.findViewById(R.id.btnHD1);
            btnHDX = (TextView) itemView.findViewById(R.id.btnHDX1);

            switchImage.setOnClickListener(this);
            btnSD.setOnClickListener(this);
            btnHD.setOnClickListener(this);
            btnHDX.setOnClickListener(this);

            episodeImage.setFocusable(true);
            episodeImage.setFocusableInTouchMode(true);
            episodeImage.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            episodeImage.setNextFocusUpId(R.id.favoriteIcon);
            episodeImage.setNextFocusLeftId(R.id.switchImage1);
            //episodeImage.setNextFocusDownId(R.id.switchImage_tv_apps);

            /*switchImage.setFocusableInTouchMode(true);
            switchImage.setFocusable(true);
            switchImage.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            switchImage.setPadding(10,10,10,10);
            switchImage.setNextFocusUpId(R.id.episodeImage1);*/
            switchImage.setFocusable(true);
            switchImage.setFocusableInTouchMode(true);
            switchImage.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            switchImage.setNextFocusUpId(R.id.episodeImage1);
            switchImage.setNextFocusRightId(R.id.btnSD1);
            switchImage.setNextFocusDownId(R.id.movie_payment_imageview);

            btnSD.setFocusable(true);
            btnSD.setFocusableInTouchMode(true);
            btnSD.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            btnSD.setNextFocusUpId(R.id.episodeImage1);
            btnSD.setNextFocusRightId(R.id.btnHD1);
            btnSD.setNextFocusLeftId(R.id.btnSD1);
            btnSD.setNextFocusDownId(R.id.movie_payment_imageview);

            btnHD.setFocusable(true);
            btnHD.setFocusableInTouchMode(true);
            btnHD.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            btnHD.setNextFocusUpId(R.id.episodeImage1);
            btnHD.setNextFocusRightId(R.id.btnHDX1);
            btnHD.setNextFocusLeftId(R.id.btnHD1);
            btnHD.setNextFocusDownId(R.id.movie_payment_imageview);

            btnHDX.setFocusable(true);
            btnHDX.setFocusableInTouchMode(true);
            btnHDX.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_white));
            btnHDX.setNextFocusUpId(R.id.episodeImage1);
            btnHDX.setNextFocusLeftId(R.id.btnHD1);
            btnHDX.setNextFocusDownId(R.id.movie_payment_imageview);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.switchImage:

                    mFlexboxLayout.setVisibility(View.VISIBLE);//flex box layout
                    labelNoData2.setVisibility(View.GONE);

                    if (switchSelected) {
                        switchSelected = false;
                        switchImage.setImageResource(R.drawable.off);
                        showAndroidFreeLinksApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
                    } else {
                        switchSelected = true;
                        switchImage.setImageResource(R.drawable.on);
                        showAndroidBuyRentApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
                    }
                    break;

                case R.id.btnSD:
                    setActiveTextView(context, btnSD);
                    setInActiveTextView(context, btnHD);
                    setInActiveTextView(context, btnHDX);

                    if (selected_app_format.equalsIgnoreCase("android"))
                        if (switchSelected)
                            presenterAppsLinks.createAppsLinksLayout(context, paid_sd_list, mFlexboxLayout);
                        else
                            presenterAppsLinks.createAppsLinksLayout(context, free_sd_list, mFlexboxLayout);
                    break;

                case R.id.btnHD:
                    setActiveTextView(context, btnHD);
                    setInActiveTextView(context, btnSD);
                    setInActiveTextView(context, btnHDX);

                    if (selected_app_format.equalsIgnoreCase("android"))
                        if (switchSelected)
                            presenterAppsLinks.createAppsLinksLayout(context, paid_hd_list, mFlexboxLayout);
                        else
                            presenterAppsLinks.createAppsLinksLayout(context, free_hd_list, mFlexboxLayout);
                    break;

                case R.id.btnHDX:
                    setActiveTextView(context, btnHDX);
                    setInActiveTextView(context, btnSD);
                    setInActiveTextView(context, btnHD);

                    if (selected_app_format.equalsIgnoreCase("android"))
                        if (switchSelected)
                            presenterAppsLinks.createAppsLinksLayout(context, paid_hdx_list, mFlexboxLayout);
                        else
                            presenterAppsLinks.createAppsLinksLayout(context, free_hdx_list, mFlexboxLayout);
                    break;
            }
        }
    }

    private void setFocusView(final View view, boolean focusableTouchMode) {
        view.setFocusableInTouchMode(focusableTouchMode);
        view.post(new Runnable() {
            @Override
            public void run() {
                view.requestFocusFromTouch();
                view.setFocusableInTouchMode(false);
                view.clearFocus();
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

    private void showAndroidFreeLinksApps(TextView btnSD, TextView btnHD, TextView btnHDX, TextView labelNoData2, FlexboxLayout mFlexboxLayout) {
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
                labelNoData2.setText("No free links currently available for the current episode");
            else
                labelNoData2.setText("No free mobile links currently available for this movie");
            labelNoData2.setVisibility(View.VISIBLE);
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

    private void showAndroidBuyRentApps(TextView btnSD, TextView btnHD, TextView btnHDX, TextView labelNoData2, FlexboxLayout mFlexboxLayout) {
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
                labelNoData2.setText("No links currently available for the current episode");
            else
                labelNoData2.setText("No mobile links currently available for this movie");
            labelNoData2.setVisibility(View.VISIBLE);
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

   /* public void createAppsLinksLayout(Context context, ArrayList<AppFormatBean> data_list, FlexboxLayout mFlexboxLayout) {
        mFlexboxLayout.removeAllViews();
        if (data_list.size() <= 0)
            mFlexboxLayout.setVisibility(View.GONE);

        for (AppFormatBean appFormatBean : data_list) {
            View itemView = buildView(context, appFormatBean);
            mFlexboxLayout.addView(itemView);
        }
    }*/

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

    @Override
    public void showAppStoreDialog(AppFormatBean app) {

        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
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
            showAndroidBuyRentApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
        } else {
            if ((free_sd_list.size() == 0) && (free_hd_list.size() == 0) && (free_hdx_list.size() == 0)) {
                swapSwitch();
            } else {
                showAndroidFreeLinksApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
            }
        }
    }

    private void swapSwitch() {
        mFlexboxLayout.setVisibility(View.VISIBLE);//flex box layout
        labelNoData2.setVisibility(View.GONE);

        if (switchSelected) {
            switchSelected = false;
            switchImage.setImageResource(R.drawable.off);
            showAndroidFreeLinksApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
        } else {
            switchSelected = true;
            switchImage.setImageResource(R.drawable.on);
            showAndroidBuyRentApps(btnSD, btnHD, btnHDX, labelNoData2, mFlexboxLayout);
        }
    }
    private void getDescription(final MyViewHolder holder, final SeasonsBean seasonsBean) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int episodeId = seasonsBean.getId();
                    JSONObject m_jsonEpisodeDeatils = JSONRPCAPI.getEpisodeDetail(episodeId);
                    final String description = m_jsonEpisodeDeatils.getString("description");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seasonsBean.setDescription(description);
                            holder.layoutDescriptionHolder.setVisibility(View.VISIBLE);
                            holder.episodeDescription.setText(description);//getdescription(episode id)  //pass episode id
                            int lineCount = holder.episodeDescription.getLineCount();
                            if (lineCount > 2) {
                                holder.episodeDescription.setPadding(20, 20, 20, 0);
                                holder.txtMore.setVisibility(View.VISIBLE);
                                holder.episodeDescription.setMaxLines(2);
                                holder.txtMore.setText("more..");
                            } else {
                                holder.episodeDescription.setPadding(20, 20, 20, 20);
                                holder.txtMore.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
