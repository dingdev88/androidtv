package com.selecttvapp.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.databinding.AppmanagerListItemBinding;
import com.selecttvapp.databinding.ChannelListLayoutBinding;
import com.selecttvapp.databinding.FragmentAppManagerBinding;
import com.selecttvapp.databinding.HorizontalListItemBinding;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.HorizontalListAppManager;
import com.selecttvapp.model.Network;
import com.selecttvapp.presentation.fragments.PresenterAppManager;
import com.selecttvapp.presentation.views.ViewAppManagerListener;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.base.BaseFragment;
import com.selecttvapp.ui.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import okhttp3.internal.Util;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppManagerFragment.OnAppFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppManagerFragment extends BaseFragment implements ViewAppManagerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected PresenterAppManager presenter = new PresenterAppManager();
    private FragmentAppManagerBinding binding;
    private OnAppFragmentInteractionListener mListener;
    private OnDemandContentAdapter mOnDemandContentAdapter;

    public AppManagerFragment() {
        // Required empty public constructor
    }

    public static AppManagerFragment newInstance(String param1, String param2) {
        AppManagerFragment fragment = new AppManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_app_manager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter.onAttach(this);
        super.onCreate(savedInstanceState);
        PreferenceManager.setDemandFirstTime(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = (FragmentAppManagerBinding) getBinding();

        binding.donebtn.setOnClickListener(v -> mListener.onAppFragmentInteraction());

        setLinearLayoutManager(binding.dynamicContentListview, LinearLayoutManager.VERTICAL);
        setLinearLayoutManager(binding.categoriesListView, LinearLayoutManager.HORIZONTAL);
        binding.dynamicContentListview.hasFixedSize();
        binding.categoriesListView.hasFixedSize();
        presenter.loadAppCategories();
        return getRootView();
    }


    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.APP_MANAGER_SCREEN);
        if(mOnDemandContentAdapter!=null)
            mOnDemandContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppFragmentInteractionListener) {
            mListener = (OnAppFragmentInteractionListener) context;
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
    public void loadAppCategories(ArrayList<CategoryBean> appCategoriesList) {
        CategoryAdapter mCategoryAdapter = new CategoryAdapter(appCategoriesList, getActivity());
        binding.categoriesListView.setAdapter(mCategoryAdapter);
        if (appCategoriesList.size() > 0)
            presenter.loadCategoryItems(appCategoriesList.get(0).getId());
    }

    @Override
    public void loadCategoryItems(ArrayList<HorizontalListAppManager> categoryItems) {
        mOnDemandContentAdapter = new OnDemandContentAdapter(categoryItems, getActivity());
        binding.dynamicContentListview.setAdapter(mOnDemandContentAdapter);
    }

    private void setLinearLayoutManager(RecyclerView recyclerView, int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), orientation, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public interface OnAppFragmentInteractionListener {
        void onAppFragmentInteraction();
    }

    private class CategoryAdapter extends BaseRecyclerViewAdapter<CategoryAdapter.DataObjectHolder> {
        private ArrayList<CategoryBean> category_list;
        private Context context;
        private int mSelectedItem;
        private Drawable img;
        private ScaleDrawable sd;

        public CategoryAdapter(ArrayList<CategoryBean> category_list, Context context) {
            this.category_list = category_list;
            this.context = context;
            this.mSelectedItem = 0;
            try {
                if (img != null) {
                    img.setBounds(0, 0, 12, 12);
                    img.setBounds(0, 0, (int) (img.getIntrinsicWidth() * 0.5),
                            (int) (img.getIntrinsicHeight() * 0.5));
                    sd = new ScaleDrawable(img, 0, 12, 12);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        @Override
        public CategoryAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ChannelListLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.channel_list_layout, parent, false);
            return new CategoryAdapter.DataObjectHolder(binding);
        }

        @Override
        public void onBindViewHolder(final CategoryAdapter.DataObjectHolder holder, final int position) {
            CategoryBean item = category_list.get(position);
            holder.bind(item, position);
        }

        @Override
        public int getItemCount() {
            return category_list.size();
        }

        @Override
        protected Context getContext() {
            return context;
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private ChannelListLayoutBinding binding;

            public DataObjectHolder(ChannelListLayoutBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(CategoryBean item, int position) {
                try {
                    binding.txtItem.setText(item.getName());
                    ViewGroup.LayoutParams params = binding.itemsLayout.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.itemsLayout.setLayoutParams(params);
                    binding.itemsLayout.setPadding(20, 0, 20, 0);

                    if (position == mSelectedItem) {
                        binding.bottomLine.setVisibility(View.VISIBLE);
                        binding.txtItem.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        binding.bottomLine.setVisibility(View.INVISIBLE);
                        binding.txtItem.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
                    }

                    binding.itemsLayout.setOnClickListener(v -> {
                        mSelectedItem = position;
                        notifyDataSetChanged();
                        presenter.loadCategoryItems(item.getId());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class OnDemandContentAdapter extends RecyclerView.Adapter<OnDemandContentAdapter.DataObjectHolder> {
        private ArrayList<HorizontalListAppManager> list_data;
        private Context context;
        private int mSelectedItem = 0;

        public OnDemandContentAdapter(ArrayList<HorizontalListAppManager> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
        }



        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AppmanagerListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.appmanager_list_item, parent, false);
            return new DataObjectHolder(binding);
        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            final HorizontalListAppManager app = list_data.get(position);
            holder.bind(app, position);
        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private AppmanagerListItemBinding binding;

            public DataObjectHolder(AppmanagerListItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                //Utils.requestfocus(binding.networkImageViewv1);

            }

            public void bind(HorizontalListAppManager app, int position) {
                try {
                    Network network = app.getNetwork();
                    Carousel carousel = app.getCarousel();

                    binding.gameCategoryName.setText(Utils.stripHtml(app.getName()));
                    final String image_url = app.getImage();
                    if (!TextUtils.isEmpty(image_url)) {
                        Image.loadGridImage(image_url, binding.networkImageViewv1);
                    }

                    try {
                        binding.dynamicImageLayout1.removeAllViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String package_name = app.getPackage();
                    binding.networkImageViewv1.setOnClickListener(v -> {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        if (!Utils.appInstalledOrNot(getActivity(), package_name)) {
                            AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + app.getName(), "" + app.getImage(), app.getLink());
                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                        }
                    });

                    if (Utils.appInstalledOrNot(getActivity(), package_name)) {
                        binding.txtAddOrInstalled.setText("Installed");
                        binding.txtAddOrInstalled.setBackgroundResource(R.drawable.bg_installed_app);
                    } else {
                        binding.txtAddOrInstalled.setText("Add");
                        binding.txtAddOrInstalled.setBackgroundResource(R.drawable.bg_uninstalled_app);
                    }

                    if (app.getSize() != null && !app.getSize().isEmpty() && !app.getSize().equalsIgnoreCase("null")) {
                        binding.txtAppSize.setText("~" + app.getSize());
                        binding.txtAppSize.setVisibility(View.VISIBLE);
                    }


                    binding.txtAddOrInstalled.setOnClickListener(v -> {
                        String package_name1 = app.getPackage();
                        if (!Utils.appInstalledOrNot(getActivity(), package_name1)) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + app.getName(), "" + app.getImage(), app.getLink());
                            dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                        }
                    });

                    for (int l = 0; l < carousel.getData_list().size() && l < 6; l++) {
                        final int ll = l;
                        HorizontalListItemBinding binding = HorizontalListItemBinding.inflate(LayoutInflater.from(getActivity()));
                        int width = 0;
                        if (isLandcapeView())
                            width = getHeightPixels();
                        else width = getWidthPixels();

                        LinearLayout.LayoutParams vp;
                        if (carousel.getData_list().get(l).getType().equalsIgnoreCase("m"))
                            vp = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.MATCH_PARENT);
                        else
                            vp = new LinearLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.MATCH_PARENT);
                        if (l != 0) vp.setMargins(20, 0, 0, 0);

                        String imageUrl = carousel.getData_list().get(l).getImage();
                        if (carousel.getData_list().get(l).getType().equalsIgnoreCase("m")) {
                            Image.loadMovieImage(imageUrl, binding.dynamicImageView);
                        } else {
                            Image.loadShowImage(imageUrl, binding.dynamicImageView);
                        }
                        binding.dynamicImageView.setLayoutParams(vp);

                        binding.getRoot().setOnClickListener(v -> {
                            if (!Utils.appInstalledOrNot(getActivity(), package_name)) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + app.getName(), "" + app.getImage(), app.getLink());

                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                            } else {
                                String type = carousel.getData_list().get(ll).getType();
                                if (type.equalsIgnoreCase("s") || type.equalsIgnoreCase("show")) {
                                    int showId = carousel.getData_list().get(ll).getId();
                                    int payMode = RabbitTvApplication.getPaymode();
                                    startActivity(ShowDetailsActivity.getIntent(getActivity(), showId, payMode));

                                } else if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
                                    int movieId = carousel.getData_list().get(ll).getId();
                                    startActivity(MovieDetailsActivity.getIntent(getActivity(), movieId));
                                }
                            }
                        });
                        this.binding.dynamicImageLayout1.addView(binding.getRoot());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



}