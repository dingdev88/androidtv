package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.network.LoaderWebserviceInterface;
import com.selecttvapp.presentation.views.ViewGamesListener;
import com.selecttvapp.ui.bean.SideMenu;
import com.selecttvapp.ui.fragments.GamesFragment;
import com.selecttvapp.ui.helper.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public class PresenterGames {
    private Activity activity;
    private ViewGamesListener gamesListener;
//    private Handler handler = new Handler();

    public PresenterGames() {
    }

    public PresenterGames(GamesFragment fragment) {
        activity = fragment.getActivity();
        gamesListener = fragment;
    }

    public void loadGames(final int paymode) {
        if (RabbitTvApplication.getInstance().getGamesList() != null && RabbitTvApplication.getInstance().getGamesList().size() > 0) {
            ArrayList<Carousel> gamesList = RabbitTvApplication.getInstance().getGamesList();
            final ArrayList<SideMenu> categories = parseCategories(gamesList);
            gamesListener.loadGames(gamesList);
            gamesListener.loadCategories(categories);
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("games.carousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    final ArrayList<Carousel> gamesList = parseGames(carousel_array);
                                    final ArrayList<SideMenu> categories = parseCategories(gamesList);
                                    RabbitTvApplication.getInstance().setGamesList(gamesList);
                                    activity.runOnUiThread(() -> {
                                        gamesListener.loadGames(gamesList);
                                        gamesListener.loadCategories(categories);
                                    });
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
                }, paymode, "A");
            }
        };
        thread.start();
    }

    public void loadGameDetails(final int id) {
        Thread thread = new Thread(() -> {
            try {
                JSONObject response = JSONRPCAPI.getGAMEDetail(id);
                if (response == null) return;
                if (response.has("url")) {
                    String url = response.getString("url");
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
                    if (!url.isEmpty()) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        activity.startActivity(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        });
        thread.start();
    }

    public ArrayList<SideMenu> parseCategories(ArrayList<Carousel> gamesList) {
        ArrayList<SideMenu> categories = new ArrayList<>();
        try {
            categories.add(new SideMenu("", "Games", "", ""));
            for (int i = 0; i < gamesList.size(); i++) {
                Carousel game = gamesList.get(i);
                categories.add(new SideMenu(game.getId() + "", game.getName(), "", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<Carousel> parseGames(JSONArray jsonArray) {
        ArrayList<Carousel> gamesList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Carousel game = new Carousel(jsonArray.getJSONObject(i));
                if (game.getData_list().size() > 0) {
                    gamesList.add(game);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gamesList;
    }

}
