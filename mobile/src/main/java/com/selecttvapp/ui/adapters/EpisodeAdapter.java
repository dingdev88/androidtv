package com.selecttvapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.network.common.AppFonts;
import com.selecttvapp.R;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utils;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.bean.SeasonsBean;
import com.selecttvapp.ui.fragments.AppLinksFragment;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 01-Aug-17.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.DataObjectHolder> {
    private Typeface MYRIADPRO_BOLD, MYRIADPRO_ITALIC, MYRIADPRO_REGULAR, MYRIADPRO_SEMIBOLD;

    private ArrayList<SeasonsBean> episodeBeansList = new ArrayList<>();
    private ArrayList<EpisodeAdapter.DataObjectHolder> holders = new ArrayList<>();
    private Context context;
    private Activity activity;
    private String mAirtime = "";
    private boolean[] listDescDownload;
    private FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ArrayList<SeasonsBean> getEpisodeBeansList() {
        return episodeBeansList;
    }

    public EpisodeAdapter(Context context, ArrayList<SeasonsBean> episodeBeansList) {
        this.context = context;
        this.activity = (Activity) context;
        this.episodeBeansList.clear();
        this.episodeBeansList = episodeBeansList;
        listDescDownload = new boolean[episodeBeansList.size()];
        for (int i = 0; i < listDescDownload.length; i++) {
            listDescDownload[i] = false;
        }
        holders.clear();

        MYRIADPRO_BOLD = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_BOLD);
        MYRIADPRO_ITALIC = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_ITALIC);
        MYRIADPRO_REGULAR = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_REGULAR);
        MYRIADPRO_SEMIBOLD = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_SEMIBOLD);
    }

    @Override
    public EpisodeAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.season_episode_layout1, parent, false);
        return new EpisodeAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final EpisodeAdapter.DataObjectHolder holder, final int position) {
        holders.add(holder);

        holder.episodeName.setTypeface(MYRIADPRO_SEMIBOLD);
        holder.episodeDate.setTypeface(MYRIADPRO_ITALIC);
        holder.freeLabel.setTypeface(MYRIADPRO_BOLD);
        holder.episodeDescription.setTypeface(MYRIADPRO_REGULAR);
        holder.txtMore.setTypeface(MYRIADPRO_REGULAR);

//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;

//        if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            holder.layoutEpisodeContents.getLayoutParams().height = (width / 100) * 30;
//            holder.seasons_episodes_poster.getLayoutParams().height = (width / 100) * 30;
//        } else {
//            holder.layoutEpisodeContents.getLayoutParams().height = (height / 100) * 35;
//            holder.seasons_episodes_poster.getLayoutParams().height = (height / 100) * 35;
//        }

        final SeasonsBean seasonsBean = episodeBeansList.get(position);

        Image.loadShowImage(context, seasonsBean.getPoster_url(), holder.episodeImage);// display image
        holder.episodeName.setText(seasonsBean.getName());

        if (seasonsBean.hasAuthenticatedItems()) {
            holder.freeLabel.setVisibility(View.GONE);
        } else if (seasonsBean.getFreeLinks()) {
            holder.freeLabel.setVisibility(View.VISIBLE);
        } else {
//                        if (sub_list.length > 0 && seasonsBean.getSubscriptionsList().size() > 0) {
//                            for (int i = 0; i < seasonsBean.getSubscriptionsList().size(); i++)
//                                if (Arrays.asList(sub_list).contains(seasonsBean.getSubscriptionsList().get(i))) {
//                                    holder.episode_free_textview.setVisibility(View.GONE);
//                                    break;
//                                }
//                        }
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

        try {
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
        }

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

        holder.episodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // poster_url = seasonsBeans.get(position).getPoster_url();
//                            mEpisodeId = episodeBeansList.get(position).getId();
//                            selecetd_season = seasonid;
//                            new EpisodeDetailsLoading().execute();
            }
        });

        holder.playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (holder.layoutAppsList.getVisibility() != View.VISIBLE) {
                        if (holders.size() > 0) {
                            for (int i = 0; i < holders.size(); i++) {
                                if (holders.get(i) != null) {
                                    holders.get(i).layoutAppsList.setVisibility(View.GONE);
                                }
                            }
                        }
                        holder.layoutAppsList.setVisibility(View.VISIBLE);
                        setFocusView(holder.layoutAppsList, true);
                    } else
                        holder.layoutAppsList.setVisibility(View.GONE);
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

    public class DataObjectHolder extends RecyclerView.ViewHolder {
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

        public DataObjectHolder(View itemView) {
            super(itemView);
//            layoutEpisodeContents = (RelativeLayout) itemView.findViewById(R.id.layoutEpisodeContents);
            episodeImage = (DynamicImageView) itemView.findViewById(R.id.episodeImage);
            playIcon = (DynamicImageView) itemView.findViewById(R.id.playIcon);
            episodeName = (TextView) itemView.findViewById(R.id.episodeName);
            episodeDate = (TextView) itemView.findViewById(R.id.episodeDate);
            freeLabel = (TextView) itemView.findViewById(R.id.freeLabel);
            episodeDescription = (TextView) itemView.findViewById(R.id.showDescription);
            txtMore = (TextView) itemView.findViewById(R.id.txtMore);
            labelNoData = (TextView) itemView.findViewById(R.id.labelNoData);
            layoutDescriptionHolder = (RelativeLayout) itemView.findViewById(R.id.layoutDescriptionHolder);

            layoutAppsList = (FrameLayout) itemView.findViewById(R.id.layoutAppsList);
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

    private void getDescription(final EpisodeAdapter.DataObjectHolder holder, final SeasonsBean seasonsBean) {
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


//    public class EpisodeDetailsLoading extends AsyncTask<String, Object, Object> {
//        EpisodeAdapter.DataObjectHolder holder;
//        JSONObject m_jsonshowLinks;
//
//        ProgressHUD mProgressHUD;
//
//        public EpisodeDetailsLoading(EpisodeAdapter.DataObjectHolder holder) {
//            this.holder = holder;
//        }
//
//
//        @Override
//        protected Object doInBackground(String... params) {
//            try {
//                int nIndex = 1;
//                int seasonId = Integer.parseInt(params[0]);
//                int episodeId = Integer.parseInt(params[1]);
//
//                nIndex = SHOW_ID;
//                m_jsonshowLinks = JSONRPCAPI.getShowLinks(nIndex, seasonId, episodeId);
//                if (m_jsonshowLinks == null) return null;
//                JSONRPCAPI.longInfo("m_jsonShowLinks::::" + m_jsonshowLinks);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
//
//        }
//
//        @Override
//        protected void onPostExecute(Object params) {
//            try {
//
//                mProgressHUD.dismiss();
//                try {
//                    displayApps(holder, true, m_jsonshowLinks.toString());
//                    holder.layoutShowsAppList.setVisibility(View.VISIBLE);
//                    final View view = holder.layoutShowsAppList;
//                    view.clearFocus();
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            view.requestFocus();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}