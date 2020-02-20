package com.selecttvapp.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.presentation.activities.PresenterSearch;
import com.selecttvapp.presentation.views.ViewSearch;
import com.selecttvapp.ui.adapters.SearchResultsAdapter;
import com.selecttvapp.ui.bean.SearchResultBean;
import com.selecttvapp.ui.bean.SearchResultListBean;
import com.selecttvapp.ui.fragments.NetworkDetailsFragment;

import java.util.ArrayList;


public class SearchResultsActivity extends AppCompatActivity implements ViewSearch {
    public static final String SEARCH_LIST = "search_list";

    private static OnBackPressedListener onBackPressedListener;
    private PresenterSearch presenter;

    private ArrayList<SearchResultListBean> searchList = new ArrayList<>();

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private ImageView backButton;


    public static void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        SearchResultsActivity.onBackPressedListener = onBackPressedListener;
    }

    public static Intent getIntent(Activity activity, ArrayList<SearchResultListBean> searchList) {
        Intent intent = new Intent(activity, SearchResultsActivity.class);
        intent.putExtra(SEARCH_LIST, searchList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new PresenterSearch(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        smartTabLayout = (SmartTabLayout) findViewById(R.id.slidingTab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null)
            searchList = (ArrayList<SearchResultListBean>) getIntent().getSerializableExtra(SEARCH_LIST);// get search key word
//        if (savedInstanceState != null)
//            searchList = (ArrayList<SearchResultListBean>) savedInstanceState.getSerializable(SEARCH_LIST);

        if (searchList.size() > 0)
            onSuccess(searchList);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (searchList.size() > 0)
//            outState.putSerializable(SEARCH_LIST, searchList);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.SEARCH_RESULTS_SCREEN);
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onSuccess(ArrayList<SearchResultListBean> searchList) {
        viewPager.setOffscreenPageLimit(searchList.size());
        PresenterSearch.SectionsPagerAdapter sectionsPagerAdapter = presenter.getSectionsPagerAdapter(getSupportFragmentManager(), searchList);
        viewPager.setAdapter(sectionsPagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        presenter.setPageChangeListener(smartTabLayout);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void loadItem(SearchResultBean item) {

    }


    @SuppressLint("ValidFragment")
    public static class SearchResultsFragment extends Fragment implements ViewSearch {
        private final String CATEGORY_NAME = "category_name";
        private final String CATEGORY_LIST = "category_list";

        private PresenterSearch presenter;
        private RecyclerView recyclerView;
        private FrameLayout networkLayout;

        private ArrayList<SearchResultBean> categoryList = new ArrayList<>();
        private String categoryName;
        private int payMode = Constants.ALL;


        public SearchResultsFragment() {
            //Empty Contructor
        }

        @SuppressLint("ValidFragment")
        public static SearchResultsFragment newInstance(String name, ArrayList<SearchResultBean> categoryList) {
            SearchResultsFragment fragment = new SearchResultsFragment();
            fragment.categoryName = name;
            fragment.categoryList = categoryList;
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            presenter = new PresenterSearch(this);
            payMode = RabbitTvApplication.getInstance().getPaymode();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_search_results, container, false);
            networkLayout = (FrameLayout) view.findViewById(R.id.networkLayout);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            setRecyclerViewGridLayoutManager(recyclerView, 2);

//            if (savedInstanceState != null) {
//                categoryName = savedInstanceState.getString(CATEGORY_NAME);
//                categoryList = (ArrayList<SearchResultBean>) savedInstanceState.getSerializable(CATEGORY_LIST);
//            }

            if (categoryName != null && categoryList != null)
                showGrid(categoryName, categoryList);

            return view;
        }

//        @Override
//        public void onSaveInstanceState(Bundle outState) {
//            if (categoryName != null) {
//                outState.putString(CATEGORY_NAME, categoryName);
//                outState.putSerializable(CATEGORY_LIST, categoryList);
//            }
//            super.onSaveInstanceState(outState);
//        }

        @Override
        public void onSuccess(ArrayList<SearchResultListBean> searchList) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void loadItem(SearchResultBean item) {
            getChildFragmentManager().popBackStack();
            setViewType(VIEWTYPE_NETWORK_DETAIL);
            Fragment fragment = NetworkDetailsFragment.newInstance(item.getId() + "", Constants.ALL);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.replace(networkLayout.getId(), fragment, fragment.getClass().getName()).commit();
        }

        OnBackPressedListener onBackPressedListener = new OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                if (PREVIOUS_VIEW != -1)
                    setViewType(PREVIOUS_VIEW);
            }
        };

        private int PREVIOUS_VIEW = -1;
        private final int VIEWTYPE_LIST = 0;
        private final int VIEWTYPE_NETWORK_DETAIL = 1;

        private void setViewType(int viewType) {
            switch (viewType) {
                case VIEWTYPE_LIST:
                    setOnBackPressedListener(null);
                    PREVIOUS_VIEW = -1;
                    networkLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
                case VIEWTYPE_NETWORK_DETAIL:
                    setOnBackPressedListener(onBackPressedListener);
                    PREVIOUS_VIEW = VIEWTYPE_LIST;
                    recyclerView.setVisibility(View.GONE);
                    networkLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (networkLayout != null && recyclerView != null) {
                setViewType(VIEWTYPE_LIST);
            }
        }

        private void showGrid(String categoryName, ArrayList<SearchResultBean> categoryList) {
            if (categoryName.equalsIgnoreCase("TV Shows")) {
                setRecyclerViewGridLayoutManager(recyclerView, 5);
            } else if (categoryName.equalsIgnoreCase("Movies") || categoryName.equalsIgnoreCase("networks")) {
                setRecyclerViewGridLayoutManager(recyclerView, 5);
            } else if (categoryName.equalsIgnoreCase("TV Stations") || categoryName.equalsIgnoreCase("Channels") || categoryName.equalsIgnoreCase("Radio")) {
                setRecyclerViewGridLayoutManager(recyclerView, 5);
            } else {
                setRecyclerViewGridLayoutManager(recyclerView, 5);
            }

            SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity(), payMode, categoryList);
            adapter.setOnClickListener(presenter.getAdapterClickListener());
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        }

        private void setRecyclerViewGridLayoutManager(RecyclerView recyclerView, int spanCount) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }

    }//End of SearchResultsFragment

}
