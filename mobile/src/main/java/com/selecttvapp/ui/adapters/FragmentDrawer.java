package com.selecttvapp.ui.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Constants;
import com.selecttvapp.leftmenu.CustomExpandableList;
import com.selecttvapp.leftmenu.LeftMenuInterface;
import com.selecttvapp.leftmenu.MenuParentListAdapter;
import com.selecttvapp.model.SideMenu;
import com.selecttvapp.model.SideMenuChild;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.parser.DataParser;
import com.selecttvapp.personalization.PersonalizationDialogFragment;
import com.selecttvapp.ui.WebView;
import com.selecttvapp.ui.activities.WebBrowserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.selecttvapp.common.Constants.CHANNEL;
import static com.selecttvapp.common.Constants.CHANNELS_BY_CATEGORY;
import static com.selecttvapp.common.Constants.CHANNELS_CATEGORY;
import static com.selecttvapp.common.Constants.EMPTY;
import static com.selecttvapp.common.Constants.FEATURED_PAGE;
import static com.selecttvapp.common.Constants.FRONT_PAGE;
import static com.selecttvapp.common.Constants.KID;
import static com.selecttvapp.common.Constants.LIVE_CHANNELS;
import static com.selecttvapp.common.Constants.LIVE_Kids;
import static com.selecttvapp.common.Constants.LIVE_MOVIES;
import static com.selecttvapp.common.Constants.LIVE_MUSIC;
import static com.selecttvapp.common.Constants.LIVE_SPORT;
import static com.selecttvapp.common.Constants.LIVE_SPORTS;
import static com.selecttvapp.common.Constants.LIVE_TV;
import static com.selecttvapp.common.Constants.LIVE_WORLD;
import static com.selecttvapp.common.Constants.MOVIES;
import static com.selecttvapp.common.Constants.MOVIES_RECOMMENDED;
import static com.selecttvapp.common.Constants.MUSIC;
import static com.selecttvapp.common.Constants.PAY_PER_VIEW_KIDS;
import static com.selecttvapp.common.Constants.PAY_PER_VIEW_MOVIES;
import static com.selecttvapp.common.Constants.PAY_PER_VIEW_SHOWS;
import static com.selecttvapp.common.Constants.SHOWS;
import static com.selecttvapp.common.Constants.SUBSCRIPTIONS_MOVIES;
import static com.selecttvapp.common.Constants.SUBSCRIPTION_TV;
import static com.selecttvapp.common.Constants.TEST_BRAND_ADMIN;
import static com.selecttvapp.common.Constants.TV;
import static com.selecttvapp.common.Constants.WORLDS;

/**
 * Created by Ocs pl-79(17.2.2016) on 10/5/2016.
 */
public class FragmentDrawer extends Fragment {

