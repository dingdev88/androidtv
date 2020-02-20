package com.selecttvapp.ondemandpage;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.demo.network.common.AppFonts;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.SlidersAndCarousels.CarouselsPagerFragment;
import com.selecttvapp.SlidersAndCarousels.SlidersPagerFragment;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.callbacks.GridViewListener;
import com.selecttvapp.callbacks.LoadMoreCarouselsListener;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.callbacks.OnDemandListener;
import com.selecttvapp.callbacks.OnDemandsListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.Network;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.RatingBean;
import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.adapters.GridAdapter;
import com.selecttvapp.ui.fragments.NetworkDetailsFragment;
import com.selecttvapp.ui.views.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Calendar;

public class OnDemandFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PAGETYPE = "page_type";
    private static final String ARG_PARAM_CATEGORYITEM = "category_item";
    private static final String ARG_PARAM_SUBCATEGORYITEM = "sub_category_item";
    private final int VIEWTYPE_HOME = 0;
    private final int VIEWTYPE_OFF = 100;
    private final int VIEWTYPE_VIEW_MORE = 1;
    private final int VIEWTYPE_GRID = 2;
    private final int VIEWTYPE_NETWORK_DETAIL = 3;
    // TODO: Rename and change types of parameters
    private String mPAGE_TYPE = "";
    private String mMAIN_CONTENT = "";
    private String mFIRST_SPINNER_ITEM = "";
    private String mSECOND_SPINNER_ITEM = "";
    private Typeface MYRIADPRO_BOLD, MYRIADPRO_ITALIC, MYRIADPRO_REGULAR, MYRIADPRO_SEMIBOLD;
    private FragmentManager fragmentManager;
    private FragmentOndemandMainBinding binding;
    private NonSwipeableViewPager nonSwipeableViewPager;
    private SmartTabLayout smartTabLayout;
    private FrameLayout carouselsLayout;
    private LinearLayout rootView;
    private LinearLayout upperLayout;
    private SectionPagerAdapter mSectionsPagerAdapter;
    private RecyclerView gridView;
    LoadMoreCarouselsListener loadMoreCarouselsListener = new LoadMoreCarouselsListener() {
        @Override
        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId) {
            if (gridView != null)
                if (gridView.getAdapter() != null)
                    if (gridView.getAdapter() instanceof GridAdapter) {
                        GridAdapter gridAdapter = (GridAdapter) gridView.getAdapter();
                        gridAdapter.setMoreData(listItems);
                    }
        }
    };
    private LinearLayout layoutTools;
    private LinearLayout layoutPayMode;
    private FrameLayout slidersLayout;
    private FrameLayout networkLayout;
    private FrameLayout gridListLayout;


    private Spinner mainSpinner, subSpinner;
    private ImageView switchImage;
    private TextView txtAll, txtFree;
    private TextView labelNoData;
    private ImageView PlaceHolderIMG;
    private ArrayList<OnDemandList> mCategorylist = new ArrayList<>();
    private ArrayList<RatingBean> ratingList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private boolean onSwitchMode = false;
    private int payMode = RabbitTvApplication.getInstance().getPaymode();
    private int pagePosition = 0;
    private int mHeight = 0;
    private int mWidth = 0;
    private int PREVIOUS_VIEW = -1;
    GridViewListener gridViewListener = (id, recall_method, recall_id) -> {
        loadNetworkDetails(id);
        PREVIOUS_VIEW = VIEWTYPE_VIEW_MORE;
//            setViewType(VIEWTYPE_NETWORK_DETAIL);
    };
    private int CURRENT_VIEW = -1;
    OnDemandListener onDemandListener = new OnDemandListener() {

        @Override
        public void onLoadedSlidersAndCarousels(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
            setSlider(sliders);
            setContent(carousels);
        }

        @Override
        public void onRatingListCallback(ArrayList<RatingBean> ratingList) {
            setRatingList(ratingList);
        }

        @Override
        public void onGridAdapterListCallback(ArrayList<CauroselsItemBean> listItems, String type) {
            setGridItems(type, listItems);
            setViewType(VIEWTYPE_GRID);
        }

        @Override
        public void onNetworkListCallback(ArrayList<CauroselsItemBean> listItems, String type) {
            setGridItems(type, listItems);
            setViewType(VIEWTYPE_GRID);
        }

        @Override
        public void onViewAllCallback(ArrayList<CauroselsItemBean> listItems, final String viewAllId) {
            final GridAdapter gridAdapter = setGridItems("", listItems);
            PREVIOUS_VIEW = VIEWTYPE_HOME;
            setViewType(VIEWTYPE_VIEW_MORE);
            if (gridAdapter != null) {
                gridAdapter.setRecyclerview(gridView);
                gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        int OFFSET = gridAdapter.getItemCount() - 1;
                        loadingViewAllData(viewAllId, true, OFFSET);
                    }
                });
            }
        }

        @Override
        public void onNetworkDataLoadedCallback(ArrayList<CauroselsItemBean> listItems, Network network) {
        }

        @Override
        public void onSecondSpinnerListCallback(ArrayList<CategoryBean> secondSpinnerListItems) {
            setSecondSpinnerData(secondSpinnerListItems);
        }

        @Override
        public void onLoadMoreItemsCallback(ArrayList<CauroselsItemBean> listItems) {
        }
    };
    private CategoryBean CURRENT_ITEM_MAIN_SPINNER;
    private CategoryBean CURRENT_ITEM_SUB_SPINNER;
    OnDemandsListener onDemandsListener = categoryList -> getActivity().runOnUiThread(() -> {
        mCategorylist = categoryList;
        loadCategories(mCategorylist);
    });
    Spinner.OnItemSelectedListener mainSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                CategoryBean categoryBean = (CategoryBean) mainSpinner.getSelectedItem();

                if (!mFIRST_SPINNER_ITEM.isEmpty()) {
                    for (int i = 0; i < mainSpinner.getAdapter().getCount(); i++) {
                        Object obj = mainSpinner.getItemAtPosition(i);
                        if (obj instanceof CategoryBean) {
                            CategoryBean sb1 = (CategoryBean) obj;
                            if (mFIRST_SPINNER_ITEM.equalsIgnoreCase(sb1.getSlug())) {
                                mFIRST_SPINNER_ITEM = "";
                                mainSpinner.setSelection(i);
                                return;
                            }
                        }
                    }
                }

                if (categoryBean != null && mFIRST_SPINNER_ITEM.isEmpty())
                    loadFirstSpinnerItem(categoryBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    Spinner.OnItemSelectedListener subSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                CategoryBean categoryBean = (CategoryBean) subSpinner.getSelectedItem();

                if (!mSECOND_SPINNER_ITEM.isEmpty()) {
                    for (int i = 0; i < subSpinner.getAdapter().getCount(); i++) {
                        Object obj = subSpinner.getItemAtPosition(i);
                        CategoryBean sb1 = (CategoryBean) obj;
                        if (mSECOND_SPINNER_ITEM.trim().equalsIgnoreCase(sb1.getName().trim()) || mSECOND_SPINNER_ITEM.trim().equalsIgnoreCase(sb1.getSlug().trim())
                                || mSECOND_SPINNER_ITEM.trim().equalsIgnoreCase((sb1.getId() + "").trim())) {
                            mSECOND_SPINNER_ITEM = "";
                            if (i != 0) {
                                subSpinner.setSelection(i);
                                return;
                            }
                        }
                    }
                }

                if (categoryBean != null)
                    loadSecondSpinnerItem(categoryBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private String CURRENT_VIEWMORE_ITEM = "";
    OnBackPressedListener onBackPressedListener = new OnBackPressedListener() {
        @Override
        public void onBackPressed() {
            if (PREVIOUS_VIEW != -1) {
                if (PREVIOUS_VIEW == VIEWTYPE_HOME)
                    if (mCategorylist.size() > pagePosition)
                        if (!OnDemandUtils.hasList(mCategorylist.get(pagePosition).getType()))
                            loadContent();

                setViewType(PREVIOUS_VIEW);

                if (onSwitchMode != switchImage.isSelected()) {  //back from network details to view more
                    onSwitchMode = switchImage.isSelected();
                    if (PREVIOUS_VIEW == VIEWTYPE_VIEW_MORE && !CURRENT_VIEWMORE_ITEM.isEmpty()) {
                        PREVIOUS_VIEW = VIEWTYPE_HOME;
                        loadingViewAllData(CURRENT_VIEWMORE_ITEM, false, 0);
                    }
                } else if (PREVIOUS_VIEW == VIEWTYPE_VIEW_MORE)
                    PREVIOUS_VIEW = VIEWTYPE_HOME;
            }
        }
    };
    CarouselsListener carouselsItemListener = new CarouselsListener() {
        @Override
        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String type) {
        }

        @Override
        public void onClickItem(HorizontalListitemBean item) {
            carouselsLayout.setVisibility(View.GONE);
            loadNetworkDetails(item.getId() + "");
            PREVIOUS_VIEW = VIEWTYPE_HOME;
//            setViewType(VIEWTYPE_NETWORK_DETAIL);
        }

        @Override
        public void viewMore(String value) {
            CURRENT_VIEWMORE_ITEM = value;
            setViewType(VIEWTYPE_OFF, true);
            loadingViewAllData(value, false, 0);
        }

    };

    public OnDemandFragment() {
    }

    public static OnDemandFragment newInstance(String pageTye, String categoryItem, String subCategoryItem) {
        OnDemandFragment fragment = new OnDemandFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_PAGETYPE, pageTye);
        bundle.putString(ARG_PARAM_CATEGORYITEM, categoryItem);
        bundle.putString(ARG_PARAM_SUBCATEGORYITEM, subCategoryItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        OnDemandRPCAPI.activity = getActivity();
        MYRIADPRO_BOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_BOLD);
        MYRIADPRO_ITALIC = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_ITALIC);
        MYRIADPRO_REGULAR = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_REGULAR);
        MYRIADPRO_SEMIBOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_SEMIBOLD);

        if (getArguments() != null) {
            mPAGE_TYPE = getArguments().getString(ARG_PARAM_PAGETYPE);
            mMAIN_CONTENT = getArguments().getString(ARG_PARAM_CATEGORYITEM);
            mSECOND_SPINNER_ITEM = getArguments().getString(ARG_PARAM_SUBCATEGORYITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ondemand_main, container, false);
        binding = new FragmentOndemandMainBinding();
        binding.bind(view);
        rootView = (LinearLayout) view.findViewById(R.id.rootView);
        upperLayout = (LinearLayout) view.findViewById(R.id.upperLayout);
        nonSwipeableViewPager = (NonSwipeableViewPager) view.findViewById(R.id.pager);
        nonSwipeableViewPager.setPagingEnabled(false);
        smartTabLayout = (SmartTabLayout) view.findViewById(R.id.maintab);
        smartTabLayout.setFocusableInTouchMode(true);
        carouselsLayout = (FrameLayout) view.findViewById(R.id.carouselsLayout);
        carouselsLayout.setVisibility(View.GONE);

        setView(view);
        //change layout view orientation
        onConfigurationChanged(getResources().getConfiguration());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null)
            fragmentManager = getFragmentManager();
        if (mPAGE_TYPE.equalsIgnoreCase("payperview")) {
            payMode = Constants.PAID;
            loadPayperViewCategories();
        } else {
            loadDemandCategories();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.ON_DEMAND_SCREEN);
    }

    private void setOnPageChangeListener() {
        final LinearLayout lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        nonSwipeableViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                popBackStack();
                changeTabsTitleTypeFace(lyTabs, position);
                if (switchImage != null)
                    onSwitchMode = switchImage.isSelected();
                if (CURRENT_VIEW == VIEWTYPE_NETWORK_DETAIL)
                    layoutTools.setVisibility(View.VISIBLE);
                setViewType(VIEWTYPE_OFF);
                loadContent();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            tvTabTitle.setTypeface(MYRIADPRO_REGULAR);
            if (j == position) tvTabTitle.setTypeface(MYRIADPRO_BOLD);
        }
    }

    private void loadCategories(final ArrayList<OnDemandList> mOnDemandCategorylist) {
        try {
            if (mOnDemandCategorylist != null && mOnDemandCategorylist.size() > 0) {
                nonSwipeableViewPager.setOffscreenPageLimit(mOnDemandCategorylist.size());
                mSectionsPagerAdapter = new SectionPagerAdapter(getActivity().getSupportFragmentManager(), mOnDemandCategorylist);
                nonSwipeableViewPager.setAdapter(mSectionsPagerAdapter);
                smartTabLayout.setViewPager(nonSwipeableViewPager);
                setOnPageChangeListener();
                loadContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPayperViewCategories() {
        mCategorylist.add(new OnDemandList("", "Movies", Constants.PPV_MOVIES, ""));
        mCategorylist.add(new OnDemandList("", "TV Shows", Constants.PPV_SHOWS, ""));
        loadCategories(mCategorylist);
    }// end of the loadPayperViewCategories() method

    private void loadDemandCategories() {
        try {
            if (RabbitTvApplication.getInstance().getmOnDemandCategorylist() != null && RabbitTvApplication.getInstance().getmOnDemandCategorylist().size() > 0) {
                mCategorylist = RabbitTvApplication.getInstance().getmOnDemandCategorylist();
                loadCategories(mCategorylist);
            } else {
                OnDemandRPCAPI.loadDemandMenuCategories(onDemandsListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }// end of loadDemandCategories() method

    public void setRatingList(ArrayList<RatingBean> ratingList) {
        this.ratingList = ratingList;
    }

    private void setView(View view) {
        layoutTools = (LinearLayout) view.findViewById(R.id.layoutTools);
        layoutPayMode = (LinearLayout) view.findViewById(R.id.layoutPayMode);
        slidersLayout = (FrameLayout) view.findViewById(R.id.slidersLayout);
        slidersLayout.setVisibility(View.GONE);
        networkLayout = (FrameLayout) view.findViewById(R.id.networkLayout);
        gridListLayout = (FrameLayout) view.findViewById(R.id.gridListLayout);
        mainSpinner = (Spinner) view.findViewById(R.id.mainSpinner);
        subSpinner = (Spinner) view.findViewById(R.id.subSpinner);
        switchImage = (ImageView) view.findViewById(R.id.switchImage);
        txtAll = (TextView) view.findViewById(R.id.txtAll);
        txtFree = (TextView) view.findViewById(R.id.txtFree);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        gridView = (RecyclerView) view.findViewById(R.id.gridView);
        PlaceHolderIMG = view.findViewById(R.id.PlaceHolderIMG3);
        mainSpinner.setOnItemSelectedListener(mainSpinnerSelectedListener);
        subSpinner.setOnItemSelectedListener(subSpinnerSelectedListener);

        if (mPAGE_TYPE.equalsIgnoreCase("payperview"))
            payMode = Constants.PAID;

        if (payMode == Constants.PAID)
            layoutPayMode.setVisibility(View.GONE);
        else if (payMode == Constants.FREE) {
            onSwitchMode = true;
            onModeSwitchImage();
        } else if (payMode == Constants.ALL) {
            onSwitchMode = false;
            offModeSwitchImage();
        }

        switchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RabbitTvApplication.getInstance().getSliderBeanHashMap().clear();
                RabbitTvApplication.getInstance().getHorizontalBeanHashMap().clear();
                popBackStack();
                if (v.isSelected())
                    offModeSwitchImage();
                else
                    onModeSwitchImage();

                if (CURRENT_VIEW == VIEWTYPE_VIEW_MORE && !CURRENT_VIEWMORE_ITEM.isEmpty())
                    loadingViewAllData(CURRENT_VIEWMORE_ITEM, false, 0);
                else if (CURRENT_ITEM_SUB_SPINNER != null)
                    loadSecondSpinnerItem(CURRENT_ITEM_SUB_SPINNER);
                else if (CURRENT_ITEM_MAIN_SPINNER != null)
                    loadFirstSpinnerItem(CURRENT_ITEM_MAIN_SPINNER);
                else
                    loadContent();
            }
        });
    }

    private void onModeSwitchImage() {
        RabbitTvApplication.getInstance().setPaymode(Constants.FREE);
        switchImage.setSelected(true);
        payMode = Constants.FREE;
        switchImage.setImageResource(R.drawable.on);
        txtAll.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
        txtFree.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    private void offModeSwitchImage() {
        RabbitTvApplication.getInstance().setPaymode(Constants.ALL);
        switchImage.setSelected(false);
        payMode = Constants.ALL;
        switchImage.setImageResource(R.drawable.off);
        txtAll.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        txtFree.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            if (payMode != Constants.PAID)
                if (payMode != RabbitTvApplication.getInstance().getPaymode()) {
                    payMode = RabbitTvApplication.getInstance().getPaymode();
                    if (payMode == Constants.FREE) {
                        onSwitchMode = true;
                        onModeSwitchImage();
                    } else if (payMode == Constants.ALL) {
                        onSwitchMode = false;
                        offModeSwitchImage();
                    }

                    if (CURRENT_VIEW == VIEWTYPE_VIEW_MORE && !CURRENT_VIEWMORE_ITEM.isEmpty())
                        loadingViewAllData(CURRENT_VIEWMORE_ITEM, false, 0);
                    else if (CURRENT_ITEM_SUB_SPINNER != null)
                        loadSecondSpinnerItem(CURRENT_ITEM_SUB_SPINNER);
                    else if (CURRENT_ITEM_MAIN_SPINNER != null)
                        loadFirstSpinnerItem(CURRENT_ITEM_MAIN_SPINNER);
                    else
                        loadContent();
                }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getActivity() == null)
            return;
        displayMetrics();
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView.setOrientation(LinearLayout.VERTICAL);
            ((LinearLayout.LayoutParams) upperLayout.getLayoutParams()).weight = 0;
            ((LinearLayout.LayoutParams) upperLayout.getLayoutParams()).setMargins(0, 0, 0, 0);
            slidersLayout.getLayoutParams().height = (mHeight / 100) * 35;
        } else {
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout.LayoutParams) upperLayout.getLayoutParams()).weight = 1;
            ((LinearLayout.LayoutParams) upperLayout.getLayoutParams()).setMargins(0, 0, 5, 0);
            slidersLayout.getLayoutParams().height = 500;
        }
    }

    private void setSlider(ArrayList<Slider> sliders) {
        if (nonSwipeableViewPager.getCurrentItem() != pagePosition)
            return;
        try {
            if (sliders != null)
                if (sliders.size() > 0) {
                    if (sliders.size() > 10)
                        sliders.addAll(sliders.subList(0, 10));

                    Fragment fragment = SlidersPagerFragment.newInstance(pagePosition, payMode, sliders);
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                    fragmentTransaction.replace(slidersLayout.getId(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                    slidersLayout.setVisibility(View.VISIBLE);
                } else slidersLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setContent(ArrayList<Carousel> carousels) {
        if (getActivity() == null)
            return;

        if (nonSwipeableViewPager.getCurrentItem() != pagePosition)
            return;

        if (carousels.size() > 0) {
            Fragment fragment = CarouselsPagerFragment.newInstance(pagePosition, payMode, carousels, carouselsItemListener);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.replace(carouselsLayout.getId(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            carouselsLayout.setVisibility(View.VISIBLE);
        } else carouselsLayout.setVisibility(View.GONE);
    }

    public GridAdapter setGridItems(final String type, final ArrayList<CauroselsItemBean> listItems) {
        GridAdapter gridAdapter = null;
        String mType = type;
        if (listItems.size() > 0) {
            if (type.isEmpty())
                mType = listItems.get(0).getType();

            if (mType.equalsIgnoreCase("show") || mType.equalsIgnoreCase("s"))
                setGridLayoutManager(gridView, 2);
            else setGridLayoutManager(gridView, 3);

            gridAdapter = new GridAdapter(listItems, payMode, getActivity(), gridViewListener);
            this.gridView.setAdapter(gridAdapter);
            this.gridView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
            labelNoData.setVisibility(View.VISIBLE);
        }

//        Fragment fragment = GridListFragment.newInstance(payMode, type, listItems, gridViewListener);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
////        fragments.add(fragment);
//        fragmentTransaction.replace(gridListLayout.getId(), fragment).commit();

        return gridAdapter;
    }

    private void loadNetworkDetails(String id) {
        HomeActivity.setOnBackPressedListener(onBackPressedListener);
        Fragment fragment = NetworkDetailsFragment.newInstance(id, Constants.ALL);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.replace(networkLayout.getId(), fragment, NetworkDetailsFragment.TAG).commit();
        networkLayout.setVisibility(View.VISIBLE);
    }

    private void setSecondSpinnerData(final ArrayList<CategoryBean> secondSpinnerListItems) {
        if (secondSpinnerListItems.size() > 0) {
            subSpinner.setVisibility(View.VISIBLE);
            binding.subSpinnerContainer.setVisibility(View.VISIBLE);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), secondSpinnerListItems);
            subSpinner.setAdapter(spinnerAdapter);
        }
    }

    private void setFirstSpinnerData(final ArrayList<CategoryBean> firstSpinnerListItems) {
        if (firstSpinnerListItems.size() > 0) {
            mainSpinner.setVisibility(View.VISIBLE);
            binding.mainSpinnerContainer.setVisibility(View.VISIBLE);
            subSpinner.setVisibility(View.GONE);
            binding.subSpinnerContainer.setVisibility(View.INVISIBLE);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), firstSpinnerListItems);
            mainSpinner.setAdapter(spinnerAdapter);
        }
    }

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setViewType(int viewType) {
        setViewType(viewType, false);
    }

    private void setViewType(int viewType, boolean onPage) {
        CURRENT_VIEW = viewType;
        switch (viewType) {
            case VIEWTYPE_HOME:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                networkLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                gridListLayout.setVisibility(View.GONE);
                slidersLayout.setVisibility(View.VISIBLE);
                layoutTools.setVisibility(View.VISIBLE);
                carouselsLayout.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_GRID:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                networkLayout.setVisibility(View.GONE);
                slidersLayout.setVisibility(View.GONE);
                carouselsLayout.setVisibility(View.GONE);
                layoutTools.setVisibility(View.VISIBLE);
                gridListLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_VIEW_MORE:
                HomeActivity.setOnBackPressedListener(onBackPressedListener);
                networkLayout.setVisibility(View.GONE);
                slidersLayout.setVisibility(View.GONE);
                carouselsLayout.setVisibility(View.GONE);
                gridListLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                layoutTools.setVisibility(View.VISIBLE);
                break;
//            case VIEWTYPE_NETWORK_DETAIL:
//                HomeActivity.setOnBackPressedListener(onBackPressedListener);
//                slidersLayout.setVisibility(View.GONE);
//                layoutTools.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
//                gridListLayout.setVisibility(View.GONE);
//                carouselsLayout.setVisibility(View.GONE);
//                networkLayout.setVisibility(View.VISIBLE);
//                break;
            case VIEWTYPE_OFF:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                if (!onPage) {
                    mainSpinner.setVisibility(View.GONE);
                    binding.mainSpinnerContainer.setVisibility(View.INVISIBLE);
                    subSpinner.setVisibility(View.GONE);
                    binding.subSpinnerContainer.setVisibility(View.INVISIBLE);
                }
                networkLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                gridListLayout.setVisibility(View.GONE);
                slidersLayout.setVisibility(View.GONE);
                carouselsLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void payModeVisibility(int visibility) {
        if (payMode == Constants.PAID)
            layoutPayMode.setVisibility(View.GONE);
        else layoutPayMode.setVisibility(visibility);
    }

    private void displayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;
    }

    private void popBackStack() {
        getChildFragmentManager().popBackStack();
        getFragmentManager().popBackStack();
    }

    private void loadContent() {
        try {
            popBackStack();
            setViewType(VIEWTYPE_OFF);
            String categoryTag = "";
            pagePosition = nonSwipeableViewPager.getCurrentItem();
            CURRENT_ITEM_MAIN_SPINNER = null;
            CURRENT_ITEM_SUB_SPINNER = null;

            if (!mMAIN_CONTENT.isEmpty()) {
                String[] strings = mMAIN_CONTENT.split("/");

                switch (strings[0]) {
                    case "movies":
                    case "movies-recommended":
                        categoryTag = Constants.MOVIES;
                        break;
                    case "shows":
                    case "shows-recommended":
                        categoryTag = Constants.TV_SHOWS;
                        break;
                    case "ppv-movies":
                    case "pay-per-view-movies":
                        categoryTag = Constants.PPV_MOVIES;
                        break;
                    case "pay-per-view-shows":
                        categoryTag = Constants.PPV_SHOWS;
                        break;
                }

                for (int i = 0; i < mCategorylist.size(); i++)
                    if (categoryTag.equalsIgnoreCase(mCategorylist.get(i).getType())) {
                        mMAIN_CONTENT = "";
                        if (i != 0) {
                            nonSwipeableViewPager.setCurrentItem(i, true);
                            return;
                        }
                    }
            } else {
                categoryTag = mCategorylist.get(pagePosition).getType();
            }

            if (categoryTag.equalsIgnoreCase(Constants.NETWORKS)) {
                payModeVisibility(View.GONE);
                layoutTools.setVisibility(View.GONE);
                PlaceHolderIMG.setVisibility(View.GONE);
            } else {
                payModeVisibility(View.VISIBLE);
                layoutTools.setVisibility(View.VISIBLE);
                PlaceHolderIMG.setVisibility(View.VISIBLE);
            }

            switch (categoryTag) {
                case Constants.FEATURED:
                    binding.mainSpinnerContainer.setVisibility(View.INVISIBLE);
                    binding.subSpinnerContainer.setVisibility(View.INVISIBLE);
                    loadFeaturedContent();
                    break;
                case Constants.TV_SHOWS:
                    setFirstSpinnerData(OnDemandUtils.getTVshowSubCategories());
                    break;
                case Constants.PRIMETIME:
                    loadPrimeTime();
                    break;
                case Constants.NETWORKS:
                    binding.mainSpinnerContainer.setVisibility(View.GONE);
                    binding.subSpinnerContainer.setVisibility(View.GONE);
                    PlaceHolderIMG.setVisibility(View.GONE);
                    loadNetworks();
                    break;
                case Constants.MOVIES:
                    setFirstSpinnerData(OnDemandUtils.getMoviesSubCategories());
                    break;
                case Constants.WEB_ORIGINALS:
                    binding.mainSpinnerContainer.setVisibility(View.INVISIBLE);
                    binding.subSpinnerContainer.setVisibility(View.INVISIBLE);
                    loadWebOriginals();
                    break;
                case Constants.KIDS:
                    binding.mainSpinnerContainer.setVisibility(View.INVISIBLE);
                    binding.subSpinnerContainer.setVisibility(View.INVISIBLE);
                    loadKids();
                    break;
                case Constants.PPV_SHOWS:
                    setFirstSpinnerData(OnDemandUtils.getPPVTVshowSubCategories());
                    break;
                case Constants.PPV_MOVIES:
                    setFirstSpinnerData(OnDemandUtils.getPPVMoviesSubCategories());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFirstSpinnerItem(CategoryBean sb) {
        popBackStack();
        CURRENT_ITEM_SUB_SPINNER = null;
        gridView.setVisibility(View.GONE);
        carouselsLayout.setVisibility(View.GONE);
        slidersLayout.setVisibility(View.GONE);
        subSpinner.setVisibility(View.GONE);
        binding.subSpinnerContainer.setVisibility(View.INVISIBLE);

        CURRENT_ITEM_MAIN_SPINNER = sb;
        String slug = sb.getSlug();
        if (!slug.equalsIgnoreCase(Constants.SHOW_SUB_NETWORKS))
            payModeVisibility(View.VISIBLE);

        switch (slug) {
            case Constants.TV_SHOWS:
                loadShows();
                break;
            case Constants.PPV_SHOWS:
                loadPPVShows();
                break;
            case Constants.PPV_MOVIES:
                loadPPVMovies();
                break;
            case Constants.MOVIES:
                loadMovies();
                break;
            case Constants.SHOW_SUB_NETWORKS:
                payModeVisibility(View.GONE);
                loadNetworks();
                break;
            case Constants.SHOW_SUB_CATEGORY:
                LoadingCategoryList();
                break;
            case Constants.SHOW_SUB_GENRE:
                loadingMovieGenre("show");
                break;
            case Constants.SHOW_SUB_DECADE:
                loadingDecadeList();
                break;
            case Constants.MOVIE_SUB_GENRE:
                loadingMovieGenre("movie");
                break;
            case Constants.MOVIE_SUB_RATING:
                loadingMovieRatingList();
                break;
            case Constants.PRIMETIME:
                String selected_day = sb.getName();
                String dayofweek = "";
                switch (selected_day.toLowerCase()) {
                    case "saturday":
                        dayofweek = "sat";
                        break;
                    case "sunday":
                        dayofweek = "sun";
                        break;
                    case "monday":
                        dayofweek = "mon";
                        break;
                    case "tuesday":
                        dayofweek = "tue";
                        break;
                    case "wednesday":
                        dayofweek = "wed";
                        break;
                    case "thursday":
                        dayofweek = "thu";
                        break;
                    case "friday":
                        dayofweek = "fri";
                        break;
                }
                loadPrimetimeData(dayofweek);
                break;
        }
    }

    private void loadSecondSpinnerItem(CategoryBean sb) {
        gridView.setVisibility(View.GONE);
        CURRENT_ITEM_SUB_SPINNER = sb;
        String slug = sb.getSlug();

        switch (slug) {
            case Constants.TV_SHOWS:
                break;
            case Constants.SHOW_SUB_NETWORKS:
                break;
            case Constants.SHOW_SUB_CATEGORY:
                loadingCategoryItems(sb.getId() + "");
                break;
            case Constants.SHOW_SUB_GENRE:
                loadingTVShowsByGenre("" + sb.getId(), "show");
                break;
            case Constants.SHOW_SUB_DECADE:
                LoadingTVShowsByDecade(sb.getId() + "");
                break;
            case Constants.MOVIE_SUB_GENRE:
                loadingTVShowsByGenre("" + sb.getId(), "movie");
                break;

            case Constants.MOVIE_SUB_RATING:
                try {
                    String rat_slug = "";
                    for (int i = 0; i < ratingList.size(); i++) {
                        if (sb.getId() == ratingList.get(i).getId()) {
                            rat_slug = ratingList.get(i).getSlug();
                        }
                    }
                    loadingTVShowsByRating("" + rat_slug);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void loadFeaturedContent() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent(Constants.FEATURED, onDemandListener);
        if (!success)
            OnDemandRPCAPI.loadingFeaturedData(getActivity(), payMode, onDemandListener);
    }

    private void loadShows() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent("tvshows", onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingTVshowData(getActivity(), payMode, onDemandListener);
    }

    private void loadPrimeTime() {
        try {
            setFirstSpinnerData(OnDemandUtils.getPrimeTimeSubCategories());
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case Calendar.SUNDAY:
                    mainSpinner.setSelection(4);
                    break;
                case Calendar.MONDAY:
                    mainSpinner.setSelection(5);
                    break;
                case Calendar.TUESDAY:
                    mainSpinner.setSelection(6);
                    break;
                case Calendar.WEDNESDAY:
                    mainSpinner.setSelection(0);
                    break;
                case Calendar.THURSDAY:
                    mainSpinner.setSelection(1);
                    break;
                case Calendar.FRIDAY:
                    mainSpinner.setSelection(2);
                    break;
                case Calendar.SATURDAY:
                    mainSpinner.setSelection(3);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// end of loadPrimeTime() method

    private void loadPrimetimeData(final String day) {
        OnDemandRPCAPI.loadingPrimetime(getActivity(), day, payMode, onDemandListener);
    }

    private void loadMovies() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent("movies", onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingMovies(getActivity(), payMode, onDemandListener);
    }

    private void loadWebOriginals() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent(Constants.WEB_ORIGINALS, onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingWebcarouselData(getActivity(), payMode, onDemandListener);
    }

    private void loadKids() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent(Constants.KIDS, onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingKidscarouselData(getActivity(), payMode, onDemandListener);
    }

    private void loadPPVShows() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent("ppvtvshows", onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingPPVTVshowData(getActivity(), onDemandListener);
    }

    private void loadPPVMovies() {
        boolean success = OnDemandRPCAPI.loadOnDemandContent("ppvtvmovies", onDemandListener);
        if (!success)
            OnDemandRPCAPI.LoadingPPVTMovieData(getActivity(), onDemandListener);
    }

    private void LoadingCategoryList() {
        OnDemandRPCAPI.loadingCategoryList(onDemandListener);
    }

    private void loadingMovieGenre(final String key) {
        OnDemandRPCAPI.loadingMovieGenre(key, onDemandListener);
    }

    private void loadingDecadeList() {
        OnDemandRPCAPI.loadingDecadeList(onDemandListener);
    }

    private void loadingMovieRatingList() {
        OnDemandRPCAPI.loadingMovieRatingList(onDemandListener);
    }

    private void loadingCategoryItems(final String id) {
        OnDemandRPCAPI.loadingCategoryItems(getActivity(), id, payMode, onDemandListener);
    }

    private void loadingTVShowsByGenre(final String genreId, final String type) {
        OnDemandRPCAPI.loadingTVShowsByGenre(genreId, type, payMode, 100, 0, false, new CarouselsListener() {
            @Override
            public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, final String type) {
                setViewType(VIEWTYPE_GRID);
                final GridAdapter gridAdapter = setGridItems(type, listItems);
                if (gridAdapter != null) {
                    gridAdapter.setRecyclerview(gridView);
                    gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            int OFFSET = gridAdapter.getItemCount() - 1;
                            OnDemandRPCAPI.loadingTVShowsByGenre(genreId, type, payMode, 100, OFFSET, true, null, loadMoreCarouselsListener);
                        }
                    });
                }
            }

            @Override
            public void onClickItem(HorizontalListitemBean item) {
            }

            @Override
            public void viewMore(String value) {
            }
        }, null);
    }

    private void loadingTVShowsByRating(final String slug) {
        OnDemandRPCAPI.loadingTVShowsByRating(slug, "M", payMode, 100, 0, false, new CarouselsListener() {
            @Override
            public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String type) {
                setViewType(VIEWTYPE_GRID);
                final GridAdapter gridAdapter = setGridItems(type, listItems);
                if (gridAdapter != null) {
                    gridAdapter.setRecyclerview(gridView);
                    gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            int OFFSET = gridAdapter.getItemCount() - 1;
                            OnDemandRPCAPI.loadingTVShowsByRating(slug, "M", payMode, 100, OFFSET, true, null, loadMoreCarouselsListener);
                        }
                    });
                }
            }

            @Override
            public void onClickItem(HorizontalListitemBean item) {

            }

            @Override
            public void viewMore(String value) {

            }
        }, null);
    }

    private void LoadingTVShowsByDecade(final String genreId) {
        OnDemandRPCAPI.LoadingTVShowsByDecade(genreId, payMode, "show", 100, 0, false, new CarouselsListener() {
            @Override
            public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String type) {
                setViewType(VIEWTYPE_GRID);
                final GridAdapter gridAdapter = setGridItems(type, listItems);
                if (gridAdapter != null) {
                    gridAdapter.setRecyclerview(gridView);
                    gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            int OFFSET = gridAdapter.getItemCount() - 1;
                            OnDemandRPCAPI.LoadingTVShowsByDecade(genreId, payMode, "show", 100, OFFSET, true, null, loadMoreCarouselsListener);
                        }
                    });
                }
            }

            @Override
            public void onClickItem(HorizontalListitemBean item) {

            }

            @Override
            public void viewMore(String value) {

            }
        }, null);
    }

    private void loadNetworks() {
        OnDemandRPCAPI.loadingNetworkList(onDemandListener);
    }

    private void loadNetworkData(final String id) {
        OnDemandRPCAPI.loadNetworkData(id, payMode, 100, 0, false, null, null);
    }

    private void loadingViewAllData(final String id, boolean canLoadMore, int OFFSET) {
        OnDemandRPCAPI.loadingViewAllData(id, payMode, 100, OFFSET, canLoadMore, onDemandListener, loadMoreCarouselsListener);
    }

    private class SectionPagerAdapter extends PagerAdapter {
        private ArrayList<OnDemandList> mOnDemandCategorylist;

        public SectionPagerAdapter(FragmentManager fm, ArrayList<OnDemandList> mOnDemandCategorylist) {
            this.mOnDemandCategorylist = mOnDemandCategorylist;
        }

        @Override
        public View instantiateItem(ViewGroup collection, int position) {
            TextView textView = new TextView(getActivity());
            return textView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mOnDemandCategorylist.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mOnDemandCategorylist.get(position).getName();
        }
    }

    private class FragmentOndemandMainBinding {
        public CardView mainSpinnerContainer, subSpinnerContainer;

        public void bind(View view) {
            mainSpinnerContainer = (CardView) view.findViewById(R.id.mainSpinnerContainer);
            subSpinnerContainer = (CardView) view.findViewById(R.id.subSpinnerContainer);

        }
    }

}
