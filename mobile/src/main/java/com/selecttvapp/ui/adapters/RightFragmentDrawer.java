package com.selecttvapp.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.channels.PlayerYouTubeFrag;
import com.selecttvapp.ui.WebView;


public class RightFragmentDrawer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View containerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentRightgDrawerListener drawerListener;
    private TextView ui_right_menu_profile;
    private TextView ui_right_menu_settings;
    private TextView ui_right_menu_favorites;
    private TextView ui_right_menu_subscriptions;
    private TextView ui_right_menu_support;
    private TextView ui_right_menu_privacypolicy;
    private TextView ui_right_menu_logout;

    public static final int VIEW_MYACCOUNT = 12;
    public static final int VIEW_LOGOUT = 19;
    public static final int VIEW_MYSUBSCRIPTION = 21;
    public static final int VIEW_MYINTERST = 11;

    public RightFragmentDrawer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RightFragmentDrawer.
     */
    // TODO: Rename and change types and number of parameters
    public static RightFragmentDrawer newInstance(String param1, String param2) {
        RightFragmentDrawer fragment = new RightFragmentDrawer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_right_fragment_drawer, container, false);
        ui_right_menu_profile = (TextView) layout.findViewById(R.id.ui_right_menu_profile);
        ui_right_menu_settings = (TextView) layout.findViewById(R.id.ui_right_menu_settings);
        ui_right_menu_subscriptions = (TextView) layout.findViewById(R.id.ui_right_menu_subscriptions);
        ui_right_menu_favorites = (TextView) layout.findViewById(R.id.ui_right_menu_favorites);
        ui_right_menu_support = (TextView) layout.findViewById(R.id.ui_right_menu_support);
        ui_right_menu_privacypolicy = (TextView) layout.findViewById(R.id.ui_right_menu_privacypolicy);
        ui_right_menu_logout = (TextView) layout.findViewById(R.id.ui_right_menu_logout);
        ui_right_menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                drawerListener.onItemSelected(ui_right_menu_logout, VIEW_LOGOUT);
            }
        });
        ui_right_menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                drawerListener.onItemSelected(ui_right_menu_profile, VIEW_MYACCOUNT);
            }
        });
        ui_right_menu_subscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                drawerListener.onItemSelected(ui_right_menu_subscriptions, VIEW_MYSUBSCRIPTION);
            }
        });
        ui_right_menu_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                drawerListener.onItemSelected(ui_right_menu_favorites, VIEW_MYINTERST);
            }
        });
        ui_right_menu_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
        ui_right_menu_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                showCreateAccountScreen();
            }
        });
        ui_right_menu_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                String url="https://selecttv.com/tos";
                Intent intent = new Intent(getActivity(), WebView.class);
                intent.putExtra("url", url);
                intent.putExtra("name", "Privacy Policy");
                getActivity().startActivity(intent);
            }
        });
        return layout;
    }

    private void showCreateAccountScreen() {
        /*Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 0);*/
        try {
            String url = getString(R.string.need_help_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onDetach() {
        super.onDetach();
        drawerListener = null;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getActivity() != null && !getActivity().isFinishing())
                    if (PlayerYouTubeFrag.getPlayerYouTubeFrag() != null)
                        if (!PlayerYouTubeFrag.getPlayerYouTubeFrag().isPlayerPaused())
                            PlayerYouTubeFrag.getPlayerYouTubeFrag().playPlayer(true);
                if (getActivity() != null)
                    getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                toolbar.setAlpha(1 - slideOffset / 2);
//                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
//                drawerListener.moveLeft(slideOffset * drawerView.getWidth());
//                mDrawerLayout.bringChildToFront(drawerView);
//                mDrawerLayout.requestLayout();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public void setDrawerListener(FragmentRightgDrawerListener listener) {
        this.drawerListener = listener;
    }

    public interface FragmentRightgDrawerListener {
        public void onItemSelected(View view, int position);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        }
        return false;
    }

}

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */


