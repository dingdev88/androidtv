package com.selecttvapp.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvapp.R;
import com.selecttvapp.callbacks.DownloadListener;
import com.selecttvapp.callbacks.InstallationListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Permission;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.databinding.AppmanagerAdapterLayoutBinding;
import com.selecttvapp.databinding.FragmentFastDownloadBinding;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.presentation.fragments.PresenterFastDownload;
import com.selecttvapp.presentation.views.ViewFastDownload;
import com.selecttvapp.ui.base.BaseFragment;
import com.selecttvapp.ui.base.BaseRecyclerViewAdapter;
import com.selecttvapp.ui.dialogs.OnDemandDialogVideo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FastDownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FastDownloadFragment extends BaseFragment implements ViewFastDownload {
    protected PresenterFastDownload presenter = new PresenterFastDownload();
    private FragmentFastDownloadBinding binding;
    private OnAppDownloadFragmentInteractionListener mListener;
    private OnDemandContentAdapter adapter;
    private ArrayList<CategoryBean> listItems = new ArrayList<>();

    private int pagePosition = -1;
    private int categoryId = -1;

    public FastDownloadFragment() {
        // Required empty public constructor
    }

    public static FastDownloadFragment newInstance(int position, int categoryId) {
        FastDownloadFragment fragment = new FastDownloadFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PAGE_POSITION, position);
        args.putInt(Constants.CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_fast_download;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onAttach(this);
        if (getArguments() != null) {
            pagePosition = getArguments().getInt(Constants.PAGE_POSITION, -1);
            categoryId = getArguments().getInt(Constants.CATEGORY_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = (FragmentFastDownloadBinding) getBinding();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recyclerViewApks.setLayoutManager(gridLayoutManager);

        binding.txtWatchVideo.setOnClickListener(view -> {
            OnDemandDialogVideo demanddialog = new OnDemandDialogVideo();
            demanddialog.show(getChildFragmentManager(), demanddialog.getClass().getSimpleName());
        });

        binding.doneBtn.setOnClickListener(view -> {
            if (mListener != null)
                mListener.onAppDownloadFragmentInteraction();
        });
        return getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.post(() -> {
//            if (pagePosition != -1)
//                listItems = presenter.getLists(pagePosition);
//            if (listItems != null)
//                if (listItems.size() > 0) {
//                    loadAppsList(listItems);
//                }
//        });

        if (categoryId != -1)
            presenter.loadAppsList(categoryId);
    }


    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.FAST_DOWNLOAD_SCREEN);
    }

    @Override
    public void onStart() {
//        if (AppDownloadFragment.getInstance() != null)
//            AppDownloadFragment.getInstance().stopProgressDialog();
        super.onStart();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Permission.CODE_UNKNOWNSOURCE_INSTALLATION)
            presenter.installLastSelectedApk();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Permission.CODE_WRITE_EXTERNAL_STORAGE)
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (Permission.getInstance().checkPermission(getActivity(), Permission.getInstance().WRITE_EXTERNAL_STORAGE, Permission.CODE_WRITE_EXTERNAL_STORAGE)) {
                        presenter.downloadLastSelectedApk();
                    }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppDownloadFragmentInteractionListener) {
            mListener = (OnAppDownloadFragmentInteractionListener) context;
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
    public void loadAppsList(ArrayList<CategoryBean> appsList) {
        binding.progressBar.setVisibility(View.GONE);
        if (appsList.size() > 0) {
            adapter = new OnDemandContentAdapter(appsList, getActivity());
            binding.recyclerViewApks.setAdapter(adapter);
            binding.recyclerViewApks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        binding.progressBar.setVisibility(View.GONE);
    }

    public interface OnAppDownloadFragmentInteractionListener {
        void onAppDownloadFragmentInteraction();
    }

    private class OnDemandContentAdapter extends BaseRecyclerViewAdapter<OnDemandContentAdapter.DataObjectHolder> {
        private ArrayList<CategoryBean> application_list = new ArrayList<>();
        private Context context;

        public OnDemandContentAdapter(ArrayList<CategoryBean> category_list, Context context) {
            this.application_list = category_list;
            this.context = context;
        }


        @Override
        public OnDemandContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AppmanagerAdapterLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.appmanager_adapter_layout, parent, false);
            return new OnDemandContentAdapter.DataObjectHolder(binding);
        }

        @Override
        public void onBindViewHolder(final OnDemandContentAdapter.DataObjectHolder holder, final int position) {
            final CategoryBean item = application_list.get(position);
            holder.bind(item, position);
        }

        @Override
        public int getItemCount() {
            return application_list.size();
        }

        @Override
        protected Context getContext() {
            return context;
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private AppmanagerAdapterLayoutBinding binding;

            public DataObjectHolder(AppmanagerAdapterLayoutBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(final CategoryBean item, int position) {
                try {
//                    networkImage.setImageResource(item.getId());
                    Image.loadGridImage(item.getImage(), binding.networkImage);

                    if (item.getSize() != null && !item.getSize().isEmpty() && !item.getSize().equalsIgnoreCase("null")) {
                        binding.txtAppSize.setText("~" + item.getSize());
                        binding.txtAppSize.setVisibility(View.VISIBLE);
                    }

                    if (!item.getPackageName().isEmpty())
                        Utils.appInstalledOrNot(context, item.getPackageName(), new InstallationListener() {
                            @Override
                            public void appInstalled() {
                                binding.txtDownload.setEnabled(false);
                                binding.rootView.setEnabled(false);
                                binding.txtDownload.setText("Installed");
                            }

                            @Override
                            public void appNotInstalled() {
                                binding.txtDownload.setEnabled(true);
                                binding.txtDownload.setText("Download");
                                String apkName = item.getLink().replace("apks/", "");
                                if (!apkName.isEmpty() && presenter.isApkAlreadyExist(apkName))
                                    binding.txtDownload.setText("Install");
                            }
                        });
                    binding.rootView.setOnClickListener(view -> {
                        try {
                            if (item.getLink().toLowerCase().startsWith("https://play.google.com/store/apps/details?id")) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + item.getName(), "" + item.getImage(), item.getLink());
//                                AppStoreDialogFragment dialogFragment = AppStoreDialogFragment.newInstance("" + item.getName(), item.getId(), item.getLink());
                                dialogFragment.show(fm.beginTransaction(), "FragmentDetailsDialog");
                            } else {
                                if (binding.txtDownload.getText().toString().toLowerCase().equalsIgnoreCase("Install")) {
                                    String apkName = item.getLink().replace("apks/", "");
                                    if (!apkName.isEmpty())
                                        presenter.installApk(apkName, true);
                                    return;
                                }

                                String appDownloadLink = item.getLink().replaceAll(" ", "%20");
                                appDownloadLink = appDownloadLink.startsWith("http://tvappsplus.com/") ? appDownloadLink : "http://tvappsplus.com/" + appDownloadLink;

                                presenter.mDownloadApk(appDownloadLink, getActivity(), new DownloadListener() {
                                    @Override
                                    public void onStart(String message) {
                                    }

                                    @Override
                                    public void onLoading(String message) {
                                        binding.txtDownload.setEnabled(false);
                                        binding.txtDownload.setText(message);
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onSuccess(String message) {
                                        binding.txtDownload.setEnabled(true);
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.txtDownload.setText("Install");
                                    }

                                    @Override
                                    public void onError(String error) {
                                        binding.txtDownload.setEnabled(true);
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.txtDownload.setText("Download");
                                        showMessage("Download failed");
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
