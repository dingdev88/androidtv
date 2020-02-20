package com.selecttvapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.bean.HomeBean;
import com.selecttvapp.ui.bean.HomeScreenArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeScreenGrid extends FragmentActivity {
    private TextView date_textview, terms_textview;
    private ArrayList<HomeBean> home_list = new ArrayList<>();
    ArrayList<HomeScreenArray> homeScreenArrayArrayList = new ArrayList<>();

    public static final int VIEW_MAIN = 0;
    public static final int VIEW_TV = 1;
    public static final int VIEW_MOVIES = 2;
    public static final int VIEW_MOVIEDETAIL = 3;
    public static final int VIEW_NETWORK = 4;
    public static final int VIEW_KIDS = 5;
    public static final int VIEW_ADDS = 6;
    public static final int VIEW_HOME = 7;
    public static final int VIEW_RADIOSTATION = 8;
    public static final int VIEW_DEMAND = 9;
    public static final int VIEW_SEARCH = 10;
    public static final int VIEW_MYINTERST = 11;
    public static final int VIEW_MYACCOUNT = 12;
    public static final int VIEW_SUBSCRPTIONS = 13;
    public static final int VIEW_OTACABLE = 14;
    public static final int VIEW_GAME = 15;
    public static final int VIEW_MORE = 16;
    public static final int VIEW_PAYPERVIEW = 17;
    public static final int VIEW_APPMANAGER = 18;
    public static final int VIEW_LOGOUT = 19;
    public static final int VIEW_CHANNELS = 20;
    private int VIEW_MODE = VIEW_MAIN;

//    public CategoryMenuFragment categoryMenuFragment;

    public static void newInstance(Activity activity) {
        Intent intent = new Intent(activity, HomeScreenGrid.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_grid);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        LinearLayout linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);
        LinearLayout linearLayout6 = (LinearLayout) findViewById(R.id.linearLayout6);
        LinearLayout linearLayout7 = (LinearLayout) findViewById(R.id.linearLayout7);
        LinearLayout linearLayout8 = (LinearLayout) findViewById(R.id.linearLayout8);
        LinearLayout linearLayout9 = (LinearLayout) findViewById(R.id.linearLayout9);
        LinearLayout linearLayout10 = (LinearLayout) findViewById(R.id.linearLayout10);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) findViewById(R.id.imageView4);
        ImageView imageView5 = (ImageView) findViewById(R.id.imageView5);
        ImageView imageView6 = (ImageView) findViewById(R.id.imageView6);
        ImageView imageView7 = (ImageView) findViewById(R.id.imageView7);
        ImageView imageView8 = (ImageView) findViewById(R.id.imageView8);
        ImageView imageView9 = (ImageView) findViewById(R.id.imageView9);
        ImageView imageView10 = (ImageView) findViewById(R.id.imageView10);

        TextView text_View1 = (TextView) findViewById(R.id.text_View1);
        TextView text_View2 = (TextView) findViewById(R.id.text_View2);
        TextView text_View3 = (TextView) findViewById(R.id.text_View3);
        TextView text_View4 = (TextView) findViewById(R.id.text_View4);
        TextView text_View5 = (TextView) findViewById(R.id.text_View5);
        TextView text_View6 = (TextView) findViewById(R.id.text_View6);
        TextView text_View7 = (TextView) findViewById(R.id.text_View7);
        TextView text_View8 = (TextView) findViewById(R.id.text_View8);
        TextView text_View9 = (TextView) findViewById(R.id.text_View9);
        TextView text_View10 = (TextView) findViewById(R.id.text_View10);
        TextView support_textview = (TextView) findViewById(R.id.support_textview);
        support_textview.setVisibility(View.GONE);

        home_list.add(new HomeBean(linearLayout1, imageView1, text_View1));
        home_list.add(new HomeBean(linearLayout2, imageView2, text_View2));
        home_list.add(new HomeBean(linearLayout3, imageView3, text_View3));
        home_list.add(new HomeBean(linearLayout4, imageView4, text_View4));
        home_list.add(new HomeBean(linearLayout5, imageView5, text_View5));
        home_list.add(new HomeBean(linearLayout6, imageView6, text_View6));
        home_list.add(new HomeBean(linearLayout7, imageView7, text_View7));
        home_list.add(new HomeBean(linearLayout8, imageView8, text_View8));
        home_list.add(new HomeBean(linearLayout9, imageView9, text_View9));
        home_list.add(new HomeBean(linearLayout10, imageView10, text_View10));


        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.channels, "Channels"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ondemand, "On-Demand"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.ic_speaker, "Listen"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.payview, "Pay Per View"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.addons, "Subscriptions"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.cable, "Over the Air"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.interest, "My Interests"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.myaccount, "My Account"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.games, "Games"));
        homeScreenArrayArrayList.add(new HomeScreenArray(R.drawable.more, "More"));

        imageView1.setImageResource(R.drawable.channels);
        text_View1.setText("Channels");

        imageView2.setImageResource(R.drawable.ondemand);
        text_View2.setText("On-Demand");

        imageView3.setImageResource(R.drawable.ic_speaker);
        text_View3.setText("Listen");

        imageView4.setImageResource(R.drawable.payview);
        text_View4.setText("Pay Per View");

        imageView5.setImageResource(R.drawable.addons);
        text_View5.setText("Subscriptions");

        imageView6.setImageResource(R.drawable.cable);
        text_View6.setText("Over the Air");

        imageView7.setImageResource(R.drawable.interest);
        text_View7.setText("My Interests");

        imageView8.setImageResource(R.drawable.myaccount);
        text_View8.setText("My Account");

        imageView9.setImageResource(R.drawable.games);
        text_View9.setText("Games");

        imageView10.setImageResource(R.drawable.more);
        text_View10.setText("More");
        support_textview.setText("Support");
