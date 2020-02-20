package com.selecttvapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.GridBean;
import com.selecttvapp.model.GridSpacingItemDecoration;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterGames;
import com.selecttvapp.presentation.views.ViewGamesListener;
import com.selecttvapp.ui.adapters.SpinnerGameAdapter;
import com.selecttvapp.ui.bean.SideMenu;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.GridViewItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment implements ViewGamesListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PresenterGames presenter;
    private Handler handler=new Handler();

    private ArrayList<Carousel> gamesList = new ArrayList<>();

    private LinearLayout game_dynamic_list_layout;
    private ScrollView game_dynamic_list_scroll;
    private ProgressBar game_progress;
    private Spinner demand_spinner1;

    private RecyclerView gridviewallGames;
    LayoutInflater inflate;

    public GamesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterGames(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        game_dynamic_list_layout = (LinearLayout) view.findViewById(R.id.game_dynamic_list_layout);
        game_dynamic_list_scroll = (ScrollView) view.findViewById(R.id.game_dynamic_list_scroll);
        game_progress = (ProgressBar) view.findViewById(R.id.game_progress);
        gridviewallGames = (RecyclerView) view.findViewById(R.id.gridviewallGames);
        demand_spinner1 = (Spinner) view.findViewById(R.id.demand_spinner1);
        demand_spinner1.setOnItemSelectedListener(m_GameSelectedListener);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridviewallGames.hasFixedSize();
        gridviewallGames.setLayoutManager(layoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        gridviewallGames.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        game_progress.setVisibility(View.VISIBLE);
        game_dynamic_list_scroll.setVisibility(View.GONE);
        gridviewallGames.setVisibility(View.GONE);
        if (presenter != null)
            presenter.loadGames(Constants.ALL);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.GAMES_SCREEN);
    }

    @Override
    public void loadGames(ArrayList<Carousel> gamesList) {
        game_progress.setVisibility(View.GONE);
        game_dynamic_list_scroll.setVisibility(View.VISIBLE);
        gridviewallGames.setVisibility(View.GONE);
        if (gamesList.size() > 0) {
            this.gamesList = gamesList;
            for (int i = 0; i < gamesList.size(); i++) {
                Carousel game = gamesList.get(i);
                addlayouts(game_dynamic_list_layout, game.getName(), game.getId(), game.getData_list(), i);
            }
        }
    }

    @Override
    public void loadCategories(ArrayList<SideMenu> categories) {
        SpinnerGameAdapter spinnerAdapter = new SpinnerGameAdapter(getActivity(), categories);
        demand_spinner1.setAdapter(spinnerAdapter);
        demand_spinner1.setSelection(0);
    }


    Spinner.OnItemSelectedListener m_GameSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                if (position != 0) {
                    Carousel game = gamesList.get(position - 1);
                    loadAllGame(game.getId());
                } else {
                    game_dynamic_list_scroll.setVisibility(View.VISIBLE);
                    gridviewallGames.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void addlayouts(final LinearLayout dynamic_horizontalViews_layout, final String name, final int id, final ArrayList<HorizontalListitemBean> listData, final int position) {
        final View itemView = inflate.inflate(R.layout.horizontal_list_layout, null);
        TextView gameCategoryName = (TextView) itemView.findViewById(R.id.gameCategoryName);
        final TextView btnViewAll = (TextView) itemView.findViewById(R.id.btnViewAll);
        ImageView left_slide = (ImageView) itemView.findViewById(R.id.left_slide);
        ImageView right_slide = (ImageView) itemView.findViewById(R.id.right_slide);
        final LinearLayout recycler_layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);
        final RecyclerView horizontalListview = (RecyclerView) itemView.findViewById(R.id.horizontalListview);
        horizontalListview.setTag(id);

        String name_html = stripHtml(name);
        gameCategoryName.setText(name_html);


        final LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        horizontalListview.hasFixedSize();
        horizontalListview.setLayoutManager(linearLayoutManager);
        int spanCount = listData.size(); // 3 columns
        final int spacing = 20; // 50px
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        boolean includeEdge = false;
        horizontalListview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        try {
            ViewTreeObserver vto = recycler_layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recycler_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width = recycler_layout.getMeasuredWidth();
                    int height = recycler_layout.getMeasuredHeight();
                    setlistadapter(horizontalListview, listData, width - (spacing * 4));
                }
            });

            right_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int firstVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition + 4;
                    horizontalListview.smoothScrollToPosition(firstVisiblePosition);
                }
            });

            left_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                    firstVisiblePosition = firstVisiblePosition - 4;
                    if (firstVisiblePosition > 0)
                        horizontalListview.smoothScrollToPosition(firstVisiblePosition);
                    else horizontalListview.smoothScrollToPosition(0);

                }
            });
            btnViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadAllGame(id);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamic_horizontalViews_layout.addView(itemView);
    }

    public static String stripHtml(String html) {
        return html.replaceAll("\\<[^>]*>", "");
    }

    private void setlistadapter(RecyclerView horizontal_listview, ArrayList<HorizontalListitemBean> listData, int i) {
        if (listData.size() > 0) {
            NetworkGameAdapter adpater = new NetworkGameAdapter(listData, getActivity(), i);
            horizontal_listview.setAdapter(adpater);
        }
    }

    public class NetworkGameAdapter extends RecyclerView.Adapter<NetworkGameAdapter.DataObjectHolder> {
        ArrayList<HorizontalListitemBean> images;
        Context context;
        int width;
        String name;

        public NetworkGameAdapter(ArrayList<HorizontalListitemBean> images, Context context, int width) {
            this.images = images;
            this.context = context;
            this.width = width;
        }

        @Override
        public NetworkGameAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_network_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(NetworkGameAdapter.DataObjectHolder holder, final int position) {
            try {
                String image_url = images.get(position).getImage();
                final int id = images.get(position).getId();
                final String type = images.get(position).getType();

                holder.gridview_item.setFocusable(true);
                holder.horizontal_imageView.setFocusable(true);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.horizontal_imageView.setLayoutParams(layoutParams);
                holder.horizontal_imageView.setVisibility(View.VISIBLE);
                holder.gridview_item.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(image_url))
//                    Picasso.with(context.getApplicationContext()).load(image_url).fit().centerInside().placeholder(R.drawable.loader_network).into(holder.horizontal_imageView);
                    Image.loadGridImage(image_url,holder.horizontal_imageView);
                else
                    holder.horizontal_imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loader_network));


                holder.horizontal_imageView.setOnClickListener(v -> {
                    try {
                        presenter.loadGameDetails(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private GridViewItem gridview_item;
            private DynamicImageView horizontal_imageView;

            public DataObjectHolder(View itemView) {
                super(itemView);
                gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);
                horizontal_imageView = (DynamicImageView) itemView.findViewById(R.id.horizontal_imageView);
            }
        }
    }

    private void loadAllGame(final int id) {
        game_dynamic_list_scroll.setVisibility(View.GONE);
        game_progress.setVisibility(View.VISIBLE);

        Thread thread = new Thread(() -> {
            final JSONArray response = JSONRPCAPI.getAllGames(id, 1000, 0, 0);
            if (response == null) return;
            handler.post(() -> {
                game_progress.setVisibility(View.GONE);
                gridviewallGames.setVisibility(View.VISIBLE);
                setGameData(response.toString());
            });
        });
        thread.start();
    }


    ArrayList<GridBean> grid_list = new ArrayList<>();

    public void setGameData(String data) {
        try {
            grid_list.clear();
            if (!TextUtils.isEmpty(data)) {
                JSONArray response = new JSONArray(data);
                for (int i = 0; i < response.length(); i++) {
                    grid_list.add(new GridBean(response.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridviewallGames.removeAllViews();
        GridAdapter adpater = new GridAdapter(grid_list, getActivity());
        gridviewallGames.setAdapter(adpater);
    }


    public class GridAdapter extends RecyclerView.Adapter<GridAdapter.DataObjectHolder> {
        private ArrayList<GridBean> list_data;
        private Context context;

        public GridAdapter(ArrayList<GridBean> list_data, Context context) {
            this.list_data = list_data;
            this.context = context;
        }

        public void setData(ArrayList<GridBean> list_data) {
            this.list_data = list_data;
        }


        @Override
        public GridAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.fragment_grid_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final GridAdapter.DataObjectHolder holder, final int position) {
            try {
                final String image_url = list_data.get(position).getImage();
                final String type = list_data.get(position).getType();

                holder.gridview_item.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);

                if (image_url != null)
                    Image.loadGridImage(holder.imageView.getContext(), image_url, holder.imageView);

                holder.imageView.setOnClickListener(v -> {
                    if (!list_data.get(position).getId().isEmpty())
                        presenter.loadGameDetails(Integer.parseInt(list_data.get(position).getId()));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list_data.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            private DynamicImageView imageView;
            private GridViewItem gridview_item;

            public DataObjectHolder(View itemView) {
                super(itemView);
                imageView = (DynamicImageView) itemView.findViewById(R.id.imageView);
                gridview_item = (GridViewItem) itemView.findViewById(R.id.gridview_item);

            }
        }
    }


}
