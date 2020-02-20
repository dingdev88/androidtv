package com.selecttvapp.ondemandpage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.GridViewListener;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.ui.adapters.GridAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_PAYMODE = "paymode";
    private static final String ARG_PARAM_LIST_TYPE = "listType";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridViewListener gridViewListener;
    private RecyclerView gridView;
    private TextView labelNoData;

    private ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
    private String listType = "";
    private int payMode = RabbitTvApplication.getInstance().getPaymode();

    public GridListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param payMode  Parameter 1.
     * @param listType Parameter 2.
     * @return A new instance of fragment GridListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridListFragment newInstance(int payMode, String listType, ArrayList<CauroselsItemBean> listItems, GridViewListener gridViewListener) {
        GridListFragment fragment = new GridListFragment();
        fragment.listItems = listItems;
        fragment.gridViewListener = gridViewListener;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        args.putString(ARG_PARAM_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
            listType = getArguments().getString(ARG_PARAM_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid_list, container, false);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        gridView = (RecyclerView) view.findViewById(R.id.gridView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listItems != null)
            setGridItems(listType, listItems);
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
        return gridAdapter;
    }

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

}
