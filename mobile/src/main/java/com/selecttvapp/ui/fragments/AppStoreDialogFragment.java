package com.selecttvapp.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.selecttvapp.R;
import com.selecttvapp.ui.views.DynamicSquareImageview;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/29/2016.
 */
public class AppStoreDialogFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    Runnable run;
    // TODO: Rename and change types of parameters
    private String networkname;
    private String networkimage = "";
    private String playstoreurl;
    private int drawableImage = -1;
    private TextView header_textView, note_textView, cancle_textView, install_textView;
    private DynamicSquareImageview network_imageView;
    private AQuery aq;
    private LinearLayout whole_layout;

    public AppStoreDialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppStoreDialogFragment newInstance(String networkname, String networkImage, String playstoreUrl) {
        AppStoreDialogFragment fragment = new AppStoreDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, networkname);
        args.putString(ARG_PARAM2, networkImage);
        args.putString(ARG_PARAM3, playstoreUrl);
        fragment.setArguments(args);
        return fragment;
    }// TODO: Rename and change types and number of parameters

    public static AppStoreDialogFragment newInstance(String networkname, int networkImage, String playstoreUrl) {
        AppStoreDialogFragment fragment = new AppStoreDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, networkname);
        args.putInt(ARG_PARAM2, networkImage);
        args.putString(ARG_PARAM3, playstoreUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            networkname = getArguments().getString(ARG_PARAM1);
            if (getArguments().getString(ARG_PARAM2) instanceof String)
                networkimage = getArguments().getString(ARG_PARAM2);
            else drawableImage = getArguments().getInt(ARG_PARAM2);
            playstoreurl = getArguments().getString(ARG_PARAM3);
        }
        aq = new AQuery(getActivity());
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appstore_dialog_layout, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));


        network_imageView = (DynamicSquareImageview) view.findViewById(R.id.network_imageView);
        header_textView = (TextView) view.findViewById(R.id.header_textView);
        cancle_textView = (TextView) view.findViewById(R.id.cancle_textView);
        note_textView = (TextView) view.findViewById(R.id.note_textView);
        install_textView = (TextView) view.findViewById(R.id.install_textView);

        BitmapDrawable bd = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.pop_bg);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        Log.d("layoutwidth::", "width" + width);




           /* ViewTreeObserver vto = whole_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    whole_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width = whole_layout.getMeasuredWidth();
                    int height = whole_layout.getMeasuredHeight();




                }
            });
*/

        Log.d("networkimage:::::", "networkimage:::" + networkimage);
        Log.d("playstoreurl:::::", "playstoreurl:::" + playstoreurl);

        if (!TextUtils.isEmpty(networkimage)) {
            aq.id(network_imageView).image(networkimage);
        }
        if (drawableImage != -1)
            aq.id(network_imageView).image(drawableImage);
        header_textView.setText(networkname + " " + getString(R.string.app_dialog_download_txt));
        String text = "<font color=#ffffff><b>Note: </b>After you have downloaded " + networkname + " app, please </font> <font color=#F8C100>return to our app </font><font color=#ffffff>to continue.</font>";
        note_textView.setText(Html.fromHtml(text));

        install_textView.setText("INSTALL " + networkname);

        final Handler handler = new Handler();

        run = () -> {
            try {
                handler.removeCallbacks(run);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playstoreurl));
                if (myIntent != null) {
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        handler.postDelayed(run, 4000);

        install_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handler.removeCallbacks(run);
                    getDialog().dismiss();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playstoreurl));
                    if (myIntent != null) {
                        startActivity(myIntent);
                    } else {
                        Toast.makeText(getContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        cancle_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handler.removeCallbacks(run);
                    getDialog().dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}