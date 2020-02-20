package com.selecttvapp.personalization;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Jul-17.
 */

public class PersonalizationContentFragment extends DialogFragment {
    private final String TYPE_TV_SHOWS = "tv_shows";
    private final String TYPE_MOVIES = "movies";
    private final String TYPE_MUSIC = "radio";

    private PresenterMyInterest instanceMyInterest;
    private ArrayList<PersonalizationShows> mPersonalizationShowsArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TextView mTitle;
    private ProgressBar progressBar;
    private JSONArray jsonCarousels;
    private JSONArray jsonfavouriteItems;

    private String TYPE;

    public PersonalizationContentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceMyInterest = PresenterMyInterest.getInstance();
        if (getArguments() != null) {
            TYPE = getArguments().getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personalization, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTitle = (TextView) view.findViewById(R.id.title);
        setTitle(mTitle);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        new ServiceGetData().execute();
    }

    @Override
    public void onDestroyView() {
        PresenterMyInterest.dismisDialog();
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (TYPE.equalsIgnoreCase(TYPE_MOVIES) || TYPE.equalsIgnoreCase(TYPE_MUSIC))
                setRecyclerViewGridLayoutManager(mRecyclerView, 4);

        } else {
            if (TYPE.equalsIgnoreCase(TYPE_MOVIES) || TYPE.equalsIgnoreCase(TYPE_MUSIC))
                setRecyclerViewGridLayoutManager(mRecyclerView, 3);
        }
    }

    private void setRecyclerViewGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        recyclerView.setHasFixedSize(true);
    }

    private class ServiceGetData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String[] objects) {
            try {
                jsonCarousels = getPersonalizationCarousels();
                jsonfavouriteItems = getPersonalizationFavoriteItems();
                return "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                progressBar.setVisibility(View.GONE);
                if (jsonCarousels != null) {
                    for (int i = 0; i < jsonCarousels.length(); i++) {
                        PersonalizationShows personalizationShows = new PersonalizationShows(jsonCarousels.getJSONObject(i));
                        mPersonalizationShowsArrayList.add(personalizationShows);
                    }

                    ArrayList<String> mFavoritItems = new ArrayList<>();
                    if (jsonfavouriteItems != null) {
                        for (int i = 0; i < jsonfavouriteItems.length(); i++) {
                            mFavoritItems.add(jsonfavouriteItems.getString(i));
                        }
                    }

                    if (TYPE.equalsIgnoreCase(TYPE_TV_SHOWS)) {
                        SnapAdapter snapAdapter = new SnapAdapter(getActivity(), TYPE, mPersonalizationShowsArrayList, mFavoritItems);
                        mRecyclerView.setAdapter(snapAdapter);
                    }

                    if (TYPE.equalsIgnoreCase(TYPE_MOVIES) || TYPE.equalsIgnoreCase(TYPE_MUSIC)) {
                        PersonalizationItemsAdapter adapter = new PersonalizationItemsAdapter(getActivity(), TYPE, mPersonalizationShowsArrayList, mFavoritItems);
                        onConfigurationChanged(getResources().getConfiguration());
                        mRecyclerView.setAdapter(adapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setTitle(TextView textView) {
        if (TYPE != null) {
            if (TYPE == TYPE_TV_SHOWS)
                textView.setText("Add Some TV Shows You Enjoy");
            if (TYPE == TYPE_MOVIES)
                textView.setText("Now Pick Your Most Watched Movie Categories");
            if (TYPE == TYPE_MUSIC)
                textView.setText("Lastly, Music Genres You Listen To");
        }
    }

    private JSONArray getPersonalizationCarousels() {
        try {
            Object object = null;
            if (TYPE != null) {
                if (TYPE == TYPE_TV_SHOWS)
                    object = JSONRPCAPI.getPersonalizationTVShows(PreferenceManager.getAccessToken());
                if (TYPE == TYPE_MOVIES)
                    object = JSONRPCAPI.getPersonalizationMovies(PreferenceManager.getAccessToken());
                if (TYPE == TYPE_MUSIC)
                    object = JSONRPCAPI.getPersonalizationMusic(PreferenceManager.getAccessToken());
            }

            if (object != null) {
                if (object instanceof JSONArray) {
                    return (JSONArray) object;
                } else if (object instanceof JSONObject) {
                    JSONObject json = (JSONObject) object;
                    if (json.has("name")) {
                        if (json.getString("name").equalsIgnoreCase("JSONRPCError")) {
                            if (json.has("message")) {
                                String message = json.getString("message");
                                if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                                    instanceMyInterest.showSessionExpiredDialog(getActivity());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONArray getPersonalizationFavoriteItems() {
        try {
            Object object = null;
            if (TYPE != null) {
                if (TYPE == TYPE_TV_SHOWS)
                    object = JSONRPCAPI.getFavoritePersonalizationTVShows(PreferenceManager.getAccessToken());
                if (TYPE == TYPE_MOVIES)
                    object = JSONRPCAPI.getFavoritePersonalizationMovies(PreferenceManager.getAccessToken());
                if (TYPE == TYPE_MUSIC)
                    object = JSONRPCAPI.getFavoritePersonalizationMusic(PreferenceManager.getAccessToken());
            }

            if (object != null) {
                if (object instanceof JSONArray) {
                    return (JSONArray) object;
                } else if (object instanceof JSONObject) {
                    JSONObject json = (JSONObject) object;
                    if (json.has("name")) {
                        if (json.getString("name").equalsIgnoreCase("JSONRPCError")) {
                            if (json.has("message")) {
                                String message = json.getString("message");
                                if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                                    instanceMyInterest.showSessionExpiredDialog(getActivity());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
