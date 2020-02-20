package com.selecttvapp.network;

import android.text.TextUtils;
import android.util.Log;

import com.selecttvapp.BuildConfig;
import com.selecttvapp.RPC.JSONRPCClient;
import com.selecttvapp.RPC.JSONRPCException;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.ui.splash.BrandsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*import org.alexd.jsonrpc.JSONRPCClient;
import org.alexd.jsonrpc.JSONRPCException;*/

/**
 * Created by panda on 5/6/15.
 */
public class JSONRPCAPI {
    public static String REQUEST_POST = "POST";
    public static String REQUEST_GET = "GET";

    //    public static String JSON_URL = "http://rtv2.rabbittvgo.com/RPC2/";//"http://mdevvpc.rabbittvgo.com/RPC2/";/////"http://mgmt.rabbittvgo.com/RPC2/";//"http://rtv2.rabbittvgo.com/RPC2/";//"http://mgmt.rabbittvgo.com/RPC2/";//"http://rtv2.rabbittvgo.com/RPC2/";//"http://mgmt.rabbittvgo.com/RPC2/";//"http://mdev.rabbittvgo.com/RPC2/";//"http://mgmt.rabbittvgo.com/RPC2/";
    // public static String JSON_URL = "http://mdev.rabbittvgo.com/RPC2/";
    //public static String JSON_URL = "http://selecttv.freecast.com/RPC2/";
//    public static String JSON_URL = "http://stv.freecast.com/RPC2/";

  //public static String JSON_URL = "http://stv6.istamqulov.info/RPC2/"; //temperory use for testing purpose
  public static String JSON_URL = "https://mobile.freecast.com/";   //base url

//    public static String JSON_URL = "http://dev7stv.freecast.com/RPC2/"; //to testing personalization
    // public static String JSON_URL = "http://stv9.neatsoft.info/RPC2/"; //base url

//    public static String JSON_URL = "http://stv3.neatsoft.info/RPC2/";
//    public static String JSON_URL = "http://stv4.neatsoft.info/RPC2/";

    //public static String JSON_URL = "http://stv1.neatsoft.info/RPC2/";
//    public static String JSON_URL = "http://stv3.neatsoft.info/RPC2/";

    // public static String JSON_URL1 = "http://stv6.neatsoft.info/RPC2/";
//     public static String JSON_URL = "http://stv5.neatsoft.info/RPC2/";


    public JSONRPCAPI() {
    }

    private static JSONRPCClient getApiClient() {
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        return client;
    }

