package com.selecttvapp.ui.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.RadioDetailBean;
import com.selecttvapp.service.RadioServiceNew;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.views.DynamicImageView;

/**
 * Created by Appsolute dev on 12-Dec-17.
 */

public class RadioDetailsFragment extends Fragment {
    public static RadioDetailsFragment instance = null;
    private Intent radioService;
    private BackPressed backPressedListener;
    private Handler handler;
    private Runnable run;
    private RadioServiceNew mServer;
    private RadioDetailBean radio;
    private View toolbar_search;
    private boolean isPlaying = false;

    private TextView radio_play_button,
            radio_status,
            radio_name,
            radio_description,
            slogan,
            address,
            phone,
            email,
            bitrate,
            txtBackButton;
    private DynamicImageView banner_image;
    private LinearLayout slogan_layout, address_layout, phone_layout, email_layout, web_layout;

    private boolean bNewRadio = false;
    private boolean mBounded;

    public RadioDetailsFragment() {
    }

    public static RadioDetailsFragment newInstance(RadioDetailBean radio) {
        RadioDetailsFragment fragment = new RadioDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("radio", radio);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface BackPressed {
        void onBackPressed();
    }

    public void setBackPressedListener(BackPressed backPressedListener) {
        this.backPressedListener = backPressedListener;
    }

    public static RadioDetailsFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        radioService = new Intent(getActivity(), RadioServiceNew.class);
        if (getArguments() != null){
            radio = (RadioDetailBean) getArguments().getSerializable("radio");
        }

    }

    private int getLayoutResId() {
        return R.layout.fragment_radio_details;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getLayoutResId(), container, false);
        radio_status = (TextView) view.findViewById(R.id.radio_status);
        slogan_layout = (LinearLayout) view.findViewById(R.id.slogan_layout);
        address_layout = (LinearLayout) view.findViewById(R.id.address_layout);
        phone_layout = (LinearLayout) view.findViewById(R.id.phone_layout);
        email_layout = (LinearLayout) view.findViewById(R.id.email_layout);
        web_layout = (LinearLayout) view.findViewById(R.id.web_layout);
        banner_image = (DynamicImageView) view.findViewById(R.id.banner_image);

        txtBackButton = (TextView) view.findViewById(R.id.txtBackButton);
        radio_play_button = (TextView) view.findViewById(R.id.radio_play_button);
        radio_name = (TextView) view.findViewById(R.id.radio_name);
        radio_description = (TextView) view.findViewById(R.id.radio_description);
        slogan = (TextView) view.findViewById(R.id.slogan);
        address = (TextView) view.findViewById(R.id.address);
        phone = (TextView) view.findViewById(R.id.phone);
        email = (TextView) view.findViewById(R.id.email);
        bitrate = (TextView) view.findViewById(R.id.bitrate);

        toolbar_search=((HomeActivity)getActivity()).getFocusOnToolbarSearch();
        //((ImageView) toolbar_search).setImageDrawable(getResources().getDrawable(R.drawable.my_account));
        toolbar_search.setFocusable(true);
        toolbar_search.setFocusableInTouchMode(true);
        toolbar_search.setNextFocusDownId(R.id.txtBackButton);

        txtBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
                backPressedListener.onBackPressed();
            }
        });

        radio_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlay();
            }
        });
        return view;
    }

    private void onClickPlay(){
        try {
//                    if (!Permission.checkPermission(getActivity(), Permission.SYSTEM_ALERT_WINDOW))
//                        return;

            String stream_url = radio.getStream();
            String image = radio.getImage();
            String name = radio.getName();

            if (radio_play_button.getText().toString().equalsIgnoreCase("Play")) {

                isPlaying=true;
                radio_status.setText("Connecting...");
                radio_status.setTextColor(getResources().getColor(R.color.red));
                bNewRadio = true;
                getActivity().stopService(radioService);
                radioService.putExtra("url", stream_url);
                radioService.putExtra("image", image);
                radioService.putExtra("name", name);
                radioService.putExtra("height", 200);
                if (bNewRadio == true) {
                    radioService.setAction(RadioServiceNew.ACTION_SETUP_AND_PLAY);
                } else {
                    radioService.setAction(RadioServiceNew.ACTION_RESUME);
                }
                getActivity().startService(radioService);
                bNewRadio = false;
            } else {
                radioService.putExtra("url", stream_url);
                radioService.putExtra("image", image);
                radioService.putExtra("name", name);
                radioService.putExtra("height", 200);
                radioService.setAction(RadioServiceNew.ACTION_PAUSE);
                getActivity().startService(radioService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (radio != null)
            showRadioDetails(radio);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPlaying){
            onClickPlay();
        }
        Utilities.googleAnalytics(Constants.LISTEN_GENRE_DETAILS_SCREEN);
    }

    public void showRadioDetails(RadioDetailBean radioDetailBean) {
        Image.loadGridImage(getActivity(), radioDetailBean.getImage(), banner_image);
        if (!TextUtils.isEmpty(radioDetailBean.getName().trim()) && !radioDetailBean.getName().trim().equals("null")) {
            radio_name.setText(radioDetailBean.getName());
            radio_name.setVisibility(View.VISIBLE);
        } else radio_name.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getDescription().trim()) && !radioDetailBean.getDescription().trim().equals("null")) {
            radio_description.setText(radioDetailBean.getDescription());
            radio_description.setVisibility(View.VISIBLE);
        } else radio_description.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getSlogan().trim()) && !radioDetailBean.getSlogan().trim().equals("null")) {
            slogan.setText(radioDetailBean.getSlogan());
            slogan_layout.setVisibility(View.VISIBLE);
        } else slogan_layout.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getAddress().trim()) && !radioDetailBean.getAddress().trim().equals("null")) {
            address.setText(radioDetailBean.getAddress());
            address_layout.setVisibility(View.VISIBLE);
        } else address_layout.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getPhone().trim()) && !radioDetailBean.getPhone().trim().equals("null")) {
            phone.setText(radioDetailBean.getPhone());
            phone_layout.setVisibility(View.VISIBLE);
        } else phone_layout.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getEmail().trim()) && !radioDetailBean.getEmail().trim().equals("null")) {
            email.setText(radioDetailBean.getEmail());
            email_layout.setVisibility(View.VISIBLE);
        } else email_layout.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(radioDetailBean.getBitrate().trim()) && !radioDetailBean.getBitrate().trim().equals("null")) {
            bitrate.setText(radioDetailBean.getBitrate());
            web_layout.setVisibility(View.VISIBLE);
        } else web_layout.setVisibility(View.GONE);
    }

    private void stopService() {
        setRadioBar(new Intent(RadioServiceNew.RECIEVER_ACTION_CLOSE));
        Intent radioService = new Intent(getActivity(), RadioServiceNew.class);
        getActivity().stopService(radioService);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mServer = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            RadioServiceNew.LocalBinder mLocalBinder = (RadioServiceNew.LocalBinder) service;
            mServer = mLocalBinder.getServerInstance();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if (handler != null && run != null)
                handler.removeCallbacks(run);
            if (mBounded) {
                getActivity().unbindService(mConnection);
                mBounded = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopService();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Utilities.isMyServiceRunning(getActivity(), RadioServiceNew.class)) {
            stopService();
        }
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
        stopService();
    }

    private void setRadioStatus(String processStatus, int statusColor, String btnText, boolean btnEnabled, int btnColor) {
        radio_status.setText(processStatus);
        radio_status.setTextColor(getResources().getColor(statusColor));
        radio_play_button.setText(btnText);
        radio_play_button.setEnabled(btnEnabled);
        radio_play_button.setTextColor(getResources().getColor(btnColor));
    }

    public void setRadioBar(Intent intent) {
        try {
            if (intent.getAction().equalsIgnoreCase(RadioServiceNew.RECIEVER_ACTION_PREPARING)) {
                setRadioStatus("Buffering...(please wait)", R.color.white, "Pause", false, R.color.white);
                if (handler != null)
                    handler.removeCallbacks(run);
            } else if (intent.getAction().equalsIgnoreCase(RadioServiceNew.RECIEVER_ACTION_PLAYING)) {
                setRadioStatus("Playing...", R.color.letter_bg_color, "Pause", true, R.color.white);
                if (handler != null)
                    handler.removeCallbacks(run);
            } else if (intent.getAction().equalsIgnoreCase(RadioServiceNew.RECIEVER_ACTION_STOPPED)) {
                setRadioStatus("Stopped", R.color.white, "Play", true, R.color.white);
                if (handler != null)
                    handler.removeCallbacks(run);
            } else if (intent.getAction().equalsIgnoreCase(RadioServiceNew.RECIEVER_ACTION_PREPARE_ERROR)) {
                setRadioStatus("Connecting to station...", R.color.red, "Play", true, R.color.white);
                handler = new Handler();
                try {
                    run = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.removeCallbacks(run);
                                if (radio_status.getText().toString().equalsIgnoreCase("Connecting to station... Please wait...")) {
                                    radio_status.setText("Error Occured");
                                    radio_status.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                                    radio_play_button.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(run, 60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (intent.getAction().equalsIgnoreCase(RadioServiceNew.RECIEVER_ACTION_CLOSE)) {
                setRadioStatus("", R.color.red, "Play", true, R.color.white);
                bNewRadio = true;
                try {
                    if (handler != null)
                        handler.removeCallbacks(run);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