//        Drawable mDrawable = ContextCompat.getDrawable(R.drawable.channels);
//        mDrawable.setColorFilter(new
//                PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
        support_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("entervalue");
//                Intent viewIntent =
//                        new Intent("android.intent.action.VIEW",
//                                Uri.parse("http://support.freecast.com/"));
//                startActivity(viewIntent);

//                new GetNeedHelpURI().execute();

                try {
                    String url = getString(R.string.need_help_url1);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_CHANNELS);
                startActivity(intent);

            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_DEMAND);
                startActivity(intent);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_RADIOSTATION);
                startActivity(intent);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_PAYPERVIEW);
                startActivity(intent);

            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_SUBSCRPTIONS);
                startActivity(intent);
            }
        });
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_OTACABLE);
                startActivity(intent);

            }
        });
        linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_MYINTERST);
                startActivity(intent);
            }
        });
        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_MYACCOUNT);
                startActivity(intent);
            }
        });
        linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                intent.putExtra("mode", VIEW_GAME);
                startActivity(intent);
            }
        });
        linearLayout10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeScreenGrid.this, HomeActivity.class);
                in.putExtra("mode", VIEW_MORE);
                startActivity(in);
            }
        });








        /*for (int i = 0; i < 10; i++) {
            final int ii = i;
            LinearLayout ll = home_list.get(i).getLinearLayout();
            ImageView iv = home_list.get(i).getImageView();
            TextView tv = home_list.get(i).getTextView();

            iv.setImageResource(homeScreenArrayArrayList.get(i).getmHomescreenGridImages());
            tv.setText(homeScreenArrayArrayList.get(i).getmHomeScreenGridname());


            if (i == 0) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_CHANNELS);
                        startActivity(intent);

                    }
                });

                }
            if (i == 1) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                      *//*  Intent intent = new Intent(HomeScreenGrid.this, OnDemandActivity.class);
                        startActivity(intent);*//*
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_DEMAND);
                        startActivity(intent);


                    }
                });
            }
            if (i == 2) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, MainActivity.class);
                        intent.putExtra("mode",VIEW_RADIOSTATION);
                        startActivity(intent);

                    }
                });
            }
            if (i == 3) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {

                    }
                });
            }
            if (i == 4) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {


                        *//*Intent intent = new Intent(MainActivity.this, SubScrptionActivity.class);
                        startActivity(intent);*//*

                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_SUBSCRPTIONS);
                        startActivity(intent);

                    }
                });
            }
            if (i == 5) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_OTACABLE);
                        startActivity(intent);

                    }
                });
            }
            if (i == 6) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_MYINTERST);
                        startActivity(intent);

                    }
                });
            }
            if (i == 7) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_MYACCOUNT);
                        startActivity(intent);

                    }
                });
            }
            if (i == 8) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        Intent intent = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        intent.putExtra("mode",VIEW_GAME);
                        startActivity(intent);

                    }
                });
            }
            if (i == 9) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClickItem(View v) {
                        *//*Intent intent = new Intent(HomeScreenGrid.this, MainActivity.class);
                        intent.putExtra("mode",VIEW_MORE);
                        startActivity(intent);*//*
                        Intent in = new Intent(HomeScreenGrid.this, HomeActivity.class);
                        in.putExtra("mode",VIEW_MORE);
                        startActivity(in);

                    }
                });
            }
        }*/
    }

//    private MainActivity mainScreenActivity;
//
//    private void onActionChannels() {
//        if (mainScreenActivity != null) {
//            requestToHideContent();
//            mainScreenActivity.setViewMode(MainActivity.VIEW_MAIN);
//            mainScreenActivity.resumeVideoPlayer();
//        }
//    }
//
//    private void requestToHideContent() {
//        if (mainScreenActivity != null) {
//            mainScreenActivity.hideLeftMenuAndShowContent();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.HOME_GRID_SCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showNeedHelp(String url) {
        /*Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 0);*/
        try {
            //String url = getString(R.string.need_help_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (i != null) {
                i.setData(Uri.parse(url));
                System.out.println("bsbsb" + url);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    // Get need help apis
    private class GetNeedHelpURI extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            //           if (!InternetConnection.isConnected(getApplicationContext()))
//                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
            JSONObject object = JSONRPCAPI.getNeedHelpURI(getString(R.string.need_help_url1));
            return object;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json == null)
                return;

            Log.e("need help json object", json.toString());
            String appName = getString(R.string.app_name);

            Iterator iterator = json.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (appName.toLowerCase().toString().replace(" ", "").equals(key.toLowerCase().replace(" ", "")))
                    try {
                        showNeedHelp(json.getString(key));
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    }


}
