package com.selecttvapp.ondemandpage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.model.CategoryBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpinnerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinnerDialogFragment extends DialogFragment {
    public final String TAG = this.getClass().getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnClickListener onClickListener;

    private SpinnerAdapter spinnerAdapter;
    private RecyclerView spinnerListView;
    private FrameLayout layoutContent;
    private ImageView imgClose;

    private ArrayList<CategoryBean> listItems = new ArrayList<>();

    private int mHeight = 0;
    private int mWidth = 0;

    public SpinnerDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SpinnerDialogFragment(ArrayList<CategoryBean> listItems) {
        this.listItems = listItems;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpinnerDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpinnerDialogFragment newInstance(String param1, String param2) {
        SpinnerDialogFragment fragment = new SpinnerDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    interface OnClickListener {
        void onClickItem(CategoryBean categoryBean);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spinner_dialog, container, false);
        layoutContent = (FrameLayout) view.findViewById(R.id.layoutContent);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);
        spinnerListView = (RecyclerView) view.findViewById(R.id.spinnerListView);
        setLinearLayoutManager(spinnerListView);

        layoutContent.getLayoutParams().height = (mWidth / 100) * 75;
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible())
                    dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listItems != null)
            if (listItems.size() > 0)
                setSpinnerList(listItems);
    }

//    private void dismis(){
//        this.
//    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_spinner_dialog, null);
//        imgClose = (ImageView) view.findViewById(R.id.imgClose);
//        spinnerListView = (RecyclerView) view.findViewById(R.id.spinnerListView);
//        setLinearLayoutManager(spinnerListView);
//
//        if (listItems != null)
//            if (listItems.size() > 0)
//                setSpinnerList(listItems);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        return builder.create();
//    }

    private void setSpinnerList(final ArrayList<CategoryBean> listItems) {
        if (listItems.size() > 0) {
            spinnerAdapter = new SpinnerAdapter(getActivity(), listItems, onClickListener);
            spinnerListView.setAdapter(spinnerAdapter);
        }
    }

    private void setLinearLayoutManager(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ViewHolder> {
        private Activity activity;
        private OnClickListener onClickListener;
        private ArrayList<CategoryBean> listItems = new ArrayList<>();

        public SpinnerAdapter(Activity activity, ArrayList<CategoryBean> listItems, OnClickListener onClickListener) {
            this.activity = activity;
            this.listItems = listItems;
            this.onClickListener = onClickListener;
        }

        @Override
        public SpinnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.spinner_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SpinnerAdapter.ViewHolder holder, int position) {
            final CategoryBean item = listItems.get(position);
            holder.textView.setText(item.getName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.onClickItem(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textCategory);
                textView.setTextSize(Dimension.SP, 18);
                textView.setPadding(10, 10, 10, 10);
            }

        }
    }

}
