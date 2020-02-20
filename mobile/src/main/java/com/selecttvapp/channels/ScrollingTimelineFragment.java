package com.selecttvapp.channels;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.helper.MyApplication;

import java.util.ArrayList;


public class ScrollingTimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int listPosition;
    private ChannelScheduler mChannelScheduler;
    ChannelDatabaseMethods mChannelDatabaseMethods;
    int nWidth, nHeight,tabwidth,cellwidth;
    private static int numberOfMoves = 0;
    private RelativeLayout timeline_block_layout1,timeline_block_layout2,timeline_block_layout3;
    private TextView txtChannelName1,txtChannelName2,txtChannelName3;
    private TextView txtChannel_time1,txtChannel_time2,txtChannel_time3;
    private TextView stream_txtChannelName,stream_txtChannel_time,txtLoading;
    private View rest_program,rest_stream;

    Handler handler=new Handler();
    int currentindex=0;
    Runnable runable;
    int seekOffset;

    int d=1000;
    long scrollDelay=0;

    private ScrollingFragment.OnScrollingFragmentInteractionListener mListener;

    public ScrollingTimelineFragment() {
        // Required empty public constructor
    }


    public static ScrollingTimelineFragment newInstance(ChannelScheduler list, int param2) {
        ScrollingTimelineFragment fragment = new ScrollingTimelineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM1, new Gson().toJson(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            listPosition = getArguments().getInt(ARG_PARAM2);

            mChannelScheduler = new Gson().fromJson(mParam1, ChannelScheduler.class);
            mChannelDatabaseMethods=new ChannelDatabaseMethods();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_scrolling_timeline, container, false);
        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayProgramView();
        setData();
    }


    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.HORIZONTAL_CHANNELS_LIST_SCREEN);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScrollingFragment.OnScrollingFragmentInteractionListener) {
            mListener = (ScrollingFragment.OnScrollingFragmentInteractionListener) context;
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



    private void setData() {
        if(mChannelScheduler!=null){
            Log.d(":::::",":::"+mChannelScheduler.getType()+"::::"+mChannelScheduler.getName());
            if(mChannelScheduler.getType().equalsIgnoreCase("live/video")||mChannelScheduler.getType().equalsIgnoreCase("live/radio")){
                //loadStream(mChannelScheduler,stream_txtChannelName);
                stream_txtChannel_time.setText("Live");
            }else{
                txtLoading.setVisibility(View.VISIBLE);
                loadProgramList(mChannelScheduler);
            }
        }
    }

    private void loadProgramList(final ChannelScheduler mChannelScheduler) {
        Programs mPrograms=mChannelDatabaseMethods.getPrograms(mChannelScheduler.getParentcategorySlug(),mChannelScheduler.getSlug());
        if(mPrograms!=null){
            setProgramList(mChannelScheduler.getParentcategorySlug(),mChannelScheduler.getSlug(),mPrograms);
        }else{
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadProgramData(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug(),numberOfMoves,new ProgramApiListener(){

                    @Override
                    public void onProgramsLoaded(String categorySlug, String pSlug, final Programs mPrograms) {
                        setProgramList(categorySlug,pSlug,mPrograms);

                    }

                    @Override
                    public void onLoadingFailed() {

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

    private void setProgramList(String categorySlug, String pSlug, Programs mPrograms) {
        if(((HomeActivity) getActivity()).getmChannelsRestFragment()!=null){
            /*if (mPrograms!=null) {
                long duration = 0L;
                for (int i = 0; i < mPrograms.getProgramlist().size(); i++) {
                    long video_duration = ChannelUtils.getDuration(mPrograms.getProgramlist().get(i).getDuration());
                    duration = duration + video_duration;
                }
                long first_playlist_starting_point = 0L;
                long first_playlist_duration = 0L;
                for (int i = 0; i < mPrograms.getProgramlist().size(); i++) {
                    ProgramList mProgramList=mPrograms.getProgramlist().get(i);

                    String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                    long created_date = 0L;
                    DateFormat formatter = new SimpleDateFormat(inputPattern, Locale.getDefault());
                    try {
                        Date date = formatter.parse(mPrograms.getStarted());
                        created_date = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long nowAsPerDeviceTimeZone = 0;
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
                    long video_duration = ChannelUtils.getDuration(mPrograms.getProgramlist().get(i).getDuration());
                    nowAsPerDeviceTimeZone= ChannelUtils.GetUnixTime();
                    if (i == 0) {
                        first_playlist_starting_point = created_date + duration * ((nowAsPerDeviceTimeZone - created_date) / duration);
                        mProgramList.setStart_at(ChannelUtils.getDate(first_playlist_starting_point));
                        mProgramList.setEnd_at(ChannelUtils.getDate(first_playlist_starting_point + video_duration));
                        first_playlist_duration = video_duration;

                    } else {

                        long second_playlist_item_starting_point = first_playlist_starting_point + first_playlist_duration;
                        mProgramList.setStart_at(ChannelUtils.getDate(second_playlist_item_starting_point));
                        mProgramList.setEnd_at(ChannelUtils.getDate(second_playlist_item_starting_point + video_duration));
                        first_playlist_duration = video_duration;
                        first_playlist_starting_point = second_playlist_item_starting_point;

                    }
                    mProgramList.setScroll_dealay((int) video_duration / numberOfMoves);


                                *//*ArrayList<ProgramList> filteredList=new ArrayList<ProgramList>();
                                if(ChannelUtils.getDurationFromDate(mProgramList.getEnd_at())>ChannelUtils.GetUnixTime()){
                                    filteredList.addAll()
                                }*//*
                }
            }*/
            mChannelScheduler.setPrograms(mPrograms);

            if(mListener!=null)
                mListener.onFirstDataLoaded(listPosition,mChannelScheduler,mChannelScheduler.getSlug());

            setHorizontalList();
        }
    }

    private void setHorizontalList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayProgramView();
                ArrayList<ProgramList> filteredList=new ArrayList<>();
                filteredList=VideoFilter.removePastVideos(mChannelScheduler.getPrograms().getProgramlist());
                setVideoposition(filteredList);
            }
        });

    }

    private void setVideoposition(final ArrayList<ProgramList> filteredList) {
        if(filteredList.size()>0){
            currentindex= VideoFilter.getCurentVideoPosition(filteredList);
            int i1 = ((int) ChannelUtils.getDuration(filteredList.get(currentindex).getDuration()))/ cellwidth;
            int i2 = (int) (ChannelUtils.GetUnixTime() - ChannelUtils.getDurationFromDate(filteredList.get(currentindex).getStart_at()));
            seekOffset = (i2 / i1) + 2;

            setcurrentData(currentindex,filteredList,seekOffset);
            collapse(timeline_block_layout1,100,timeline_block_layout1.getWidth()-seekOffset);

            scrollDelay= ChannelUtils.getDuration(filteredList.get(currentindex).getDuration())/numberOfMoves;

            runable=new Runnable() {
                @Override
                public void run() {

                    collapse(timeline_block_layout1,100,timeline_block_layout1.getWidth()-cellwidth/numberOfMoves);

                    int newIndex=VideoFilter.getCurentVideoPosition(filteredList);
                    //seekoffset!=0 to check whether its called first time or not
                    //to check current video playing for the time
                    if(newIndex>=0&&(newIndex!=currentindex)){
                        currentindex=newIndex;
                        scrollDelay= ChannelUtils.getDuration(filteredList.get(newIndex).getDuration())/numberOfMoves;
                        expand(timeline_block_layout1,500,cellwidth+cellwidth/2);
                        setcurrentData(currentindex,filteredList,0);


                        seekOffset=0;





                    }else if(newIndex<0&&currentindex>=filteredList.size()-1){

                        Log.d("update:::",":::"+new Gson().fromJson(mParam1, ChannelScheduler.class).getSlug()+"::");
                        return;
                    }

                    handler.postDelayed(runable, scrollDelay);
                }
            };
            handler.postDelayed(runable, scrollDelay);

        }
    }

    private void setcurrentData(int currentindex, ArrayList<ProgramList> filteredList, int seekOffset) {

        if(currentindex>=0){
            if(filteredList.size()>currentindex&&filteredList.get(currentindex)!=null){
                ProgramList mProgramList=filteredList.get(currentindex);
                txtChannelName2.setText(mProgramList.getName());
                String strTextTime = ChannelUtils.parseDateToddMMyyyy(mProgramList.getStart_at());
                txtChannel_time2.setText(String.valueOf("Start Time: " + strTextTime));
            }
            if(currentindex>0&&filteredList.size()>currentindex-1&&filteredList.get(currentindex-1)!=null){
                ProgramList mProgramList=filteredList.get(currentindex-1);
                txtChannelName1.setText(mProgramList.getName());
                String strTextTime = ChannelUtils.parseDateToddMMyyyy(mProgramList.getStart_at());
                txtChannel_time1.setText(String.valueOf("Start Time: " + strTextTime));
            }
            if(filteredList.size()>currentindex+1&&filteredList.get(currentindex+1)!=null){
                ProgramList mProgramList=filteredList.get(currentindex+1);
                txtChannelName3.setText(mProgramList.getName());
                String strTextTime = ChannelUtils.parseDateToddMMyyyy(mProgramList.getStart_at());
                txtChannel_time3.setText(String.valueOf("Start Time: " + strTextTime));
            }
        }
    }

    private void displayNoNetwork() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),"Error in Internet Connection !",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initializeViews(View rootView) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nHeight= displayMetrics.widthPixels;
            nWidth = displayMetrics.heightPixels;

        }else{
            nWidth = displayMetrics.widthPixels;
            nHeight = displayMetrics.heightPixels;
        }
        tabwidth = nWidth / 3;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(getActivity())) / 3;
        numberOfMoves = cellwidth/ 5;

        timeline_block_layout1=(RelativeLayout)rootView.findViewById(R.id.timeline_block_layout1);
        timeline_block_layout2=(RelativeLayout)rootView.findViewById(R.id.timeline_block_layout2);
        timeline_block_layout3=(RelativeLayout)rootView.findViewById(R.id.timeline_block_layout3);

        ViewGroup.LayoutParams params1 = timeline_block_layout1.getLayoutParams();
        params1.width = cellwidth+cellwidth/2;
        ViewGroup.LayoutParams params2 = timeline_block_layout2.getLayoutParams();
        params2.width = cellwidth;
        ViewGroup.LayoutParams params3 = timeline_block_layout3.getLayoutParams();
        params3.width = cellwidth;
        timeline_block_layout1.setLayoutParams(params1);
        timeline_block_layout2.setLayoutParams(params2);
        timeline_block_layout3.setLayoutParams(params3);

        txtChannelName1=(TextView)rootView.findViewById(R.id.txtChannelName1);
        txtChannelName2=(TextView)rootView.findViewById(R.id.txtChannelName2);
        txtChannelName3=(TextView)rootView.findViewById(R.id.txtChannelName3);
        txtChannel_time1=(TextView)rootView.findViewById(R.id.txtChannel_time1);
        txtChannel_time2=(TextView)rootView.findViewById(R.id.txtChannel_time2);
        txtChannel_time3=(TextView)rootView.findViewById(R.id.txtChannel_time3);
        stream_txtChannelName=(TextView)rootView.findViewById(R.id.stream_txtChannelName);
        stream_txtChannel_time=(TextView)rootView.findViewById(R.id.stream_txtChannel_time);
        txtLoading=(TextView)rootView.findViewById(R.id.txtLoading);
        rest_program=(View)rootView.findViewById(R.id.rest_program);
        rest_stream=(View)rootView.findViewById(R.id.rest_stream);
        displayLoaderView();


    }
    private void displayProgramView(){
        rest_program.setVisibility(View.VISIBLE);
        rest_stream.setVisibility(View.GONE);
        txtLoading.setVisibility(View.GONE);
    }
    private void displayStreamView(){
        rest_program.setVisibility(View.GONE);
        rest_stream.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.GONE);
    }
    private void displayLoaderView(){
        rest_program.setVisibility(View.GONE);
        rest_stream.setVisibility(View.GONE);
        txtLoading.setVisibility(View.VISIBLE);
    }

    public static final void expand(final View v, int duration, int targetHeight) {

        int prevHeight = v.getWidth();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().width = (int) animation.getAnimatedValue();
                v.requestLayout();

            }
        });
//        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static final void collapse(final View v, int duration, int targetHeight) {
        int prevHeight = v.getWidth();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().width = (int) animation.getAnimatedValue();
                v.requestLayout();

            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

}
