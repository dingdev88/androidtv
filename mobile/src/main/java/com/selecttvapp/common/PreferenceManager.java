package com.selecttvapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.selecttvapp.ui.helper.MyApplication;

/**
 * Created by Babin on 12/30/2015.
 */
public class PreferenceManager {
    private static final String PREFERENCE_TYPE = "UserPrefrence";
    private static SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TYPE, Context.MODE_PRIVATE);
    private Context context = MyApplication.getInstance();

    public static String getAccessToken() {
        return preferences.getString("AccessToken", "");
    }

    public static void setAccessToken(String AccessToken) {
        preferences.edit().putString("AccessToken", AccessToken).commit();
    }

    public static void setUsername(String username) {
        preferences.edit().putString("username", username).commit();
    }

    public static String getUsername() {
        return preferences.getString("username", "");
    }

    public static void setCity(String city) {
        preferences.edit().putString("city", city).commit();
    }

    public static String getCity() {
        return preferences.getString("city", "");
    }

    public static void setFirstName(String first_name) {
        preferences.edit().putString("first_name", first_name).commit();
    }

    public static String getFirstName() {
        return preferences.getString("first_name", "");
    }

    public static void setLastName(String last_name) {
        preferences.edit().putString("last_name", last_name).commit();
    }

    public static String getLastName() {
        return preferences.getString("last_name", "");
    }

    public static void setGender(String gender) {
        preferences.edit().putString("gender", gender).commit();
    }

    public static String getGender() {
        return preferences.getString("gender", "");
    }

    public static void setEmail(String gender) {
        preferences.edit().putString("email", gender).commit();
    }

    public static String getEmail() {
        return preferences.getString("email", "");
    }

    public static void setState(String state) {
        preferences.edit().putString("state", state).commit();
    }

    public static String getState() {
        return preferences.getString("state", "");
    }

    public static void setDateOfBirth(String date_of_birth) {
        preferences.edit().putString("date_of_birth", date_of_birth).commit();
    }

    public static String getDateOfBirth() {
        return preferences.getString("date_of_birth", "");
    }

    public static void setLastLogin(String last_login) {
        preferences.edit().putString("last_login", last_login).commit();
    }

    public static String getLastLogin() {
        return preferences.getString("last_login", "");
    }

    public static void setAddress1(String address_1) {
        preferences.edit().putString("address_1", address_1).commit();
    }

    public static String getAddress1() {
        return preferences.getString("address_1", "");
    }

    public static void setAddress2(String address_2) {
        preferences.edit().putString("address_2", address_2).commit();
    }

    public static String getAddress2() {
        return preferences.getString("address_2", "");
    }

    public static void setPostalCode(String postal_code) {
        preferences.edit().putString("postal_code", postal_code).commit();
    }

    public static String getPostalCode() {
        return preferences.getString("postal_code", "");
    }

    public static void setPhoneNumber(String phone_number) {
        preferences.edit().putString("phone_number", phone_number).commit();
    }

    public static String getPhoneNumber() {
        return preferences.getString("phone_number", "");
    }

    public static void setId(int id) {
        preferences.edit().putInt("id", id).commit();
    }

    public static int getId() {
        return preferences.getInt("id", 0);
    }

    public static boolean isDemandFirstTime() {
        return preferences.getBoolean("demandstate", true);
    }

    public static void setDemandFirstTime(boolean state) {
        preferences.edit().putBoolean("demandstate", state).commit();
    }

    public static void setSubscribedList(String list[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            sb.append(list[i]).append(",");
        }
        preferences.edit().putString("SubscribedList", sb.toString()).commit();
    }

    public static String[] geSubscribedList() {
        String[] playlists = preferences.getString("SubscribedList", "").split(",");
        return playlists;
    }

    public static boolean isFirstLogin() {
        return preferences.getBoolean("FirstLogin", false);
    }

    public static void setFirstLogin(boolean FirstLogin) {
        preferences.edit().putBoolean("FirstLogin", FirstLogin).commit();
    }

    public static long getNTPTime() {
        return preferences.getLong("nowAsPerDeviceTimeZone", 0);
    }

    //changed after 30/05/2017
    public static void setNTPTime(long nowAsPerDeviceTimeZone) {
        preferences.edit().putLong("nowAsPerDeviceTimeZone", nowAsPerDeviceTimeZone).apply();
    }
}