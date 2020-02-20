package com.selecttvapp.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.adapters.GridAdapter;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.views.DynamicImageView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 23-Aug-17.
 */

public class MyInterestContentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TYPE = "type";

    private final String TAB_TV_SHOWS = "TV Shows";
    private final String TAB_MOVIES = "Movies";
    private final String TAB_MOVIE_GENRES = "Movie Genres";
    private final String TAB_CHANNELS = "Channels";
    private final String TAB_TV_NETWORKS = "TV Networks";
    private final String TAB_VIDEO_LBRARIES = "Video libraries";

    private final int VIEWTYPE_HOME = 0;
    private final int VIEWTYPE_SUB_LIST = 1;
    private int PREVIOUS_VIEWTYPE = -1;

    private String mTYPE = "";

    private View toolbarSearch;

    private RecyclerView recyclerView,
            recyclerviewSubList;
    OnBackPressedListener onBackPressedListener = () -> {
        if (PREVIOUS_VIEWTYPE != -1) {
            setViewType(PREVIOUS_VIEWTYPE);
        }
    };
    private TextView labelNoData;
    private ArrayList<FavoriteBean> listItems = new ArrayList<>();
    private int payMode = RabbitTvApplication.getInstance().getPaymode();

    public MyInterestContentFragment() {

    }

    public static MyInterestContentFragment newInstance(String type, ArrayList<FavoriteBean> listItems) {
        MyInterestContentFragment fragment = new MyInterestContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putSerializable("array", listItems);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected int getContentView() {
//        return R.layout.fragment_my_interest_content;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTYPE = getArguments().getString(ARG_TYPE);
            if (getArguments().getSerializable("array") != null)
                listItems = (ArrayList<FavoriteBean>) getArguments().getSerializable("array");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_interest_content, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerviewSubList = (RecyclerView) view.findViewById(R.id.recyclerviewSubList);
        labelNoData = (TextView) view.findViewById(R.id.txtNoData);

        toolbarSearch=((HomeActivity)getActivity()).getFocusOnToolbarSearch();
        toolbarSearch.setNextFocusDownId(R.id.entity_imageview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerviewSubList.setLayoutManager(gridLayoutManager);

        if (listItems.size() > 0) {
            ListContentAdapter mListContentAdapter = new ListContentAdapter(getActivity(), listItems, mTYPE);
            recyclerView.setAdapter(mListContentAdapter);
        } else {
            labelNoData.setVisibility(View.VISIBLE);
            labelNoData.setText("No " + mTYPE.toLowerCase() + " found");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mTYPE))
            addGoogleAnalytics(mTYPE);
        if (recyclerView != null)
            if (recyclerView.getAdapter() != null)
                recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void addGoogleAnalytics(String type) {
        if (!type.isEmpty()) {
            switch (getPageType(type).toLowerCase()) {
                case "shows":
                case "TV Shows":
                    Utilities.googleAnalytics(Constants.MY_INTERESTS_TV_SHOWS_SCREEN);
                    break;
                case "movies":
                    Utilities.googleAnalytics(Constants.MY_INTERESTS_MOVIES_SCREEN);
                    break;
                case "movie genres":
                case "moviegenre":
                    Utilities.googleAnalytics(Constants.MY_INTERESTS_MOVIE_GENRES_SCREEN);
                    break;
                case "channel":
                case "Channels":
                    Utilities.googleAnalytics(Constants.MY_INTEREST_CHANNELS_SCREEN);
                    break;
                case "networks":
                case "network":
                    Utilities.googleAnalytics(Constants.MY_INTEREST_TV_NETWORKS_SCREEN);
                    break;
                case "video libraries":
                case "videolibrary":
                    Utilities.googleAnalytics(Constants.MY_INTEREST_VIDEO_LIBRARIES_SCREEN);
                    break;
            }
        }
    }


    public String getPageType(String type) {
        String pageType = "";
        switch (type) {
            case TAB_TV_SHOWS:
                return "show";
            case TAB_MOVIES:
                return "movie";
            case TAB_MOVIE_GENRES:
                return "moviegenre";
            case TAB_CHANNELS:
                return getString(R.string.interest_channel_slug);
            case TAB_TV_NETWORKS:
                return "network";
            case TAB_VIDEO_LBRARIES:
                return "videolibrary";
        }
        return pageType;
    }

    public boolean canNavigateBack() {
        if (recyclerviewSubList.getVisibility() == View.VISIBLE)
            return true;
        return false;
    }

    public void goback() {
        if (recyclerviewSubList.getVisibility() == View.VISIBLE) {
            recyclerviewSubList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setViewType(int viewType) {
        switch (viewType) {
            case VIEWTYPE_HOME:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEWTYPE = -1;
                recyclerviewSubList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_SUB_LIST:
                HomeActivity.setOnBackPressedListener(onBackPressedListener);
                PREVIOUS_VIEWTYPE = VIEWTYPE_HOME;
                recyclerView.setVisibility(View.VISIBLE);
                recyclerviewSubList.setVisibility(View.GONE);
                break;
        }
    }

    private void loadVideoLibrary(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                Uri webpage = Uri.parse(url);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No application can handle this request. Please install a web browser or check your URL.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public GridAdapter setGridItems(final String type, final ArrayList<CauroselsItemBean> listItems) {
        GridAdapter gridAdapter = null;
        if (listItems.size() > 0) {
            setViewType(VIEWTYPE_SUB_LIST);
            labelNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            if (type.equalsIgnoreCase("show") || type.equalsIgnoreCase("s"))
                setGridLayoutManager(recyclerviewSubList, 2);
            else setGridLayoutManager(recyclerviewSubList, 3);

            gridAdapter = new GridAdapter(listItems, payMode, getActivity(), null);
            this.recyclerviewSubList.setAdapter(gridAdapter);
            this.recyclerviewSubList.setVisibility(View.VISIBLE);
        } else {
            recyclerviewSubList.setVisibility(View.GONE);
            labelNoData.setVisibility(View.VISIBLE);
        }
        return gridAdapter;
    }

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadingMoviesByGenre(final int id, final String type) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            try {
                ArrayList<CauroselsItemBean> listdata = new ArrayList<>();
                if (type.equalsIgnoreCase("movie")) {
                    JSONArray carousel_array = JSONRPCAPI.getMovieListbyGenre(id, 100, 0, 0);
                    listdata = PresenterMyInterest.getInstance().parseArray(carousel_array, "M");

                } else {
                    JSONArray carousel_array = JSONRPCAPI.getTVGenreDatabyId(id, 100, 0, 0);
                    listdata = PresenterMyInterest.getInstance().parseArray(carousel_array, "S");
                }
                if (listdata.size() > 0) {
                    onGridListResponse(type, listdata);
                }
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }
        });
        thread.start();
    }

    private void loadNetworkData(final int id) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            try {
                ArrayList<CauroselsItemBean> listdataShow = new ArrayList<>();
                JSONArray carousel_array = JSONRPCAPI.getTVNetworkList(id, 100, 0, 0);
                listdataShow = PresenterMyInterest.getInstance().parseArray(carousel_array, "S");
                if (listdataShow.size() > 0) {
                    onGridListResponse("s", listdataShow);
                }
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }
        });
        thread.start();
    }

    private void onGridListResponse(final String type, final ArrayList<CauroselsItemBean> listItems) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setGridItems(type, listItems);
            }
        });
    }

    public class ListContentAdapter extends RecyclerView.Adapter<ListContentAdapter.DataObjectHolder> {
        ArrayList<String> list;
        Context context;
        int mlistPosition = 0;
        ArrayList<FavoriteBean> tvShowsList;
        String type;

        public ListContentAdapter(Context context, ArrayList<FavoriteBean> tvShowsList, String type) {
            this.context = context;
            this.tvShowsList = tvShowsList;
            this.type = getPageType(type);
        }

        @Override
        public ListContentAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.interest_content_list_item, parent, false);
            return new DataObjectHolder(view);
        }


        @Override
        public void onBindViewHolder(final ListContentAdapter.DataObjectHolder holder, final int position) {

            final FavoriteBean item = tvShowsList.get(position);
            if (type.equalsIgnoreCase("movie"))
                Image.loadMovieImage(item.getImage(), holder.entity_imageview);
            else if (item.getImage() != null && type.equalsIgnoreCase("show"))
                Image.loadShowImage(item.getImage(), holder.entity_imageview);
            else if (item.getImage() != null)
                Image.loadGridImage(item.getImage(), holder.entity_imageview);
            else if (item.getImage() == null && item.getLogo() != null)
                Image.loadGridImage(item.getLogo(), holder.entity_imageview);

            holder.title_textView.setText(item.getName());

            String rating = item.getRating();
            String runtime = item.getRuntime();
            String description = item.getDescription();

            if (!TextUtils.isEmpty(rating) && !rating.equalsIgnoreCase("null")) {
                holder.rating_textView_value.setText(item.getRating());
                holder.rating_textView_value.setVisibility(View.VISIBLE);
                holder.rating_tex.setVisibility(View.VISIBLE);
            } else {
                holder.rating_textView_value.setVisibility(View.GONE);
                holder.rating_tex.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(runtime) && !runtime.equalsIgnoreCase("null")) {
                holder.runtime_textView_value.setText(runtime);
                holder.runtime_textView_value.setVisibility(View.VISIBLE);
                holder.runtime_text.setVisibility(View.VISIBLE);
            } else {
                holder.runtime_textView_value.setVisibility(View.GONE);
                holder.runtime_text.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(description) && !description.equalsIgnoreCase("null")) {
                holder.description_textView.setText(description);
                holder.description_textView.setVisibility(View.VISIBLE);
            } else {
                holder.description_textView.setVisibility(View.GONE);
            }

            holder.runtime_textView_value.setText(item.getRuntime());
            holder.description_textView.setText(item.getDescription());
            holder.remove_button.setOnClickListener(v -> {
                try {
                    if (type.equalsIgnoreCase(getString(R.string.interest_channel_slug)))
                        PresenterMyInterest.getInstance().removeFavoriteItem(getActivity(), type, "" + item.getSlug(), null);
                    else
                        PresenterMyInterest.getInstance().removeFavoriteItem(getActivity(), type, "" + item.getId(), null);
                    tvShowsList.remove(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            holder.text_layout.setOnClickListener(v -> holder.entity_imageview.performClick());

            holder.entity_imageview.setOnClickListener(v -> {
                try {
                    if (type.equalsIgnoreCase("show")) {
                        int payMode = RabbitTvApplication.getInstance().getPaymode();
                        startActivity(ShowDetailsActivity.getIntent(getActivity(), item.getId(), payMode));
                    } else if (type.equalsIgnoreCase("movie")) {
                        startActivity(MovieDetailsActivity.getIntent(getActivity(), item.getId()));
                    } else if (type.equalsIgnoreCase("moviegenre")) {
                        loadingMoviesByGenre(item.getId(), "movie");
                    } else if (type.equalsIgnoreCase("network")) {
                        loadNetworkData(item.getId());
                    } else if (type.equalsIgnoreCase("videolibrary")) {
                        loadVideoLibrary(item.getUrl());
                    } else if (type.equalsIgnoreCase(getString(R.string.interest_channel_slug))) {
                        try {
                            String parentSlug = item.getChannelCategories().get(0) != null ? item.getChannelCategories().get(0) : "";
                            ((HomeActivity) getActivity()).loadChannel(item.getSlug(), parentSlug);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }

        @Override
        public int getItemCount() {
            return tvShowsList.size();
        }


        public class DataObjectHolder extends RecyclerView.ViewHolder {
            DynamicImageView entity_imageview;
            TextView title_textView, rating_tex, rating_textView_value, runtime_text, runtime_textView_value, description_textView;
            Button remove_button;
            LinearLayout text_layout;


            public DataObjectHolder(View itemView) {
                super(itemView);

                entity_imageview = (DynamicImageView) itemView.findViewById(R.id.entity_imageview);
                title_textView = (TextView) itemView.findViewById(R.id.title_textView);
                rating_textView_value = (TextView) itemView.findViewById(R.id.rating_textView_value);
                rating_tex = (TextView) itemView.findViewById(R.id.rating_tex);
                runtime_text = (TextView) itemView.findViewById(R.id.runtime_text);
                runtime_textView_value = (TextView) itemView.findViewById(R.id.runtime_textView_value);
                description_textView = (TextView) itemView.findViewById(R.id.description_textView);
                remove_button = (Button) itemView.findViewById(R.id.remove_button);
                text_layout = (LinearLayout) itemView.findViewById(R.id.text_layout);
            }
        }
    }

}
