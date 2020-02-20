package com.selecttvapp.common;

/**
 * Created by Ocs pl-79(17.2.2016) on 9/22/2016.
 */
public class Constants {

    public static final int ALL = 0;
    public static final int FREE = 1;
    public static final int PAID = 2;

    /*common*/
    public static final String GOOGLE_ANALYTICS_TRACKING_ID = "google_analytics_tracking_id";

    public static final String FEATURED = "all";
    public static final String TV_SHOWS = "tv-shows";
    public static final String PRIMETIME = "primetime";
    public static final String NETWORKS = "tv-networks";
    public static final String MOVIES = "movies";
    public static final String WEB_ORIGINALS = "web-originals";
    public static final String KIDS = "kids";
    public static final String WORLD = "International";

    public static final String PPV_SHOWS = "ppv_shows";
    public static final String PPV_MOVIES = "ppv_movies";

    //SubCategories

    public static final String SHOW_SUB_NETWORKS = "byNetwork";
    public static final String SHOW_SUB_CATEGORY = "byCategory";
    public static final String SHOW_SUB_GENRE = "byGenre";
    public static final String SHOW_SUB_DECADE = "byDecade";


    public static final String MOVIE_SUB_GENRE = "byMovieGenre";
    public static final String MOVIE_SUB_RATING = "byRating";

    //On-Demand Reload method names
    public static final String LoadNetworkData = "LoadNetworkData";
    public static final String CATEGORY_FAST_DOWNLOAD = "fast_download";
    public static final String CATEGORY_APPMANAGER = "app_category";

    public static final String PAGE_POSITION = "PagePosition";
    public static final String CATEGORY_ID = "CategoryId";

    ////View types////
    public static final int VIEW_MAIN = 0;
    public static final int VIEW_TV = 1;
    public static final int VIEW_MOVIES = 2;
    public static final int VIEW_MOVIEDETAIL = 3;
    public static final int VIEW_NETWORK = 4;
    public static final int VIEW_KIDS = 5;
    public static final int VIEW_ADDS = 6;
    public static final int VIEW_HOME = 7;
    public static final int VIEW_RADIOSTATION = 8;
    public static final int VIEW_DEMAND = 9;
    public static final int VIEW_SEARCH = 10;
    public static final int VIEW_MYINTERST = 11;
    public static final int VIEW_MYACCOUNT = 12;
    public static final int VIEW_SUBSCRPTIONS = 13;
    public static final int VIEW_OTACABLE = 14;
    public static final int VIEW_GAME = 15;
    public static final int VIEW_MORE = 16;
    public static final int VIEW_PAYPERVIEW = 17;
    public static final int VIEW_APPMANAGER = 18;
    public static final int VIEW_LOGOUT = 19;
    public static final int VIEW_CHANNELS = 20;
    public static final int VIEW_MYSUBSCRIPTION = 21;
    public static final int VIEW_GETAPPS = 30;
    public static final int VIEW_APPDOWNLOAD = 22;
    public static final int VIEW_ONDEMAND_SUGGESTIONS = 23;
    public static final int VIEW_ONDEMAND_SUBSCRIPTIONS = 24;
    public static final int VIEW_MYTOOLS_SUBSCRIPTIONS_OFFERS = 25;
    public static final int VIEW_MYTOOLS_PPV = 26;
    public static final int TV_SHOWS_FEATURED = 27;
    public static final int LIVE_TV = 40;
    public static final int LIVE_MOVIES = 41;
    public static final int LIVE_CHANNELS = 42;
    public static final int LIVE_MUSIC = 43;
    public static final int LIVE_SPORTS = 44;
    public static final int LIVE_Kids = 45;
    public static final int LIVE_WORLD = 46;


    /*my interest screens tabs*/
    public static final String TAB_TV_SHOWS = "TV Shows";
    public static final String TAB_MOVIES = "Movies";
    public static final String TAB_MOVIE_GENRES = "Movie Genres";
    public static final String TAB_CHANNELS = "Channels";
    public static final String TAB_TV_NETWORKS = "TV Networks";
    public static final String TAB_VIDEO_LBRARIES = "Video libraries";