    public static TextView previousSelectedTxtView;
    public static String SelectedSlug = "";
    ////////////////test//////////
    public String[] state = {"A", "B", "C"};
    private Handler handler = new Handler();
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private FragmentDrawerListener drawerListener;
    private CustomExpandableList elv_left_menu;
    private MenuParentListAdapter mMenuParentListAdapter;
    private int prevMainList = -1;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.ui_left_menu_drawer, container, false);

        elv_left_menu = (CustomExpandableList) layout.findViewById(R.id.elv_left_menu);
        if (RabbitTvApplication.getInstance().getSideMenus().size() > 0)
            loadMenuList();
        else
            loadSideMenus();


        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //loadMenuList();
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //loadMenuList();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 3);
                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
                drawerListener.moveright(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(() -> mDrawerToggle.syncState());

    }

    public boolean isLeftDrawerOpen(){
        if(mDrawerLayout.isDrawerOpen((GravityCompat.START))){
            mDrawerLayout.closeDrawer((GravityCompat.START));
            return true;
        }
        return false;
    }

    private void loadSideMenus() {
        Thread thread = new Thread(() -> {
            try {

//                String data = DataReader.readDataFromFile(getActivity(), "sidemenu.json");
//                Log.v("sidemenu", "" + data);
//                JSONArray jsonArray = new JSONArray(data);

                JSONArray object = JSONRPCAPI.getLeftmenu();
                //JSONArray response = JSONRPCAPI.getSideBar("selecttv-freecast-com");
                JSONArray response = JSONRPCAPI.getSideBar(BuildConfig.BRAND_SLUG);
                if (object != null) {
                    if (object.length() > 0)
                        DataParser.loadSideMenu(object, null);
                }

                Log.v("leftmenu", "__" + object);
                Log.v("leftmenu1", "__" + response);
                if (object == null) return;
                if (response == null) return;

                if (response.length() > 0) {

                    ArrayList<SideMenuChild> sideMenuChildren = new ArrayList<>();
                    SideMenu sideMenu = new SideMenu();
                    for (int i = 0; i < response.length(); i++) {
                        SideMenuChild sideMenuChild = new SideMenuChild();
                        JSONObject jsonObject = response.getJSONObject(i);
                        String url = "";
                        String name = "";
                        if (jsonObject.has("url"))
                            url = jsonObject.getString("url");
                        if (jsonObject.has("name"))
                            name = jsonObject.getString("name");
                        sideMenuChild.setName(name);
                        sideMenuChild.setSlug(url);
                        sideMenuChild.setType("mytools");
                        sideMenuChild.setUrl(url);
                        ArrayList<SideMenuChild> sideMenuChildren1 = new ArrayList<>();
                        sideMenuChild.setSideMenuChild(sideMenuChildren1);
                        sideMenuChildren.add(sideMenuChild);
                    }
                    sideMenu.setSideMenuChild(sideMenuChildren);
                    sideMenu.setName("Extras");
                    sideMenu.setSlug("mytools");
                    DataParser.sideMenus.add(sideMenu);
                }

                if (DataParser.sideMenus.size() > 0) {
                    DataParser.sideMenus = DataParser.sortMenus(DataParser.sideMenus);
                }

                if (DataParser.sideMenus.size() > 0)
                    RabbitTvApplication.getInstance().setSideMenus(DataParser.sideMenus);
                //loadMenuList();
                handler.post(() -> setUpDrawerMenu(DataParser.sideMenus));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void loadMenuList() {
        ArrayList<SideMenu> sideMenus = new ArrayList<>();

        if (RabbitTvApplication.getInstance().getSideMenus().size() > 0)
            sideMenus = RabbitTvApplication.getInstance().getSideMenus();
        else if (DataParser.sideMenus.size() > 0)
            sideMenus = DataParser.sideMenus;

        if (sideMenus.size() > 0)
            setUpDrawerMenu(sideMenus);
        else loadSideMenus();
    }

    void NavigateChildList(final String type, final String parentSlug, final String childSlug) {
     //   Log.e("sidemenu", type + ", " + parentSlug + ", " + childSlug);
        mDrawerLayout.closeDrawers();
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (type.toLowerCase()) {
                    case FEATURED_PAGE:
                    case CHANNEL:
                    case CHANNELS_BY_CATEGORY:
                    case CHANNELS_CATEGORY:
                        if (childSlug.trim().isEmpty())
                            drawerListener.onDrawerItemSelected(EMPTY, Constants.VIEW_CHANNELS, parentSlug);
                        else
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_CHANNELS, childSlug);
                        break;

                    case SHOWS:
                    case MOVIES:
                    case MOVIES_RECOMMENDED:
                        drawerListener.onDrawerItemSelected(type, Constants.VIEW_DEMAND, "");
                        break;

                    case "shows/genre":
                        drawerListener.onDrawerItemSelected(type, Constants.VIEW_DEMAND, childSlug);
                        break;
                    case "movies/genre":
                        drawerListener.onDrawerItemSelected(type, Constants.VIEW_DEMAND, childSlug);
                        break;

                    case "tv shows":
                    case "shows/by_genre":
                    case "by_decade":
                    case "network":
                    case "movies/by_genre":
                    case "movies/by_rating":
                        String type1 = type;
                        if (type.equalsIgnoreCase("by_decade") || type.equalsIgnoreCase("network"))
                            type1 = "shows/" + type;
                        drawerListener.onDrawerItemSelected(type1, Constants.VIEW_DEMAND, childSlug);
                        break;

                    case "payperview":
                        drawerListener.onDrawerItemSelected(EMPTY, Constants.VIEW_PAYPERVIEW, EMPTY);
                        break;

                    case "$ pay per view":
                        if (parentSlug.equalsIgnoreCase(PAY_PER_VIEW_SHOWS) || childSlug.equalsIgnoreCase(PAY_PER_VIEW_SHOWS))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_PAYPERVIEW, EMPTY);
                        if (parentSlug.equalsIgnoreCase(PAY_PER_VIEW_MOVIES) || childSlug.equalsIgnoreCase(PAY_PER_VIEW_MOVIES))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_PAYPERVIEW, EMPTY);
                        if (parentSlug.equalsIgnoreCase(PAY_PER_VIEW_KIDS) || childSlug.equalsIgnoreCase(PAY_PER_VIEW_KIDS))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_KIDS, EMPTY);
                        break;


                    case "$ subscriptions":
                        if (parentSlug.equalsIgnoreCase(SUBSCRIPTION_TV) || childSlug.equalsIgnoreCase(SUBSCRIPTION_TV))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUBSCRIPTIONS, childSlug);
                        if (parentSlug.equalsIgnoreCase(SUBSCRIPTIONS_MOVIES) || childSlug.equalsIgnoreCase(SUBSCRIPTIONS_MOVIES))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUBSCRIPTIONS, childSlug);
                        break;

                    case FRONT_PAGE:
                        if (parentSlug.equalsIgnoreCase(SUBSCRIPTION_TV) || childSlug.equalsIgnoreCase(SUBSCRIPTION_TV))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUBSCRIPTIONS, childSlug);
                        else if (parentSlug.equalsIgnoreCase(SUBSCRIPTIONS_MOVIES) || childSlug.equalsIgnoreCase(SUBSCRIPTIONS_MOVIES))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUBSCRIPTIONS, childSlug);
                        else if (parentSlug.equalsIgnoreCase(PAY_PER_VIEW_KIDS) || childSlug.equalsIgnoreCase(PAY_PER_VIEW_KIDS))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_KIDS, EMPTY);
                        else if (parentSlug.equalsIgnoreCase(TEST_BRAND_ADMIN) || childSlug.equalsIgnoreCase(TEST_BRAND_ADMIN))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.TV_SHOWS_FEATURED, EMPTY);
                        else if(parentSlug.equalsIgnoreCase(TV) || childSlug.equalsIgnoreCase(TV))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_TV,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(MOVIES) || childSlug.equalsIgnoreCase(MOVIES))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_MOVIES,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(CHANNEL) || childSlug.equalsIgnoreCase(CHANNEL))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_CHANNELS,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(MUSIC) || childSlug.equalsIgnoreCase(MUSIC))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_MUSIC,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(LIVE_SPORT) || childSlug.equalsIgnoreCase(LIVE_SPORT))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_SPORTS,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(KID) || childSlug.equalsIgnoreCase(KID))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_Kids,EMPTY);
                        else if(parentSlug.equalsIgnoreCase(WORLDS) || childSlug.equalsIgnoreCase(WORLDS))
                            drawerListener.onDrawerItemSelected(parentSlug,LIVE_WORLD,EMPTY);
                        else if (!parentSlug.equals("") || !childSlug.equals(""))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.TV_SHOWS_FEATURED, EMPTY);
                        break;


                    case "site_page":
                        if (parentSlug.equalsIgnoreCase("pay-per-view-shows") || childSlug.equalsIgnoreCase("pay-per-view-shows"))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_PAYPERVIEW, "");
                        if (parentSlug.equalsIgnoreCase("pay-per-view-movies") || childSlug.equalsIgnoreCase("pay-per-view-movies"))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_PAYPERVIEW, "");
                        if (parentSlug.equalsIgnoreCase("tv-shows-more-channels-kids-and-family") || childSlug.equalsIgnoreCase("tv-shows-more-channels-kids-and-family") ||
                                parentSlug.equalsIgnoreCase("movies-family-and-kids") || childSlug.equalsIgnoreCase("movies-family-and-kids"))
                            drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_KIDS, "");
                        break;

                    case "favorite":
                        drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_MYINTERST, childSlug);
                        break;

                    case "ondemand_suggestions":
                    case "shows-recommended":
                        drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUGGESTIONS, childSlug);
                        break;

