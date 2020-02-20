package com.selecttvapp.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;

import com.selecttvapp.R;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BLiveInHack on 8/7/2015.
 */
public class DeviceUtils {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceAPILevel() {
        return Build.VERSION.SDK_INT + "";
    }

    public static String getDeviceOS() {
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                return fieldName;
            }
        }
        return "UNSPECIFIED";

    }


    public static String getDeviceTimeZone() {
        return TimeZone.getDefault().getID();
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDeviceMemory(Context activity) {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;

    }

    public static String getDeviceId(Context activity) {
        return Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getPinCode(Context activity) {
        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
        List<Address> address = null;
        if (geoCoder != null && activity.getResources().getBoolean(R.bool.is_location_enabled)) {
            try {
                // Log.e("LATLONG", latPoint+" "+lngPoint);
                address = geoCoder.getFromLocation(getLastlat(activity), getLastLng(activity), 1);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {

                if (address.size() > 0) {
                    String postCode = address.get(0).getPostalCode();

                    if (postCode != null) {
                        return postCode;
                    }
                } else {
                    //Toast.makeText(MainActivity.this, "No tax found", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                //Toast.makeText(MainActivity.this, "No tax found", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(MainActivity.this, "No tax found", Toast.LENGTH_SHORT).show();
            }
        }
        return "";
    }

    public static double getLastLng(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences("GCM",
                Context.MODE_PRIVATE);
        return Double.parseDouble(prefs.getString(PushUser.LAST_LONG, "0"));
    }

    public static double getLastlat(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences("GCM",
                Context.MODE_PRIVATE);
        return Double.parseDouble(prefs.getString(PushUser.LAST_LAT, "0"));
    }

    public static void putLastLng(Context activity, String lat) {
        SharedPreferences prefs = activity.getSharedPreferences("GCM",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(PushUser.LAST_LAT, lat);
    }

    public static void putLastlat(Context activity, String lng) {

        SharedPreferences prefs = activity.getSharedPreferences("GCM",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(PushUser.LAST_LONG, lng);
    }
}
