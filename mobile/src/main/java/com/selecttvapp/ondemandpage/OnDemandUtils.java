package com.selecttvapp.ondemandpage;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Constants;
import com.selecttvapp.model.CategoryBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Aug-17.
 */

public class OnDemandUtils {
    public static ArrayList<CategoryBean> getTVshowSubCategories() {
        ArrayList<CategoryBean> mTVshowSubCategory = new ArrayList<>();
        mTVshowSubCategory.add(new CategoryBean(Constants.TV_SHOWS, "TV Shows", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_NETWORKS, "by Network", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_CATEGORY, "by Category", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_GENRE, "by Genre", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_DECADE, "by Decade", 0));
        return mTVshowSubCategory;
    }

    public static ArrayList<CategoryBean> getPPVTVshowSubCategories() {
        ArrayList<CategoryBean> mTVshowSubCategory = new ArrayList<>();
        mTVshowSubCategory.add(new CategoryBean(Constants.PPV_SHOWS, "TV Shows", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_NETWORKS, "by Network", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_CATEGORY, "by Category", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_GENRE, "by Genre", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.SHOW_SUB_DECADE, "by Decade", 0));
        return mTVshowSubCategory;
    }

    public static ArrayList<CategoryBean> getPrimeTimeSubCategories() {
        ArrayList<CategoryBean> mTVshowSubCategory = new ArrayList<>();
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Monday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Tuesday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Wednesday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Thursday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Friday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Saturday", 0));
        mTVshowSubCategory.add(new CategoryBean(Constants.PRIMETIME, "Sunday", 0));
        return mTVshowSubCategory;
    }

    public static ArrayList<CategoryBean> getMoviesSubCategories() {
        ArrayList<CategoryBean> mMoviesSubCategory = new ArrayList<>();
        mMoviesSubCategory.add(new CategoryBean(Constants.MOVIES, "Movies", 0));
        mMoviesSubCategory.add(new CategoryBean(Constants.MOVIE_SUB_GENRE, "by Genre", 0));
        mMoviesSubCategory.add(new CategoryBean(Constants.MOVIE_SUB_RATING, "by Rating", 0));
        return mMoviesSubCategory;
    }

    public static ArrayList<CategoryBean> getPPVMoviesSubCategories() {
        ArrayList<CategoryBean> mMoviesSubCategory = new ArrayList<>();
        mMoviesSubCategory.add(new CategoryBean(Constants.PPV_MOVIES, "Movies", 0));
        mMoviesSubCategory.add(new CategoryBean(Constants.MOVIE_SUB_GENRE, "by Genre", 0));
        mMoviesSubCategory.add(new CategoryBean(Constants.MOVIE_SUB_RATING, "by Rating", 0));
        return mMoviesSubCategory;
    }

    public static boolean hasList(String key) {
        switch (key) {
            case Constants.FEATURED:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(Constants.FEATURED) && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(Constants.FEATURED).size() > 0)
                        return true;
                break;
            case Constants.TV_SHOWS:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey("tvshows") && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get("tvshows").size() > 0)
                        return true;
                break;
            case Constants.PRIMETIME:
                return true;
            case Constants.NETWORKS:
                break;
            case Constants.MOVIES:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey("movies") && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get("movies").size() > 0)
                        return true;
                break;
            case Constants.WEB_ORIGINALS:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(Constants.WEB_ORIGINALS) && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(Constants.WEB_ORIGINALS).size() > 0)
                        return true;
                break;
            case Constants.KIDS:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(Constants.KIDS) && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(Constants.KIDS).size() > 0)
                        return true;
                break;
            case Constants.PPV_SHOWS:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey("ppvtvshows") && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get("ppvtvshows").size() > 0)
                        return true;
                break;
            case Constants.PPV_MOVIES:
                if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap() != null && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().size() > 0)
                    if (RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey("ppvtvmovies") && RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get("ppvtvmovies").size() > 0)
                        return true;
                break;
        }
        return false;
    }
}
