package com.selecttvapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.youtube.player.YouTubePlayer;
import com.selecttvapp.R;

/**
 * Created by babin on 3/3/2017.
 */

public class OnDemandGetAppsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private YouTubePlayer YPlayer;
    private Button getApps_continue_button;
    private OnGetAppsFragmentInteractionListener mListener;

    public OnDemandGetAppsFragment() {
        // Required empty public constructor
    }

    public static OnDemandGetAppsFragment newInstance(String param1, String param2) {
        OnDemandGetAppsFragment fragment = new OnDemandGetAppsFragment();
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
        View view = inflater.inflate(R.layout.fragment_on_demand_getapps, container, false);
        getApps_continue_button = (Button) view.findViewById(R.id.getApps_continue_button);
        getApps_continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGetAppsFragmentInteraction();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGetAppsFragmentInteractionListener) {
            mListener = (OnGetAppsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChannelFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGetAppsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGetAppsFragmentInteraction();
    }

    @Override
    public void onPause() {
        super.onPause();


    }


}
