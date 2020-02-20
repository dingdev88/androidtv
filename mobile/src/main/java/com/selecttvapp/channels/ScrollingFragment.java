package com.selecttvapp.channels;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.helper.MyApplication;
import com.selecttvapp.ui.views.CustomScrollView;

import java.util.ArrayList;


public class ScrollingFragment extends Fragment implements TImelineItemListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int listPosition;

    private ChannelScheduler mChannelScheduler;

    RecyclerView fragment_channel_program_list_item;
    TextView fragment_ondemandlist_items;
    CustomScrollView fragment_channel_program_list_horizontal_scroll;
    LinearLayout list_layout;
    TextView txtLoading;
    TextView stream_txtChannelName, stream_txtChannel_time, stream_txtLoading;
    RelativeLayout stream_item_grid;
    int nWidth, nHeight, tabwidth, cellwidth;
    private static int numberOfMoves = 0;
    int currentindex = 0;
    Handler handler;
    Runnable runable;
    int seekOffset;

    ArrayList<ProgramList> totalProgramList = new ArrayList<>();
    ArrayList<ProgramList> filteredList = new ArrayList<>();
    int d = 1000;
    private ShowListListener mShowListListener;
    long scrollDelay = 0;


    private OnScrollingFragmentInteractionListener mListener;
    ProgramListAdapter mProgramListAdapter;

    private ArrayList<Runnable> mRunnable = new ArrayList<>();
    ChannelDatabaseMethods mChannelDatabaseMethods;
    private static FragmentListener mfragmentListener;

    public ScrollingFragment() {
        // Required empty public constructor
    }


    public static ScrollingFragment newInstance(ChannelScheduler list, int param2) {
        ScrollingFragment fragment = new ScrollingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM1, new Gson().toJson(list));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            listPosition = getArguments().getInt(ARG_PARAM2);

            mChannelScheduler = new Gson().fromJson(mParam1, ChannelScheduler.class);
            mChannelDatabaseMethods = new ChannelDatabaseMethods();
        }
    }

    public static void setLoadListener(FragmentListener mFragmentListener) {
        mfragmentListener = mFragmentListener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.rest_recyclerlayout_channel, container, false);
        initializeViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.CHANNELS_SCROLLING_SCREEN);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("::ViewCreated::", "::::::" + listPosition);
        super.onViewCreated(view, savedInstanceState);
        if (mfragmentListener != null)
            mfragmentListener.onFragmentLoaded();
    }

    private void initializeViews(View rootView) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            nHeight = displayMetrics.widthPixels;
            nWidth = displayMetrics.heightPixels;

        } else {
            nWidth = displayMetrics.widthPixels;
            nHeight = displayMetrics.heightPixels;
        }
        tabwidth = nWidth / 3;
        cellwidth = (nWidth - ChannelUtils.convertDpToPixels(getActivity())) / 3;
        numberOfMoves = cellwidth / 5;

        fragment_channel_program_list_item = (RecyclerView) rootView.findViewById(R.id.fragment_channel_program_list_item);
        fragment_channel_program_list_horizontal_scroll = (CustomScrollView) rootView.findViewById(R.id.fragment_channel_program_list_horizontal_scroll);
        list_layout = (LinearLayout) rootView.findViewById(R.id.list_layout);
        txtLoading = (TextView) rootView.findViewById(R.id.txtLoading);

        stream_txtChannelName = (TextView) rootView.findViewById(R.id.stream_txtChannelName);
        stream_txtChannel_time = (TextView) rootView.findViewById(R.id.stream_txtChannel_time);
        stream_txtLoading = (TextView) rootView.findViewById(R.id.stream_txtLoading);
        stream_item_grid = (RelativeLayout) rootView.findViewById(R.id.stream_item_grid);
        setData();
    }

    public void setData() {
        if (mChannelScheduler != null) {
            Log.d(":::::", ":::" + mChannelScheduler.getType() + "::::" + mChannelScheduler.getName());
            if (mChannelScheduler.getType().equalsIgnoreCase("live/video") || mChannelScheduler.getType().equalsIgnoreCase("live/radio")) {
                fragment_channel_program_list_item.setVisibility(View.GONE);
                stream_item_grid.setVisibility(View.VISIBLE);
                txtLoading.setVisibility(View.GONE);
                loadStream(mChannelScheduler, stream_txtChannelName);
                stream_txtChannel_time.setText("Live");


            } else {
                fragment_channel_program_list_item.setVisibility(View.GONE);
                txtLoading.setVisibility(View.VISIBLE);
                stream_item_grid.setVisibility(View.GONE);
                loadProgramList(mChannelScheduler, false);
            }
        }
    }


    private void loadProgramList(final ChannelScheduler mChannelScheduler, final boolean pagination) {
        Programs mPrograms = mChannelDatabaseMethods.getPrograms(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug());
        /*if(mPrograms!=null){
            setProgramList(mChannelScheduler.getParentcategorySlug(),mChannelScheduler.getSlug(),mPrograms,pagination);
        }else{*/
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadProgramData(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug(), numberOfMoves, new ProgramApiListener() {

                    @Override
                    public void onProgramsLoaded(String categorySlug, String pSlug, final Programs mPrograms) {
                        setProgramList(categorySlug, pSlug, mPrograms, pagination);

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
        //}

    }

    private void displayNoNetwork() {
        getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(), "Error in Internet Connection !", Toast.LENGTH_LONG).show());

    }

    private void setProgramList(String categorySlug, String pSlug, Programs mPrograms, boolean pagination) {
        if (((HomeActivity) getActivity()).getmChannelsRestFragment() != null) {
            if (!pagination)
                totalProgramList.clear();
           /* if (mPrograms!=null) {
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
            totalProgramList.clear();
            totalProgramList.addAll(mPrograms.getProgramlist());
            mChannelScheduler.setPrograms(mPrograms);

            if (listPosition == 0)
                d = 0;

            if (pagination) {
                getActivity().runOnUiThread(() -> {
                    fragment_channel_program_list_item.setVisibility(View.VISIBLE);
                    txtLoading.setVisibility(View.GONE);
                    updateHorizontalList();
                    if (mListener != null)
                        mListener.onFirstDataLoaded(listPosition, mChannelScheduler, mChannelScheduler.getSlug());
                });

            } else {
                if (mListener != null)
                    mListener.onFirstDataLoaded(listPosition, mChannelScheduler, mChannelScheduler.getSlug());

                setHorizontalList();
            }
        }
    }

    private void updateHorizontalList() {
        filteredList.clear();
        filteredList = VideoFilter.removePastVideos(totalProgramList);
        mProgramListAdapter.setVideoData(filteredList);
        if (handler != null) {
            handler.removeCallbacks(runable);
            runable = null;
        }
        setVideoposition(filteredList);
    }

    private void setHorizontalList() {
        filteredList.clear();
        filteredList = VideoFilter.removePastVideos(totalProgramList);

        /*ArrayList<ProgramList> limitedList=new ArrayList<>();
        if(filteredList.size()<=3){
            limitedList.addAll(filteredList);
        }else{
            limitedList.addAll(filteredList.subList(0,3));
        }*/
        if (filteredList.size() > 0) {
            getActivity().runOnUiThread(() -> {
            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 1000);*/
                fragment_channel_program_list_item.setVisibility(View.VISIBLE);
                txtLoading.setVisibility(View.GONE);
                CustomLayoutManager layoutManager5 = new CustomLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                layoutManager5.setHoriScrollEnabled(false);
                fragment_channel_program_list_item.setHasFixedSize(false);
                fragment_channel_program_list_item.setLayoutManager(layoutManager5);
                if (getActivity() != null && ((HomeActivity) getActivity()).getmChannelsRestFragment() != null) {
                    mProgramListAdapter = new ProgramListAdapter(filteredList, getActivity(), null, "", 0, ScrollingFragment.this);
                    fragment_channel_program_list_item.setAdapter(mProgramListAdapter);
                    setVideoposition(filteredList);
                }

            });

        } else {
            UpdateList(new Gson().fromJson(mParam1, ChannelScheduler.class));
        }


    }

    private void setVideoposition(final ArrayList<ProgramList> mprogramlist) {
        if (mprogramlist.size() > 0) {
            currentindex = VideoFilter.getCurentVideoPosition(mprogramlist);
            if (currentindex >= 0) {
                int i1 = ((int) ChannelUtils.getDuration(mprogramlist.get(currentindex).getDuration())) / cellwidth;
                int i2 = (int) (ChannelUtils.GetUnixTime() - ChannelUtils.getDurationFromDate(mprogramlist.get(currentindex).getStart_at()));
                seekOffset = (i2 / i1) + 2;

                fragment_channel_program_list_horizontal_scroll.post(() -> fragment_channel_program_list_horizontal_scroll.scrollTo(seekOffset + (currentindex * cellwidth) - cellwidth, 0));


                scrollDelay = ChannelUtils.getDuration(mprogramlist.get(currentindex).getDuration()) / numberOfMoves;

                runable = () -> {

                    fragment_channel_program_list_horizontal_scroll.scrollTo(fragment_channel_program_list_horizontal_scroll.getScrollX() + (cellwidth / numberOfMoves), 0);

                    int newIndex = VideoFilter.getCurentVideoPosition(mprogramlist);
                    //seekoffset!=0 to check whether its called first time or not
                    //to check current video playing for the time
                    if (newIndex >= 0 && (newIndex != currentindex)) {
                        currentindex = newIndex;
                        mProgramListAdapter.newIndex(currentindex);
                        scrollDelay = ChannelUtils.getDuration(mprogramlist.get(newIndex).getDuration()) / numberOfMoves;
                        fragment_channel_program_list_horizontal_scroll.scrollTo((newIndex * cellwidth) - cellwidth, 0);


                        seekOffset = 0;
                        if (currentindex >= mprogramlist.size() - 1) {
                            UpdateList(new Gson().fromJson(mParam1, ChannelScheduler.class));
                        }


               /* int currentListSize=mprogramlist.size();
                if(totalProgramList.size()>currentListSize){
                    ProgramList nextProgramList=totalProgramList.get(currentListSize);
                    mprogramlist.add(nextProgramList);
                    if(mProgramListAdapter!=null){
                        mProgramListAdapter.addVideoItem(nextProgramList);
                    }

                }*/

                /*if(currentindex==mprogramlist.size()-1){
                    if(allProgramList.size()>mprogramlist.size()){
                        ArrayList<ProgramList> paginationList=new ArrayList<>();
                        paginationList.addAll(allProgramList.subList(mprogramlist.size(),allProgramList.size()-1));
                        mProgramListAdapter.addVideoData(paginationList);
                    }else{
                        UpdateList();
                    }


                }*/


                    } else if (newIndex < 0 && currentindex >= mprogramlist.size() - 1) {
                        UpdateList(new Gson().fromJson(mParam1, ChannelScheduler.class));
                        Log.d("update:::", ":::" + new Gson().fromJson(mParam1, ChannelScheduler.class).getSlug() + "::");
                        return;
                    } else if (newIndex == -1) {
                        UpdateList(new Gson().fromJson(mParam1, ChannelScheduler.class));
                        Log.d("update:::", ":::" + new Gson().fromJson(mParam1, ChannelScheduler.class).getSlug() + "::");
                    }

                    handler.postDelayed(runable, scrollDelay);
                };
                mRunnable.add(runable);
                handler.postDelayed(runable, scrollDelay);

            } else {
                UpdateList(new Gson().fromJson(mParam1, ChannelScheduler.class));
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            for (int i = 0; i < mRunnable.size(); i++) {
                handler.removeCallbacks(mRunnable.get(i));
            }
            mRunnable.clear();
        }
    }

    private void UpdateList(ChannelScheduler channelScheduler) {
        loadProgramList(channelScheduler, true);
    }

    private void loadStream(final ChannelScheduler mChannelScheduler, final TextView stream_txtChannelName) {
        ArrayList<Streams> mStreams = mChannelDatabaseMethods.getStreams(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug());
        /*if(mStreams!=null&&mStreams.size()>0){
            setStreamdata(mChannelScheduler.getParentcategorySlug(),mChannelScheduler.getSlug(),mStreams,stream_txtChannelName);
        }else{*/
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadSteamData(mChannelScheduler.getParentcategorySlug(), mChannelScheduler.getSlug(), new StreamApiListener() {
                    @Override
                    public void onStreamLoaded(final String categorySlug, final String sSlug, final ArrayList<Streams> mStream) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setStreamdata(categorySlug, sSlug, mStream, stream_txtChannelName);

                            }
                        });
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
        // }

    }

    private void setStreamdata(String categorySlug, String sSlug, ArrayList<Streams> mStream, TextView stream_txtChannelName) {
        if (ChannelsRestFragment.getInstance() != null) {
            mChannelScheduler.setStreams(mStream);
            mListener.onFirstDataLoaded(listPosition, mChannelScheduler, mChannelScheduler.getSlug());
            stream_txtChannelName.setText(mChannelScheduler.getDescription());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScrollingFragmentInteractionListener) {
            mListener = (OnScrollingFragmentInteractionListener) context;
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
    public void onTimelineItemSelected(String uid) {
        mListener.onTimeLineSelected(uid, totalProgramList, mChannelScheduler);


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
    public interface OnScrollingFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFirstDataLoaded(int listPosition, ChannelScheduler uri, String slug);

        void onTimeLineSelected(String uid, ArrayList<ProgramList> mProgramList, ChannelScheduler channelScheduler);
    }

}
