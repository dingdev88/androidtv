package com.selecttvapp.channels;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.AudioManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.FavoriteItemListener;

import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.DialogBinder;
import com.selecttvapp.ui.fragments.LayoutBinder;
import com.selecttvapp.ui.helper.MyApplication;
import com.selecttvapp.ui.samplevideoplayer.VideoFragment;








import java.util.ArrayList;

import java.util.LinkedList;

import java.util.Queue;




public class ChannelsRestFragment extends Fragment implements SubMenuListener, ChannelListListener, ScrollingFragment.OnScrollingFragmentInteractionListener, TimelineListListener, ShowListListener, ChannelDialogGridListener, YoutubeDataListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    public static ArrayList<ChannelCategoryList> channelMainCategoryList = new ArrayList<>();
    public static ArrayList<ChannelSubCategoryList> channelAllCategoryList = new ArrayList<>();
    public static ChannelsRestFragment instance = null;
    private static int numberOfMoves = 0;
    ArrayList<ProgramList> programList;
    int nWidth, nHeight, tabwidth, cellwidth;
    ChannelListAdapter mChannelListAdapter;
    ChannelTimelineAdapter mChannelTimelineAdapter;
    PlayerYouTubeFrag playerfragment;
    ChannelScheduler previousScheduler;
    ProgressHUD mmProgressHUD;
    AdView rest_adView;
    LinearLayout rest_adViewLayout;
    int setChannelsCount = 0;
    LayoutBinder mBinding;
    private ExoPlayerFragment fragment;
    private ExoPlayerFragmentFullScreen fragment_fullscreen;
    DialogBinder dialogBinding;
    boolean queueLoading = false;
    ChannelDatabaseMethods mChannelDatabaseMethods;
    String screenMode = "";
    String VideoMode = "normal";
    View view;
    ChannelCategoryAdapter channelCategoryAdapter;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    LinearLayoutManager hLayoutManager;
    private Handler handler = new Handler();
    private String mParamSlug;
    private String mParamMainCategory;
    private boolean ismSearch;
    private String mChannelSlug;
    private OnChannelFragmentInteractionListener mListener;
    private ArrayList<ChannelScheduler> channelSchedulerList = new ArrayList<>();
    private ArrayList<ChannelScheduler> allChannelSchedulerList = new ArrayList<>();
    private boolean isTablet;
    private int sliderLayoutProportion = 2;
    private Dialog mBrowseAllChannel;
    private Casting mcasting;
    private Activity mActivity;
    private Context mContext;
    /*for swiping timeline*/
    private String video_url = "";
    private boolean swipingflag = false;
    private Queue<FragmentModel> mfragmentQueue = new LinkedList<>();
    private ChannelScheduler currentPlayingChannelScheduler;
    private boolean isFavoriteChannel = false;
    private boolean inSilentMode = false;
    private VideoFragment mVideoFragment;
    private boolean isExoPlayer = false;
    private String exoVideoList = null;
    private String SType = null;
    private int Offset = 0;
    private String CType = null;


    public ChannelsRestFragment() {
        // Required empty public constructor
    }

    public static ChannelsRestFragment newInstance(String param1, String param2, boolean isSearch, String channelSlug) {
        ChannelsRestFragment fragment = new ChannelsRestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, isSearch);
        args.putString(ARG_PARAM4, channelSlug);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChannelsRestFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getArguments() != null) {
            mParamSlug = getArguments().getString(ARG_PARAM1);
            mParamMainCategory = getArguments().getString(ARG_PARAM2);
            mChannelSlug = getArguments().getString(ARG_PARAM4);
            ismSearch = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.rest_fragment_channels, container, false);
        mActivity = getActivity();
        mChannelDatabaseMethods = new ChannelDatabaseMethods();
        ((HomeActivity) getActivity()).setmChannelsRestFragment(this);
        isTablet = ChannelUtils.checkIsTablet(getActivity());
        /*Check device is mobile or tab to set slider width.*/
        if (isTablet) {
            sliderLayoutProportion = 2;
        }
        mBinding = new LayoutBinder(view);
        mBinding.initializeview();
        initializeViews(view);
       /* mcasting = new Casting(getActivity());
        mcasting.initializeCasty(getActivity(), HomeActivity.media_route_button, new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                if (playerfragment != null) {
                    playerfragment.playPlayer(false);
                }
            }

            @Override
            public void onDisconnected() {
                if (playerfragment != null) {
                    playerfragment.playPlayer(true);
                }
            }
        });*/
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadmainCategories();
        initializebrowseAllDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isExoPlayer){
            fragment.stopExoPlayer();
        }
    }


    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
        PlayerYouTubeFrag.setPlayerYouTubeFrag(null);
        if (getActivity() != null) {
            if (getActivity() instanceof HomeActivity)
                ((HomeActivity) getActivity()).setmChannelsRestFragment(null);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            if (mcasting != null)
                mcasting.stopCasting();
        }
    }

    private void initializebrowseAllDialog() {
        mBrowseAllChannel = new Dialog(getActivity(), R.style.MY_DIALOG);
        mBrowseAllChannel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBrowseAllChannel.setContentView(R.layout.rest_browse_channel_dialog);
        dialogBinding = new DialogBinder(mBrowseAllChannel);
        dialogBinding.initializeview();
        mBrowseAllChannel.setCancelable(false);
        final LinearLayoutManager mLayoutManagerr = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dialogBinding.restHorizontalListview.setLayoutManager(mLayoutManagerr);
        dialogBinding.restHorizontalListview.hasFixedSize();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        dialogBinding.restHorizontalListview.setLayoutManager(gridLayoutManager);
        dialogBinding.restHorizontalListviewChannelClose.setOnClickListener(v -> {
            if (mBrowseAllChannel.isShowing()) {
                mBrowseAllChannel.dismiss();
            }
        });
        dialogBinding.restLeftSlide.setOnClickListener(v -> {
            int firstPosition = mLayoutManagerr.findFirstCompletelyVisibleItemPosition();
            if (firstPosition == 0) {
                dialogBinding.restLeftSlide.setImageResource(R.drawable.prev_inactive);
                dialogBinding.restLeftSlide.setImageResource(R.drawable.next_active);
            } else {
                dialogBinding.restLeftSlide.setImageResource(R.drawable.prev_active);
                mLayoutManagerr.scrollToPosition(mLayoutManagerr.findFirstVisibleItemPosition() - 1);
            }
        });
        dialogBinding.restRightSlide.setOnClickListener(v -> {
            int lastPosition = mLayoutManagerr.findLastCompletelyVisibleItemPosition();
            if (lastPosition == channelSchedulerList.size() - 1) {
                dialogBinding.restRightSlide.setImageResource(R.drawable.prev_active);
                dialogBinding.restRightSlide.setImageResource(R.drawable.next_inactive);
            } else {
                dialogBinding.restRightSlide.setImageResource(R.drawable.next_active);
                mLayoutManagerr.scrollToPosition(mLayoutManagerr.findLastVisibleItemPosition() + 1);
            }
        });
    }

    private void loadmainCategories() {
        /*Fetching channels categories list.*/
        channelMainCategoryList = mChannelDatabaseMethods.getChannelMaincategoriesList();
        if (channelMainCategoryList != null && channelMainCategoryList.size() > 0) {
            setMenu(channelMainCategoryList);
        } else {
            mmProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    MyApplication.getmWebService().loadChannelsCategories(new CategoryListener() {
                        @Override
                        public void onCategoriesLoaded(ArrayList<ChannelCategoryList> categorylist) {
                            if (categorylist != null && categorylist.size() > 0) {
                                MenuFilter mMainCategoryFilter = new MenuFilter(categorylist);
                                channelMainCategoryList = mMainCategoryFilter.getFilterList();
                                for (ChannelCategoryList menuItem : channelMainCategoryList) {
                                    MenuFilter mSubCategoryFilter = new MenuFilter(categorylist, menuItem.getSlug());
                                    menuItem.setSubCategories(mSubCategoryFilter.getFilterList());
                                }
                            }
                            setMenu(channelMainCategoryList);

                        }

                        @Override
                        public void onLoadingFailed() {
                            handler.post(() -> {
                                if (mmProgressHUD != null && mmProgressHUD.isShowing()) {
                                    mmProgressHUD.dismiss();
                                }
                            });
                        }
                    });
                }
            };
            thread.start();
        }


    }

    private void setMenu(final ArrayList<ChannelCategoryList> channelMainCategoryList) {
        if (getActivity() != null) {
            handler.post(() -> {
                if (mmProgressHUD != null && mmProgressHUD.isShowing())
                    mmProgressHUD.dismiss();
                SetHorzontalMenuAdapter(channelMainCategoryList, mParamMainCategory);
                /*mParamSlug-to check channel page is called from home page or click actions from left menu */
                if (!TextUtils.isEmpty(mParamSlug)) {
                    loadChannelList(mParamSlug, false, ismSearch);
                } else {
//                        loadChannelList(channelMainCategoryList.get(0).getSlug(), false, false);
                    loadChannelList("news", false, false);
                }
            });
        }
    }

    private void SetHorzontalMenuAdapter(ArrayList<ChannelCategoryList> channelMainCategoryList, String selectedSlug) {
        recyclerView = new RecyclerView(getActivity());

        recyclerView.setNestedScrollingEnabled(false);
        hLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(hLayoutManager);

        channelCategoryAdapter = new ChannelCategoryAdapter(channelMainCategoryList, getActivity(), -1, this, selectedSlug, false);

        recyclerView.setAdapter(channelCategoryAdapter);

        mBinding.endlessHScrollView.setChildViewGroup(recyclerView);
//        channelCategoryAdapter = new ChannelCategoryAdapter(channelMainCategoryList, getActivity(), -1, this, selectedSlug, false);
//        mBinding.categoriesListView.setAdapter(channelCategoryAdapter);
    }

    private void initializeViews(View rootView) {
//        rest_adView = (AdView) rootView.findViewById(R.id.rest_adView);
//        rest_adViewLayout = (LinearLayout) rootView.findViewById(R.id.rest_adViewLayout);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        rest_adView.loadAd(adRequest);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nHeight = displayMetrics.widthPixels;
            nWidth = displayMetrics.heightPixels;
            screenMode = "port";
        } else {
            nWidth = displayMetrics.widthPixels;
            nHeight = displayMetrics.heightPixels;
            screenMode = "port";
        }
        /*cellwidth = 1/3rd of screen-width of channels logo*/
        tabwidth = nWidth / 3;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(getActivity())) / 3;
        /*numberOfMoves-number of times to scroll a cell with particular offset*/
        numberOfMoves = cellwidth / 5;
        LinearLayout.LayoutParams rel_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rel_lp.height = 14 * (nWidth / 12);
        mBinding.playerFrameLayout.setLayoutParams(rel_lp);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.categoriesListView.setLayoutManager(mLayoutManager);
        mBinding.categoriesListView.hasFixedSize();

        mBinding.btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.showTabTextview.setVisibility(View.GONE);
                mBinding.showsViewLayout.setVisibility(View.GONE);
                Typeface google_font = Typeface.createFromAsset(getResources().getAssets(), "font/gmedium.ttf");
                mBinding.showTabTextview.setTypeface(google_font);
                mBinding.overallScroll.setVisibility(View.VISIBLE);
                if (previousScheduler != null) {
                    setVideoType(previousScheduler);
                }
            }
        });
        mBinding.imgSTAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPlayingChannelScheduler != null)
                    if (!isFavoriteChannel) {
                        setFavoriteItem(true);
                        addFavorite(currentPlayingChannelScheduler.getSlug());
                    } else {
                        setFavoriteItem(false);
                        removeFavorite(currentPlayingChannelScheduler.getSlug());
                    }
            }
        });
        mBinding.imgFULLSCREEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFullScreen();
            }
        });

        mBinding.imgVOLUME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putVolumeMute();
            }
        });


        //Listener for expand/collapse timelineview
        mBinding.swipingView.setListener(new HorizontalchannelSwipingView.onSwipechangedListener() {
            @Override
            public void onSwipeStarted() {
                mBinding.overallScroll.setEnableScrolling(false);
            }

            @Override
            public void onSwipeRealesed() {
                mBinding.overallScroll.setEnableScrolling(true);
                if (mBinding.swipingView.isOpen()) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.setMargins(nWidth / sliderLayoutProportion, 0, -nWidth / sliderLayoutProportion, 0);
                    mBinding.rightSliderLayout.setLayoutParams(lp);
                } else {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    int margin = ChannelUtils.convertDpToPixels(getActivity());
                    lp.setMargins(margin, 0, -margin, 0);
                    mBinding.rightSliderLayout.setLayoutParams(lp);
                }
                swipingflag = true;
            }
        });
        mBinding.rightSliderLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mBinding.swipingView.isMoving()) {
                    v.setTop(oldTop);
                    v.setBottom(oldBottom);
                    v.setLeft(oldLeft);
                    v.setRight(oldRight);
                }
            }
        });
        //channels list pagination
        mBinding.overallScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = mBinding.overallScroll.getScrollY();
            // Log.d("scroll::::", ":::::" + scrollY);
            View view = (View) mBinding.overallScroll.getChildAt(mBinding.overallScroll.getChildCount() - 1);
            // Calculate the scrolldiff
            int diff = (view.getBottom() - (mBinding.overallScroll.getHeight() + mBinding.overallScroll.getScrollY()));
            // if diff is zero, then the bottom has been reached

            if (diff == 0) {
                // notify that we have reached the bottom
                Log.d("::::::::", "MyScrollView: Bottom has been reached");
                updateChannelList();
            }
        });
        //Initialize views and adapters
        mBinding.browseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayBrowseAllDialog();
            }
        });
        final CustomLayoutManager linearLayoutManager = new CustomLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.fragmentChannelAlllist.setLayoutManager(linearLayoutManager);
        mBinding.fragmentChannelAlllist.setHasFixedSize(true);
        mBinding.fragmentChannelAlllist.setNestedScrollingEnabled(false);
        CustomLayoutManager linearLayoutManager1 = new CustomLayoutManager(getActivity());
        mBinding.fragmentChannelProgramList.setLayoutManager(linearLayoutManager1);
        mBinding.fragmentChannelProgramList.setHasFixedSize(true);
        mBinding.fragmentChannelProgramList.setNestedScrollingEnabled(false);
        mBinding.fragmentChannelAlllist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //scrolling chnnels list recyclerview and timelineview parallely
                mBinding.fragmentChannelProgramList.scrollTo(dx, dy);
            }
        });
        //Keep timeline slide open on startup
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(nWidth / sliderLayoutProportion, 0, -nWidth / sliderLayoutProportion, 0);
        mBinding.rightSliderLayout.setLayoutParams(lp);
        //Swiching to channels/live view
        mBinding.channelTabTextview.setOnClickListener(v -> {
            mBinding.showTabTextview.setVisibility(View.GONE);
            mBinding.showsViewLayout.setVisibility(View.GONE);
            Typeface google_font = Typeface.createFromAsset(getResources().getAssets(), "font/gmedium.ttf");
            mBinding.channelTabTextview.setTypeface(google_font);
            mBinding.overallScroll.setVisibility(View.VISIBLE);
            if (previousScheduler != null) {
                setVideoType(previousScheduler);
            }
        });
        mBinding.imgLeft.setOnClickListener(view -> {
            mBinding.endlessHScrollView.loopScrollPosition();
            mBinding.endlessHScrollView.smoothScrollTo(mBinding.endlessHScrollView.getScrollX() - (tabwidth * 2), 0);
        });
        mBinding.imgRight.setOnClickListener(view -> {
            mBinding.endlessHScrollView.loopScrollPosition();
            mBinding.endlessHScrollView.smoothScrollTo(mBinding.endlessHScrollView.getScrollX() + (tabwidth * 2), 0);
        });
    }

    private void updateChannelList() {
        if (mChannelListAdapter != null) {
            int end = 0;
            ArrayList<ChannelScheduler> filteredList = new ArrayList<>();
            if (setChannelsCount < allChannelSchedulerList.size()) {
                if (allChannelSchedulerList.size() >= setChannelsCount + 2) {
                    //end=setChannelsCount+2;
                    filteredList.addAll(allChannelSchedulerList.subList(setChannelsCount, setChannelsCount + 2));
                } else {
                    end = allChannelSchedulerList.size() - 1;
//                    filteredList.addAll(allChannelSchedulerList.subList(setChannelsCount, allChannelSchedulerList.size() - 1));
                    if (setChannelsCount < allChannelSchedulerList.size())
                        filteredList.add(allChannelSchedulerList.get(setChannelsCount));
                }
                setChannelsCount += filteredList.size();
                channelSchedulerList.addAll(filteredList);
                mChannelListAdapter.addChannelListData(filteredList);
                mChannelTimelineAdapter.addTimeLineData(filteredList);
                setRulerHieght(channelSchedulerList.size());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isExoPlayer){
            fragment.resumeExoPlayer();
        }
        Utilities.googleAnalytics(Constants.CHANNELS_SCREEN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    private void displayBrowseAllDialog() {
        //Loads an display browse all channels list
        ChannelCategoryAdapter channelCategoryAdapter = new ChannelCategoryAdapter(channelMainCategoryList, getActivity(), -1, this, "", true);
        dialogBinding.restHorizontalListview.setAdapter(channelCategoryAdapter);
        mBrowseAllChannel.show();
        loadChannelList(channelMainCategoryList.get(0).getSlug(), true, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //renewAd();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displayPortraitMode();
        } else {
            displayPortraitMode();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        intitializeListener(context);
    }

    private void renewAd() {
//        rest_adView = (AdView) view.findViewById(R.id.rest_adView);
//        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) rest_adView.getLayoutParams();
//        // change relative layout for your layout which contains the adView
//        LinearLayout parent = (LinearLayout) rest_adView.getParent();
        rest_adViewLayout.removeView(view.findViewById(R.id.rest_adView));
        AdView newAd = new AdView(getActivity());
        newAd.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        newAd.setAdUnitId(getActivity().getString(R.string.banner_ad_unit_id_new));
        newAd.setId(R.id.rest_adView);
        newAd.setLayoutParams(rest_adViewLayout.getLayoutParams());
        rest_adViewLayout.addView(newAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        newAd.loadAd(adRequest);
    }

    private void intitializeListener(Context context) {
        if (context instanceof OnChannelFragmentInteractionListener) {
            mListener = (OnChannelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChannelFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSubMenuSelected(String slug, boolean isDialog) {
        loadChannelList(slug, isDialog, false);
    }

    //isDialog-browse all dialog/Main channels view
    public void loadChannelList( final String slug, final boolean isDialog, final boolean isfromSearch) {
        mBinding.showTabTextview.setVisibility(View.GONE);
        mBinding.showsViewLayout.setVisibility(View.GONE);
        Typeface google_font = Typeface.createFromAsset(getResources().getAssets(), "font/gmedium.ttf");
        mBinding.channelTabTextview.setTypeface(google_font);
        mBinding.overallScroll.setVisibility(View.VISIBLE);

        //Loading channels list for selected category
        if (isDialog) {
            setChannelsCount = 0;
            dialogBinding.restHorizontalChannelListProgressBar.setVisibility(View.VISIBLE);
            dialogBinding.restHorizontalChannelList.setVisibility(View.GONE);
        }
        ArrayList<ChannelScheduler> channelList = mChannelDatabaseMethods.getChannelListBycategorySlug(slug);
        if (channelList != null && channelList.size() > 0) {
            for (int i = 0; i < channelList.size(); i++) {
                Log.d(":::::realm::", ":::" + channelList.get(i).getName());
            }
            channelListLoaded(channelList, isDialog, isfromSearch);
        } else {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    MyApplication.getmWebService().loadChannelsDtaa(slug, isDialog, new ChannelApiListener() {
                        @Override
                        public void onChannelListLoaded(String categorySlug, final ArrayList<ChannelScheduler> channelList, boolean isDialog) {
                            mChannelDatabaseMethods.saveChannelList(channelList, slug);
                            channelListLoaded(channelList, isDialog, isfromSearch);
                        }

                        @Override
                        public void onNetworkError() {
                            displayNoNetwork();
                        }
                    });
                }
            };
            thread.start();
        }

    }

    private void displayNoNetwork() {
        handler.post(() -> Toast.makeText(getActivity().getApplicationContext(), "Error in Internet Connection !", Toast.LENGTH_LONG).show());

    }

    private void channelListLoaded(final ArrayList<ChannelScheduler> channelList, boolean isDialog, boolean isfromSearch) {
        if (isDialog) {
            handler.post(() -> {
                if (dialogBinding.restHorizontalChannelListProgressBar.getVisibility() == View.VISIBLE) {
                    dialogBinding.restHorizontalChannelListProgressBar.setVisibility(View.GONE);
                    dialogBinding.restHorizontalChannelList.setVisibility(View.VISIBLE);
                }
                displayChannelsInDialog(channelList);
            });

        } else {
            if (isfromSearch && !TextUtils.isEmpty(mChannelSlug)) {
                int mpos = VideoFilter.getSelectedChannelPosition(channelList, mChannelSlug);
                if (mpos > 0 && mpos < channelList.size()) {
                    ChannelScheduler selectedScheduler = channelList.get(mpos);
                    channelList.remove(mpos);
                    channelList.add(0, selectedScheduler);
                }
                displayChannelsList(channelList);
            } else {
                displayChannelsList(channelList);
            }
        }
    }

    private void displayChannelsInDialog(final ArrayList<ChannelScheduler> channelList) {
        dialogBinding.restHorizontalListviewItemCount.setText(channelList.size() + " " + "Channels found");
        dialogBinding.restHorizontalChannelList.setVisibility(View.VISIBLE);
        dialogBinding.restHorizontalChannelListProgressBar.setVisibility(View.GONE);
        AllChannelDialogAdapter allChannelAdapter = new AllChannelDialogAdapter(channelList, getActivity(), this);
        dialogBinding.restHorizontalChannelList.setAdapter(allChannelAdapter);
    }

    private void displayChannelsList(ArrayList<ChannelScheduler> channelList) {
        channelSchedulerList.clear();
        allChannelSchedulerList.clear();
        allChannelSchedulerList.addAll(channelList);
        ArrayList<ChannelScheduler> filteredList = new ArrayList<ChannelScheduler>();
        /*filtering first 10 channles to increase loading speed, rest channels will be loaded on scrolling down*/
        if (channelList.size() > 10) {
            filteredList.addAll(channelList.subList(0, 9));
        } else {
            filteredList.addAll(channelList);
        }
        setChannelsCount = filteredList.size();
        channelSchedulerList.addAll(filteredList);
        listChannels(filteredList);
    }

    private void listChannels(final ArrayList<ChannelScheduler> channelList) {
        handler.post(() -> setChannelsList(channelList));
    }

    private void setChannelsList(ArrayList<ChannelScheduler> channelList) {
        ArrayList<ChannelScheduler> mchannelList1 = new ArrayList<>();
        mchannelList1.addAll(channelList);
        mChannelListAdapter = new ChannelListAdapter(mchannelList1, getActivity(), this);
        mBinding.fragmentChannelAlllist.setAdapter(mChannelListAdapter);
        ArrayList<ChannelScheduler> newlist = new ArrayList<>();
        newlist.addAll(channelList);
        mChannelTimelineAdapter = new ChannelTimelineAdapter(mchannelList1, getActivity(), this);
        mBinding.fragmentChannelProgramList.setAdapter(mChannelTimelineAdapter);
        /*Positioning and setting height of white line indicating current position in timeline*/
        setRulerHieght(channelList.size());
    }

    private void setRulerHieght(int size) {
        if (getActivity() == null)
            return;
        int rulerHeight = size * ChannelUtils.convertDpToPixels(getActivity(), 53);
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(1, rulerHeight);
        int cellwidth = (nWidth - ChannelUtils.convertDpToPixels(getActivity())) / 3;
        vp.setMargins(cellwidth, 0, 0, 0);
        //vp.setMargins(cellwidth+cellwidth/2, 0, 0, 0);
        mBinding.rulerLine.setLayoutParams(vp);
    }

    @Override
    public void onChannelListSelected(int adapterPosition, String slug) {
        if (adapterPosition <= mChannelTimelineAdapter.getItemCount())
            interchangeChannelsposition(adapterPosition, slug);
    }

    private void interchangeChannelsposition(int adapterPosition, String slug) {
        /*interchanging selected rows in timeline*/
        if (mChannelListAdapter != null) {
            mChannelListAdapter.swapChannelList(adapterPosition);
        }
        if (mChannelTimelineAdapter != null) {
            mChannelTimelineAdapter.swapChannelTimeline(adapterPosition);
        }
        /*loading videos with a delat to prevent block in UI thread*/
        final ChannelScheduler cs = VideoFilter.getSelectedChannel(channelSchedulerList, slug);
        if (cs != null) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                previousScheduler = cs;
                setVideoType(cs);
            }, 1000);
        }
        /*scroll list to top after interchanging*/
        mBinding.overallScroll.post(() -> mBinding.overallScroll.fullScroll(mBinding.overallScroll.FOCUS_UP));
    }

    @Override
    public void onFirstDataLoaded(int listPosition, ChannelScheduler mPrograms, String slug) {
        /*inorder to reload program list after returing from ondemand view current program list in assigned in seprate variable*/
        previousScheduler = mPrograms;
        setVideoType(mPrograms);
    }

    @Override
    public void onTimeLineSelected(String uid, ArrayList<ProgramList> mProgramList, ChannelScheduler channelScheduler) {

    }

    public void setVideoFragment(int listPosition, ChannelScheduler mPrograms, String slug) {
        /*Loaded program & stream list will be added in the parent Arrylist(program & stream list will be loaded in scrolling fragment)*/
        if (listPosition < channelSchedulerList.size())
            channelSchedulerList.set(listPosition, mPrograms);
        /*video of channel in first position will gets played*/
        if (listPosition == 0) {
            previousScheduler = mPrograms;
            setVideoType(mPrograms);
        }
    }

    @Override
    public void onTimelineListSelected(int adapterPosition, String channelSchedulerSlug) {
        /*channels will be iinterchanged on click on live channels desciption*/
        interchangeChannelsposition(adapterPosition, channelSchedulerSlug);
    }

    @Override
    public void onFragmentQueued(FragmentManager mFragmentManager, int layoutId, ChannelScheduler mChannelScheduler, int pos, ScrollingFragment mScrollingFragment) {
        mfragmentQueue.add(new FragmentModel(mFragmentManager, layoutId, mChannelScheduler, pos, mScrollingFragment));
        if (pos == 0) {
            queueLoading = true;
            startFragment();

        }/*else{
            if(!queueLoading){
                startFragment();
            }
        }*/

    }

    private void startFragment() {
        FragmentModel model = mfragmentQueue.element();

        model.getmScrollingFragment().setLoadListener(() -> {
             /*mfragmentQueue.remove();
            if(mfragmentQueue.size()>0){
                //startFragment();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startFragment();
                    }
                },1500);
            }else{
                queueLoading=false;
            }*/
            mBinding.channelVisibleLayout.setAlpha(1);

        });
        mBinding.channelVisibleLayout.setAlpha(1);
        //model.getmFragmentManager().beginTransaction().replace(model.layoutId, model.getmScrollingFragment()).commit();
        /*model.getmScrollingFragment().setData(model.getmChannelScheduler());
        mBinding.channelVisibleLayout.setAlpha(1);
        mfragmentQueue.remove();
        if(mfragmentQueue.size()>0){
            //startFragment();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startFragment();
                }
            },1500);
        }*/

    }

    /*Displays ondemand list*/
    private void showShowsList(ArrayList<ProgramList> cs, ChannelScheduler channelScheduler) {
//        show_tab_textview.setVisibility(View.VISIBLE);
        mBinding.showsViewLayout.setVisibility(View.VISIBLE);
        mBinding.overallScroll.setVisibility(View.GONE);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        mBinding.channelShowAlllist.setLayoutManager(lm);
        ShowListAdapter listAdapter = new ShowListAdapter(cs, getActivity(), this);
        mBinding.channelShowAlllist.setAdapter(listAdapter);
        setVideoPlayOptions(channelScheduler.getType(), channelScheduler.getName(), true);
    }

    public void setShowsList(String uid, ArrayList<ProgramList> mProgramList, ChannelScheduler channelScheduler) {
        showShowsList(mProgramList, channelScheduler);
        int pos = VideoFilter.getSelectedVideoPosition(mProgramList, uid);
        if (mBinding.channelShowAlllist.getLayoutManager() != null) {
            mBinding.channelShowAlllist.getLayoutManager().scrollToPosition(pos);
        }
        //Checking if the stream is MRSS or a youtube playlist
        if(channelScheduler.getPrograms().getProgramlist().get(1).getPlaylist().get(0).getType().equals("url"))
        {
            SType = "url";
            String MRSSInListVideoData = channelScheduler.getPrograms().getProgramlist().get(Integer.parseInt(String.valueOf(pos))).getPlaylist().get(0).getData();
            playVideoInVideoViewMRSS(MRSSInListVideoData,"0");
        }
        else {
            SType = "yt";
            playVideoInYoutubeView(mProgramList, pos, channelScheduler.getSlug());
        }
    }

    @Override
    public void onShowListItemSelected(int position, ArrayList<ProgramList> mProgramList) {
        if(mProgramList.get(1).getPlaylist().get(0).getType().equals("url"))
        {
            SType = "url";
            String MRSSInListVideoData = mProgramList.get(Integer.parseInt(String.valueOf(position))).getPlaylist().get(0).getData();
            playVideoInVideoViewMRSS(MRSSInListVideoData,"0");
        }
        else
        if (playerfragment != null) {
            playerfragment.updatePlayer(position, mProgramList);
        }
    }

    public void goBack() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || screenMode.equalsIgnoreCase("land")) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            handler.post(() -> {
                screenMode = "port";
                VideoMode = "normal";
                fragment = ExoPlayerFragment.newInstance(exoVideoList, CType, Offset, VideoMode);
                getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();
                View HomeActivityToolbar = getActivity().findViewById(R.id.container_toolbar);
                HomeActivityToolbar.setVisibility(View.VISIBLE);
                //  rest_adView.setVisibility(View.GONE);
                //  rest_adViewLayout.setVisibility(View.GONE);
                mListener.showToolbar(true);
                mBinding.layoutCategories.setVisibility(View.VISIBLE);
                mBinding.categoriesListView.setVisibility(View.VISIBLE);
                mBinding.channelShowAlllist.setVisibility(View.VISIBLE);
                //LinearLayout.LayoutParams playerframe_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
                //mBinding.playerFrameLayout.setLayoutParams(playerframe_lp);
                mBinding.playerLayout.setVisibility(View.VISIBLE);
                mBinding.showsViewLayout.setVisibility(View.GONE);
                LinearLayout.LayoutParams rel_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams rel_lp_sub = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                rel_lp_sub.setMargins(0,-390,0,0);
                mBinding.channelVisibleLayout.setLayoutParams(rel_lp);
                mBinding.channelVisibleLayout.setVisibility(View.VISIBLE);
                mBinding.linearSubMainRight.setLayoutParams(rel_lp_sub);
                mBinding.linearSubMainRight.setVisibility(View.VISIBLE);



                if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.categoriesListView)) {
                    Log.d(":::::view overlapped", "::::categories");
                } else if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.linearSubMainRight)) {
                    Log.d(":::::view overlapped", "::::linearSubMainRight");
                }
            });

            //VideoMode = "normal";
            //fragment = ExoPlayerFragment.newInstance(exoVideoList, CType, Offset, VideoMode);
            //getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();
            //displayPortraitMode();
        } else if (mBinding.showsViewLayout.getVisibility() == View.VISIBLE) {
            mBinding.showTabTextview.setVisibility(View.VISIBLE);
            Typeface google_font = Typeface.createFromAsset(getResources().getAssets(), "font/gmedium.ttf");
            mBinding.showTabTextview.setTypeface(google_font);
            mBinding.showsViewLayout.setVisibility(View.VISIBLE);
            mBinding.overallScroll.setVisibility(View.VISIBLE);
            if (previousScheduler != null) {
                setVideoType(previousScheduler);
            }
        } else {
            if (getActivity()!=null && getActivity() instanceof HomeActivity){
                ((HomeActivity) getActivity()).ExitActivityDialog();
            }
            /*Intent intentt = new Intent(getActivity(), HomeScreenGrid.class);
            startActivity(intentt);
            getActivity().finish();*/
        }
    }

    @Override
    public void onChannelSelected(ArrayList<ChannelScheduler> mChannelList) {
        if (mBrowseAllChannel.isShowing()) {
            mBrowseAllChannel.dismiss();
        }
        displayChannelsList(mChannelList);
    }

    @Override
    public void loadYoutubeVideo(String data, String title, int i) {
        if (mcasting != null) {
            mcasting.setmTitle(title);
            mcasting.setmData(data);
            mcasting.setmOffset(i);
            // mcasting.castVideoFromId(title,data,i);
            mcasting.extractStream(title, data, i);
        }
    }

    @Override
    public ArrayList<ProgramList> getProgramList(String mSlug) {
        final ChannelScheduler cs = VideoFilter.getSelectedChannel(channelSchedulerList, mSlug);
        if (cs != null)
            return cs.getPrograms().getProgramlist();
        return new ArrayList<ProgramList>();
    }

    public void startVideoCast() {

    }

    private void setVideoType(ChannelScheduler mPrograms) {
        Log.d(getClass().getSimpleName(), "Set Video Type");
        //  getActivity().runOnUiThread(this::addVideoPlayer);
        try {
            /*loading players (webview, youtube exoplayer) based on proded type*/
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_UNDEFINED)
                displayLandscapeView();
            intitializeListener(mContext);
            if (mPrograms != null && ((mPrograms.getPrograms() != null && mPrograms.getPrograms().getProgramlist().size() > 0) || (mPrograms.getStreams() != null && mPrograms.getStreams().size() > 0))) {
                String program_type = mPrograms.getType();
                String video_type = "";
                if (program_type.equalsIgnoreCase("live/video") || program_type.equalsIgnoreCase("live/radio")) {
                    Log.d("TYPE DEBUG RAIN",mPrograms.toString());
                    video_type = mPrograms.getStreams().get(0).getType();
                } else {
                    if (mPrograms.getPrograms().getProgramlist().get(0).getPlaylist() != null && mPrograms.getPrograms().getProgramlist().get(0).getPlaylist().size() > 0) {
                        video_type = mPrograms.getPrograms().getProgramlist().get(0).getPlaylist().get(0).getType();
                    } else {
                        if (mPrograms.getPrograms().getProgramlist().size() > 1) {
                            if (mPrograms.getPrograms().getProgramlist().get(1).getPlaylist() != null && mPrograms.getPrograms().getProgramlist().get(1).getPlaylist().size() > 0) {
                                video_type = mPrograms.getPrograms().getProgramlist().get(1).getPlaylist().get(0).getType();
                            }
                        }
                    }
                }
                try {
                    currentPlayingChannelScheduler = mPrograms;
                    checkIsFavoriteChannel(mPrograms.getSlug());
                    setVideoPlayOptions(program_type, mPrograms.getName(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (video_type) {
                    case "embed":
                        if (mPrograms.getStreams().get(0).getData().contains("youtube")) {
                            mListener.showCastIcon(false);
                            playVideoInYoutubeView(mPrograms.getStreams().get(0).getName(), mPrograms.getStreams().get(0).getData(), mPrograms.getSlug());
                        } else {
                            mListener.showCastIcon(false);
                            playVideoInWebView(mPrograms.getStreams().get(0).getData());

                        }
                        break;
                    case "stream":
                        mListener.showCastIcon(true);
                        if (mPrograms.getStreams().get(0).getData().contains("youtube")) {
                            playVideoInYoutubeView(mPrograms.getStreams().get(0).getName(), mPrograms.getStreams().get(0).getData(), mPrograms.getSlug());

                        } else {
                            playVideoInVideoView(mPrograms.getStreams().get(0).getData());
                        }

                        break;
                    case "custom":
                        mListener.showCastIcon(true);
                        playVideoInYoutubeView(mPrograms.getPrograms().getProgramlist(), mPrograms.getSlug());
                        break;
                    case "src":
                        mListener.showCastIcon(false);
                        if (program_type.equalsIgnoreCase("live/video") || program_type.equalsIgnoreCase("live/radio")) {
                            if (mPrograms.getStreams().get(0).getData().contains(".flv")) {
                                extractStreamUrl(mPrograms.getStreams().get(0).getData());
                            } else {
                                playVideoInVideoView(mPrograms.getStreams().get(0).getData());
                            }

                        } else {
                            extractStreamUrl(mPrograms.getPrograms().getProgramlist());
                        }
                        break;
                    case "url":
                        mListener.showCastIcon(false);
                        if (program_type.equalsIgnoreCase("linear/stream")) {
                            Log.d("IMINSIDEMPOGRAMS",mPrograms.getPrograms().getProgramlist().get(0).toString());
                            if (mPrograms.getPrograms().getProgramlist().get(1).getPlaylist().get(0).getData().contains(".mp4")) {
                                //getting video position and offset of MRSS video feed
                                String MRSSPosition = GetMRSSPlayOffset(mPrograms.getPrograms().getProgramlist())[1];
                                String MRSSOffset = GetMRSSPlayOffset(mPrograms.getPrograms().getProgramlist())[0];
                                Log.d("VIDEO POSITION RAIN", MRSSPosition);
                                Log.d("MRSSOFSET RAIN", MRSSOffset);
                                String FinalMRSSVideoURL = mPrograms.getPrograms().getProgramlist().get(Integer.parseInt(MRSSPosition)).getPlaylist().get(0).getData();

                                playVideoInVideoViewMRSS(FinalMRSSVideoURL,MRSSOffset);
                            } else {
                                playVideoInVideoView(mPrograms.getPrograms().getProgramlist().get(1).getPlaylist().get(0).getData());
                            }
                        } else {
                            extractStreamUrl(mPrograms.getPrograms().getProgramlist().get(1).getPlaylist().get(0).getData());
                        }
                        break;
                    case "":
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractStreamUrl(String data) {

    }

    private void playVideoInYoutubeView(final ArrayList<ProgramList> programlist, final String mSlug) {
        if (PlayerYouTubeFrag.getPlayerYouTubeFrag() != null) {
            handler.post(() -> PlayerYouTubeFrag.setNewData(programlist, mSlug));
        } else {
            removePlayerView();
            playerfragment = PlayerYouTubeFrag.newInstance(programlist, this, mSlug);
            getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), playerfragment).commit();

        }
       /* playerfragment = PlayerYouTubeFrag.newInstance(programlist, this, mSlug);
        fragmentManager.beginTransaction().replace(newContainerId, playerfragment).commit();
        PlayerYouTubeFrag.setNewData(programlist,mSlug);*/

    }

    private void playVideoInYoutubeView(String title, String mData, String mSlug) {

        isExoPlayer = false;
        addVideoPlayer();

        if (PlayerYouTubeFrag.getPlayerYouTubeFrag() != null) {
            PlayerYouTubeFrag.setNewData(title, mData, mSlug);
            return;
        }

        removePlayerView();
        playerfragment = PlayerYouTubeFrag.newInstance(title, mData, this, mSlug);
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), playerfragment).commit();
    }

    private void playVideoInYoutubeView(ArrayList<ProgramList> programlist, int position, String mSlug) {
        isExoPlayer = false;
        addVideoPlayer();
        if (PlayerYouTubeFrag.getPlayerYouTubeFrag() != null) {
            PlayerYouTubeFrag.setNewData(programlist, position, mSlug);
            return;
        }

        removePlayerView();
        playerfragment = PlayerYouTubeFrag.newInstance(programlist, position, this, mSlug);
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), playerfragment).commit();
    }

    private int getPlayerLayoutId() {
        return R.id.player_layout;
    }

    private void extractStreamUrl(final ArrayList<ProgramList> data) {

    }

    private void playVideoInVideoView(String data) {
        exoVideoList = data;

        Offset = 0;

        isExoPlayer = true;
        removePlayerView();
        CType = "HLS";
        addVideoPlayer();

        /*removePlayerView();
        ExoPlayerFragment fragment = ExoPlayerFragment.newInstance(data);
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();*/
    }

    private void playVideoInVideoViewMRSS(String data, String MRSSOFFSET) {
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        //int newIndex = VideoFilter.getCurentVideoPosition(programlist);
        //int MRSSoffset = 0;
        //MRSSoffset = (int) (nowAsPerDeviceTimeZone - ChannelUtils.getDurationFromDate(programlist.get(newIndex).getStart_at()));
        //Log.d("MRSSoffset",String.valueOf(MRSSoffset));
        exoVideoList = data;
        CType = "MRSS";
        Offset = Integer.parseInt(MRSSOFFSET);
        isExoPlayer = true;
        removePlayerView();
        addVideoPlayer();

        /*removePlayerView();
        ExoPlayerFragment fragment = ExoPlayerFragment.newInstance(data);
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();*/
    }

    private void playVideoInWebView(String data) {
        removePlayerView();
        WebViewPlayerFragment fragment = WebViewPlayerFragment.newInstance(data, "");
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();
    }

    private void removePlayerView() {
        try {
            if (getChildFragmentManager() != null)
                if (getChildFragmentManager().findFragmentById(getPlayerLayoutId()) != null) {
                    Fragment fragment = getChildFragmentManager().findFragmentById(getPlayerLayoutId());
                    if (fragment != null)
                        if (fragment instanceof PlayerYouTubeFrag | fragment instanceof ExoPlayerFragment | fragment instanceof WebViewPlayerFragment)
                            getChildFragmentManager().beginTransaction().remove(fragment).commit();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayLandscapeView() {
        handler.post(() -> {
            screenMode = "land";
            //  rest_adView.setVisibility(View.GONE);
            //  rest_adViewLayout.setVisibility(View.GONE);
            mListener.showToolbar(false);
            mBinding.linearSubMainRight.setVisibility(View.GONE);
            mBinding.layoutCategories.setVisibility(View.GONE);
            mBinding.categoriesListView.setVisibility(View.GONE);
            mBinding.playerLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams rel_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mBinding.playerFrameLayout.setLayoutParams(rel_lp);

            if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.categoriesListView)) {
                Log.d(":::::view overlapped", "::::categories");
            } else if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.linearSubMainRight)) {
                Log.d(":::::view overlapped", "::::linearSubMainRight");
            }
        });
    }
    public void displayFullScreenView() {
        handler.post(() -> {
            screenMode = "land";
            View HomeActivityToolbar = getActivity().findViewById(R.id.container_toolbar);
            HomeActivityToolbar.setVisibility(View.GONE);
            //  rest_adView.setVisibility(View.GONE);
            //  rest_adViewLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams zeroed_out = new LinearLayout.LayoutParams(0, 0);
            mListener.showToolbar(false);
            mBinding.linearSubMainRight.setVisibility(View.GONE);
            mBinding.layoutCategories.setVisibility(View.GONE);
            mBinding.categoriesListView.setVisibility(View.GONE);
            LinearLayout.LayoutParams rel_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200);
            rel_lp.setMargins(0,-40,0,0);
            mBinding.channelVisibleLayout.setLayoutParams(rel_lp);
            mBinding.playerLayout.setVisibility(View.VISIBLE);


            if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.categoriesListView)) {
                Log.d(":::::view overlapped", "::::categories");
            } else if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.linearSubMainRight)) {
                Log.d(":::::view overlapped", "::::linearSubMainRight");
            }
        });
    }

    private void displayPortraitMode() {
        if (HomeActivity.getInstance() != null)
            HomeActivity.getInstance().setDrawerLockMode(false);
        handler.post(() -> {
            screenMode = "port";
            //   rest_adView.setVisibility(View.VISIBLE);
            //  rest_adViewLayout.setVisibility(View.VISIBLE);
            View HomeActivityToolbar = getActivity().findViewById(R.id.container_toolbar);
            HomeActivityToolbar.setVisibility(View.VISIBLE);
            mListener.showToolbar(true);
            mBinding.playerLayout.setVisibility(View.VISIBLE);
            mBinding.linearSubMainRight.setVisibility(View.VISIBLE);
            mBinding.layoutCategories.setVisibility(View.VISIBLE);
            mBinding.categoriesListView.setVisibility(View.VISIBLE);
            mBinding.playerLayout.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams rel_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rel_lp.height = 9 * (nWidth / 16);
            mBinding.playerFrameLayout.setLayoutParams(rel_lp);
            LinearLayout.LayoutParams linearParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mBinding.linearSubMainRight.setLayoutParams(rel_lp);
            if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.categoriesListView)) {
                Log.d(":::::view overlapped", "::::categories");
            } else if (ChannelUtils.isViewOverlapping(mBinding.playerLayout, mBinding.linearSubMainRight)) {
                Log.d(":::::view overlapped", "::::linearSubMainRight");
            }
        });
    }

    private void setVideoPlayOptions(final String videoType, final String videoName, final boolean showsList) {
        handler.post(() -> {
            String LIVE_TYPE1 = "live/video";
            String LIVE_TYPE2 = "live/radio";
            String STREAM = "stream";

            if (videoType.equalsIgnoreCase(LIVE_TYPE1) || videoType.equalsIgnoreCase(LIVE_TYPE2)) {
                mBinding.txtStream.setVisibility(View.GONE);
                mBinding.btnLive.setVisibility(View.GONE);
                mBinding.imgPAUSE.setVisibility(View.GONE);
                mBinding.txtLive.setVisibility(View.VISIBLE);
                mBinding.txtVideoTitle.setVisibility(View.VISIBLE);
                mBinding.imgFULLSCREEN.setVisibility(View.VISIBLE);
                mBinding.imgSTAR.setVisibility(View.VISIBLE);
                mBinding.imgVOLUME.setVisibility(View.VISIBLE);
            } else if (!videoType.isEmpty()) {
                mBinding.txtLive.setVisibility(View.GONE);
                mBinding.btnLive.setVisibility(View.GONE);
                mBinding.txtStream.setVisibility(View.VISIBLE);
                mBinding.txtVideoTitle.setVisibility(View.VISIBLE);
                mBinding.imgFULLSCREEN.setVisibility(View.VISIBLE);
                mBinding.imgSTAR.setVisibility(View.VISIBLE);
                mBinding.imgPAUSE.setVisibility(View.GONE);
                mBinding.imgVOLUME.setVisibility(View.VISIBLE);
            }
            if (!videoName.isEmpty())
                mBinding.txtVideoTitle.setText(" - " + videoName);

            if (showsList) {
                mBinding.imgPAUSE.setVisibility(View.GONE);
                mBinding.btnLive.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkIsFavoriteChannel(String slug) {
        if (RabbitTvApplication.getInstance().getMyfavoriteList() != null)
            if (RabbitTvApplication.getInstance().getMyfavoriteList().get("favorite-channels") != null)
                if (RabbitTvApplication.getInstance().getMyfavoriteList().get("favorite-channels").size() > 0) {
                    ArrayList<FavoriteBean> FAVORITE_CHANNELS_LIST = RabbitTvApplication.getInstance().getMyfavoriteList().get("favorite-channels");
                    for (int i = 0; i < FAVORITE_CHANNELS_LIST.size(); i++) {
                        if (FAVORITE_CHANNELS_LIST.get(i) != null)
                            if (FAVORITE_CHANNELS_LIST.get(i).getSlug().equalsIgnoreCase(slug)) {
                                setFavoriteItem(true);
                                break;
                            } else {
                                setFavoriteItem(false);
                            }
                    }
                }
    }

    private void putVolumeMute() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (!inSilentMode) {
            inSilentMode = true;
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mBinding.imgVOLUME.setImageResource(R.drawable.volume_mute);
        } else {
            inSilentMode = false;
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mBinding.imgVOLUME.setImageResource(R.drawable.volume);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDestroyView() {
        instance = null;
        if (playerfragment != null) {
            playerfragment.releasePlayer();
        }
        setStreamMute(false);
        super.onDestroyView();
    }

    private void setStreamMute(boolean streamMute) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, streamMute);
        mBinding.imgVOLUME.setImageResource(R.drawable.volume);
    }

    private void setFavoriteItem(final boolean isAddedToFavorite) {
        handler.post(() -> {
            if (isAddedToFavorite) {
                isFavoriteChannel = true;
                mBinding.imgSTAR.setImageResource(R.drawable.star_filled);
            } else {
                isFavoriteChannel = false;
                mBinding.imgSTAR.setImageResource(R.drawable.star);
            }
        });

    }

    private void addFavorite(final String slug) {
        PresenterMyInterest.getInstance().addFavoriteItem(getActivity(), getString(R.string.interest_channel_slug), slug, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {
                setFavoriteItem(true);
                FavoriteBean favoriteBean = new FavoriteBean(currentPlayingChannelScheduler.getSlug(),
                        currentPlayingChannelScheduler.getLogo(), currentPlayingChannelScheduler.getName(), 0, null, null, currentPlayingChannelScheduler.getDescription(), "");
                RabbitTvApplication.getInstance().getMyfavoriteList().get("favorite-channels").add(favoriteBean);
            }

            @Override
            public void onItemRemoved() {
                setFavoriteItem(false);
            }

            @Override
            public void onFailureResponse() {
                setFavoriteItem(false);
            }
        });
    }

    private void goFullScreen()
    {
        VideoMode = "FullScreen";
        fragment_fullscreen = ExoPlayerFragmentFullScreen.newInstance(exoVideoList, CType, Offset, VideoMode);
        getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment_fullscreen).commit();
        displayFullScreenView();
    }

    private void removeFavorite(final String slug) {
        PresenterMyInterest.getInstance().removeFavoriteItem(getActivity(), getString(R.string.interest_channel_slug), slug, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {
            }

            @Override
            public void onItemRemoved() {
                setFavoriteItem(false);
            }

            @Override
            public void onFailureResponse() {
                setFavoriteItem(true);
            }
        });
    }


    private void addVideoPlayer() {
        Log.d("isExoPlayer",   "Add Video Player");

        mVideoFragment = new VideoFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(getPlayerLayoutId(), mVideoFragment);
        ft.commit();
//        fm.executePendingTransactions();
    }

    private String[] GetMRSSPlayOffset(ArrayList<ProgramList> programlist) {
        long nowAsPerDeviceTimeZone = ChannelUtils.GetUnixTime();
        int newIndex= VideoFilter.getCurentVideoPosition(programlist);
        for(int i=newIndex;i<programlist.size();i++){
            ArrayList<PlayList> playList=programlist.get(i).getPlaylist();
            for(int j=0;j<playList.size();j++){
                playList.get(j).getData();
            }

        }

        int moffset = (int) (nowAsPerDeviceTimeZone - ChannelUtils.getDurationFromDate(programlist.get(newIndex).getStart_at()));
        String PosAndOffset [] = {Integer.toString(moffset),Integer.toString(newIndex)};
        return PosAndOffset;

    }
















    public void removeVideoPlayer(boolean isLoadData) {
        Log.d("isExoPlayer", isExoPlayer + "");

        if (getActivity() != null && mVideoFragment != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(mVideoFragment);
            ft.commit();
        }


        try {
            if (isLoadData) {
                if (isExoPlayer){
                    VideoMode = "normal";
                    fragment = ExoPlayerFragment.newInstance(exoVideoList, CType, Offset, VideoMode);
                    getChildFragmentManager().beginTransaction().replace(getPlayerLayoutId(), fragment).commit();
                }
                // fragment.playPlayer(true);
                else
                    playerfragment.playPlayer(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
