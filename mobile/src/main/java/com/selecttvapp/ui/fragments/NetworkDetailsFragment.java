package com.selecttvapp.ui.fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.callbacks.LoadMoreCarouselsListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.Network;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.adapters.GridAdapter;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "Network_Details";
    private static final String ARG_PARAM_ITEM_ID = "item_id";
    private static final String ARG_PARAM_PAYMODE = "paymode";

    // TODO: Rename and change types of parameters
    private ProgressHUD progressHUD;
    private Network network;
    private RecyclerView recyclerSelectedNetworkListView;
    private DynamicImageView network_imageView,
            land_network_imageView;
    private TextView labelNoData,
            labelShows, land_labelShows;
    private LinearLayout layoutNetworkDetail,
            layout_port_view,
            layout_land_view;

    private ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
    private String networkId = "";
    private int payMode = Constants.ALL;

    public NetworkDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param networkId Parameter 1.
     * @param payMode   Parameter 2.
     * @return A new instance of fragment NetworkDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkDetailsFragment newInstance(String networkId, int payMode) {
        NetworkDetailsFragment fragment = new NetworkDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_ITEM_ID, networkId);
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            networkId = getArguments().getString(ARG_PARAM_ITEM_ID);
            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_details, container, false);
        network_imageView = (DynamicImageView) view.findViewById(R.id.network_imageView);
        land_network_imageView = (DynamicImageView) view.findViewById(R.id.land_network_imageView);
        recyclerSelectedNetworkListView = (RecyclerView) view.findViewById(R.id.recyclerSelectedNetworkListView);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        labelShows = (TextView) view.findViewById(R.id.labelShows);
        land_labelShows = (TextView) view.findViewById(R.id.land_labelShows);
        layoutNetworkDetail = (LinearLayout) view.findViewById(R.id.layoutNetworkDetail);
        layout_port_view = (LinearLayout) view.findViewById(R.id.layout_port_view);
        layout_land_view = (LinearLayout) view.findViewById(R.id.layout_land_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGridLayoutManager(recyclerSelectedNetworkListView, 2);

        if (!networkId.isEmpty())
            loadNetworkData(networkId, 100, 0, false, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.NETWORK_DETAILS_SCREEN);
    }

    @Override
    public void onDestroyView() {
        if(progressHUD!=null)
            progressHUD.dismiss();
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (network == null) {
            layout_port_view.setVisibility(View.GONE);
            layout_land_view.setVisibility(View.GONE);
            return;
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutNetworkDetail.setOrientation(LinearLayout.VERTICAL);
            layout_land_view.setVisibility(View.GONE);
            layout_port_view.setVisibility(View.VISIBLE);
            recyclerSelectedNetworkListView.setVisibility(View.VISIBLE);
        } else {
            layoutNetworkDetail.setOrientation(LinearLayout.HORIZONTAL);
            layout_port_view.setVisibility(View.GONE);
            layout_land_view.setVisibility(View.VISIBLE);
            recyclerSelectedNetworkListView.setVisibility(View.VISIBLE);
        }
    }

    public void setDetails() {
        onConfigurationChanged(getResources().getConfiguration());
        if (network != null) {
            Image.loadShowImage(network_imageView.getContext(), network.getAppImage(), network_imageView);
            Image.loadShowImage(land_network_imageView.getContext(), network.getAppImage(), land_network_imageView);
        }

        if (listItems.size() > 0) {
            labelShows.setVisibility(View.VISIBLE);
            land_labelShows.setVisibility(View.VISIBLE);
            setGridLayoutManager(recyclerSelectedNetworkListView, 2);
            final GridAdapter gridAdapter = new GridAdapter(listItems, payMode, getActivity(), null);
            this.recyclerSelectedNetworkListView.setAdapter(gridAdapter);
            this.recyclerSelectedNetworkListView.setVisibility(View.VISIBLE);
            gridAdapter.setRecyclerview(recyclerSelectedNetworkListView);
            gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    int OFFSET = gridAdapter.getItemCount() - 1;
                    loadNetworkData(networkId, 100, OFFSET, true, new LoadMoreCarouselsListener() {
                        @Override
                        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId) {
                            gridAdapter.setMoreData(listItems);
                        }
                    });
                }
            });
        } else {
            labelShows.setVisibility(View.GONE);
            land_labelShows.setVisibility(View.GONE);
            labelNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void loadNetworkData(final String ID, final int LIMIT, final int OFFSET, final boolean canLoadMore, final LoadMoreCarouselsListener loadMoreListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int g_id = Integer.parseInt(ID);
                    JSONArray carousel_array = JSONRPCAPI.getTVNetworkList(g_id, LIMIT, OFFSET, payMode);
                    if (!canLoadMore) {
                        JSONObject networkDetailsResponse = JSONRPCAPI.getNetworkDetails(g_id);
                        if (networkDetailsResponse != null) {
                            network = new Network(networkDetailsResponse);
                        }
                    }
                    listItems = parseArray(carousel_array, "s");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadMoreListener != null)
                                loadMoreListener.onLoadCarousels(listItems, ID);
                            else if (!canLoadMore) setDetails();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressLoading(progressHUD);
                }
            }
        });
        thread.start();
    }

    public void startProgressDialog() {
        progressHUD = ProgressHUD.show(getActivity(), "Please wait..", true, false, null);
    }

    public void stopProgressLoading(ProgressHUD progressHUD) {
        if (progressHUD != null)
            if (progressHUD.isShowing())
                progressHUD.dismiss();
    }

    public ArrayList<CauroselsItemBean> parseArray(JSONArray carousel_array, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();
        try {
            if (carousel_array != null) {
                if (carousel_array.length() > 0) {
                    for (int i = 0; i < carousel_array.length(); i++) {
                        JSONObject object = carousel_array.getJSONObject(i);
                        CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                        if (cauroselsItemBean.getType().isEmpty())
                            cauroselsItemBean.setType(type);
                        parse_list.add(cauroselsItemBean);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }

}
