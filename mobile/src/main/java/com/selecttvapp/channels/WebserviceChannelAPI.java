package com.selecttvapp.channels;


public class WebserviceChannelAPI {

    /**
     * headers related
     */

    public final static String AUTHORIZATION = "Authorization";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String CONTENT_JSON = "application/json";
    public final static String AUTH_HEADER_VALUE = "Basic aW9zOkw4dTBjNTZpU0UzUA==";
    public final static String BASE_URL1 = "http://api.freecast.com";
    //        public final static String BASE_URL1 = "http://54.225.41.165";
    private final static String BASE_URL = "http://qtv3.neatsoft.org";
    private final static String LIVE_API = "/live/api/v1/";
    private final static String LIVE_API1 = "/live/api/v2/android/";
    private final static String USER_API = "user/api/v1/";
    private final static String USER_API1 = "user/api/v2/android/";
    private final static String GUIDE_API = "guide/api/v1/";
    private final static String GUIDE_API1 = "guide/api/v2/android/";
    private final static String LOGIN_METHOD = "login/";
    private final static String CATEGORIES_METHOD = "categories/";
    public static String CATEGORIES_SLUG_METHOD = "";
    public static String CHANNEL_SLUG_METHOD = "";
    public final static String NESTED_CHANNELS_METHOD = "/channels/";
    public final static String SCHEDULE_METHOD = "/schedule/";
    private final static String CHANNEL_METHOD = "channels/";
    public final static String PROGRAMS_METHOD = "/programs/";
    public static String HOUR = "";
    public static String YEAR = "";
    public static String MONTH = "";
    public static String DATE = "";
    private final static String SUBSCRIPTIONS_METHOD = "subscriptions/";
    private final static String PROFILE_METHOD = "profile/";
    private final static String FAVORITES_METHOD = "favorites/";
    private final static String GENRE_METHOD = "genres/";
    public static String STREAM_METHOD = "/streams/";
    public static String PLAYLIST_METHOD = "/playlist/";


    /**
     * connection related
     */

    ///user Api///
    public static final String LOGIN = BASE_URL + USER_API + LOGIN_METHOD;
    public static final String SUBSCRIPTIONS = BASE_URL + USER_API + SUBSCRIPTIONS_METHOD;
    public static final String PROFILE = BASE_URL + USER_API + PROFILE_METHOD;
    public static final String FAVOURITES = BASE_URL + USER_API + FAVORITES_METHOD;

    //Live Api////
//    public static final String LIVE_CATEGORIES = BASE_URL + LIVE_API + CATEGORIES_METHOD;
//    public static final String LIVE_SELECTED_CATEGORIES = BASE_URL + LIVE_API + CATEGORIES_METHOD;
//    public static final String LIVE_LINEAR_CHANNEL_SCHEDULE = BASE_URL + LIVE_API + CHANNEL_METHOD;
//    public static final String LIVE_STREAM = BASE_URL + LIVE_API + CHANNEL_METHOD;

/*
    public static final String LIVE_CATEGORIES = BASE_URL1 + LIVE_API1 + CATEGORIES_METHOD;
    public static final String LIVE_SELECTED_CATEGORIES = BASE_URL1 + LIVE_API1 + CATEGORIES_METHOD;
    public static final String LIVE_LINEAR_CHANNEL_SCHEDULE = BASE_URL1 + LIVE_API1 + CHANNEL_METHOD;
    public static final String LIVE_STREAM = BASE_URL1 + LIVE_API1 + CHANNEL_METHOD;
    public static final String PROGRAMS = BASE_URL1 + LIVE_API1 + CHANNEL_METHOD;*/

    public static final String LIVE_CATEGORIES =  CATEGORIES_METHOD;
    public static final String LIVE_SELECTED_CATEGORIES =  CATEGORIES_METHOD;
    public static final String LIVE_LINEAR_CHANNEL_SCHEDULE = BASE_URL1 + LIVE_API1 + CHANNEL_METHOD;
    public static final String LIVE_STREAM = BASE_URL1 + LIVE_API1 + CHANNEL_METHOD;
    public static final String PROGRAMS =  CHANNEL_METHOD;

    public static final String CHANNEL = BASE_URL + LIVE_API + CHANNEL_METHOD;

    ///Guide Api///
    public static final String GUIDE_CATEGORIES = BASE_URL + GUIDE_API + CATEGORIES_METHOD;
    public static final String GUIDE_C = BASE_URL + GUIDE_API + GENRE_METHOD;

}
