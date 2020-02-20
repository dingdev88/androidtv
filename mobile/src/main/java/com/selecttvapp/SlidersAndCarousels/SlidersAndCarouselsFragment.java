package com.selecttvapp.SlidersAndCarousels;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.network.common.AppFonts;
import com.selecttvapp.R;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.callbacks.GridViewListener;
import com.selecttvapp.callbacks.LoadMoreCarouselsListener;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.personalization.PersonalizationDialogFragment;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.ui.WebView;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.activities.WebBrowserActivity;
import com.selecttvapp.ui.adapters.GridAdapter;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.NetworkDetailsFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Sep-17.
 */

public class SlidersAndCarouselsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_POSITION = "page_position";
    private static final String ARG_PARAM_PAYMODE = "paymode";
    private static final String ARG_PARAM_PAGE_TYPE = "page_type";
    // TODO: Rename and change types of parameters

    private String mParam2 = "";
    private Typeface MYRIADPRO_BOLD, MYRIADPRO_ITALIC, MYRIADPRO_REGULAR, MYRIADPRO_SEMIBOLD;

    private ProgressHUD progressHUD;
    private Fragment fragment;

    private LinearLayout rootView;
    private LinearLayout upperLayout;
    private FrameLayout slidersLayout;
    private FrameLayout carouselsLayout;
    private FrameLayout networkLayout;
    private RecyclerView gridView;
    private TextView labelNoData;
    private CardView layoutPersonalize;
    private TextView btnPersonalize;

    private ArrayList<Carousel> carousels = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();

    private int payMode = Constants.ALL;
    private int mHeight = 0;
    private int mWidth = 0;
    private int PAGE_TYPE = -1;
    private int mPagePosition = 0;


    public SlidersAndCarouselsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnDemandSuggestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlidersAndCarouselsFragment newInstance(String param1, String param2) {
        SlidersAndCarouselsFragment fragment = new SlidersAndCarouselsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SlidersAndCarouselsFragment newInstance(int pagePosition, int payMode, int pageType, ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        SlidersAndCarouselsFragment fragment = new SlidersAndCarouselsFragment();
        fragment.sliders = sliders;
        fragment.carousels = carousels;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POSITION, pagePosition);
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        args.putInt(ARG_PARAM_PAGE_TYPE, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        MYRIADPRO_BOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_BOLD);
        MYRIADPRO_ITALIC = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_ITALIC);
        MYRIADPRO_REGULAR = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_REGULAR);
        MYRIADPRO_SEMIBOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_SEMIBOLD);
        if (getArguments() != null) {
            mPagePosition = getArguments().getInt(ARG_PARAM_POSITION);
            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
            PAGE_TYPE = getArguments().getInt(ARG_PARAM_PAGE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sliders_and_carousels, container, false);
        rootView = (LinearLayout) view.findViewById(R.id.rootView);
        upperLayout = (LinearLayout) view.findViewById(R.id.upperLayout);
        layoutPersonalize = (CardView) view.findViewById(R.id.layoutPersonalize);
        btnPersonalize = (TextView) view.findViewById(R.id.btnPersonalize);
        slidersLayout = (FrameLayout) view.findViewById(R.id.slidersLayout);
        carouselsLayout = (FrameLayout) view.findViewById(R.id.carouselsLayout);
        networkLayout = (FrameLayout) view.findViewById(R.id.networkLayout);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        gridView = (RecyclerView) view.findViewById(R.id.gridView);
        setGridLayoutManager(gridView, 4);

        /*personalize button visibility*/
//        if (PAGE_TYPE == Constants.VIEW_ONDEMAND_SUGGESTIONS)
//            layoutPersonalize.setVisibility(View.VISIBLE);
        onConfigurationChanged(getResources().getConfiguration());

        btnPersonalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalizationDialogFragment customDialogFragment = new PersonalizationDialogFragment();
                customDialogFragment.show(getActivity().getSupportFragmentManager(), PersonalizationDialogFragment.TAG);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (sliders != null)
            if (sliders.size() > 0)
                setSlider(sliders);
        if (carousels != null)
            if (carousels.size() > 0)
                setContent(carousels);
    }


    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.SLIDERS_AND_CAROUSELS_SCREEN);
    }

    OnBackPressedListener onBackPressedListener = new OnBackPressedListener() {
        @Override
        public void onBackPressed() {
            if (PREVIOUS_VIEW != -1) {
                setViewType(PREVIOUS_VIEW);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HomeActivity.setOnBackPressedListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            loadPortView();
        else loadLandView();
    }

    private void loadPortView() {
        rootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 0;
        upperLayout.setLayoutParams(layoutParams);
        slidersLayout.getLayoutParams().height = 500;
        slidersLayout.setVisibility(slidersLayout.getVisibility());
    }

    private void loadLandView() {
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        upperLayout.setLayoutParams(layoutParams);
        slidersLayout.getLayoutParams().height = 500;
    }

    private void setSlider(ArrayList<Slider> sliders) {
        try {
            if (sliders != null)
                if (sliders.size() > 0) {
                    if (sliders.size() > 10) {
                        sliders.addAll(sliders.subList(0, 10));
                    }
                    slidersLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = SlidersPagerFragment.newInstance(mPagePosition, payMode, sliders);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(slidersLayout.getId(), fragment).commit();
                } else slidersLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setContent(ArrayList<Carousel> horizontalListdata) {
        if (horizontalListdata != null)
            if (horizontalListdata.size() > 0) {
                carouselsLayout.setVisibility(View.VISIBLE);
                Fragment fragment = CarouselsPagerFragment.newInstance(mPagePosition, payMode, horizontalListdata, carouselsListener);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(carouselsLayout.getId(), fragment).commit();
            } else
                carouselsLayout.setVisibility(View.GONE);
    }

    public GridAdapter setGridItems(final String type, final ArrayList<CauroselsItemBean> listItems) {
        GridAdapter gridAdapter = null;
        if (listItems.size() > 0) {
            if (type.equalsIgnoreCase("show") || type.equalsIgnoreCase("s"))
                setGridLayoutManager(gridView, 4);
            else setGridLayoutManager(gridView, 4);

            gridAdapter = new GridAdapter(listItems, payMode, getActivity(), gridViewListener);
            this.gridView.setAdapter(gridAdapter);
            this.gridView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
            labelNoData.setVisibility(View.VISIBLE);
        }
        return gridAdapter;
    }

    GridViewListener gridViewListener = new GridViewListener() {
        @Override
        public void onClickItem(String id, String recall_method, int recall_id) {
            PREVIOUS_VIEW = VIEWTYPE_VIEW_MORE;
            setViewType(VIEWTYPE_NETWORK_DETAIL);
            fragment = NetworkDetailsFragment.newInstance(id, Constants.ALL);
            getChildFragmentManager().beginTransaction().replace(networkLayout.getId(), fragment, NetworkDetailsFragment.TAG).commit();
        }
    };

    CarouselsListener carouselsListener = new CarouselsListener() {
        @Override
        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String type) {

        }

        @Override
        public void onClickItem(HorizontalListitemBean item) {
            if (item.getType().equalsIgnoreCase("n")) {
                PREVIOUS_VIEW = VIEWTYPE_HOME;
                setViewType(VIEWTYPE_NETWORK_DETAIL);
                fragment = NetworkDetailsFragment.newInstance(item.getId() + "", Constants.ALL);
                getChildFragmentManager().beginTransaction().replace(networkLayout.getId(), fragment, NetworkDetailsFragment.TAG).commit();
            } else if (item.getType().equalsIgnoreCase("L")) {
//                ((HomeActivity) getActivity()).loadChannel(Constants.VIEW_CHANNELS, "", item.getSlug());

//                String baseDomain=(AppPrefrence.getInstance().getBrandDomain());
//               // String url = "http://selecttv.freecast.com/" + item.getUrl();
//                String url = baseDomain + item.getUrl();
//                Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
//                intent.putExtra("url", url);
//                intent.putExtra("name", item.getName());
//                getActivity().startActivity(intent);

                String url2=item.getUrl();
                Intent intent = new Intent(getActivity(), WebView.class);
                intent.putExtra("url", url2);
                intent.putExtra("name", item.getName());
                getActivity().startActivity(intent);
            }
        }

        @Override
        public void viewMore(String itemId) {
            if (!itemId.isEmpty()) {
                setViewType(VIEWTYPE_VIEW_MORE);
                if (gridView.getAdapter() != null)
                    if (gridView.getAdapter() instanceof GridAdapter)
                        ((GridAdapter) gridView.getAdapter()).clearValues();
                loadViewAllData(itemId, 100, 0, false, loadMoreListener);
            }
        }
    };

    LoadMoreCarouselsListener loadMoreListener = new LoadMoreCarouselsListener() {
        @Override
        public void onLoadCarousels(final ArrayList<CauroselsItemBean> listItems, final String viewAllId) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (listItems.size() > 0) {
                        String type = listItems.get(0).getType();
                        final GridAdapter gridAdapter = setGridItems(type, listItems);
                        if (gridAdapter != null) {
                            gridAdapter.setRecyclerview(gridView);
                            gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    int OFFSET = gridAdapter.getItemCount() - 1;
                                    loadViewAllData(viewAllId, 100, OFFSET, true, new LoadMoreCarouselsListener() {
                                        @Override
                                        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId) {
                                            gridAdapter.setMoreData(listItems);
                                        }
                                    });
                                }
                            });
                        }
                    } else labelNoData.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    private final int VIEWTYPE_HOME = 0;
    private final int VIEWTYPE_VIEW_MORE = 1;
    private final int VIEWTYPE_NETWORK_DETAIL = 2;
    private int PREVIOUS_VIEW = -1;

    private void setViewType(int viewType) {
        if (viewType >= 0)
            switch (viewType) {
                case VIEWTYPE_HOME:
                    HomeActivity.setOnBackPressedListener(null);
                    PREVIOUS_VIEW = -1;
                    labelNoData.setVisibility(View.GONE);
                    networkLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    upperLayout.setVisibility(View.VISIBLE);
                    if (sliders != null)
                        if (sliders.size() > 0)
                            slidersLayout.setVisibility(View.VISIBLE);
                    if (carousels != null)
                        if (carousels.size() > 0)
                            carouselsLayout.setVisibility(View.VISIBLE);
//                    if (PAGE_TYPE == Constants.VIEW_ONDEMAND_SUGGESTIONS)
//                        layoutPersonalize.setVisibility(View.VISIBLE);
                    break;
                case VIEWTYPE_VIEW_MORE:
                    HomeActivity.setOnBackPressedListener(onBackPressedListener);
                    PREVIOUS_VIEW = VIEWTYPE_HOME;
                    slidersLayout.setVisibility(View.GONE);
                    carouselsLayout.setVisibility(View.GONE);
                    networkLayout.setVisibility(View.GONE);
                    layoutPersonalize.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    break;
                case VIEWTYPE_NETWORK_DETAIL:
                    HomeActivity.setOnBackPressedListener(onBackPressedListener);
                    upperLayout.setVisibility(View.GONE);
                    slidersLayout.setVisibility(View.GONE);
                    carouselsLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    layoutPersonalize.setVisibility(View.GONE);
                    networkLayout.setVisibility(View.VISIBLE);
                    break;
            }
    }

    public void startProgressDialog() {
        progressHUD = ProgressHUD.show(getActivity(), "Please wait..", true, false, null);
    }

    public void stopProgressLoading(ProgressHUD progressHUD) {
        if (progressHUD != null)
            if (progressHUD.isShowing())
                progressHUD.dismiss();
    }

    private void loadViewAllData(final String id, final int LIMIT, final int OFFSET, final boolean canLoadMore, final LoadMoreCarouselsListener loadMoreListener) {
        final ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final JSONArray jsonArray = JSONRPCAPI.getAllCarouselsData(Integer.parseInt(id), LIMIT, OFFSET, payMode);
                    if (jsonArray != null) {
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                                listItems.add(cauroselsItemBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressLoading(progressHUD);
                    loadMoreListener.onLoadCarousels(listItems, id);
                }
            }
        };
        thread.start();
    }

}