    public static JSONObject getResponseByURL(String api, String requestMethod) {  //request method = GET or POST
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(api);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            if (requestMethod.equalsIgnoreCase("post"))
                retObject = client.callJSONObject();
            else
                retObject = client.callJSONGetObject();
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getSideBar(String channelIDS) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("brands.sidebarbuttons", channelIDS);
            Log.d("brands::", " brands.sidebarbuttons" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getLeftmenu() {
//        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
//        client.setConnectionTimeout(5000);
//        client.setSoTimeout(5000);
//
//        try {
//            retObject = client.callJSONObject("user.get_left_menu");
//            longInfo("user.get_left_menu::" + retObject.toString());
//        } catch (JSONRPCException e) {
//            e.printStackTrace();
//        }
//        return retObject;
        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("user.get_left_menu", "A", BuildConfig.BRAND_SLUG);
//            retObject = client.callJSONArray("user.get_left_menu");
            longInfo("user.get_left_menu::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
//        return sortJsonArray(retObject);
    }

    public static JSONObject getAppConfig() {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("selecttvbox.config");
            longInfo("selecttvbox.config::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static BrandsEntity getBrands() {
        try {
            JSONObject retObject = getApiClient().callJSONObject("brands.get", BuildConfig.BRAND_SLUG);
            longInfo("brands.get" + retObject.toString());
            return Utilities.fromJson(retObject.toString(), BrandsEntity.class);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getServerTime() {
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        JSONObject jObj = null;
        String strTime = null;

        client.setConnectionTimeout(2000);
        client.setSoTimeout(2000);

        try {
            jObj = client.callJSONObject("server.time");
            strTime = jObj.getString("time");
            Log.e("SERVERTIME", strTime);
            //Log.e("SERVERTIME", );
        } catch (JSONRPCException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  Log.e("CATEGORIES", jObj.toString());

        return strTime;
    }

    public static JSONArray getAppsByCategories(int categoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("apps.list", "7", categoryId);

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getAllApps(Object categoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("apps.listAll", "7", categoryId);
            longInfo("apps.listAll::" + categoryId + "::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONArray getShowAllEpisodes(int showId, int freeorall) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("shows.episodes", showId, null, freeorall, "A");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    public static JSONArray getSportsCarousels(int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.sportsCarousels", "a", free);
            Log.d("Selecttv::", "selecttvbox.sportsCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    public static JSONArray getCategories() {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //retObj = client.callJSONA("youlive.channels");
            retArray = client.callJSONArray("youlive.categories", true);
            longInfo(":youlive.categories:::::::::" + retArray.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        if (retArray != null) Log.e("CATEGORIES", retArray.toString());

        return retArray;
    }

    public static JSONArray getChannels(int nCatNumber) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //retObj = client.callJSONA("youlive.channels");
            retArray = client.callJSONArray("youlive.channels", nCatNumber);
            longInfo(":youlive.channels:::::::::" + retArray.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }


        if (retArray != null) Log.e("CHANNELS", retArray.toString());

        return retArray;
    }

    public static JSONArray getChannelsForActor(int nCatNumber, String filter) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //retObj = client.callJSONA("youlive.channels");
            retArray = client.callJSONArray("youlive.channels", nCatNumber, filter);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }


        if (retArray != null) Log.e("CHANNELS", retArray.toString());

        return retArray;
    }

    public static JSONArray getStreams(int nChannelID) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retObj = client.callJSONObject("youlive.scheduller", String.format("%d", nChannelID));
            try {
                retArray = retObj.getJSONObject(String.format("%d", nChannelID)).getJSONArray("videos");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        if (retArray != null) Log.e("SCHEDULER", retObj.toString());

        return retArray;
    }

    public static JSONObject getScheduller(String channelIDS) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObj = client.callJSONObject("youlive.scheduller", channelIDS, 25);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObj;
    }

    public static JSONObject getScheduller(String channelIDS, int timeOffset, int timeLimit) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(50000);
        client.setSoTimeout(50000);

        try {
            retObj = client.callJSONObject("youlive.schedullerTime", channelIDS, timeOffset, timeLimit);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObj;
    }

    public static JSONArray getMovieList() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("movie.list", "", 100, 0, "A");

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }


        return retArray;
    }


    public static JSONArray getAllTVfeaturedCarousels(int id, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.viewAll", id, limit, offset, free);
            Log.d("Selecttv::", " selecttvbox.viewAll:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getAppsList() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("apps.list", "7", "7");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retArray;
    }

    public static JSONArray getAppCategories() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("apps.categories", "7");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getAppCategories(String categoryType) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("apps.categories", "7", categoryType);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONArray getMovieListbyGenre(int nID) {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("movies.listByGenre", String.format("%d", nID), 100, 0);

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONArray getMovieListbyGenreWithStatus(int nID, int nStatus) {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("movies.listByGenre", String.format("%d", nID), 100, 0, nStatus);
            //retArray = client.callJSONArray("movies.listByGenre", 3, 10, 0, 4);

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }


    public static JSONArray getPrimetimeCarousels(String day, int paymode) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.primetimeAnytimeCarousels", day, paymode);
            Log.d("Selecttv::", "selecttvbox.primetimeAnytimeCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray primetimeAnytimeSlider(String day) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.primetimeAnytimeSlider", day, "A");
            Log.d("Selecttv::", "selecttvbox.sportsSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    public static JSONArray getAllMenus() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retArray = client.callJSONArray("app.menu");
            longInfo("appmenu::::::" + retArray.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONObject getMovieDetail(int nMovieID) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("movie.details", String.format("%d", nMovieID));
            longInfo("movie.details :: " + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //Shows

    public static JSONArray getShowListbyGenre(int nID) {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("shows.listByGenre", String.format("%d", nID), 100, 0);
            //retArray = client.callJSONArray("shows.listByGenre", 3, 10, 0, 4);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONObject getShowDetail(int nShowID) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("shows.details", String.format("%d", nShowID));
            Log.d("show_details:::", "::::" + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    //Network
    public static JSONArray getAllNetworks() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("networks.list", "", 100, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getNetworkList(int networkId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("shows.listByNetwork", networkId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getShowLinks(int showId, int seasonId, int episodeId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            Log.d("showid:::", ":::" + showId + ":::" + seasonId + "::" + episodeId);
            if (episodeId == 0) {
                retObject = client.callJSONObject("shows.linksList", showId, null, null);
            } else {
                retObject = client.callJSONObject("shows.linksList", showId, seasonId, episodeId);
            }
            Log.d("show result:::", "::::" + retObject);
            longInfo("show_result:::" + retObject);

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getMovieLinks(int movieId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("movie.linksList", movieId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    //Show Seasons

//    public static JSONArray getShowSeasons(int showId) {
//        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
//        client.setConnectionTimeout(5000);
//        client.setSoTimeout(5000);
//
//        try {
//            retObject = client.callJSONArray("shows.seasons", showId);
//        } catch (JSONRPCException e) {
//            e.printStackTrace();
//        }
//
//        return retObject;
//    }

    //old method
//    public static JSONArray getShowEpisodes(int showId, int seasonId) {
//        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
//        client.setConnectionTimeout(5000);
//        client.setSoTimeout(5000);
//
//        try {
//            retObject = client.callJSONArray("shows.episodes", showId, seasonId);
//        } catch (JSONRPCException e) {
//            e.printStackTrace();
//        }
//
//        return retObject;
//    }

//    public static JSONArray getShowEpisodes(int showId, int seasonId, int payMode) {
//        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
//        client.setConnectionTimeout(5000);
//        client.setSoTimeout(5000);
//
//        try {
//            retObject = client.callJSONArray("shows.episodes", showId, seasonId, payMode);
//        } catch (JSONRPCException e) {
//            e.printStackTrace();
//        }
//
//        return retObject;
//    }

    public static JSONObject getEpisodeDetail(int episodeId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("shows.episodeDetails", episodeId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //Kids APIS
    public static JSONArray getKidsCategories() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("kids.categories");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getKidSubCategories(int categoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("kids.subcategories", categoryId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getKidItems(int subCategoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("kids.items", subCategoryId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //Subscription APIs
    public static JSONArray getSubscriptionCategories() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("subscriptions.categories");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getHomeScreen() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("carousel.getHomeScreen");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getSubscriptionSubCategories(int categoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("subscriptions.subcategories", categoryId);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getSubscriptionItems(int subCategoryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("subscriptions.items", subCategoryId, 0, 100);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getRegister(String email, String username, String password, JSONObject data) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("auth.register", email, password, data);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getLogin(String username, String password, String brand_slug) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {

            retObject = client.callJSONObject("auth.login", username, password, brand_slug);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getForget(String email, String brand_slug) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("auth.forgot_password", email, brand_slug);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getRadioGenre() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getGenres", 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioGenreList(int genreId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getListByGenre", genreId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioLanguage() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getLanguages", 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioLanguageList(int languageId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getListByLanguage", languageId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioContinent() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getContinents", 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioCountry(int continentId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getCountries", continentId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioRegion(int countryId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getRegions", countryId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioCity(int countryId, int regionId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getCities", countryId, regionId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray getRadioLocationList(int continentId, int countryId, int regionId, int cityId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("radio.getListByLocation", continentId, countryId, regionId, cityId, 1000, 0);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONArray sortJsonArray(JSONArray array) {
        if (array == null) return null;
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString("name");
                    rid = rhs.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    public static JSONObject getPackageName(String appName) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("apps.getByName", appName, 7);
            longInfo("apps.getByName :: " + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getDecade(int categoryID) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("youlive.getDecades", categoryID);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONArray getChannelsByDecade(String decade, int categoryID) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("youlive.getChannelsByDecade", decade, categoryID);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONObject getChannelsById(int categoryID) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("youlive.getChannel", categoryID);
            longInfo("apps.getByName :: " + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray onSearchTerm(String term, int limit) {
        JSONArray retArray = null;
        JSONObject retObj = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //retObj = client.callJSONA("youlive.channels");
            if (limit > 0)
                retObj = client.callJSONObject("search.combined", term, limit);
            else
                retObj = client.callJSONObject("search.combined", term, 30);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }


        if (retObj != null) {
            Log.e("SEARCH RESULTS", retObj.toString());
            try {
                JSONArray tempArray = new JSONArray();
                String[] categories = {"network", "show", "movie", "actor", "station", "radio", "tvstation", "live"};
                //String[] categories = {"show","movie"};
                for (int k = 0; k < categories.length; k++) {
                    String iCategory = categories[k];
                    if (retObj.has(iCategory)) {
                        JSONObject iData = retObj.getJSONObject(iCategory);
                        if (iData.getInt("count") > 0) {
                            JSONArray iArray = iData.getJSONArray("items");
                            for (int i = 0; i < iArray.length(); i++) {
                                JSONObject iArrayData = iArray.getJSONObject(i);
                                tempArray.put(iArrayData);
                            }
                        }
                    }
                }
                if (tempArray.length() > 0) {
                    retArray = tempArray;
                }

            } catch (Exception ex) {

            }

        }

        return retArray;
    }

    public static JSONObject getSearchResult(String term, int limit) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("search.combined", term, limit);
            longInfo("search.combined::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getFavoriteList(String access_token) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("favorites.userList", access_token);
            longInfo("favorites.userList::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return retObject;
    }

    public static JSONObject removeFavorite(String access_token, String entity, Object entity_id) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("favorites.remove", access_token, entity, entity_id);
            Log.d("favorites.remove::", "" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONArray getMovieGenreList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("movies.genres_list");
            Log.d("Selecttv::", "movies.genres_list::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return sortJsonArray(retObject);
    }

    public static JSONObject addToFavorite(String access_token, String entity, Object entity_id) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("favorites.add", access_token, entity, entity_id);
        } catch (JSONRPCException e) {
            e.printStackTrace();


            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return retObject;
    }

    public static JSONObject updateUserProfile(String access, JSONObject json) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("auth.userUpdate", access, json);
            Log.d("auth.userUpdate::", ":::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return retObject;
    }

    public static JSONObject updatepassword(String access, String old, String newpass) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("auth.userChangePassword", access, old, newpass);
            Log.d("auth.userChangePasd::", "::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONObject getUserProfile(String access_token) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("auth.userProfile", access_token);
            Log.d("auth.userProfile::", "::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONArray getTvShowListbyCategory() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("selecttvbox.categories");
            //retArray = client.callJSONArray("shows.listByGenre", 3, 10, 0, 4);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONArray getShowcarouselsbycategory(String id) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.getShowsCarouselsByCategory", id, "A");
            Log.d("Selecttv::", "selecttvbox.getShowsCarouselsByCategory:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getShowcarouselsbycategory(String id, int paymode) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.getShowsCarouselsByCategory", id, paymode, "A");
            Log.d("Selecttv::", "selecttvbox.getShowsCarouselsByCategory:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getCategoriesSlider(String id) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.getShowsSliderByCategory", id);
            Log.d("Selecttv::", "selecttvbox.getShowsSliderByCategory:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getTVNetworkList(int networkId, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.showsByNetwork", networkId, limit, offset, free);
            longInfo("shows.listByNetwork:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getAllShow(int id) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.viewAll", id);
            Log.d("Selecttv::", " selecttvbox.viewAll:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getNetworkDetails(int nId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        //JSONRPCClient client = JSONRPCClient.create(TEST_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("networks.details", nId);
            Log.i("selecttv::", "networks.details::" + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    public static JSONArray getNewSubscriptionList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("subscriptions.items", 2669);
            Log.d("Selecttv::", "subscriptions.items:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getGamecarousels() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
//addded device parameter

        try {
            retObject = client.callJSONArray("games.carousels", 0, "A");
            Log.d("Selecttv::", "games.carousels:" + retObject.toString());
            longInfo("selectTv:::games.carousels::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getAllGames(int id, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.viewAll", id, limit, offset, free, "A");
            Log.d("Selecttv::", " selecttvbox.viewAll:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getGameMoreList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("games.list", "A", "more");
            Log.d("Selecttv::", "games.list:" + retObject.toString());
            longInfo("Selecttv::games.list::::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //New Api Implementation
    public static JSONArray getDemandMenuList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            retObject = client.callJSONArray("selecttvbox.ondemandSideMenu");
            longInfo("leftmenu::" + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getDemandCarousels(int paymode) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            retObject = client.callJSONArray("selecttvbox.ondemandCarousels", paymode, "A");
            Log.d("Selecttv::", "selecttvbox.ondemandCarousels:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getDemandSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.ondemandSlider");
            Log.d("Selecttv::", "selecttvbox.ondemandSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getShowcarousels(int paymode) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            retObject = client.callJSONArray("selecttvbox.showsCarousels", paymode, "A");
            Log.d("Selecttv::", "selecttvbox.showsCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getShowSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.showsSlider");
            Log.d("Selecttv::", "selecttvbox.showsSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getPPVShowsSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.ppvShowsSlider");
            Log.d("Selecttv::", "selecttvbox.ppvShowsSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getPPVmoviesSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.ppvMoviesSlider");
            Log.d("Selecttv::", "selecttvbox.ppvMoviesSlider" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getMovieListByPayperView() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
//addded device parameter

        try {
            retObject = client.callJSONArray("selecttvbox.ppvMoviesCarousels", "pay-per-view-movies", "A");
            Log.d("payperview::", "selecttvbox.ppvMoviesCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    public static JSONArray getShowListByPayperView() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
//addded device parameter

        try {
            retObject = client.callJSONArray("selecttvbox.ppvShowsCarousels", "pay-per-view-shows", "A");
            Log.d("payperview::", "selecttvbox.ppvShowsCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getSlideEntity(int slideId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("shows.getSlideEntity", slideId);
            Log.d("Selecttv::", "shows.getSlideEntity:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getMovieSlideEntity(int slideId) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("movies.getSlideEntity", slideId);
            Log.d("Selecttv::", "movies.getSlideEntity:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.e("selecttv::", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("selecttv::", str);
    }

    public static JSONArray getTVGenreDatabyId(int genreId, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.showsByGenre", genreId, limit, offset, free);
            Log.d("Selecttv::", "selecttvbox.showsByGenre:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //old method
    public static JSONArray getShowFreeEpisodes(int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("shows.episodes", showId, 0, 1, "A");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getShowEpisodes(int seasonId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        //JSONRPCClient client = JSONRPCClient.create(TEST_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("show.episodes", seasonId, "A");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getShowSeasons(String showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        //JSONRPCClient client = JSONRPCClient.create(TEST_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("show.seasons", showId, "A");
            longInfo("show.seasons" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getAllCarouselsData(int id, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.viewAll", id, limit, offset, free, "A");
            longInfo("Selecttv::selecttvbox.viewAll:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getTvShowListbyDecade() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("shows.decades");
            //retArray = client.callJSONArray("shows.listByGenre", 3, 10, 0, 4);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONArray getTVDecadeDatabyId(int genreId, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.showsByDecade", genreId, limit, offset, free);
            Log.d("Selecttv::", "selecttvbox.showsByDecade:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getAllMovies(int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.movies", free, "A");
            longInfo("selecttvbox.movies:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getMovieSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selecttvbox.moviesSlider");
            Log.d("Selecttv::", "selecttvbox.moviesSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getMovieListbyGenre(int nID, int limit, int offset, int free) {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            /*if(free==1){
                free=8;
            }else if(free==2){
                free=4;
            }*/
            Log.d("movies.listByGenre::", "status id:::" + free);


            // retArray = client.callJSONArray("movies.listByGenre", String.format("%d", nID), 12, offset,null,free,"A");
            retArray = client.callJSONArray("movies.listByGenre", String.format("%d", nID), limit, offset, free, "A");

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONArray getMovieRatingList() {
        JSONArray retArray = null;

        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
        try {
            //Log.e("SCHEDULLER", String.format("CHANNEL ID = %d", nChannelID));
            retArray = client.callJSONArray("movies.getMoviesRatings");
            //retArray = client.callJSONArray("shows.listByGenre", 3, 10, 0, 4);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retArray;
    }

    public static JSONArray getMovieListByrating(String rating, int limit, int offset, int free) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
//            retObject = client.callJSONArray("movies.getMoviesByRating", rating, 100, offset, free, "A");
            retObject = client.callJSONArray("movies.getMoviesByRating", rating, limit, offset, free, "A");
            Log.d("Selecttv::", "movies.getMoviesByRating:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getWebSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selectvbox.getWebSlider");
            Log.d("Selecttv::", "selectvbox.getWebSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getWebCarousels(int ppv) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
//addded device parameter

        try {
            retObject = client.callJSONArray("selecttvbox.getWebCarousels", "tv-shows-web-originals", ppv, "A");
            Log.d("Selecttv::", "selecttvbox.getWebCarousels:" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getKidsCarousels(int ppv) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);
//addded device parameter

        try {
            retObject = client.callJSONArray("selecttvbox.getKidsCarousels", "kids", ppv, "A");
            longInfo("Selecttv:: selecttvbox.getKidsCarousels::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONArray getKidsSliderList() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("selectvbox.getKidsSlider");
            Log.d("Selecttv::", "selectvbox.getKidsSlider:" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getschedullerRelated(int id) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            Log.i("selecttv::", ":::" + id);
            retObject = client.callJSONObject("youlive.schedullerRelated", String.format("%d", id), 10);
            longInfo("youlive.schedullerRelated:::" + retObject.toString());
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getGAMEDetail(int nGameID) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("games.details", String.format("%d", nGameID));
            Log.d("selecttv::", "games.details::" + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static Object getUserSubscription(String accessToken) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("user.get_user_subscriptions", accessToken);
        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return retObject;
    }

    public static JSONArray getAllUserSubscription() {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("user.get_subscriptions");
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }

    public static JSONObject getUserSubscriptionbyCode(String code) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("user.get_subscriptions", code);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }
        return retObject;
    }


    public static JSONObject setUserSubscription(String accessToken, String values) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("user.set_user_subscriptions", accessToken, values);
        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return retObject;
    }

    //get need help api
    public static JSONObject getNeedHelpURI(String api) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(api);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject();

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //get need help api
    public static JSONObject getYoutubeRelatedVideos(String api) {
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(api);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONGetObject();
            longInfo("related videos :: " + retObject);
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }


    //get Personalization TVShows list
    public static Object getPersonalizationTVShows(String accessToken) {
        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://selecttv.freecast.com/personalization/show_carousels/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
//            retObject = client.callJSONGetObject();
            retObject = client.callJSONArray("personalization.show_carousels");
            longInfo("personalization.show_carousels::" + retObject.toString());

        } catch (JSONRPCException e) {
            Log.e("getMessage():::", ":::::" + e.getMessage());
            e.printStackTrace();
            try {
                Log.e("getMessage():::", ":::::" + e.getMessage());
                Log.e("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //get Favorite Personalization TVShows list
    public static Object getFavoritePersonalizationTVShows(String accessToken) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_shows", accessToken);
            longInfo("personalization.favorite_shows::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.e("getMessage():::", ":::::" + e.getMessage());
                Log.e("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    Log.e("getmessage", "::" + e.getMessage());
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //add Favorite Personalization TVShows list
    public static Object addFavoritePersonalizationTVShows(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_shows", accessToken, showId);
            longInfo("add.personalization.favorite_shows::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //delete Favorite item from Personalization TVShows list
    public static Object deleteFavoritePersonalizationTVShowItem(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_shows", accessToken, showId, "delete");
            longInfo("delete.personalization.favorite_shows::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //get Personalization movies list
    public static Object getPersonalizationMovies(String accessToken) {
        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://selecttv.freecast.com/personalization/movie_genres/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
//            retObject = client.callJSONGetObject();
            retObject = client.callJSONArray("personalization.movie_genres");
            longInfo("personalization.movie_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //get Favorite Personalization movies list
    public static Object getFavoritePersonalizationMovies(String accessToken) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_movie_genres", accessToken);
            longInfo("personalization.favorite_movie_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.e("getMessage():::", ":::::" + e.getMessage());
                Log.e("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    Log.e("getmessage", "::" + e.getMessage());
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //add Favorite Personalization movies list
    public static Object addFavoritePersonalizationMovies(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_movie_genres", accessToken, showId);
            longInfo("add.personalization.favorite_movie_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //delete Favorite item from Personalization movies list
    public static Object deleteFavoritePersonalizationMovieItem(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_movie_genres", accessToken, showId, "delete");
            longInfo("delete.personalization.favorite_movie_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //get Personalization music list
    public static Object getPersonalizationMusic(String accessToken) {
        JSONArray retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://selecttv.freecast.com/personalization/music_genres/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
//            retObject = client.callJSONGetObject();
            retObject = client.callJSONArray("personalization.music_genres");
            longInfo("personalization.music_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.d("getMessage():::", ":::::" + e.getMessage());
                Log.d("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //get Favorite Personalization music list
    public static Object getFavoritePersonalizationMusic(String accessToken) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_music_genres", accessToken);
            longInfo("personalization.favorite_music_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Log.e("getMessage():::", ":::::" + e.getMessage());
                Log.e("getLocalized():::", ":::::" + e.getLocalizedMessage());
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject) {
                    Log.e("getmessage", "::" + e.getMessage());
                    return new JSONObject(e.getMessage());
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //add Favorite Personalization music list
    public static Object addFavoritePersonalizationMusic(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_music_genres", accessToken, showId);
            longInfo("add.personalization.favorite_music_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //delete Favorite item from Personalization music list
    public static Object deleteFavoritePersonalizationMusicItem(String accessToken, int showId) {
        JSONArray retObject = null;
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONArray("personalization.favorite_music_genres", accessToken, showId, "delete");
            longInfo("delete.personalization.favorite_music_genres::" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //delete Favorite item from Personalization music list
    public static JSONObject getKidsResponse(String method, String slug) {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL); //pass slug only
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject(method, slug, "A"); //dev api
//            retObject = client.callJSONObject(method, slug); //live
            longInfo("kids_response" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                Object obj = new JSONTokener(e.getMessage()).nextValue();
                if (obj instanceof JSONObject)
                    return new JSONObject(e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return retObject;
    }

    //delete Favorite item from Personalization music list
    public static JSONObject getOndemandSuggestions(String slug) {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
      //  JSONRPCClient client = JSONRPCClient.create("http://stv6.istamqulov.info/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("pages.get_front_page", slug, "A", BuildConfig.BRAND_SLUG);
//            retObject = client.callJSONObject("pages.get_front_page", slug);  //live api
            longInfo("ondemand_suggestions" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }
    public static JSONObject getFeaturedChannels(String slug) {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("pages.get_front_page", slug, "A", BuildConfig.BRAND_SLUG);
//            retObject = client.callJSONObject("pages.get_front_page", slug);  //live api
            longInfo("ondemand_suggestions" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //delete Favorite item from Personalization music list
    public static JSONObject getOndemandSubscriptions(String slug) {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("pages.get_front_page", slug, "A", BuildConfig.BRAND_SLUG);
//            retObject = client.callJSONObject("pages.get_front_page", slug);  //live api
            longInfo("ondemand_subscriptions" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject loadMyToolsSubscriptionsOffers() {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
       // JSONRPCClient client = JSONRPCClient.create("http://stv6.istamqulov.info/RPC2/");
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("pages.get_site_page", "ppv", "A");
//            retObject = client.callJSONObject("pages.get_front_page", slug);  //live api
            longInfo("My Tools Subscriptions Offers" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    //load my tool pay per view
    public static JSONObject loadMyToolsPPV() {
        JSONObject retObject = null;
//        JSONRPCClient client = JSONRPCClient.create("http://dev7stv.freecast.com/RPC2/");
        JSONRPCClient client = JSONRPCClient.create(JSON_URL);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            retObject = client.callJSONObject("pages.get_front_page", "pay-view-featured", "A", BuildConfig.BRAND_SLUG);
            longInfo("mytools_ppv" + retObject.toString());

        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

    public static JSONObject getVideoAd(String api, String requestMethod) {  //request method = GET or POST
        JSONObject retObject = null;
        JSONRPCClient client = JSONRPCClient.create(api);
        client.setConnectionTimeout(5000);
        client.setSoTimeout(5000);

        try {
            if (requestMethod.equalsIgnoreCase("post"))
                retObject = client.callJSONObject();
            else
                retObject = client.callJSONGetObject();
        } catch (JSONRPCException e) {
            e.printStackTrace();
        }

        return retObject;
    }

}

