package com.selecttvapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.GridSpacingItemDecoration;
import com.selecttvapp.ui.activities.VideoTrailorActivity;
import com.selecttvapp.ui.adapters.GridTempAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OTAFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OTAFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTAFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OTAFragment() {
        // Required empty public constructor
    }

    private RelativeLayout activity_otacable, layout_ota_content, layout_youtube_cantent, layout_channel_details, premium_channel_layout;
    private TextView text_ota_connected_how, text_ota_antenna, text_ota_cable, watch_txtview, title_textView, desc_textView, get_button;
    private FrameLayout youtube_player_layout;
    private ImageView layout_ota_prev_button, logo_imageView;
    private LinearLayout layout_ota_antenna, layout_ota_cable, local_channel_layout;
    private RecyclerView channel_list;
    private ArrayList<Integer> images = new ArrayList<>();
    private GridLayoutManager layoutManager;
    private int spanCount;
    private boolean includeEdge;
    private String selectedOta = "", mOtaCategories = "";
    private int orient = 0, selected = 0;

    public static OTAFragment newInstance(String param1, String param2) {
        OTAFragment fragment = new OTAFragment();
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
        View view = inflater.inflate(R.layout.fragment_ota, container, false);
        activity_otacable = (RelativeLayout) view.findViewById(R.id.activity_otacable);
        text_ota_connected_how = (TextView) view.findViewById(R.id.text_ota_connected_how);
        text_ota_antenna = (TextView) view.findViewById(R.id.text_ota_antenna);
        text_ota_cable = (TextView) view.findViewById(R.id.text_ota_cable);
        layout_ota_prev_button = (ImageView) view.findViewById(R.id.layout_ota_prev_button);
        layout_ota_antenna = (LinearLayout) view.findViewById(R.id.layout_ota_antenna);
        layout_ota_cable = (LinearLayout) view.findViewById(R.id.layout_ota_cable);
        layout_ota_content = (RelativeLayout) view.findViewById(R.id.layout_ota_content);
        layout_youtube_cantent = (RelativeLayout) view.findViewById(R.id.layout_youtube_cantent);
        layout_channel_details = (RelativeLayout) view.findViewById(R.id.layout_channel_details);
        youtube_player_layout = (FrameLayout) view.findViewById(R.id.youtube_player_layout);

        logo_imageView = (ImageView) view.findViewById(R.id.logo_imageView);
        watch_txtview = (TextView) view.findViewById(R.id.watch_txtview);
        title_textView = (TextView) view.findViewById(R.id.title_textView);
        desc_textView = (TextView) view.findViewById(R.id.desc_textView);
        local_channel_layout = (LinearLayout) view.findViewById(R.id.local_channel_layout);
        premium_channel_layout = (RelativeLayout) view.findViewById(R.id.premium_channel_layout);
        channel_list = (RecyclerView) view.findViewById(R.id.channel_list);
        channel_list.setNestedScrollingEnabled(false);
        get_button = (TextView) view.findViewById(R.id.get_button);
        layout_ota_prev_button.setVisibility(View.GONE);

        images.add(R.drawable.ota_icon1);
        images.add(R.drawable.ota_icon2);
        images.add(R.drawable.ota_icon3);
        images.add(R.drawable.ota_icon4);
        images.add(R.drawable.ota_icon5);
        images.add(R.drawable.ota_icon6);
        images.add(R.drawable.ota_icon7);
        images.add(R.drawable.ota_icon8);
        images.add(R.drawable.ota_icon9);
        images.add(R.drawable.ota_icon10);
        images.add(R.drawable.ota_icon11);
        images.add(R.drawable.ota_icon12);
        images.add(R.drawable.ota_icon13);
        images.add(R.drawable.ota_icon14);
        images.add(R.drawable.ota_icon15);
        images.add(R.drawable.ota_icon16);
        images.add(R.drawable.ota_icon17);
        images.add(R.drawable.ota_icon18);
        images.add(R.drawable.ota_icon19);
        images.add(R.drawable.ota_icon20);
        images.add(R.drawable.ota_icon21);
        images.add(R.drawable.ota_icon22);
        images.add(R.drawable.ota_icon23);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 4);
            spanCount = 4;
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 6);
            spanCount = 6;
        }


        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        channel_list.hasFixedSize();
        channel_list.setLayoutManager(layoutManager);
        // 3 columns
        int spacing = 25; // 50px
        includeEdge = true;
        channel_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridTempAdapter adpater = new GridTempAdapter(images, getActivity());
        channel_list.setAdapter(adpater);

        watch_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedOta.equalsIgnoreCase("local")) {
                    watchVideo(getVideoId("1KzYpvXrLL4"));
                } else if (selectedOta.equalsIgnoreCase("premium")) {
                    watchVideo(getVideoId("ZM0j2ZSLm1w"));
                }
            }
        });


        layout_ota_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_ota_prev_button.setVisibility(View.GONE);
                if (layout_channel_details.getVisibility() == View.VISIBLE) {
                    selected = 0;
                    layout_youtube_cantent.setVisibility(View.VISIBLE);
                    layout_ota_content.setVisibility(View.VISIBLE);
                    text_ota_connected_how.setVisibility(View.VISIBLE);
                    layout_channel_details.setVisibility(View.GONE);


                } else {
                }


            }
        });
        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOta.equalsIgnoreCase("local")) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (selectedOta.equalsIgnoreCase("premium")) {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.premium_channel_url)));
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        layout_ota_antenna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* selected=1;
                mOtaCategories = "Local HD Channels";
                selectedOta = "local";
                layout_ota_prev_button.setVisibility(View.VISIBLE);
                displayLocalChannels();*/
                if (checkIsTV()) {
                    try {
                        if (Utils.appInstalledOrNot(getActivity(),"com.pineone.sb")) {


                       /* Intent myIntent = new Intent("com.pineone.sb");
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }*/


                            Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
                            intentToResolve.addCategory(Intent.CATEGORY_HOME);
                            intentToResolve.setPackage("com.pineone.sb");
                            ResolveInfo ri = getActivity().getPackageManager().resolveActivity(intentToResolve, 0);
                            if (ri != null) {
                                Intent intent = new Intent(intentToResolve);
                                intent.setClassName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name);
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                if (intent != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Error in opening the app", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(getContext(), "The Application is not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.local_channel_url)));
                        if (myIntent != null) {
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        layout_ota_cable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 2;

                mOtaCategories = "Premium Channels";
                layout_ota_prev_button.setVisibility(View.VISIBLE);
                selectedOta = "premium";
                displayPremiumChannels();

            }
        });
        if (orient == 1) {
            if (selected == 1) {
                layout_ota_antenna.performClick();
            } else if (selected == 2) {
                layout_ota_cable.performClick();
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());*/
        orient = 1;
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null);
        viewGroup.addView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnChannelFragmentInteractionListener) {
            mListener = (OnChannelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChannelFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_ota, viewGroup);


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void displayPremiumChannels() {
        layout_youtube_cantent.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.VISIBLE);

        logo_imageView.setImageResource(R.drawable.sling_new);
        title_textView.setText(R.string.premium_channel_title);
        desc_textView.setText(R.string.premium_channel_desc);
        local_channel_layout.setVisibility(View.GONE);
        premium_channel_layout.setVisibility(View.VISIBLE);
        get_button.setText("START FREE TRIAL");


    }

    private void displayLocalChannels() {
        layout_youtube_cantent.setVisibility(View.GONE);
        layout_ota_content.setVisibility(View.GONE);
        text_ota_connected_how.setVisibility(View.GONE);
        layout_channel_details.setVisibility(View.VISIBLE);

        logo_imageView.setImageResource(R.drawable.wifi);
        title_textView.setText(R.string.local_channel_title);
        desc_textView.setText(R.string.local_channel_desc);
        local_channel_layout.setVisibility(View.VISIBLE);
        premium_channel_layout.setVisibility(View.GONE);
        get_button.setText("GET IT NOW");
    }

    private void watchVideo(String strVideoId) {
        Intent intent = new Intent(getActivity(), VideoTrailorActivity.class);
        intent.putExtra("videoid", strVideoId);
        startActivity(intent);

    }

    private String getVideoId(String strUrl) {

        String strVideoId = null;

        if (strUrl.contains("v=")) {
            String[] separated = strUrl.split("v=");
            strVideoId = separated[separated.length - 1];
        } else if (strUrl.contains("embed/")) {
            String[] separated = strUrl.split("embed/");
            strVideoId = separated[separated.length - 1];
        }

        return strVideoId;
    }

//    boolean isInstalledPackageName(String packagename) {
//        if (packagename.toLowerCase().equals("google play")) return true;
//        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_HOME);
//        List<ResolveInfo> ril = getActivity().getPackageManager().queryIntentActivities(mainIntent, 0);
//        for (ResolveInfo ri : ril) {
//            Log.e("Info", "" + ri.activityInfo.parentActivityName);
//            if (ri != null) {
//                String key = ri.activityInfo.packageName;
//                Log.e("Info", "" + key);
//                if (key.equals(packagename))
//                    return true;
//            }
//        }
//        return false;
//    }

    private boolean checkIsTV() {

        try {
            String inputSystem;
            inputSystem = android.os.Build.ID;
            Log.d("hai", inputSystem);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();  // deprecated
            Log.d("hai", width + "");
            Log.d("hai", height + "");
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(width / dm.xdpi, 2);
            double y = Math.pow(height / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            Log.d("hai", "Screen inches : " + screenInches + "");
            return screenInches > 12.0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
