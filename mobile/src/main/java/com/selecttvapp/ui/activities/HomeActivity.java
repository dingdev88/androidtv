package com.selecttvapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.network.util.AppConstants;
import com.demo.network.util.AppPreference;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.channels.ChannelScheduler;
import com.selecttvapp.channels.ChannelsRestFragment;
import com.selecttvapp.channels.MediaRouteButtonHoloDark;
import com.selecttvapp.channels.OnChannelFragmentInteractionListener;
import com.selecttvapp.channels.ProgramList;
import com.selecttvapp.channels.ScrollingFragment;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ondemandpage.OnDemandMain;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.service.RadioServiceNew;
import com.selecttvapp.ui.adapters.FragmentDrawer;
import com.selecttvapp.ui.adapters.RightFragmentDrawer;
import com.selecttvapp.ui.fragments.AppDownloadFragment;
import com.selecttvapp.ui.fragments.AppManagerFragment;
import com.selecttvapp.ui.fragments.FastDownloadFragment;
import com.selecttvapp.ui.fragments.FeaturedTvShowsFragment;
import com.selecttvapp.ui.fragments.GamesFragment;
import com.selecttvapp.ui.fragments.KidsFragment;
import com.selecttvapp.ui.fragments.ListenFragment;
import com.selecttvapp.ui.fragments.LiveStreamsFragment;
import com.selecttvapp.ui.fragments.MoreFragment;
import com.selecttvapp.ui.fragments.MyAccountFragment;
import com.selecttvapp.ui.fragments.MyInterestFragment;
import com.selecttvapp.ui.fragments.MySubscriptionFragment;
import com.selecttvapp.ui.fragments.MyToolsSubcriptionsOffersFragment;
import com.selecttvapp.ui.fragments.MytoolsPPVFragment;
import com.selecttvapp.ui.fragments.OTAFragment;
import com.selecttvapp.ui.fragments.OnDemandGetAppsFragment;
import com.selecttvapp.ui.fragments.OnDemandSuggestionFragment;
import com.selecttvapp.ui.fragments.OnDemandYoutubeFragment;
import com.selecttvapp.ui.fragments.RadioDetailsFragment;
import com.selecttvapp.ui.fragments.SearchFragment;
import com.selecttvapp.ui.fragments.SubscriptionsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.selecttvapp.common.Constants.APP_MANAGER;
import static com.selecttvapp.common.Constants.CHANNELS;
import static com.selecttvapp.common.Constants.FEATURED_TITLE;
import static com.selecttvapp.common.Constants.GAMES_SCREEN;
import static com.selecttvapp.common.Constants.ID;
import static com.selecttvapp.common.Constants.KIDS;
import static com.selecttvapp.common.Constants.LISTEN_SCREEN;
import static com.selecttvapp.common.Constants.MORE;
import static com.selecttvapp.common.Constants.MY_ACCOUNT_SCREEN;
import static com.selecttvapp.common.Constants.MY_INTEREST;
import static com.selecttvapp.common.Constants.MY_SUBSCRIPTIONS;
import static com.selecttvapp.common.Constants.ON_DEMAND_SCREEN;
import static com.selecttvapp.common.Constants.OVER_THE_AIR_SCREEN;
import static com.selecttvapp.common.Constants.PAY_PER_VIEW_SCREEN;
import static com.selecttvapp.common.Constants.PPV_DEAL_FINDER;
import static com.selecttvapp.common.Constants.SUBSCRIPTIONS;
import static com.selecttvapp.common.Constants.SUBSCRIPTIONS_OFFERS;
import static com.selecttvapp.common.Constants.SUGGESTIONS;

