package com.selecttvapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.callbacks.RadioListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.RadioDetailBean;
import com.selecttvapp.presentation.fragments.PresenterRadio;
import com.selecttvapp.presentation.views.ViewRadioListener;
import com.selecttvapp.service.RadioServiceNew;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.bean.ListenGridBean;

import java.util.ArrayList;

/**
 * to handle interaction events.
 * Use the {@link ListenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListenFragment extends Fragment implements ViewRadioListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";

    private PresenterRadio presenter;

    private LinearLayout linearRadioDetail;
    private TextView txtRadioBack;
    private RecyclerView recyclerRadioGenres, recyclerSubRadioGenres;
    private FrameLayout layoutRadioDetail;
    private View toolBar_search;

    public ListenFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListenFragment(View view) {
        this.toolBar_search = view;
    }

    public void setFocus(){
        toolBar_search=((HomeActivity)getActivity()).getFocusOnToolbarSearch();
        Utils.requestfocus(toolBar_search);
        toolBar_search.setNextFocusDownId(R.id.imageViewRadio);
    }

    public static ListenFragment newInstance(String param1, String param2) {
        ListenFragment fragment = new ListenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PresenterRadio(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private int getLayoutRadioDetailsId() {
        return R.id.layoutRadioDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listen, container, false);
        ((HomeActivity) getActivity()).setMlistenFragment(this);
        layoutRadioDetail = (FrameLayout) view.findViewById(getLayoutRadioDetailsId());
        recyclerRadioGenres = (RecyclerView) view.findViewById(R.id.fragmentlisten_grid);
        recyclerSubRadioGenres = (RecyclerView) view.findViewById(R.id.fragmentgridRadio);
        linearRadioDetail = (LinearLayout) view.findViewById(R.id.linearRadioDetail);
        setFocus();
        presenter.setGridLayoutManager(recyclerRadioGenres, 3);
        presenter.setGridLayoutManager(recyclerSubRadioGenres, 3);

        txtRadioBack = (TextView) view.findViewById(R.id.textRaioBack1);

        txtRadioBack.setOnClickListener(v -> {
            goback();

        });

       // view.setFocusableInTouchMode(true);
       // view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                if (PREVIOUS_VIEW_TYPE != -1) {
                    goback();
                    return true;
                }
            return false;
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadGenreList();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.LISTEN_SCREEN);
    }

    @Override
    public void loadGenreList(ArrayList<ListenGridBean> genreList) {
        recyclerRadioGenres.setVisibility(View.VISIBLE);
        recyclerSubRadioGenres.setVisibility(View.GONE);
        linearRadioDetail.setVisibility(View.GONE);
        txtRadioBack.setVisibility(View.GONE);

        if (genreList != null && genreList.size() > 0) {
            PresenterRadio.ListenGridAdapter listenGridAdapter = presenter.getListenGridAdapter(genreList);
            listenGridAdapter.setRadioListener(radioListener);
            recyclerRadioGenres.setAdapter(listenGridAdapter);
        }
    }

    @Override
    public void loadRadioSubList(ArrayList<RadioDetailBean> listItems) {
        try {
            if (listItems.size() <= 0)
                return;
            txtRadioBack.setText("Genre List");
            txtRadioBack.setVisibility(View.VISIBLE);

            PresenterRadio.ListenSubGridAdapter listenSubGridAdapter = presenter.getListenSubGridAdapter(listItems);
            listenSubGridAdapter.setRadioListener(radioListener);
            recyclerSubRadioGenres.setAdapter(listenSubGridAdapter);
            recyclerRadioGenres.setVisibility(View.GONE);
            recyclerSubRadioGenres.setVisibility(View.VISIBLE);
            linearRadioDetail.setVisibility(View.GONE);

            toolBar_search.setNextFocusDownId(R.id.textRaioBack1);
            txtRadioBack.setNextFocusDownId(R.id.imageViewSubRadio);
            txtRadioBack.setNextFocusUpId(R.id.activity_homescreen_toolbar_search);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showRadioDetails(RadioDetailBean radio) {
        switchFragment(radio);
    }

    private void switchFragment(RadioDetailBean radio) {
        getChildFragmentManager().popBackStack();
        RadioDetailsFragment fragment = RadioDetailsFragment.newInstance(radio);
        fragment.setBackPressedListener(() -> goback());

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.replace(getLayoutRadioDetailsId(), fragment).commitAllowingStateLoss();
        layoutRadioDetail.setVisibility(View.VISIBLE);

        PREVIOUS_VIEW_TYPE = RADIO_SUB_LIST;
        setViewType(RADIO_DETAIL);
    }

    private final int RADIO_HOME = 0;
    private final int RADIO_SUB_LIST = 1;
    private final int RADIO_DETAIL = 3;
    private int PREVIOUS_VIEW_TYPE = -1;

    private void goback() {
        if (PREVIOUS_VIEW_TYPE == RADIO_HOME) {
            toolBar_search.setNextFocusDownId(R.id.imageViewRadio);
//            showGenreList();
        } else if (PREVIOUS_VIEW_TYPE == RADIO_SUB_LIST) {
            if (RadioDetailsFragment.getInstance() != null)
                RadioDetailsFragment.getInstance().setRadioBar(new Intent(RadioServiceNew.RECIEVER_ACTION_CLOSE));
            Intent radioService = new Intent(getActivity(), RadioServiceNew.class);
            getActivity().stopService(radioService);
            getChildFragmentManager().popBackStack();
        }
        setViewType(PREVIOUS_VIEW_TYPE);
    }

    private void setViewType(int viewType) {
        switch (viewType) {
            case RADIO_HOME:
                PREVIOUS_VIEW_TYPE = -1;
                txtRadioBack.setVisibility(View.GONE);
                recyclerSubRadioGenres.setVisibility(View.GONE);
                linearRadioDetail.setVisibility(View.GONE);
                layoutRadioDetail.setVisibility(View.GONE);
                recyclerRadioGenres.setVisibility(View.VISIBLE);
                break;
            case RADIO_SUB_LIST:
                toolBar_search.setNextFocusDownId(R.id.textRaioBack1);
                txtRadioBack.setNextFocusDownId(R.id.imageViewSubRadio);
                PREVIOUS_VIEW_TYPE = RADIO_HOME;
                recyclerRadioGenres.setVisibility(View.GONE);
                linearRadioDetail.setVisibility(View.GONE);
                layoutRadioDetail.setVisibility(View.GONE);
                txtRadioBack.setVisibility(View.VISIBLE);
                recyclerSubRadioGenres.setVisibility(View.VISIBLE);
                break;
            case RADIO_DETAIL:
                recyclerRadioGenres.setVisibility(View.GONE);
                recyclerSubRadioGenres.setVisibility(View.GONE);
//                linearRadioDetail.setVisibility(View.VISIBLE);
//                txtRadioBack.setVisibility(View.VISIBLE);
                break;
        }
    }

    RadioListener radioListener = new RadioListener() {
        @Override
        public void onLoadRadioList() {
            PREVIOUS_VIEW_TYPE = -1;
            setViewType(RADIO_HOME);
        }

        @Override
        public void onLoadRadioSubList() {
            ArrayList<RadioDetailBean> emptyList = new ArrayList<>();
            PresenterRadio.ListenSubGridAdapter listenSubGridAdapter = presenter.getListenSubGridAdapter(emptyList);
            recyclerSubRadioGenres.setAdapter(listenSubGridAdapter);
            PREVIOUS_VIEW_TYPE = RADIO_HOME;
            setViewType(RADIO_SUB_LIST);
        }

        @Override
        public void onClickItem() {
            PREVIOUS_VIEW_TYPE = RADIO_SUB_LIST;
            setViewType(RADIO_DETAIL);
        }
    };

}