//            case "ondemand_subscriptions":
//                drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_ONDEMAND_SUBSCRIPTIONS, childSlug);
//                break;

                    case "mytools":
                        if (childSlug.toLowerCase().startsWith("pay per view"))
                            drawerListener.onDrawerItemSelected("", Constants.VIEW_MYTOOLS_PPV, "");
                        else if (childSlug.toLowerCase().startsWith("subscription offers"))
                            drawerListener.onDrawerItemSelected("", Constants.VIEW_MYTOOLS_SUBSCRIPTIONS_OFFERS, "");
                        else if (childSlug.toLowerCase().startsWith("subscription manager"))
                            drawerListener.onDrawerItemSelected("", Constants.VIEW_SUBSCRPTIONS, "");
                        else {
                            Intent intent = new Intent(getActivity(), WebView.class);
                            intent.putExtra("url", parentSlug);
                            intent.putExtra("name", childSlug);
                            getActivity().startActivity(intent);
                        }
                        break;
                    default:
                        break;

                }
            }
        });
    }

    void mNavigateList(String slug, String parentSlug, String childSlug) {
        Log.e("sidemenu1", slug + ", " + parentSlug + ", " + childSlug);

        switch (slug) {
            case "home":
                drawerListener.onDrawerItemSelected(parentSlug, Constants.VIEW_MAIN, "");
                break;
            case "games":
                drawerListener.onDrawerItemSelected(slug, Constants.VIEW_GAME, parentSlug);
                break;
            case "radio":
                drawerListener.onDrawerItemSelected(slug, Constants.VIEW_RADIOSTATION, parentSlug);
                break;
            case "personalize":
                PersonalizationDialogFragment customDialogFragment = new PersonalizationDialogFragment();
                customDialogFragment.show(getActivity().getSupportFragmentManager(), PersonalizationDialogFragment.TAG);
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    private void setUpDrawerMenu(ArrayList<SideMenu> sideMenus) {
        if (!sideMenus.isEmpty()) {
            ExpandableListView.OnChildClickListener subMenuchildClickLst = (eListView, view, groupPosition, childPosition, id) -> {
                SideMenu mSideMenu = (SideMenu) eListView.getTag();

                SideMenuChild mSideMenuChild = (SideMenuChild) view.getTag(R.string.CHILD);
                SideMenuChild mSideMenuChildParent = (SideMenuChild) view.getTag(R.string.PARENT);

                if (mSideMenuChild != null) {
                    if (mSideMenuChild.getSideMenuChild() != null && mSideMenuChild.getSideMenuChild().size() > 0)
                        Log.d(":::::::", "Selected::child11, Have more child");
                    else {
                        SelectedSlug = mSideMenuChild.getSlug();
                        TextView sequence = (TextView) view.findViewById(R.id.itemChildTitle);
                        if (previousSelectedTxtView != null) {
                            previousSelectedTxtView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        }
                        if (sequence != null) {
                            sequence.setTextColor(ContextCompat.getColor(getActivity(), R.color.leftmenu_selected));
                            previousSelectedTxtView = sequence;
                        }
                        if (mSideMenuChild.getType().equalsIgnoreCase("channels/category")) {
                            NavigateChildList(mSideMenuChild.getType(), mSideMenuChildParent.getSlug(), mSideMenuChild.getSlug());
                        } else {
                            if (mSideMenuChild.getType().equalsIgnoreCase("network") || mSideMenuChild.getType().equalsIgnoreCase("by_decade"))
                                NavigateChildList(mSideMenuChild.getType(), mSideMenuChildParent.getSlug(), mSideMenuChild.getId());  // old
                            else if ((mSideMenuChild.getType().trim().isEmpty() || mSideMenuChild.getType() == null) && mSideMenu.getName().trim().equalsIgnoreCase("channels"))
                                NavigateChildList(mSideMenu.getName().toLowerCase(), mSideMenuChildParent.getSlug(), mSideMenuChild.getSlug());
                            else if (mSideMenuChild.getSlug().trim().equalsIgnoreCase(""))
                                NavigateChildList(mSideMenuChild.getType(), mSideMenuChildParent.getSlug(), mSideMenuChild.getId());
                            else
                                NavigateChildList(mSideMenuChild.getType(), mSideMenuChildParent.getSlug(), mSideMenuChild.getSlug());
                        }
                    }
                }
                return false;/* or false depending on what you need */
            };

            ExpandableListView.OnGroupClickListener subMenuGroupClickLst = (expandableListView, view, i, l) -> {
                SideMenuChild mSideMenuChild = (SideMenuChild) view.getTag();
                if (mSideMenuChild != null) {
                    if (mSideMenuChild.getSideMenuChild() != null && mSideMenuChild.getSideMenuChild().size() > 0)
                        Log.d(":::::::", "Selected::group11, Have more child");
                    else {
                        TextView sequence = (TextView) view.findViewById(R.id.itemParentTitle);
                        if (previousSelectedTxtView != null) {
                            previousSelectedTxtView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        }
                    }
                }
                return false;
            };

            mMenuParentListAdapter = new MenuParentListAdapter(getActivity(), sideMenus, subMenuGroupClickLst, subMenuchildClickLst, new LeftMenuInterface() {
                @Override
                public void onSelectedMenuUrl(String url) {
                    SelectedSlug = url;

                }

                @Override
                public void onSelectedTextView(TextView mTextView) {
                    previousSelectedTxtView = mTextView;
                }
            });
            elv_left_menu.setAdapter(mMenuParentListAdapter);

            elv_left_menu.setOnGroupClickListener((expandableListView, view, i, l) -> {
                SideMenu mSideMenuChild = (SideMenu) view.getTag();
                if (mSideMenuChild != null) {
                    if (mSideMenuChild.getSideMenuChild() != null && mSideMenuChild.getSideMenuChild().size() > 0)
                        Log.d(":::::::", "Selected::group22, Have more child");
                    else
                        mNavigateList(mSideMenuChild.getName().toLowerCase(), "", "");
                    //displayToast("Selected::group22, Have no child");

                }
                return false;
            });

            elv_left_menu.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
                SideMenuChild mSideMenuChild = (SideMenuChild) view.getTag();
                if (mSideMenuChild != null) {
                    if (mSideMenuChild.getSideMenuChild() != null && mSideMenuChild.getSideMenuChild().size() > 0)
                        Log.d("::::", "Selected::child22, Have more child");
                    else {
                        if (mSideMenuChild.getSlug() == null || mSideMenuChild.getSlug().trim().isEmpty() || mSideMenuChild.getType().trim().isEmpty()) {
                            if (mSideMenuChild.getType().trim().isEmpty() && !mSideMenuChild.getSlug().trim().isEmpty())
                                NavigateChildList(mSideMenuChild.getName(), mSideMenuChild.getSlug(), "");
                            else if (mSideMenuChild.getSlug().trim().isEmpty() && !mSideMenuChild.getType().trim().isEmpty())
                                NavigateChildList(mSideMenuChild.getType(), mSideMenuChild.getName(), "");
                            else
                                NavigateChildList(mSideMenuChild.getName(), mSideMenuChild.getName(), "");
                        } else if (mSideMenuChild.getType().equalsIgnoreCase("mytools"))
                            NavigateChildList(mSideMenuChild.getType(), mSideMenuChild.getSlug(), mSideMenuChild.getName());
                        else
                            NavigateChildList(mSideMenuChild.getType(), mSideMenuChild.getSlug(), "");
                    }
                }
                TextView txtView = (TextView) view.findViewById(R.id.itemParentTitle);
                if (previousSelectedTxtView != null)
                    previousSelectedTxtView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                if (txtView != null) {
                    txtView.setTextColor(ContextCompat.getColor(getActivity(), R.color.leftmenu_selected));
                    previousSelectedTxtView = txtView;
                }
                return false;
            });

            elv_left_menu.setOnGroupExpandListener(i -> {
                if (prevMainList != -1 && prevMainList != i) {
                    elv_left_menu.collapseGroup(prevMainList);
                }
                prevMainList = i;

            });
        }

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(String view, int position, String slug);

        public void onDrawerItemSelected(View view, int position);

        public void moveright(float position);
    }

    /////////////////test/////////////////////////
    public class Object {
        public String title; // use getters and setters instead
        public String type; // use getters and setters instead
        public String url; // use getters and setters instead
        public String slug; // use getters and setters instead
        public boolean isclick = false; // use getters and setters instead
        public List<Object> children; // same as above

        public Object() {
            children = new ArrayList<>();
        }
    }
}