public class HomeActivity extends AppCompatActivity implements OnChannelFragmentInteractionListener, ScrollingFragment.OnScrollingFragmentInteractionListener,
        OnDemandGetAppsFragment.OnGetAppsFragmentInteractionListener, AppManagerFragment.OnAppFragmentInteractionListener, OnDemandYoutubeFragment.OndemandyoutubeFragmentInteractionListener,
        FragmentDrawer.FragmentDrawerListener, FastDownloadFragment.OnAppDownloadFragmentInteractionListener,
        RightFragmentDrawer.FragmentRightgDrawerListener/*, ChannelsFragment.ChannelTotalListener */ {

    public static int channelID;
    public static OnBackPressedListener onBackPressedListener;
    //public static MediaRouteButtonHoloDark media_route_button;
    public static Context m_gContext;
    public static HomeActivity instance = null;
    BroadcastReceiver playerStateReceiver;
    RelativeLayout homeactivity_searchbar_layout;
    //Home Container Toolbar
    LinearLayout HCToolbar;
    TextView homeactivity_search_text;
    ImageView homeactivity_searchView_close;
    EditText homeactivity_searchView;
    Fragment fragment = null;
    List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout rightdrawerLayout;
    private RightFragmentDrawer rightFragmentDrawer;
    private FrameLayout container_body;
    private String selectedView = "";
    private String currentFragment = "";
    private TextView selected_menu;
    private String slug = "";
    //private MediaRouteButton activity_homescreen_chromecast;
    //private ImageView activity_homescreen_chromecast_image;
    private ImageView activity_homescreen_toolbar_fullview, activity_homescreen_toolbar_search, activity_homescreen_toolbar_appmanager, activity_homescreen_toolbar_myaccount;
    private ChannelsRestFragment mChannelsRestFragment;
    private ListenFragment mlistenFragment;
    private Drawable x;

    public static void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        HomeActivity.onBackPressedListener = onBackPressedListener;
    }

    public static HomeActivity getInstance() {
        return instance;
    }

    public ChannelsRestFragment getmChannelsRestFragment() {
        return mChannelsRestFragment;
    }

    public void setmChannelsRestFragment(ChannelsRestFragment mChannelsRestFragment) {
        this.mChannelsRestFragment = mChannelsRestFragment;
    }

    public ListenFragment getMlistenFragment() {
        return mlistenFragment;
    }

    public void setMlistenFragment(ListenFragment mlistenFragment) {
        this.mlistenFragment = mlistenFragment;
    }

    private int getLayoutContainerId() {
        return R.id.container_body;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home);
        instance = this;
        m_gContext = this;


        //int sel_pos = getIntent().getIntExtra("mode", 20);
        int sel_pos = Integer.parseInt(AppPrefrence.getInstance().getDefaultHomeScreen());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        container_body = (FrameLayout) findViewById(R.id.container_body);
        selected_menu = (TextView) findViewById(R.id.selected_menu);
        rightdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //   activity_homescreen_chromecast = (MediaRouteButton) findViewById(R.id.activity_homescreen_chromecast);
        //   activity_homescreen_chromecast_image = (ImageView) findViewById(R.id.activity_homescreen_chromecast_image);
        activity_homescreen_toolbar_fullview = (ImageView) findViewById(R.id.activity_homescreen_toolbar_fullview);
        activity_homescreen_toolbar_search = (ImageView) findViewById(R.id.activity_homescreen_toolbar_search);
        Utils.requestfocus(activity_homescreen_toolbar_search);
        activity_homescreen_toolbar_appmanager = (ImageView) findViewById(R.id.activity_homescreen_toolbar_appmanager);
        activity_homescreen_toolbar_myaccount = (ImageView) findViewById(R.id.activity_homescreen_toolbar_myaccount);
        homeactivity_searchbar_layout = (RelativeLayout) findViewById(R.id.homeactivity_searchbar_layout);
        homeactivity_search_text = (TextView) findViewById(R.id.homeactivity_search_text);
        homeactivity_searchView_close = (ImageView) findViewById(R.id.homeactivity_searchView_close);
        homeactivity_searchView = (EditText) findViewById(R.id.homeactivity_searchView);
        HCToolbar = (LinearLayout) findViewById(R.id.container_toolbar);
        //media_route_button = (MediaRouteButtonHoloDark) findViewById(R.id.media_route_button);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        rightFragmentDrawer = (RightFragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_right_navigation_drawer);
        rightFragmentDrawer.setUp(R.id.fragment_right_navigation_drawer, rightdrawerLayout, mToolbar);
        rightFragmentDrawer.setDrawerListener(this);
        if (getIntent().hasExtra("channelSlug")) {
            String channelSlug = getIntent().getStringExtra("channelSlug");
            String categorySlug = getIntent().getStringExtra("categorySlug");
            loadChannel(channelSlug, categorySlug);
        } else {
            displayView(sel_pos, "", "");
        }

        initReceiver();

      /*  activity_homescreen_chromecast_image.setOnClickListener(v -> {
            activity_homescreen_chromecast.performClick();
        });*/
        activity_homescreen_toolbar_fullview.setOnClickListener(v -> displayChannelinfullscreen());
        activity_homescreen_toolbar_appmanager.setOnClickListener(v -> displayView(Constants.VIEW_APPDOWNLOAD, "", ""));
        activity_homescreen_toolbar_myaccount.setOnClickListener(v -> {
            if (rightdrawerLayout.isDrawerOpen(GravityCompat.END)) {
                rightdrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                rightdrawerLayout.openDrawer(GravityCompat.END);
            }
        });
        addTextChangelistener(homeactivity_searchView);
        focusChangelistener(homeactivity_searchView);
        settouchlistener(homeactivity_searchView);

        x = getResources().getDrawable(R.drawable.clear_icon);
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        activity_homescreen_toolbar_search.setOnClickListener(v -> {
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.show(getSupportFragmentManager(), searchFragment.getClass().getSimpleName());
        });
        homeactivity_searchbar_layout.setOnTouchListener((v, event) -> true);
    }

    private void displayChannelinfullscreen() {
        if (fragment != null && fragment instanceof ChannelsRestFragment) {
            ChannelsRestFragment crs = (ChannelsRestFragment) fragment;
            crs.displayLandscapeView();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.HOME_SCREEN);
        try {

            IntentFilter filter = new IntentFilter();
            filter.addAction(RadioServiceNew.RECIEVER_ACTION_PLAYING);
            filter.addAction(RadioServiceNew.RECIEVER_ACTION_PREPARING);
            filter.addAction(RadioServiceNew.RECIEVER_ACTION_STOPPED);
            filter.addAction(RadioServiceNew.RECIEVER_ACTION_CLOSE);
            filter.addAction(RadioServiceNew.RECIEVER_ACTION_PREPARE_ERROR);
            registerReceiver(playerStateReceiver, filter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            if (Utilities.isMyServiceRunning(HomeActivity.this, RadioServiceNew.class)) {
                if (RadioDetailsFragment.getInstance() != null)
                    RadioDetailsFragment.getInstance().setRadioBar(new Intent(RadioServiceNew.RECIEVER_ACTION_CLOSE));
                Intent radioService = new Intent(HomeActivity.this, RadioServiceNew.class);
                stopService(radioService);
            }
            unregisterReceiver(playerStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Utilities.isMyServiceRunning(HomeActivity.this, RadioServiceNew.class)) {
            RadioDetailsFragment.getInstance().setRadioBar(new Intent(RadioServiceNew.RECIEVER_ACTION_CLOSE));
            Intent radioService = new Intent(HomeActivity.this, RadioServiceNew.class);
            stopService(radioService);
        }
    }

    private void focusChangelistener(final EditText editfocus) {
        editfocus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editfocus.setCompoundDrawables(null, null, editfocus.getText().toString().equals("") ? null : x, null);
            } else {
                editfocus.setCompoundDrawables(null, null, null, null);
            }
        });
    }

    private void addTextChangelistener(final EditText editext) {
        editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editext.setCompoundDrawables(null, null, editext.getText().toString().equals("") ? null : x, null);
            }
        });
    }

    private void settouchlistener(final EditText edit) {
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                if (edit.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > edit.getWidth() - edit.getPaddingRight() - x.getIntrinsicWidth()) {
                    edit.setText("");
                    edit.setCompoundDrawables(null, null, null, null);
                }
                return false;
            }
        });
    }

    public void initReceiver() {
        playerStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (RadioDetailsFragment.getInstance() != null) {
                        RadioDetailsFragment.getInstance().setRadioBar(intent);
                    }
//                    if (getMlistenFragment() != null) {
//                        getMlistenFragment().setRadioBar(intent);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            String fragName = fragment.getClass().getPackage().getName().toString();
            if (!fragName.equalsIgnoreCase("com.bumptech.glide.manager")) {
                currentFragment = fragment.getClass().getSimpleName();
            }
            fragList.add(new WeakReference(fragment));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<Fragment>();
        for (WeakReference<Fragment> ref : fragList) {
            Fragment f = ref.get();
            if (f != null) {
                if (f.isVisible()) {
                    ret.add(f);
                    if (f.getClass().getSimpleName().equalsIgnoreCase("PlayerYouTubeFrag")) {
                        //getSupportFragmentManager().beginTransaction().remove(f).commit();
                    }

                }
                if (f.isResumed()) {
                    ret.add(f);
                    Log.d("onAttachFragment::", ":::resume::" + f.getClass().getSimpleName());
                    if (f.getClass().getSimpleName().equalsIgnoreCase("PlayerYouTubeFrag")) {
                        getSupportFragmentManager().beginTransaction().remove(f).commit();
                    }

                }
            }
        }
        return ret;
    }

    @Override
    public void onBackPressed() {

        boolean isLeftDrawerOpen = drawerFragment.isLeftDrawerOpen();
        if(!isLeftDrawerOpen) {
            if (rightdrawerLayout.isDrawerOpen((GravityCompat.END))) {
                rightdrawerLayout.closeDrawer((GravityCompat.END));
            } else {

                if (onBackPressedListener != null) {
                    Toast.makeText(this, "In listner", Toast.LENGTH_SHORT).show();
                    onBackPressedListener.onBackPressed();
                    return;
                }

//        if (currentFragment.equalsIgnoreCase("ChannelsFragment")) {
//            getmChannelTotalFragment().onback();
//        } else
                if (getmChannelsRestFragment() != null) {
                    ChannelsRestFragment crs = (ChannelsRestFragment) fragment;
                    //Debugging Back Button
                    //Toast.makeText(this, "In Channel", Toast.LENGTH_SHORT).show();
                    crs.goBack();
                }
//        else if (currentFragment.equalsIgnoreCase("MyInterestContentFragment") && getmMyInterestContentFragment() != null && getmMyInterestContentFragment().canNavigateBack()) {
//            getmMyInterestContentFragment().goback();
//        }
                else {
                    ExitActivityDialog();
                    //Intent intentt = new Intent(HomeActivity.this, HomeScreenGrid.class);
                    //startActivity(intentt);
                    //finish();
                }
            }
        }
    }

    @Override
    public void onDrawerItemSelected(final String parentSlug, final int position, final String childSlug) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                displayView(position, parentSlug.trim(), childSlug.trim());
                if (position != Constants.VIEW_CHANNELS) {
                    getActiveFragments();
                }
            }
        }, 100);

    }

    @Override
    protected void onDestroy() {
        instance = null;
        deleteCache(this);
        super.onDestroy();

    }

    //deletes the cache folder present in our app
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position, "", "");
    }

    @Override
    public void moveright(float position) {
        container_body.setTranslationX(position);
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (getmChannelsRestFragment() != null)
                rightdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            rightdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }*/

    public void setDrawerLockMode(boolean canLock) {
        if (rightdrawerLayout != null)
            if (canLock)
                rightdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            else rightdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    public void displayView(int position, String paretSlug, String childSlug) {
        // activity_homescreen_chromecast_image.setVisibility(View.GONE);
        // media_route_button.setVisibility(View.GONE);
        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
        activity_homescreen_toolbar_appmanager.setVisibility(View.VISIBLE);
        activity_homescreen_toolbar_myaccount.setVisibility(View.VISIBLE);

        switch (position) {
           /* case 0:
                Intent intentt = new Intent(HomeActivity.this, HomeScreenGrid.class);
                startActivity(intentt);
                finish();
                break;*/
            case Constants.VIEW_DEMAND:
                selected_menu.setText(ON_DEMAND_SCREEN);

                if (paretSlug.trim().isEmpty()) {
                    if (PreferenceManager.isDemandFirstTime()) {
                        fragment = new OnDemandYoutubeFragment();
                        break;
                    }
                }

//                fragment = OnDemandFragment.newInstance("demand",paretSlug,childSlug);
                fragment = OnDemandMain.newInstance("demand", paretSlug, childSlug);
                break;
            case Constants.VIEW_PAYPERVIEW:
                selected_menu.setText(PAY_PER_VIEW_SCREEN);
//                try {
//                    if (getFragmentManager().findFragmentById(R.id.container_body) != null) {
//                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container_body)).commit();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                fragment = OnDemandFragment.newInstance("payperview",paretSlug,childSlug);
                fragment = OnDemandMain.newInstance("payperview", paretSlug, childSlug);
                break;
            case Constants.VIEW_ONDEMAND_SUGGESTIONS:
                selected_menu.setText(SUGGESTIONS);
                fragment = OnDemandSuggestionFragment.newInstance("home", "");
                break;

            case Constants.VIEW_ONDEMAND_SUBSCRIPTIONS:
                selected_menu.setText(SUBSCRIPTIONS);
                fragment = SubscriptionsFragment.newInstance(paretSlug, childSlug);
                break;

            case Constants.VIEW_CHANNELS:
                selected_menu.setText(CHANNELS);
                // activity_homescreen_chromecast_image.setVisibility(View.GONE);
                // media_route_button.setVisibility(View.VISIBLE);
                activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
                activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
                fragment = ChannelsRestFragment.newInstance(childSlug, paretSlug, false, "");
                //fragment = ChannelsFragment.newInstance(childSlug, paretSlug);
                break;
            case Constants.VIEW_RADIOSTATION:
                selected_menu.setText(LISTEN_SCREEN);
               // Utils.requestfocus(activity_homescreen_toolbar_search);
                //activity_homescreen_toolbar_appmanager.setNextFocusDownId(R.id.imageView_radio);
                fragment = new ListenFragment(activity_homescreen_toolbar_search);
                break;
            case Constants.VIEW_APPMANAGER:
                selected_menu.setText(getString(R.string.action_Fast_download));
                fragment = new AppDownloadFragment();
                break;
            case Constants.VIEW_APPDOWNLOAD:
                selected_menu.setText(APP_MANAGER);
                activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);
                fragment = new AppManagerFragment();
                break;
            case Constants.VIEW_GETAPPS:
                selected_menu.setText(APP_MANAGER);
                fragment = new OnDemandGetAppsFragment();
                break;
            case Constants.VIEW_SUBSCRPTIONS:
                selected_menu.setText(SUBSCRIPTIONS);
                fragment = MySubscriptionFragment.newInstance(paretSlug, childSlug);
                break;
            case Constants.TV_SHOWS_FEATURED:
                selected_menu.setText(FEATURED_TITLE);
                fragment = FeaturedTvShowsFragment.newInstance(paretSlug, "");
                break;
            case Constants.LIVE_TV:
            case Constants.LIVE_MOVIES:
            case Constants.LIVE_CHANNELS:
            case Constants.LIVE_MUSIC:
            case Constants.LIVE_SPORTS:
            case Constants.LIVE_Kids:
            case Constants.LIVE_WORLD:
                selected_menu.setText("Channel");
                fragment = LiveStreamsFragment.newInstance(paretSlug, childSlug);
                break;
            case Constants.VIEW_OTACABLE:
                selected_menu.setText(OVER_THE_AIR_SCREEN);
                fragment = new OTAFragment();
                break;
            case Constants.VIEW_GAME:
                selected_menu.setText(GAMES_SCREEN);
                fragment = new GamesFragment();
                break;
            case Constants.VIEW_MORE:
                selected_menu.setText(MORE);
                fragment = new MoreFragment();
                break;
            case Constants.VIEW_MYACCOUNT:
                selected_menu.setText(MY_ACCOUNT_SCREEN);
                fragment = new MyAccountFragment();
                break;
            case Constants.VIEW_MYINTERST:
                selected_menu.setText(MY_INTEREST);
                if (paretSlug.isEmpty())
                    fragment = new MyInterestFragment();
                else
                    fragment = MyInterestFragment.newInstance(paretSlug, "");
                break;
            case Constants.VIEW_MYSUBSCRIPTION:
                selected_menu.setText(MY_SUBSCRIPTIONS);
                fragment = new MySubscriptionFragment();
                break;
            case Constants.VIEW_KIDS:
                selected_menu.setText(KIDS);
                fragment = KidsFragment.newInstance(paretSlug);
                break;
            case Constants.VIEW_MYTOOLS_SUBSCRIPTIONS_OFFERS:
                selected_menu.setText(SUBSCRIPTIONS_OFFERS);
                fragment = new MyToolsSubcriptionsOffersFragment();
                break;
            case Constants.VIEW_MYTOOLS_PPV:
                selected_menu.setText(PPV_DEAL_FINDER);
                fragment = new MytoolsPPVFragment();
                break;
            case Constants.VIEW_LOGOUT:
                try {
                    fragment = null;
                    AppPreference.saveBoolean(m_gContext, false, AppConstants.KEY_IS_LOGIN);
                    Utils.clearPreferenceData(m_gContext);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;

            default:
                break;
        }
        if (fragment != null) {
            Fragment currentFragment = null;
            if (getSupportFragmentManager().findFragmentById(getLayoutContainerId()) != null)
                currentFragment = getSupportFragmentManager().findFragmentById(getLayoutContainerId());

            if (currentFragment != null) {
                if (currentFragment instanceof AppManagerFragment && fragment instanceof AppManagerFragment)
                    return;
            }

            if (currentFragment != null)
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commitNow();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(getLayoutContainerId(), fragment);
            fragmentTransaction.commit();

            // set the toolbar title
        }
    }


    public void loadChannel(String slug, String parentSlug) {
        selected_menu.setText(CHANNELS);
        //activity_homescreen_chromecast_image.setVisibility(View.GONE);
        //media_route_button.setVisibility(View.VISIBLE);
        activity_homescreen_toolbar_fullview.setVisibility(View.GONE);
        activity_homescreen_toolbar_appmanager.setVisibility(View.GONE);

        fragment = ChannelsRestFragment.newInstance(parentSlug, "", true, slug);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void ondemandyoutubeFragmentInteraction() {
        displayView(Constants.VIEW_APPMANAGER, "", "");
    }

    @Override
    public void onAppFragmentInteraction() {
        displayView(Constants.VIEW_DEMAND, "", "");
    }


    @Override
    public void onGetAppsFragmentInteraction() {
        displayView(Constants.VIEW_APPMANAGER, "", "");

    }

    @Override
    public void onAppDownloadFragmentInteraction() {
        displayView(Constants.VIEW_APPDOWNLOAD, "", "");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showToolbar(boolean option) {
        try {
            if (option) {
                mToolbar.setVisibility(View.VISIBLE);
            } else {
                mToolbar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCastIcon(final boolean option) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* if (option) {
                    activity_homescreen_chromecast_image.setVisibility(View.GONE);
                    media_route_button.setVisibility(View.VISIBLE);
                } else {
                    activity_homescreen_chromecast_image.setVisibility(View.GONE);
                    media_route_button.setVisibility(View.GONE);
                }*/
            }
        });
    }

    @Override
    public void onFirstDataLoaded(int listPosition, ChannelScheduler uri, String slug) {
        if (fragment != null && fragment instanceof ChannelsRestFragment) {
            ChannelsRestFragment crs = (ChannelsRestFragment) fragment;

            crs.setVideoFragment(listPosition, uri, slug);
        }
    }

    @Override
    public void onTimeLineSelected(String uid, ArrayList<ProgramList> mProgramList, ChannelScheduler channelScheduler) {
        if (fragment != null && fragment instanceof ChannelsRestFragment) {
            ChannelsRestFragment crs = (ChannelsRestFragment) fragment;
            crs.setShowsList(uid, mProgramList, channelScheduler);
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        displayView(position, "", "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (fragment != null)
            if (fragment instanceof AppDownloadFragment)
                fragment.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
           /* int channelId=data.getIntExtra(ID,0);
            StartChannelAsync startChannelAsync =new StartChannelAsync(this);
            startChannelAsync.execute(channelId);*/


            String slug = data.getStringExtra("slug");
            String parentSlug = data.getStringExtra("categories");

            if (parentSlug == null || slug == null)
                return;
            loadChannel(slug, parentSlug);

        } else if (resultCode == 202) {
            if (data != null) {
                int radioId = data.getIntExtra(ID, 0);
            }
        }
    }

    /*private static class StartChannelAsync extends AsyncTask<Integer,Void,JSONObject>{

        private WeakReference<HomeActivity> reference;

        StartChannelAsync(HomeActivity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        protected JSONObject doInBackground(Integer... ints) {
            final JSONObject json = JSONRPCAPI.getChannelsById(ints[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String slug="";
            try {
                slug=jsonObject.getString("slug");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String parentSlug ="ages-8-12";

            *//*if (parentSlug == null || slug == null)
                return;*//*
            if (reference.get()!=null)
            reference.get().loadChannel(slug, parentSlug);
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (fragment != null)
            if (fragment instanceof AppDownloadFragment)
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void ExitActivityDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle(R.string.warning);
        // Icon Of Alert Dialog
        // alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage(R.string.sure_exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//                getActivity().finishAffinity();
                //HomeActivity.super.onBackPressed();
                finishAffinity();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(HomeActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public View getFocusOnToolbarSearch(){
        return activity_homescreen_toolbar_search;
    }
    public View getFocusOnToolbarAppManager(){
        return activity_homescreen_toolbar_appmanager;
    }
    public View getFocusOnToolbarMyAccount(){
        return activity_homescreen_toolbar_myaccount;
    }
    public View getFocusOnToolbarHome(){
        View home=findViewById(R.id.home);
        return home;
    }

}
