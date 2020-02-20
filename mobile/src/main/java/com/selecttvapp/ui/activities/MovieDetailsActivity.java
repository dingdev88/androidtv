package com.selecttvapp.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Actor;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.model.Movie;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.activities.PresenterMovieDetails;
import com.selecttvapp.presentation.fragments.PresenterAppsLinksList;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewMovieListener;
import com.selecttvapp.ui.adapters.ActorsAdapter;
import com.selecttvapp.ui.adapters.TrailerListAdapter;
import com.selecttvapp.ui.fragments.AppLinksFragment;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailsActivity extends FragmentActivity implements View.OnClickListener, ViewMovieListener {
    private static final float thresholdOffset = 0.5f;
    private static final int thresholdOffsetPixels = 1;
    private final String TAG = this.getClass().getSimpleName();
    private final String PARAM_MOVIE = "movie";
    private final String PARAM_POSITION = "position";
    private final String PARAM_MOVIE_OBJECT = "movie_object";
    private String Title_1 = "Info";
    private String Title_2 = "Trailer";
    private String Title_3 = "More";
    private Context context;
    private Activity activity;
    private ViewPager viewPager;
    private SmartTabLayout slidingTab;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Movie movie;
    private PresenterMovieDetails presenter;
    private PresenterAppsLinksList presenterAppsLinks;
    private FlexboxLayout mGenreFlexboxLayout;
    private DynamicImageView movieImage;
    private TextView txtMovieTitle,
            txtRate,
            txtRuntime,
            txtGenre;
    private TextView tv_Rate,
            tv_Runtime,
            tv_Genre;
    private ImageView playIcon,
            favoriteIcon;
    private ImageButton backButton;
    private FrameLayout layoutAppsList;
    private LinearLayout layoutPagerSections;
    private TextView labelNoData;
    private boolean isFavoriteMovie = false;
    private boolean trailersExist = false;
    private boolean rightMove = false;
    private boolean leftMove = false;
    private int movieId = 0;
    private boolean[] isActiveTab = {true, true, true};

    public static Intent getIntent(Activity activity, int movieId) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra("showid", movieId);
        return intent;
    }

    public ImageView getFavoriteIcon() {
        return favoriteIcon;
    }

    private int getLayoutResId() {
        return R.layout.activity_movie_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new PresenterMovieDetails(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        activity = this;
        context = this;

        layoutAppsList = (FrameLayout) findViewById(R.id.layoutAppsList);
        movieImage = (DynamicImageView) findViewById(R.id.movieImage);
        txtMovieTitle = (TextView) findViewById(R.id.movieTitle);
        txtRate = (TextView) findViewById(R.id.rate);
        txtRuntime = (TextView) findViewById(R.id.runtime);
        txtGenre = (TextView) findViewById(R.id.genre);
        tv_Rate = (TextView) findViewById(R.id.tv_rate);
        tv_Runtime = (TextView) findViewById(R.id.tv_runtime);
        tv_Genre = (TextView) findViewById(R.id.tv_genre);
        playIcon = (ImageView) findViewById(R.id.playIcon);
        favoriteIcon = (ImageView) findViewById(R.id.favoriteIcon);
        backButton = (ImageButton) findViewById(R.id.backButton);

        labelNoData = (TextView) findViewById(R.id.labelNoData);
        layoutPagerSections = (LinearLayout) findViewById(R.id.layoutPagerSections);
        mGenreFlexboxLayout = (FlexboxLayout) findViewById(R.id.genre_flexbox_layout);

        if (presenter != null) {
            presenter.setFont(FontHelper.MYRIADPRO_REGULAR, txtMovieTitle, tv_Rate, tv_Runtime, tv_Genre);
            presenter.setFont(FontHelper.MYRIADPRO_BOLD, txtRate, txtRuntime, txtGenre);
        }

        backButton.setOnClickListener(this);
        favoriteIcon.setOnClickListener(this);
        playIcon.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setMinimumHeight(250);
        slidingTab = (SmartTabLayout) findViewById(R.id.slidingTab);
    }//end  of oncreate

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getIntent().hasExtra("showid"))
            movieId = getIntent().getExtras().getInt("showid");

        if (savedInstanceState != null) {
            try {
                movie = (Movie) savedInstanceState.getSerializable(PARAM_MOVIE_OBJECT);
                if (movie != null) {
                    onLoadedMovieDetails(movie);
                    if (movie.getAppsLinks() != null)
                        makeAppsLists(new JSONObject(movie.getAppsLinks()));
                } else presenter.loadMovieDetails(movieId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else presenter.loadMovieDetails(movieId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            if (movie != null) outState.putSerializable(PARAM_MOVIE_OBJECT, movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.MOVIE_DETAILS_SCREEN);
    }

    @Override
    public void onBackPressed() {
        try {
            if (layoutAppsList.getVisibility() == View.VISIBLE) {
                layoutAppsList.setVisibility(View.GONE);
                slidingTab.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                layoutPagerSections.setVisibility(View.VISIBLE);
            } else {
                super.onBackPressed();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) { // back button
            onBackPressed();
            return;
        } else if (v == favoriteIcon) { //favorite button
            if (movie != null)
                if (!isFavoriteMovie) {
                    setFavoriteItem(true);
                    presenter.addFavoriteItem(movie.getId());
                } else {
                    setFavoriteItem(false);
                    presenter.removeFavoriteItem(movie.getId());
                }
            return;
        } else if (v == playIcon) {  // play icon button
            layoutPagerSections.setVisibility(View.GONE);
            if (layoutAppsList.getVisibility() == View.VISIBLE) {
                layoutAppsList.setVisibility(View.GONE);
                slidingTab.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                layoutPagerSections.setVisibility(View.VISIBLE);
            } else {
                layoutAppsList.setVisibility(View.VISIBLE);
            }
            return;
        }
    }//end od Onclick

    @Override
    public void onLoadedMovieDetails(Movie movie) {
        if (movie == null)
            return;

        this.movie = movie;
        Image.loadMovieImage(this, movie.getPosterUrl(), movieImage, false);

        txtMovieTitle.setText(movie.getName());
        if (!movie.getRating().trim().isEmpty() && movie.getRating() != null && !movie.getRating().trim().equalsIgnoreCase("null"))
            txtRate.setText(movie.getRating());
        else
            txtRate.setText("Not Rated");
        txtRuntime.setText(movie.getRuntime());        //txtGenre.setText(movie.get);
        txtGenre.setText(movie.getGenre());
        if (!movie.getGenre().isEmpty())
            presenter.fillAutoSpacingLayout(movie.getGenre(), mGenreFlexboxLayout);

        presenter.checkIsFavoriteMovie(Integer.parseInt(movie.getId()));

        setSlidingTab();
    }

    @Override
    public void makeAppsLists(JSONObject response) {
        getSupportFragmentManager().beginTransaction().replace(layoutAppsList.getId(), AppLinksFragment.newInstance(response, "movie")).commitAllowingStateLoss();
    }

    @Override
    public void setFavoriteItem(boolean isAddedToFavorite) {
        if (isAddedToFavorite) {
            isFavoriteMovie = true;
            getFavoriteIcon().setImageResource(R.drawable.heart_filled);
        } else {
            isFavoriteMovie = false;
            getFavoriteIcon().setImageResource(R.drawable.heart);
        }
    }

    @Override
    public void addFavorite() {
        if (movie != null) {
            FavoriteBean favoriteBean = new FavoriteBean("", movie.getPosterUrl(), movie.getName(), Integer.parseInt(movie.getId()), movie.getRating(), movie.getRuntime(), movie.getDescription(), "");
            PresenterMyInterest.getInstance().addFavoriteItem(favoriteBean, "movie");
        }
    }

    private void setSlidingTab() {
        List<String> titles = new ArrayList<>();
        titles.add(Title_1);
        titles.add(Title_2);
        titles.add(Title_3);

        mSectionsPagerAdapter = null;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), movie, titles);
        viewPager.setAdapter(mSectionsPagerAdapter);
        slidingTab.setViewPager(viewPager);

        final LinearLayout lyTabs = (LinearLayout) slidingTab.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                    ///left move
                    rightMove = false;
                    leftMove = true;
                } else {
                    ///right move
                    leftMove = false;
                    rightMove = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    if (!trailersExist) {
                        if (rightMove)
                            viewPager.setCurrentItem(2);
                        else if (leftMove)
                            viewPager.setCurrentItem(0);
                    }

                changeTabsTitleTypeFace(lyTabs, position);
                viewPager.clearFocus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (movie.getTrailers().size() <= 0) {
            trailersExist = false;
            isActiveTab[1] = false;
            lyTabs.getChildAt(1).setClickable(false);
            lyTabs.getChildAt(1).setSelected(false);
        } else trailersExist = true;
    }


    private void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            if (presenter != null) {
                presenter.setFont(FontHelper.MYRIADPRO_REGULAR, tvTabTitle);
                if (j == position) presenter.setFont(FontHelper.MYRIADPRO_SEMIBOLD, tvTabTitle);
            }
        }
    }

    /* InfoFragment class*/
    @SuppressLint("ValidFragment")
    public static class InfoFragment extends Fragment {
        private final String PARAM_MOVIE = "movie";
        private Activity activity;
        private Context context;
        private Movie movie;
        private FontHelper fontHelper = new FontHelper();
        private RecyclerView recyclerView;
        private ActorsAdapter actorsAdapter;
        private ArrayList<Actor> actorList = new ArrayList<>();

        private TextView txtDescription;

        public InfoFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            context = getActivity();
            activity = getActivity();

            if (getArguments() != null) {
                movie = (Movie) getArguments().getSerializable(PARAM_MOVIE);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_details_page, container, false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.listActors);
            txtDescription = (TextView) rootView.findViewById(R.id.movieDescription);
            fontHelper.applyFonts(FontHelper.MYRIADPRO_REGULAR, txtDescription);
            if (movie != null) {
                txtDescription.setText(movie.getDescription());

                actorList = movie.getActors();
                for (int i = actorList.size() - 1; i >= 0; i--) {
                    Actor actor = actorList.get(i);
//                    if (actor.getImage() == null || actor.getImage().equalsIgnoreCase("null") || actor.getImage().isEmpty()) {
//                        actorList.remove(i);
//                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                actorsAdapter = new ActorsAdapter(getActivity(), actorList);
                recyclerView.setAdapter(actorsAdapter);
                recyclerView.setVisibility(View.GONE);
            }

            return rootView;
        }
    }//end of InfoFragment

    /* TrailorFragment class*/
    @SuppressLint("ValidFragment")
    public static class TrailorFragment extends Fragment {
        private final String PARAM_MOVIE = "movie";
        private final String PARAM_POSITION = "position";
        private String TAG = this.getClass().getSimpleName();
        private Movie movie;
        private int pagePosition;
        private TrailerListAdapter trailerListAdapter;

        private LinearLayout layoutMovieInfo;
        private RecyclerView recyclerView;
        private TextView labelNoData;
        private List<String> trailers = new ArrayList<>();

        public TrailorFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            if (getArguments() != null) {
                movie = (Movie) getArguments().getSerializable(PARAM_MOVIE);
                pagePosition = getArguments().getInt(PARAM_POSITION);
                Log.e(TAG, "position " + pagePosition);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_details_page, container, false);

            layoutMovieInfo = (LinearLayout) rootView.findViewById(R.id.layoutMovieInfo);
            layoutMovieInfo.setVisibility(View.GONE);
            labelNoData = (TextView) rootView.findViewById(R.id.labelNoData);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.listVideos);
            recyclerView.setVisibility(View.VISIBLE);

            if (movie != null) {
                ArrayList<String> trailers = movie.getTrailers();
                if (trailers.size() > 0) {
                    trailerListAdapter = new TrailerListAdapter(getActivity(), trailers, false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(trailerListAdapter);
                } else {
                    labelNoData.setText("No trailers");
                    labelNoData.setVisibility(View.VISIBLE);
                }
            }

            return rootView;
        }

    }//end of TrailorFragment

    /* MoreFragment class*/
    @SuppressLint("ValidFragment")
    public static class MoreFragment extends Fragment {
        private final String TAG = this.getClass().getSimpleName();
        private final String PARAM_MOVIE = "movie";
        private View rootView;
        private Activity activity;
        private RecyclerView recyclerView;
        private Movie movie;
        private TrailerListAdapter trailerListAdapter;
        private LinearLayout layoutMovieInfo;
        private TextView labelNoData;

        public MoreFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            activity = getActivity();
            if (getArguments() != null) {
                movie = (Movie) getArguments().getSerializable(PARAM_MOVIE);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_movie_details_page, container, false);
            layoutMovieInfo = (LinearLayout) rootView.findViewById(R.id.layoutMovieInfo);
            layoutMovieInfo.setVisibility(View.GONE);
            labelNoData = (TextView) rootView.findViewById(R.id.labelNoData);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.listVideos);
            recyclerView.setVisibility(View.VISIBLE);

            try {
                if (movie.getRelatedVideosId().size() > 0) {
                    onLoadedRelatedVideos(movie.getRelatedVideosId());
                } else {
                    String keyword = movie.getName() + "+movie+official+trailer";
                    loadRelatedVideos(keyword);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        private void loadRelatedVideos(final String KEY_WORD) {
            String ACC = "training.ocs2015@gmail.com";
            String PSWD = "trainingocs";
            final String API_KEY = "AIzaSyBJk1b1e9u9S3nuypkX3je833cW6AG9zNM";

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String searchRelatedVideo = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + KEY_WORD + "&type=video&key=" + API_KEY;
//                    urlGetRelatedVideo = "https://www.googleapis.com/youtube/v3/search?part=snippet&relatedToVideoId=" + VIDEO_ID + "&type=video&key=" + API_KEY;
//                    jsonDetail = JSONRPCAPI.getYoutubeRelatedVideos(urlGetRelatedVideo);
                        searchRelatedVideo = searchRelatedVideo.replace(" ", "+");
                        JSONObject response = JSONRPCAPI.getYoutubeRelatedVideos(searchRelatedVideo);
                        if (response == null) return;

                        final ArrayList<String> videoIds = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            JSONObject object = json.getJSONObject("id");
                            String videoId = object.getString("videoId");
                            videoIds.add(videoId);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoadedRelatedVideos(videoIds);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        private void onLoadedRelatedVideos(ArrayList<String> videoIds) {
            try {
                movie.setRelatedVideosId(videoIds);
                if (videoIds.size() > 0) {
                    labelNoData.setVisibility(View.GONE);
                    trailerListAdapter = new TrailerListAdapter(getActivity(), videoIds, true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(trailerListAdapter);
                } else {
                    labelNoData.setText("No videos");
                    labelNoData.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//end of MoreFragment

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        List<String> titles;
        Movie movie;

        public SectionsPagerAdapter(FragmentManager fm, Movie movie, List<String> titles) {
            super(fm);
            this.movie = movie;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (position == 0) {
                InfoFragment infoFragment = new InfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PARAM_MOVIE, movie);
                bundle.putInt(PARAM_POSITION, position);
                infoFragment.setArguments(bundle);
                return infoFragment;

            } else if (position == 1) {
                TrailorFragment trailorFragment = new TrailorFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PARAM_MOVIE, movie);
                bundle.putInt(PARAM_POSITION, position);
                trailorFragment.setArguments(bundle);
                return trailorFragment;
            } else if (position == 2) {
                MoreFragment moreFragment = new MoreFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PARAM_MOVIE, movie);
                bundle.putInt(PARAM_POSITION, position);
                moreFragment.setArguments(bundle);
                return moreFragment;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Utilities.asUpperCaseFirstChar(titles.get(position));
        }

    }//end of SectionsPagerAdapter
}
