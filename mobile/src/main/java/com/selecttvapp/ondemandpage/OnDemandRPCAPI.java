package com.selecttvapp.ondemandpage;

import android.app.Activity;
import android.util.Log;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.callbacks.LoadMoreCarouselsListener;
import com.selecttvapp.callbacks.NetworkListener;
import com.selecttvapp.callbacks.OnDemandListener;
import com.selecttvapp.callbacks.OnDemandsListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.Network;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.RatingBean;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.dialogs.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 11-Aug-17.
 */

public class OnDemandRPCAPI {

    static Activity activity;
    static ProgressHUD mProgressHUD;

    // load ondemand page categories list
    public static void loadDemandMenuCategories(final OnDemandsListener onDemandsListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<OnDemandList> categoryList = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getDemandMenuList();
                    if (jsonArray != null) {
                        Log.d("m_jsonDemandListItems::", "::" + jsonArray);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject response = jsonArray.getJSONObject(i);
                                OnDemandList onDemandList = new OnDemandList(response);
                                if (!onDemandList.getName().equalsIgnoreCase("world")) {
                                    categoryList.add(onDemandList);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onDemandsListener.onLoadMenuCategories(categoryList);
                }
            }
        });
        thread.start();
    }

    //content Types
    // load feature data -> Constants.FEATURED
    // load tv shows -> Constants.TV_SHOWS
    // load primetime -> Constants.PRIMETIME
    // load networks -> Constants.NETWORKS
    // load movies -> Constants.MOVIES
    // load web originals -> Constants.WEB_ORIGINALS
    // load kids -> Constants.KIDS
    // load pay per view shows -> Constants.PPV_SHOWS
    // load pay per view movies -> Constants.PPV_MOVIES
    public static boolean loadOnDemandContent(String contentType, OnDemandListener onDemandListener) {
        try {
            ArrayList<Slider> sliders = new ArrayList<>();
            ArrayList<Carousel> carousels = new ArrayList<>();
            if (RabbitTvApplication.getInstance().getSliderBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null) {
                if (RabbitTvApplication.getInstance().getSliderBeanHashMap().size() > 0 && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0) {
                    if (RabbitTvApplication.getSliderBeanHashMap().size() > 0)
                        if (RabbitTvApplication.getSliderBeanHashMap().get(contentType) != null)
                            sliders = RabbitTvApplication.getSliderBeanHashMap().get(contentType);
                    if (RabbitTvApplication.getHorizontalBeanHashMap().size() > 0)
                        if (RabbitTvApplication.getHorizontalBeanHashMap().get(contentType) != null)
                            carousels = RabbitTvApplication.getHorizontalBeanHashMap().get(contentType);
                    if (sliders != null && carousels != null)
                        if (sliders.size() > 0 && carousels.size() > 0) {
                            onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                            return true;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }// end of loadFeaturedContent()

    // load feature data
    public static void loadingFeaturedData(Activity activity, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getDemandSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getDemandCarousels(payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put(Constants.FEATURED, sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put(Constants.FEATURED, carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load tv shows
    public static void LoadingTVshowData(Activity activity, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getShowSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getShowcarousels(payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put("tvshows", sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put("tvshows", carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load primetime
    public static void loadingPrimetime(Activity activity, final String day, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.primetimeAnytimeSlider(day);
                    JSONArray carousel_array = JSONRPCAPI.getPrimetimeCarousels(day, payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put("primetime", sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put("primetime", carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load movies
    public static void LoadingMovies(Activity activity, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getMovieSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getAllMovies(payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put("movies", sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put("movies", carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load web carousels
    public static void LoadingWebcarouselData(Activity activity, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getWebSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getWebCarousels(payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put(Constants.WEB_ORIGINALS, sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put(Constants.WEB_ORIGINALS, carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load kids
    public static void LoadingKidscarouselData(Activity activity, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getKidsSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getKidsCarousels(payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put(Constants.KIDS, sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put(Constants.KIDS, carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load PPV tv shows
    public static void LoadingPPVTVshowData(Activity activity, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getPPVShowsSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getShowListByPayperView();
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put("ppvtvshows", sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put("ppvtvshows", carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load PPV movies
    public static void LoadingPPVTMovieData(Activity activity, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getPPVmoviesSliderList();
                    JSONArray carousel_array = JSONRPCAPI.getMovieListByPayperView();
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    RabbitTvApplication.getSliderBeanHashMap().put("ppvtvmovies", sliders);
                    RabbitTvApplication.getHorizontalBeanHashMap().put("ppvtvmovies", carousels);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    // load PPV movies
    public static void loadingCategoryItems(Activity activity, final String id, final int payMode, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Slider> sliders = new ArrayList<>();
                    ArrayList<Carousel> carousels = new ArrayList<>();
                    JSONArray slider_array = JSONRPCAPI.getCategoriesSlider(id);
                    JSONArray carousel_array = JSONRPCAPI.getShowcarouselsbycategory(id, payMode);
                    sliders = parseSlider(slider_array);
                    carousels = parseCarousel(carousel_array);
                    onLoadedSlidersAndCarousels(sliders, carousels, onDemandListener);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                }
            }
        });
        thread.start();
    }

    public static void loadingMovieGenre(final String key, final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CategoryBean> secondSpinnerListItems = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getAllMenus();
                    if (jsonArray != null) {
                        Log.d("m_jsonmovieGenre::", "::" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject genre_object = jsonArray.getJSONObject(i);
                            if (genre_object.has("module")) {
                                String module = genre_object.getString("module");
//movies
                                if (key.equalsIgnoreCase("movie")) {
                                    if (module.equals("movies")) {
                                        JSONArray itemsarray = genre_object.getJSONArray("child");
                                        for (int j = 0; j < itemsarray.length(); j++) {
                                            JSONObject itemsobject = itemsarray.getJSONObject(j);
                                            CategoryBean categoryBean = new CategoryBean(itemsobject);
                                            categoryBean.setSlug(Constants.MOVIE_SUB_GENRE);
                                            secondSpinnerListItems.add(categoryBean);
                                        }
                                    }
                                } else {
                                    if (module.equals("tv")) {
                                        JSONArray itemsarray = genre_object.getJSONArray("child");
                                        for (int j = 0; j < itemsarray.length(); j++) {
                                            JSONObject itemsobject = itemsarray.getJSONObject(j);
                                            CategoryBean categoryBean = new CategoryBean(itemsobject);
                                            categoryBean.setSlug(Constants.SHOW_SUB_GENRE);
                                            secondSpinnerListItems.add(categoryBean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onSecondSpinnerListCallback(secondSpinnerListItems, onDemandListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingDecadeList(final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CategoryBean> secondSpinnerListitems = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getTvShowListbyDecade();
                    if (jsonArray != null) {
                        Log.d("category::", "Genrelist::" + jsonArray);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Category_object = jsonArray.getJSONObject(i);
                                CategoryBean categoryBean = new CategoryBean(Category_object);
                                categoryBean.setSlug(Constants.SHOW_SUB_DECADE);
                                secondSpinnerListitems.add(categoryBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onSecondSpinnerListCallback(secondSpinnerListitems, onDemandListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingMovieRatingList(final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CategoryBean> secondSpinnerListitems = new ArrayList<>();
                ArrayList<RatingBean> ratingList = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getMovieRatingList();
                    if (jsonArray != null) {
                        Log.d("m_jsonmovieGenre::", "::" + jsonArray);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Category_object = jsonArray.getJSONObject(i);
                                CategoryBean categoryBean = new CategoryBean(Category_object);
                                categoryBean.setId(i);
                                categoryBean.setSlug(Constants.MOVIE_SUB_RATING);
                                secondSpinnerListitems.add(categoryBean);

                                String type = "";
                                if (Category_object.has("slug")) {
                                    type = Category_object.getString("slug");
                                }
                                ratingList.add(new RatingBean(type, categoryBean.getName(), Constants.MOVIE_SUB_RATING, i));
                            }
                        }
                        if (ratingList.size() > 0)
                            onRatingListCallback(ratingList, onDemandListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onSecondSpinnerListCallback(secondSpinnerListitems, onDemandListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingCategoryList(final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CategoryBean> secondSpinnerData = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getTvShowListbyCategory();
                    if (jsonArray != null) {
                        Log.d("category::", "Genrelist::" + jsonArray);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CategoryBean categoryBean = new CategoryBean(object);
                                categoryBean.setSlug(Constants.SHOW_SUB_CATEGORY);
                                secondSpinnerData.add(categoryBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onSecondSpinnerListCallback(secondSpinnerData, onDemandListener);
                }
            }
        });
        thread.start();
    }

    // loads TVShows by genre
    public static void loadingTVShowsByGenre(final String genreId, final String type, final int payMode, final int LIMIT,
                                             final int OFFSET, final boolean canLoadMore, final CarouselsListener carouselsListener, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                try {
                    int g_id = Integer.parseInt(genreId);
                    if (type.equalsIgnoreCase("movie") || type.equalsIgnoreCase("m")) {
                        JSONArray carousel_array = JSONRPCAPI.getMovieListbyGenre(g_id, LIMIT, OFFSET, payMode);
                        listItems = OnDemandRPCAPI.parseArray(carousel_array, "M");
                    } else {
                        JSONArray carousel_array = JSONRPCAPI.getTVGenreDatabyId(g_id, LIMIT, OFFSET, payMode);
                        listItems = OnDemandRPCAPI.parseArray(carousel_array, "S");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    if (canLoadMore)
                        onLoadMoreItemsCallback(listItems, genreId, loadMoreCarouselsListener);
                    else
                        onLoadCarousels(listItems, type, carouselsListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingTVShowsByRating(final String slug, final String type, final int payMode, final int LIMIT, final int OFFSET,
                                              final boolean canLoadMore, final CarouselsListener carouselsListener, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                try {
                    JSONArray carousel_array = JSONRPCAPI.getMovieListByrating(slug, LIMIT, OFFSET, payMode);
                    listItems = OnDemandRPCAPI.parseArray(carousel_array, type);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    if (canLoadMore)
                        onLoadMoreItemsCallback(listItems, slug, loadMoreCarouselsListener);
                    else
                        onLoadCarousels(listItems, type, carouselsListener);
                }
            }
        });
        thread.start();
    }

    // load LoadingTVShowsByDecade
    public static void LoadingTVShowsByDecade(final String genreId, final int payMode, final String type, final int LIMIT, final int OFFSET,
                                              final boolean canLoadMore, final CarouselsListener carouselsListener, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                try {
                    int g_id = Integer.parseInt(genreId);
                    JSONArray carousel_array = JSONRPCAPI.getTVDecadeDatabyId(g_id, LIMIT, OFFSET, payMode);
                    listItems = parseArray(carousel_array, "s");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    if (canLoadMore)
                        onLoadMoreItemsCallback(listItems, genreId, loadMoreCarouselsListener);
                    else
                        onLoadCarousels(listItems, type, carouselsListener);
                }
            }
        });
        thread.start();
    }

    public static void loadNetworkData(final String ID, final int payMode, final int LIMIT, final int OFFSET,
                                       final boolean canLoadMore, final NetworkListener networkListener, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                Network network = null;
                try {
                    int g_id = Integer.parseInt(ID);
                    JSONArray carousel_array = JSONRPCAPI.getTVNetworkList(g_id, LIMIT, OFFSET, payMode);
                    JSONObject networkDetailsResponse = JSONRPCAPI.getNetworkDetails(g_id);
                    listItems = OnDemandRPCAPI.parseArray(carousel_array, "S");
                    if (networkDetailsResponse != null) {
                        network = new Network(networkDetailsResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    if (canLoadMore)
                        onLoadMoreItemsCallback(listItems, ID, loadMoreCarouselsListener);
                    else
                        onNetworkDataLoadedCallback(listItems, network, networkListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingNetworkList(final OnDemandListener onDemandListener) {
        startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getAllNetworks();
                    if (jsonArray != null) {
                        Log.d("m_jsonmovieGenre::", "::" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject network_object = jsonArray.getJSONObject(i);
                            String type = "", name = "", image = "";
                            int id = 0;
                            if (network_object.has("id")) {
                                id = network_object.getInt("id");
                            }
                            if (network_object.has("name")) {
                                name = network_object.getString("name");
                            }
                            if (network_object.has("thumbnail")) {
                                image = network_object.getString("thumbnail");
                            }
//                    mMoviesSubCategory.add(new CategoryBean(Constants.NETWORKS, name, id));
                            listItems.add(new CauroselsItemBean(image, "n", id, name));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    onNetworkListCallback(listItems, "", onDemandListener);
                }
            }
        });
        thread.start();
    }

    public static void loadingViewAllData(final String id, final int payMode, final int LIMIT, final int OFFSET,
                                          final boolean canLoadMore, final OnDemandListener onDemandListener, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        if (!canLoadMore)
            startProgressDialog();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
                try {
                    JSONArray jsonArray = JSONRPCAPI.getAllCarouselsData(Integer.parseInt(id), LIMIT, OFFSET, payMode);
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                            listItems.add(cauroselsItemBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopProgressDialog();
                    if (canLoadMore)
                        onLoadMoreItemsCallback(listItems, id, loadMoreCarouselsListener);
                    else
                        onViewAllCallback(listItems, id, onDemandListener);
                }
            }
        });
        thread.start();
    }

    public static ArrayList<Slider> parseSlider(JSONArray slider_array) {
        ArrayList<Slider> sb = new ArrayList<>();
        try {
            if (slider_array != null) {
                Log.e("slider_array::", "::" + slider_array);
                if (slider_array.length() > 0) {
                    for (int i = 0; i < slider_array.length(); i++) {
                        JSONObject slider_object = slider_array.getJSONObject(i);
                        Slider slider = new Slider(slider_object);
                        sb.add(slider);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static ArrayList<Carousel> parseCarousel(JSONArray carousel_array) {
        ArrayList<Carousel> mCarousel = new ArrayList<>();
        Log.e("carousel_array ::", ":::" + carousel_array.toString());
        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (carousel_array.length() > 0) {
                    for (int i = 0; i < carousel_array.length(); i++) {
                        JSONObject object = carousel_array.getJSONObject(i);
                        Carousel carousel = new Carousel(object);
                        if (carousel.getData_list().size() != 0) {
                            mCarousel.add(carousel);
                        }
                    }
                    Log.d("horizontalListdata", "" + mCarousel.get(0).getData_list().size());
                    Log.d("horizontalListdata", "" + mCarousel.get(0).getData_list().get(0).getImage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mCarousel;
    }

    public static ArrayList<CauroselsItemBean> parseArray(JSONArray carousel_array, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();

        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (carousel_array.length() > 0) {
                    for (int i = 0; i < carousel_array.length(); i++) {
                        JSONObject object = carousel_array.getJSONObject(i);
                        CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                        if (cauroselsItemBean.getType().isEmpty())
                            cauroselsItemBean.setType(type);
                        if (object.has("popularity")) {
                            if (object.getInt("popularity") > 0) {
                                parse_list.add(cauroselsItemBean);
                            }
                        } else {
                            parse_list.add(cauroselsItemBean);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }


    private static void onLoadedSlidersAndCarousels(final ArrayList<Slider> sliders, final ArrayList<Carousel> carousels, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onLoadedSlidersAndCarousels(sliders, carousels);
            }
        });
    }

    private static void onRatingListCallback(final ArrayList<RatingBean> ratingList, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onRatingListCallback(ratingList);
            }
        });
    }

    private static void onGridAdapterListCallback(final ArrayList<CauroselsItemBean> listItems, final String type, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onGridAdapterListCallback(listItems, type);
            }
        });
    }

    private static void onNetworkListCallback(final ArrayList<CauroselsItemBean> listItems, final String type, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onNetworkListCallback(listItems, type);
            }
        });
    }

    private static void onViewAllCallback(final ArrayList<CauroselsItemBean> listItems, final String id, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onViewAllCallback(listItems, id);
            }
        });
    }

    private static void onNetworkDataLoadedCallback(final ArrayList<CauroselsItemBean> listItems, final Network network, final NetworkListener networkListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkListener.onLoadNetworkList(listItems, network);
            }
        });
    }

    private static void onSecondSpinnerListCallback(final ArrayList<CategoryBean> secondSpinnerListItems, final OnDemandListener onDemandListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onDemandListener.onSecondSpinnerListCallback(secondSpinnerListItems);
            }
        });
    }

    private static void onLoadCarousels(final ArrayList<CauroselsItemBean> listItems, final String type, final CarouselsListener carouselsListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                carouselsListener.onLoadCarousels(listItems, type);
            }
        });
    }

    private static void onLoadMoreItemsCallback(final ArrayList<CauroselsItemBean> listItems, final String itemId, final LoadMoreCarouselsListener loadMoreCarouselsListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadMoreCarouselsListener.onLoadCarousels(listItems, itemId);
            }
        });
    }

    public static void startProgressDialog() {
        stopProgressDialog();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
            }
        });
    }

    public static void stopProgressDialog() {
        if (mProgressHUD != null)
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
    }

}
