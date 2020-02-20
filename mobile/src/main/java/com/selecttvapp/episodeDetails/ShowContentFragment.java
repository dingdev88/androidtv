package com.selecttvapp.episodeDetails;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.SortedListHashMap.SortedListHashMap;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Episode;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.presentation.activities.PresenterShowDetails;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewShowListener;
import com.selecttvapp.ui.bean.SeasonsBean;
import com.selecttvapp.ui.views.DynamicImageView;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public class ShowContentFragment extends Fragment implements ViewShowListener {
    public static final String PARAM_SHOW_ID = "showid";
    public static final String PARAM_SEASON_ID = "seasonid";
    public static final String PARAM_SEASON_NUMBER = "seasonNumber";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_PAY_MODE = "paymode";
    public static final String PARAM_POSITION = "position";
    protected static final String PARAM_EPISODE = "episode";
    protected static final String PARAM_SEASONS_LIST = "seasons_list";
    public ShowIntractionListener intractionListener;
    private PresenterShowDetails presenter;
    private Activity activity;
    private Episode episode;
    private FontHelper fontHelper;
    private ViewPager seasonViewPager;
    private SmartTabLayout smartTabLayout;
    private DynamicImageView bannerImage;
    private LinearLayout rootView;
    private LinearLayout layoutHeader;
    private LinearLayout layoutPayMode;
    private LinearLayout layoutSeasons;
    private ImageView switchImage;
    private TextView showDescription;
    private TextView labelNoData;
    private TextView labelAll;
    private TextView labelFree;

    private boolean isswitchselected = false;
    private boolean isFavoriteShow = false;
    private int showId = 0;
    private int payMode = Constants.ALL;

    public static ShowContentFragment instance(int showId, int payMode) {
        ShowContentFragment fragment = new ShowContentFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_SHOW_ID, showId);
        args.putInt(PARAM_PAY_MODE, payMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterShowDetails(this);
        activity = getActivity();
        fontHelper = new FontHelper();
        if (getArguments() != null) {
            showId = getArguments().getInt(PARAM_SHOW_ID);
            payMode = getArguments().getInt(PARAM_PAY_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ondemand_episodes_details_page_tv_shows, container, false);
        bannerImage = (DynamicImageView) view.findViewById(R.id.showImage);
        smartTabLayout = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        seasonViewPager = (ViewPager) view.findViewById(R.id.seasonViewPager);
        layoutSeasons = (LinearLayout) view.findViewById(R.id.layoutSeasons);
        rootView = (LinearLayout) view.findViewById(R.id.showPage);
        layoutHeader = (LinearLayout) view.findViewById(R.id.layoutHeader);
        layoutPayMode = (LinearLayout) view.findViewById(R.id.layoutPayMode);
        switchImage = (ImageView) view.findViewById(R.id.switchImage1);
        showDescription = (TextView) view.findViewById(R.id.showDescription);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        labelAll = (TextView) view.findViewById(R.id.labelAll);
        labelFree = (TextView) view.findViewById(R.id.labelFree);


        //d-pad on switch image
        switchImage.setFocusable(true);
        switchImage.setFocusableInTouchMode(true);
        switchImage.setBackground(getResources().getDrawable(R.drawable.btn_selector_white));
        switchImage.setPadding(5,5,5,5);
        switchImage.setNextFocusUpId(R.id.backButton);
        switchImage.setNextFocusRightId(R.id.episodeImage1);

        switchImage.setOnClickListener(v -> {
            if (getChildFragmentManager() != null)
                if (getChildFragmentManager().getFragments()!=null && getChildFragmentManager().getFragments().size() > 0)
                    for (Fragment fragment : getChildFragmentManager().getFragments())
                        if (fragment != null && fragment instanceof EpisodesListFragment)
                            getChildFragmentManager().beginTransaction().remove(fragment).commitNow();

            RabbitTvApplication.getHorizontalBeanHashMap().clear();
            RabbitTvApplication.getSliderBeanHashMap().clear();
            if (isswitchselected) offModeSwitch();
            else onModeSwitch();
            presenter.loadShowDetails(showId, payMode);
        });
        ((ShowDetailsActivity) getActivity()).getFavoriteIcon().setOnClickListener(v -> {
            if (episode != null)
                if (!isFavoriteShow) {
                    setFavoriteItem(true);
                    presenter.addFavorite(episode.getId());
                } else {
                    setFavoriteItem(false);
                    presenter.removeFavoriteItem(episode.getId());
                }
        });

        presenter.setFont(FontHelper.MYRIADPRO_SEMIBOLD, showDescription);
        presenter.setFont(FontHelper.MYRIADPRO_REGULAR, labelAll, labelFree);
        onConfigurationChanged(getActivity().getResources().getConfiguration());

        if (payMode == Constants.PAID) layoutPayMode.setVisibility(View.GONE);
        else if (payMode == Constants.ALL) offModeSwitch();
        else if (payMode == Constants.FREE) onModeSwitch();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.TV_SHOWS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowDetailsActivity)
            intractionListener = (ShowDetailsActivity) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) presenter.extractShow(savedInstanceState);
        else presenter.loadShowDetails(showId, payMode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView.setOrientation(LinearLayout.VERTICAL);
            showDescription.setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).weight = 0;
        } else {
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            showDescription.setVisibility(View.VISIBLE);
            setDescriptionMaxLines();
            ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).weight = 1;
        }
    }

    @Override
    public void loadShow(Episode episode) {
        try {
            if (episode == null)
                return;
            else this.episode = episode;

            Image.loadShowImage(getActivity(), episode.getPosterUrl(), bannerImage);
            presenter.checkIsFavoriteTVShow(Integer.parseInt(episode.getId()));

            if (!episode.getDescription().isEmpty()) {
                showDescription.setText(episode.getDescription());
                setDescriptionMaxLines();
            }


            if (episode.getSeasonsList().size() > 0) {
                layoutSeasons.setVisibility(View.VISIBLE);
                labelNoData.setVisibility(View.GONE);
                seasonViewPager.setOffscreenPageLimit(episode.getSeasonsList().size());
                PresenterShowDetails.SeasonsSectionsPagerAdapter sectionsPagerAdapter = presenter.getSeasonsSectionsPagerAdapter(getChildFragmentManager(), showId, payMode, episode.getSeasonsList());
                seasonViewPager.setAdapter(sectionsPagerAdapter);
                smartTabLayout.setViewPager(seasonViewPager);
                presenter.setPageChangeListener(smartTabLayout);
            } else {
                layoutSeasons.setVisibility(View.GONE);
                labelNoData.setVisibility(View.VISIBLE);
                if (payMode == Constants.FREE)
                    labelNoData.setText("(No Free Mobile Episodes Currently Available)");
                else
                    labelNoData.setText("(No Mobile Episodes Currently Available)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFavoriteItem(boolean favoriteItem) {
        if (favoriteItem) {
            isFavoriteShow = true;
            if (getActivity() != null)
                ((ShowDetailsActivity) getActivity()).getFavoriteIcon().setImageResource(R.drawable.heart_filled);
        } else {
            isFavoriteShow = false;
            if (getActivity() != null)
                ((ShowDetailsActivity) getActivity()).getFavoriteIcon().setImageResource(R.drawable.heart);
        }
    }

    @Override
    public void addFavorite() {
        if (episode != null) {
            FavoriteBean favoriteBean = new FavoriteBean("", episode.getPosterUrl(), episode.getName(), Integer.parseInt(episode.getId()), episode.getRating(), episode.getRuntime(), episode.getDescription(), "");
            PresenterMyInterest.getInstance().addFavoriteItem(favoriteBean, "show");
        }
    }

    @Override
    public void onLoadedEpisodes(SortedListHashMap<Integer,SeasonsBean> episodes) {

    }

    @Override
    public void onSeasonLoaded(int showId, SeasonsBean seasonsBean) {

    }

    @Override
    public void onError() {

    }

    private void offModeSwitch() {
        isswitchselected = false;
        RabbitTvApplication.setPaymode(Constants.ALL);
        payMode = Constants.ALL;
        switchImage.setImageResource(R.drawable.off);
        labelAll.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        labelFree.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
    }

    private void onModeSwitch() {
        isswitchselected = true;
        RabbitTvApplication.setPaymode(Constants.FREE);
        payMode = Constants.FREE;
        switchImage.setImageResource(R.drawable.on);
        labelAll.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
        labelFree.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        presenter.setFont(FontHelper.MYRIADPRO_SEMIBOLD, labelFree);
        presenter.setFont(FontHelper.MYRIADPRO_REGULAR, labelAll);
    }

    private void setDescriptionMaxLines() {
        if (showDescription == null)
            return;

        showDescription.post(new Runnable() {
            @Override
            public void run() {
                if (!showDescription.getText().toString().isEmpty()) {
                    int lastVisibleLineNumber = (showDescription.getMeasuredHeight() - showDescription.getPaddingTop() - showDescription.getPaddingBottom()) / showDescription.getLineHeight();
                    if (lastVisibleLineNumber > showDescription.getMinLines()) {
                        showDescription.setMinLines(lastVisibleLineNumber);
                        showDescription.setMaxLines(lastVisibleLineNumber);
                        showDescription.setEllipsize(TextUtils.TruncateAt.END);
                    }
                }
            }
        });
    }


    public interface ShowIntractionListener {
        void setShow(Episode show);
    }
}//end of PlaceHolderFragment
