package com.selecttvapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Image;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.GridSpacingItemDecoration;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.bean.More;
import com.selecttvapp.ui.views.AutofitRecylerview;
import com.selecttvapp.ui.views.GridViewItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AutofitRecylerview more_fragment_app_list;
    private ProgressBar fragment_more_progress;
    private Activity activity;

    public MoreFragment() {
        // Required empty public constructor
    }


    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        more_fragment_app_list = (AutofitRecylerview) view.findViewById(R.id.more_fragment_app_list);
        fragment_more_progress = (ProgressBar) view.findViewById(R.id.fragment_more_progress);

        loadMoreGame();

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.HOME_MORE_SCREEN);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadMoreGame() {
        Thread thread = new Thread(() -> {
            final ArrayList<More> listItems = new ArrayList<>();
            try {
                JSONArray response = JSONRPCAPI.getGameMoreList();
                if (response == null) return;

                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    listItems.add(new More(object));
                }
                getActivity().runOnUiThread(() -> onLoadedMoreGames(listItems));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void onLoadedMoreGames(ArrayList<More> listItems) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        more_fragment_app_list.hasFixedSize();
        more_fragment_app_list.setLayoutManager(gridLayoutManager);
        int spanCount = 5; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        more_fragment_app_list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GameMoreList gameMoreList = new GameMoreList(getActivity(), listItems);
        more_fragment_app_list.setAdapter(gameMoreList);
    }


    public class GameMoreList extends RecyclerView.Adapter<GameMoreList.DataObjectHolder> {
        Context context;
        ArrayList<More> mores;


        public GameMoreList(Context context, ArrayList<More> mores) {
            this.context = context;
            this.mores = mores;


        }

        @Override
        public GameMoreList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.more_list_item, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(GameMoreList.DataObjectHolder holder, int position) {

            final String url = mores.get(position).getUrl();
            final String image_url = mores.get(position).getImage();
            String name = mores.get(position).getName();

            holder.more_gridview_text.setText(name);
            Image.loadGridImage(image_url,holder.more_gridview_item);
//            Picasso.with(context
//                    .getApplicationContext())
//                    .load(image_url)
//                    .fit()
//                    .placeholder(R.drawable.loader_network).into(holder.more_gridview_item);


            holder.more_gridview_item.setOnClickListener(v -> {
                Map<String, String> map = Utils.getPackageNameFromQuery(url);
                if (map.containsKey("id")) {
                    String packageName = map.get("id");
                    if (!packageName.isEmpty() && Utils.appInstalledOrNot(activity, packageName)) {
                        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        return;
                    }
                }
                if (!url.equalsIgnoreCase("")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mores.size();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder {
            GridViewItem more_gridview_item;
            TextView more_gridview_text;
            LinearLayout more_gridview_layout;

            public DataObjectHolder(View itemView) {
                super(itemView);
                more_gridview_layout = (LinearLayout) itemView.findViewById(R.id.more_gridview_layout);
                more_gridview_item = (GridViewItem) itemView.findViewById(R.id.more_gridview_item);
                more_gridview_text = (TextView) itemView.findViewById(R.id.more_gridview_text);
            }
        }
    }
}
