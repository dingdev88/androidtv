package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.RadioListener;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.RadioDetailBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.network.LoaderWebserviceInterface;
import com.selecttvapp.presentation.views.ViewRadioListener;
import com.selecttvapp.ui.bean.ListenGridBean;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.ListenFragment;
import com.selecttvapp.ui.helper.MyApplication;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public class PresenterRadio {
    private Activity activity;
    private ViewRadioListener mListener;
    private Handler handler=new Handler();

    public PresenterRadio(ListenFragment fragment) {
        activity = fragment.getActivity();
        mListener = fragment;
    }

    public void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void loadGenreList() {
        if (RabbitTvApplication.getInstance().getRadioList() != null && RabbitTvApplication.getInstance().getRadioList().size() > 0) {
            mListener.loadGenreList(RabbitTvApplication.getInstance().getRadioList());
        } else loadRadioStations();
    }

    private void loadRadioStations() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("radio.getGenres", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    final ArrayList<ListenGridBean> listItems = parseRadioData(carousel_array);
                                    handler.post(() -> mListener.loadGenreList(listItems));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                    }

                    @Override
                    public void onNetworkError() {
                    }
                }, 1000, 0);
            }
        };
        thread.start();
    }

    public void loadRadioSubCategory(final int radioId) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
        final Thread thread = new Thread(() -> {
            try {
                final ArrayList<RadioDetailBean> listItems = new ArrayList<>();
                JSONArray m_jsonRadioeList = JSONRPCAPI.getRadioGenreList(radioId);
                if (m_jsonRadioeList == null) return;
                for (int i = 0; i < m_jsonRadioeList.length(); i++) {
                    JSONObject jsonObject = m_jsonRadioeList.getJSONObject(i);
                    RadioDetailBean radioDetailBean = new RadioDetailBean(jsonObject);
                    listItems.add(radioDetailBean);
                }
                handler.post(() -> mListener.loadRadioSubList(listItems));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }
        });
        thread.start();
    }

    public ListenSubGridAdapter getListenSubGridAdapter(ArrayList<RadioDetailBean> emptyList) {
        ListenSubGridAdapter listenSubGridAdapter = new ListenSubGridAdapter(emptyList, activity);
        return listenSubGridAdapter;
    }

    public ListenGridAdapter getListenGridAdapter(ArrayList<ListenGridBean> listenGridBeans) {
        ListenGridAdapter listenGridAdapter = new ListenGridAdapter(listenGridBeans, activity);
        return listenGridAdapter;
    }

    public class ListenGridAdapter extends RecyclerView.Adapter<ListenGridAdapter.DataObjectHolder> {
        Activity context;
        ArrayList<ListenGridBean> listenGridBeans;
        private RadioListener radioListener;

        public void setRadioListener(RadioListener radioListener) {
            this.radioListener = radioListener;
        }

        public ListenGridAdapter(ArrayList<ListenGridBean> listenGridBeans, Activity context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;

        }

        @Override
        public ListenGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item_radio, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenGridAdapter.DataObjectHolder holder, final int position) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            holder.imageView.setLayoutParams(layoutParams);
            Image.loadGridImage(holder.imageView.getContext(), listenGridBeans.get(position).getImageUrl(), holder.imageView);
            holder.imageView.setOnClickListener(v -> {
                loadRadioSubCategory(listenGridBeans.get(position).getId());
                if (radioListener != null) {
                    radioListener.onLoadRadioSubList();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageViewRadio);
                imageView.setNextFocusUpId(R.id.activity_homescreen_toolbar_search);


            }
        }
    }


    public class ListenSubGridAdapter extends RecyclerView.Adapter<ListenSubGridAdapter.DataObjectHolder> {
        Context context;
        ArrayList<RadioDetailBean> listenGridBeans;
        private RadioListener radioListener;

        public void setRadioListener(RadioListener radioListener) {
            this.radioListener = radioListener;
        }

        public ListenSubGridAdapter(ArrayList<RadioDetailBean> listenGridBeans, Context context) {
            this.listenGridBeans = listenGridBeans;
            this.context = context;
        }

        @Override
        public ListenSubGridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grid_item_sub_radio, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(ListenSubGridAdapter.DataObjectHolder holder, final int position) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            holder.imageView.setLayoutParams(layoutParams);
            Image.loadGridImage(holder.imageView.getContext(), listenGridBeans.get(position).getImage(), holder.imageView);
            holder.imageView.setOnClickListener(v -> {
                try {
                    if (radioListener != null)
                        radioListener.onClickItem();

                    mListener.showRadioDetails(listenGridBeans.get(position));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listenGridBeans.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageViewSubRadio);
                imageView.setNextFocusUpId(R.id.textRaioBack1);
            }
        }
    }

    private ArrayList<ListenGridBean> parseRadioData(JSONArray radio_array) {
        ArrayList<ListenGridBean> listItems = new ArrayList<>();
        try {
            for (int i = 0; i < radio_array.length(); i++) {
                listItems.add(new ListenGridBean(radio_array.getJSONObject(i)));
            }
            if (listItems.size() > 0) {
                RabbitTvApplication.getInstance().getRadioList().clear();
                RabbitTvApplication.getInstance().getRadioList().addAll(listItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listItems;
    }
}
