package com.selecttvapp.parser;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Constants;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.bean.ListenGridBean;
import com.selecttvapp.ui.bean.More;
import com.selecttvapp.ui.bean.SeasonsBean;
import com.selecttvapp.ui.bean.SideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ocspl-72 on 27/12/17.
 */

public class Parser {

    /**
     * Use this factory method to parse on demand categories
     *
     * @param jsonArray Parameter 1.
     * @return A list of ondemand categories.
     */
    public static ArrayList<OnDemandList> parseOnDemandCategories(JSONArray jsonArray) {
        ArrayList<OnDemandList> categoryList = new ArrayList<>();
        try {
            if (jsonArray != null)
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject response = jsonArray.getJSONObject(i);
                        OnDemandList onDemandList = new OnDemandList(response);
                        if (!onDemandList.getName().equalsIgnoreCase("world")) {
                            categoryList.add(onDemandList);
                        }
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }


    /**
     * Use this factory method to parse sliders
     *
     * @param jsonArray Parameter 1.
     * @return A list of sliders.
     */
    public static ArrayList<Slider> parseSliders(JSONArray jsonArray) {
        ArrayList<Slider> sb = new ArrayList<>();
        try {
            if (jsonArray != null)
                if (jsonArray.length() > 0)
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject slider_object = jsonArray.getJSONObject(i);
                        Slider sliderBean = new Slider(slider_object);
                        sb.add(sliderBean);
                    }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * Use this factory method to parse sliders
     *
     * @param json Parameter 1.
     * @return A list of sliders.
     */
    public static ArrayList<Slider> parseSliders(JSONObject json) {
        ArrayList<Slider> sliders = new ArrayList<>();
        try {
            if (json.has("sliders")) {
                JSONArray slidersJsonArray = json.getJSONArray("sliders");
                for (int i = 0; i < slidersJsonArray.length(); i++) {
                    JSONObject jsonObject = slidersJsonArray.getJSONObject(i);
                    if (jsonObject.has("items")) {
                        JSONArray sliderItemsJsonArray = jsonObject.getJSONArray("items");
                        for (int j = 0; j < sliderItemsJsonArray.length(); j++) {
                            JSONObject jsonObject1 = sliderItemsJsonArray.getJSONObject(j);
                            sliders.add(new Slider(jsonObject1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sliders;
    }


    /**
     * Use this factory method to parse carousels
     *
     * @param jsonArray Parameter 1.
     * @return A list of carousels.
     */
    public static ArrayList<Carousel> parseCarousels(JSONArray jsonArray) {
        ArrayList<Carousel> carousels = new ArrayList<>();
        try {
            if (jsonArray != null)
                if (jsonArray.length() > 0)
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Carousel carousel = new Carousel(object);
                        if (carousel.getData_list().size() != 0) {
                            carousels.add(carousel);
                        }
                    }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carousels;
    }

    /**
     * Use this factory method to parse carousels
     *
     * @param object Parameter 1.
     * @return A list of carousels.
     */
    public static ArrayList<Carousel> parseCarousels(Object object) {
        ArrayList<Carousel> carousels = new ArrayList<>();
        try {
            JSONArray carouselsJsonArray = null;
            if (object instanceof JSONObject) {
                JSONObject json = (JSONObject) object;
                carouselsJsonArray = json.getJSONArray("carousels");
            }
            if (object instanceof JSONArray)
                carouselsJsonArray = (JSONArray) object;
            if (carouselsJsonArray != null)
                for (int i = 0; i < carouselsJsonArray.length(); i++) {
                    JSONObject jsonObject = carouselsJsonArray.getJSONObject(i);
                    carousels.add(new Carousel(jsonObject));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carousels;
    }

    /**
     * Use this factory method to parse shows/movies carousels
     *
     * @param jsonArray Parameter 1.
     * @param type      Parameter 2.
     * @return A list of carousels.
     */
    public static ArrayList<CauroselsItemBean> parseCarousels(JSONArray jsonArray, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();

        try {
            if (jsonArray != null)
                if (jsonArray.length() > 0)
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                        if (cauroselsItemBean.getType().isEmpty())
                            cauroselsItemBean.setType(type);
                        parse_list.add(cauroselsItemBean);
//                        if (object.has("popularity")) {
//                            if (object.getInt("popularity") > 0)
//                                parse_list.add(cauroselsItemBean);
//                        } else
//                            parse_list.add(cauroselsItemBean);

                    }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }


    /**
     * Use this factory method to parse more page list items
     *
     * @param jsonArray Parameter 1.
     * @return A list of more page.
     */
    public static ArrayList<More> parseMoreData(JSONArray jsonArray) {
        ArrayList<More> mMoreArray = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mMoreArray.add(new More(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMoreArray;
    }

    /**
     * Use this factory method to parse radios
     *
     * @param jsonArray Parameter 1.
     * @return A list of radios.
     */
    public static ArrayList<ListenGridBean> parseRadioData(JSONArray jsonArray) {
        ArrayList<ListenGridBean> listenGenreBeans = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                listenGenreBeans.add(new ListenGridBean(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listenGenreBeans;
    }


    /**
     * Use this factory method to parse games categories
     *
     * @param gamesList Parameter 1.
     * @return A list of games categories.
     */
    public static ArrayList<SideMenu> parseGamesCategories(ArrayList<Carousel> gamesList) {
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

    /**
     * Use this factory method to parse games
     *
     * @param jsonArray Parameter 1.
     * @return A list of games.
     */
    public static ArrayList<Carousel> parseGames(JSONArray jsonArray) {
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


    /**
     * Use this factory method to parse favorite list
     *
     * @param json Parameter 1.
     * @return A list of favorite items.
     */
    public static HashMap<String, ArrayList<FavoriteBean>> parseFavoriteList(JSONObject json) {
        HashMap<String, ArrayList<FavoriteBean>> favoriteList = new HashMap<>();
        try {
            ArrayList<FavoriteBean> tvShowsList = new ArrayList<>();
            ArrayList<FavoriteBean> moviesList = new ArrayList<>();
            ArrayList<FavoriteBean> movieGenresList = new ArrayList<>();
            ArrayList<FavoriteBean> channelsList = new ArrayList<>();
            ArrayList<FavoriteBean> tvnetworksList = new ArrayList<>();
            ArrayList<FavoriteBean> videoLibrariesList = new ArrayList<>();

            if (json.has("tvnetworks")) {
                JSONArray tvnetworks_array = json.getJSONArray("tvnetworks");
                if (tvnetworks_array.length() > 0) {
                    for (int i = 0; i < tvnetworks_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(tvnetworks_array.getJSONObject(i));
                        tvnetworksList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-tvnetworks", tvnetworksList);
                }
            }

            if (json.has("api_channels")) {
                JSONArray channels_array = json.getJSONArray("api_channels");
                if (channels_array.length() > 0) {
                    for (int i = 0; i < channels_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(channels_array.getJSONObject(i));
                        channelsList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-channels", channelsList);

                }
            }

            if (json.has("movies")) {
                JSONArray movies_array = json.getJSONArray("movies");
                if (movies_array.length() > 0) {
                    for (int i = 0; i < movies_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(movies_array.getJSONObject(i));
                        moviesList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-movies", moviesList);
                }
            }

            if (json.has("videolibraries")) {
                JSONArray videolibraries_array = json.getJSONArray("videolibraries");
                if (videolibraries_array.length() > 0) {
                    for (int i = 0; i < videolibraries_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(videolibraries_array.getJSONObject(i));
                        videoLibrariesList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-videolibraries", videoLibrariesList);
                }
            }

            if (json.has("moviegenres")) {
                JSONArray moviegenres_array = json.getJSONArray("moviegenres");
                if (moviegenres_array.length() > 0) {
                    for (int i = 0; i < moviegenres_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(moviegenres_array.getJSONObject(i));
                        movieGenresList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-moviegenres", movieGenresList);
                }
            }

            if (json.has("shows")) {
                JSONArray shows_array = json.getJSONArray("shows");
                if (shows_array.length() > 0) {
                    for (int i = 0; i < shows_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(shows_array.getJSONObject(i));
                        tvShowsList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-shows", tvShowsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RabbitTvApplication.getMyfavoriteList().size() > 0)
                favoriteList = RabbitTvApplication.getMyfavoriteList();
        }
        return favoriteList;
    }

    public static ArrayList<SeasonsBean> parseSeasonsList(int payMode, final JSONArray jsonArray) {
        final ArrayList<SeasonsBean> seasonsBeans = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject demandMenuItem = jsonArray.getJSONObject(i);
                SeasonsBean seasonsBean = new SeasonsBean(demandMenuItem);

                if (payMode == Constants.FREE) {
                    if (seasonsBean.getFreeLinks())
                        seasonsBeans.add(seasonsBean);
                } else seasonsBeans.add(seasonsBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seasonsBeans;
    }


}
