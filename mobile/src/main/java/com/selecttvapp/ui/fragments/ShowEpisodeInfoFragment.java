package com.selecttvapp.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.OrientationListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Actor;
import com.selecttvapp.model.Episode;
import com.selecttvapp.model.Network;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.adapters.ActorsAdapter;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Lenova on 5/3/2017.
 */

public class ShowEpisodeInfoFragment extends Fragment implements OrientationListener {
    private static final String PARAM_EPISODE = "episode";
    public static final String PARAM_SHOW_ID = "showid";
    public static final String PARAM_PAY_MODE = "paymode";

    private FontHelper fontHelper = new FontHelper();
    private Episode episode;
    private Network network;
    private Activity activity;
    private Context context;
    private ArrayList<Actor> actorList = new ArrayList<>();
    private ActorsAdapter actorsAdapter;
    private Handler handler=new Handler();

    private RecyclerView listViewActors;
    private TextView episodeTitle,
            episodeRating,
            episodeNetwork,
            episodeDescription;
    private TextView textEpisodeNetwork,
            textEpisodeRating;
    private DynamicImageView imgBanner;
    private LinearLayout rootView;
    private LinearLayout layoutHeader;
    private NestedScrollView nestedScrollView;
    private FlexboxLayout mFlexboxLayout;


    private int showid = 0;
    private int payMode = Constants.ALL;

    public ShowEpisodeInfoFragment() {

    }

    @SuppressLint("ValidFragment")
    public static ShowEpisodeInfoFragment instance(int showid, int payMode) {
        ShowEpisodeInfoFragment fragment = new ShowEpisodeInfoFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_SHOW_ID, showid);
        args.putInt(PARAM_PAY_MODE, payMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        activity = getActivity();
        context = getActivity();

        if (getArguments() != null) {
            showid = getArguments().getInt(PARAM_SHOW_ID);
            payMode = getArguments().getInt(PARAM_PAY_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_page, container, false);
        imgBanner = (DynamicImageView) view.findViewById(R.id.showImage);
        episodeTitle = (TextView) view.findViewById(R.id.episodeTitle);
        episodeRating = (TextView) view.findViewById(R.id.episodeRating);
        episodeNetwork = (TextView) view.findViewById(R.id.episodeNetwork);
        episodeDescription = (TextView) view.findViewById(R.id.showDescription);
        textEpisodeNetwork = (TextView) view.findViewById(R.id.textEpisodeNetwork);
        textEpisodeRating = (TextView) view.findViewById(R.id.textEpisodeRating);

        mFlexboxLayout = (FlexboxLayout) view.findViewById(R.id.flexbox_layout);
        listViewActors = (RecyclerView) view.findViewById(R.id.listActors);
        rootView = (LinearLayout) view.findViewById(R.id.rootView);
        layoutHeader = (LinearLayout) view.findViewById(R.id.layoutHeader);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollview);

        setFont(FontHelper.MYRIADPRO_SEMIBOLD, episodeTitle, episodeNetwork, episodeRating, episodeDescription);
        setFont(FontHelper.MYRIADPRO_REGULAR, textEpisodeRating, textEpisodeNetwork);

        if (savedInstanceState != null) {
            try {
                episode = (Episode) savedInstanceState.getSerializable(PARAM_EPISODE);
                if (episode != null)
                    showDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else loadShow();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.TV_SHOWS_SHOW_INFORMATION_SCREEN);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (episode != null) {
            outState.putSerializable(PARAM_EPISODE, episode);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            onPortraitView();
        else onLandscapeView();
    }

    @Override
    public void onPortraitView() {
        rootView.setOrientation(LinearLayout.VERTICAL);
        ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).weight = 0;
        ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).height = ViewGroup.LayoutParams.WRAP_CONTENT;
        episodeDescription.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onLandscapeView() {
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).weight = 1;
        ((LinearLayout.LayoutParams) layoutHeader.getLayoutParams()).height = ViewGroup.LayoutParams.MATCH_PARENT;
        episodeDescription.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }

    private void showDetails() {
        try {
            nestedScrollView.setVisibility(View.VISIBLE);
            episodeTitle.setVisibility(View.VISIBLE);

            network = episode.getNetwork();

            if (episode.getName() != null)
                episodeTitle.setText(episode.getName());
            if (episode.getRating() != null)
                episodeRating.setText(episode.getRating());
            if (episode.getDescription() != null)
                episodeDescription.setText(episode.getDescription());
            if (network != null)
                if (network.getName() != null)
                    episodeNetwork.setText(network.getName());

            String posterUrl = episode.getPosterUrl();
            if (!posterUrl.equalsIgnoreCase(""))
                Image.loadGridImage(getContext().getApplicationContext(), posterUrl, imgBanner);


            actorList = episode.getActors();
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            listViewActors.setLayoutManager(layoutManager);
            actorsAdapter = new ActorsAdapter(getActivity(), actorList);
            listViewActors.setAdapter(actorsAdapter);

            if (episode.getGenre() != null && !episode.getGenre().isEmpty()) {
                fillAutoSpacingLayout(episode.getGenre());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillAutoSpacingLayout(String string) {
        String[] genres = string.split(",");

        for (String text : genres) {
            TextView textView = buildLabel(text.trim());
            mFlexboxLayout.addView(textView);
        }
    }

    private TextView buildLabel(String text) {
        TextView textView = new TextView(activity);
        textView.setBackgroundColor(getResources().getColor(R.color.light_blue));
        textView.setText(text);
        textView.setTextColor(0xffffffff);
        textView.setPadding(5, 0, 5, 0);
        textView.setGravity(Gravity.START);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        setFont(FontHelper.MYRIADPRO_SEMIBOLD, textView);
        textView.setLayoutParams(createDefaultLayoutParams());
        return textView;
    }

    private FlexboxLayout.LayoutParams createDefaultLayoutParams() {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        return lp;
    }

    private void loadShow() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            if (mProgressHUD != null)
                mProgressHUD.dismiss();
            try {
                JSONObject response = JSONRPCAPI.getShowDetail(showid);
                if (response == null) return;
                episode = new Episode(response);
                handler.post(() -> showDetails());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProgressHUD != null)
                    mProgressHUD.dismiss();
            }
        });
        thread.start();
    }
}