    /*screens*/
    public static final String SPLASH_SCREEN = "Splash screen";
    public static final String LOGIN_SCREEN = "Login screen";
    public static final String FORGET_PASSWORD_SCREEN = "Forget password";
    public static final String WELCOME_SCREEN = "Welcome screen";
    public static final String APP_MANAGER_SCREEN = "App manager";
    public static final String LOADING_SCREEN = "Loading screen";
    public static final String HOME_SCREEN = "Home screen";
    public static final String HOME_GRID_SCREEN = "Home grid screen";
    public static final String CHANNELS_SCREEN = "Channels";
    public static final String LIVE_CHANNELS_SCREEN = "Live Channels";
    public static final String CHANNELS_SCROLLING_SCREEN = "Channels Scrolling";
    public static final String HORIZONTAL_CHANNELS_LIST_SCREEN = "Horizontal Channels List";
    public static final String CHANNEL_VIEW_LIST_SCREEN = "Channels List screen";
    public static final String ON_DEMAND_SCREEN = "On-Demand";
    public static final String ON_DEMANT_FEATURED_SCREEN = "On-Demand-Featured";
    public static final String ON_DEMANT_TV_SHOWS_SCREEN = "On-Demand-Tv shows";
    public static final String ON_DEMANT_PRIMETIME_SCREEN = "On-Demand-Prime time";
    public static final String ON_DEMANT_NETWORKS_SCREEN = "On-Demand-Networks";
    public static final String NETWORK_DETAILS_SCREEN = "On-Demand-Network Details";
    public static final String ON_DEMANT_MOVIES_SCREEN = "On-Demand-Movies";
    public static final String ON_DEMANT_WEB_ORIGINALS_SCREEN = "On-Demand-Web originals";
    public static final String ON_DEMANT_KIDS_SCREEN = "On-Demand-kids";
    public static final String PAY_PER_VIEW_SCREEN = "Pay Per View";
    public static final String PAY_PER_VIEW_MOVIES_SCREEN = "Pay Per View- Movies";
    public static final String PAY_PER_VIEW_TV_SHOWS_SCREEN = "Pay Per View- Tv shows";
    public static final String LISTEN_SCREEN = "Listen";
    public static final String LISTEN_GENRE_LIST_SCREEN = "Listen- Genre List";
    public static final String LISTEN_GENRE_DETAILS_SCREEN = "Listen- Genre Details";
    public static final String SUBSCRIPTIONS_SCREEN = "Subscriptions";
    public static final String SUBSCRIPTIONS_SHOWS_SCREEN = "Subscriptions- Shows";
    public static final String SUBSCRIPTIONS_MOVIES_SCREEN = "Subscriptions- Movies";
    public static final String MOVIE_DETAILS_SCREEN = "Movie-Details";
    public static final String MOVIE_DETAILS_INFO_SCREEN = "Movie-Details-Info";
    public static final String MOVIE_DETAILS_TRAILER_SCREEN = "Movie-Details-Trailer";
    public static final String MOVIE_DETAILS_MORE_SCREEN = "Movie-Details-More";
    public static final String TV_SHOWS_EPISODES_SCREEN = "Tv show- Episodes";
    public static final String EPISODES_LIST_SCREEN = "Episodes List";
    public static final String TV_SHOWS_SHOW_INFORMATION_SCREEN = "Tv show- Show information";
    public static final String OVER_THE_AIR_SCREEN = "Over The Air";
    public static final String OVER_THE_AIR_PREMIUM_CHANNLES_SCREEN = "Over the air- Premium Channels";
    public static final String MY_INTERESTS_SCREEN = "My interests";
    public static final String MY_INTERESTS_TV_SHOWS_SCREEN = "My interests- Tv shows";
    public static final String MY_INTERESTS_MOVIES_SCREEN = "My interests- Movies";
    public static final String MY_INTERESTS_MOVIE_GENRES_SCREEN = "My interests- Movie Genres";
    public static final String MY_INTEREST_CHANNELS_SCREEN = "My interests- Channels";
    public static final String MY_INTEREST_TV_NETWORKS_SCREEN = "My interests- Tv Networks";
    public static final String MY_INTEREST_VIDEO_LIBRARIES_SCREEN = "My interests- Video Libraries";
    public static final String MY_ACCOUNT_SCREEN = "My Account";
    public static final String SUGGESTIONS = "Suggestions";
    public static final String MY_INTEREST = "My Interest";
    public static final String SUBSCRIPTIONS = "Subscriptions";
    public static final String SUBSCRIPTIONS_OFFERS = "Subscriptions Offers";
    public static final String PPV_DEAL_FINDER = "Pay Per View Deal Finder";
    public static final String MY_SUBSCRIPTIONS = "My Subscriptions";
    public static final String CHANNELS = "Channels";
    public static final String FEATURED_TITLE = "Featured";
    public static final String MORE = "More";
    public static final String APP_MANAGER = "App Manager";
    public static final String GAMES_SCREEN = "Games";
    public static final String HOME_MORE_SCREEN = "Home- More";
    public static final String FAST_DOWNLOAD_SCREEN = "Fast Download";
    public static final String FAST_DOWNLOAD_ESSENTIALS_SCREEN = "Fast Download- Essentials";
    public static final String FAST_DOWNLOAD_BROADCAST_SCREEN = "Fast Download- Broadcast";
    public static final String FAST_DOWNLOAD_CABLE_SCREEN = "Fast Download- Cable";
    public static final String FAST_DOWNLOAD_SUBSCRIPTIONS_SCREEN = "Fast Download- Subscriptions";
    public static final String FAST_DOWNLOAD_OTHERS_SCREEN = "Fast Download- Others";
    public static final String SEARCH_RESULTS_SCREEN = "Search results";
    public static final String VIDEO_VIEW_SCREEN = "Video view";
    public static final String VIDEO_TRAILER_SCREEN = "Video trailer";
    public static final String WEB_BROWSER_SCREEN = "web browser";
    public static final String EXO_PLAYER_SCREEN = "Exo player";
    public static final String YOUTUBE_PLAYER_SCREEN = "Youtube player";
    public static final String WEBVIEW_PLAYER_SCREEN = "Webview player";
    public static final String SLIDERS_AND_CAROUSELS_SCREEN = "Sliders and Carousels";
    public static final String CAROUSELS_SCREEN = "Carousels";
    public static final String ID = "id";
    public static final int REQUEST_CODE_SEARCH = 201;


    //slugs
    public static final String PAY_PER_VIEW_SHOWS = "pay-per-view-shows";
    public static final String PAY_PER_VIEW_MOVIES = "pay-per-view-movies";
    public static final String PAY_PER_VIEW_KIDS = "pay-per-view-kids";
    public static final String SUBSCRIPTION_TV = "subscription-tv";
    public static final String SUBSCRIPTIONS_MOVIES = "subscriptions-movies";
    public static final String TEST_BRAND_ADMIN = "testbrandadmin";
    public static final String TV = "tv";
    public static final String CHANNEL = "channels";
    public static final String MUSIC = "music";
    public static final String LIVE_SPORT = "livesports";
    public static final String KID = "kids";
    public static final String WORLDS = "world";
    public static final String EMPTY = "";
    public static final String FRONT_PAGE = "front_page";
    public static final String FEATURED_PAGE = "featured";
    public static final String CHANNELS_BY_CATEGORY = "channels/by_category";
    public static final String CHANNELS_CATEGORY = "channels/category";
    public static final String SHOWS = "shows";
    public static final String MOVIES_RECOMMENDED = "movies-recommended";


}
