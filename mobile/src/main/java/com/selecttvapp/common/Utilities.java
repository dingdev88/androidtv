package com.selecttvapp.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.network.dialogs.AppDialogClickListiner;
import com.demo.network.dialogs.AppDialogUserActions;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.selecttvapp.R;
import com.selecttvapp.ui.helper.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utilities {

    public static String UrlEncode(String url) {
        return url.replaceAll("Ã©", "%C3%A9").replaceAll("Ã‰", "%C3%89")
                .replaceAll("Ã¨", "%C3%A8").replaceAll("Ãˆ", "%C3%88")
                .replaceAll("Ã ", "%C3%A0").replaceAll("Ãª", "%C3%AA")
                .replaceAll("Ã€", "%C3%80").replaceAll("Ã§", "%C3%A7")
                .replaceAll("Ã¯", "%C3%AF").replaceAll("Ã�", "%C3%8F")
                .replaceAll("Ã‡", "%C3%87").replaceAll("Ã‹", "%C3%8B")
                .replaceAll("Ã«", "%C3%AB").replaceAll("Ã¹", "%C3%B9")
                .replaceAll("Ã»", "%C3%BB").replaceAll("Ã¢", "%C3%A2")
                .replaceAll("Ã¶", "%C3%B6");

    }

    public static boolean checkImageExtension(String extension) {
        String[] exten = {"bmp", "dds", "dng", "gif", "jpg", "png", "psd", "pspimage", "tga", "thm", "jpeg", "yuv"};
        for (String val : exten) {
            if (extension.toLowerCase().contains(val)) {
                return true;
            }
        }
        return false;
    }

    public static String getFileExtension(String url) {
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.', slashIndex);
        String filenameWithoutExtension = "";
        if (dotIndex == -1) {
            filenameWithoutExtension = url.substring(slashIndex + 1);
        } else {
            filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
        }
        return filenameWithoutExtension;
    }


    /**
     * Gets the last image id from the media store
     *
     * @return
     */
    public static String getLastImageId(Activity activity) {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor imageCursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("Camera", "getLastImageId::id " + id);
            Log.d("Camera", "getLastImageId::path " + fullPath);
            //imageCursor.close();
            return fullPath;
        } else {
            return "";
        }
    }

    public static boolean validateEmail(String email) {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        try {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public static void showAlert(Context context, String message) {

        Builder builder = new Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        TextView titleView = (TextView) dialog
                .findViewById(context.getResources()
                        .getIdentifier("alertTitle", "id",
                                "android"));

        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }


    public static String UTF8Encode(String value) {
        try {
            String val = new String(value.getBytes("UTF-8"));
            return val;
        } catch (UnsupportedEncodingException e) {
            Log.e("utf8", "conversion", e);
        }
        return value;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function get address from geo loaction (latitude and longitude):*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static List<Address> getAddressFromGeo(Context context, Double latitude, Double longitude) {
        List<Address> address = null;
        try {
            List<Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1);
            address = addresses;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    /**
     * returns the string, the first char uppercase
     *
     * @param target
     * @return
     */
    public final static String asUpperCaseFirstChar(final String target) {
        if ((target == null) || (target.length() == 0)) {
            return target;
        }
        return Character.toUpperCase(target.charAt(0))
                + (target.length() > 1 ? target.substring(1) : "");
    }

    public static String convertURL(String str) {
        String url = null;
        try {
            url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
                    .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
                    .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
                    .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
                    .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
                    .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
                    .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
                    .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
                    .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
                    .replace("|", "%7C").replace("}", "%7D"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getLatitudeLongitudeFromAddress(Context context, String addressval, String zipcode) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            if (!zipcode.equals("") && !zipcode.equals("null") && !zipcode.equals(null)) {
                address = coder.getFromLocationName(zipcode, 5);
            } else {
                address = coder.getFromLocationName(addressval, 5);
            }
            if (address != null && !address.isEmpty()) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                return location.getLatitude() + "||" + location.getLongitude();
            } else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);


        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void hidekeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm.isAcceptingText()) {
                Log.d("==>", "Software Keyboard was shown");
                View view = activity.getCurrentFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                Log.d("==>", "Software Keyboard was not shown");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createFakeSms(Context context, String sender,
                                     String body) {
        byte[] pdu = null;
        byte[] scBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD("0000000000");
        byte[] senderBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD(sender);
        int lsmcs = scBytes.length;
        byte[] dateBytes = new byte[7];
        Calendar calendar = new GregorianCalendar();
        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(lsmcs);
            bo.write(scBytes);
            bo.write(0x04);
            bo.write((byte) sender.length());
            bo.write(senderBytes);
            bo.write(0x00);
            bo.write(0x00); // encoding: 0 for default 7bit
            bo.write(dateBytes);
            try {
                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
                        "stringToGsm7BitPacked", new Class[]{String.class});
                stringToGsm7BitPacked.setAccessible(true);
                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,
                        body);
                bo.write(bodybytes);
            } catch (Exception e) {
            }

            pdu = bo.toByteArray();
        } catch (IOException e) {
        }

        Intent intent = new Intent();
        intent.setClassName("com.android.mms",
                "com.android.mms.transaction.SmsReceiverService");
        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
        intent.putExtra("pdus", new Object[]{pdu});
        intent.putExtra("format", "3gpp");
        context.startService(intent);
    }

    private static byte reverseByte(byte b) {
        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
    }

    public static String stripHtml(String html) {
        return html.replaceAll("\\<[^>]*>", "");
    }

    public static boolean isMyServiceRunning(Activity mActivity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
//	public static void setViewFocus(final Context mcontext,View mView){
//		try {
//			mView.setFocusable(true);
//			mView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					try {
//						if (hasFocus) {
//							// run scale animation and make it bigger
//                              /*  v.setScaleX(1.1F);
//                                v.setScaleY(1.1F);*/
//							Animation anim = AnimationUtils.loadAnimation(mcontext, R.anim.scale_in_tv);
//							v.startAnimation(anim);
//							anim.setFillAfter(true);
//						} else {
//							// run scale animation and make it smaller
//                               /* v.setScaleX(1F);
//                                v.setScaleY(1F);*/
//							Animation anim = AnimationUtils.loadAnimation(mcontext, R.anim.scale_out_tv);
//							v.startAnimation(anim);
//							anim.setFillAfter(true);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public static void setTextFocus(final Context mcontext, final View mView) {
        try {
            mView.setFocusable(true);
            mView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if (hasFocus) {
                            // run scale animation and make it bigger
                            mView.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.text_hover));

                        } else {
                            // run scale animation and make it smaller
                            mView.setBackgroundColor(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyBoard(Context context, EditText editText) {
        try {
            hideKeyBoard(context, editText.getWindowToken());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void hideKeyBoard(Context context, IBinder windowToken) {
        try {
            if (context != null && windowToken != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void showDialog(final Context context, final String title, final String message, String positiveBtn,
                                  String cancelButton, final AppDialogClickListiner hanlder) {

        try {
            if (context != null) {

                Builder builder = new Builder(context);
                builder.setMessage(message).setTitle(title);
                if (positiveBtn != null) {
                    builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (hanlder != null) {
                                hanlder.onDialogClick(AppDialogUserActions.OK);
                            }
                        }
                    });
                }
                if (cancelButton != null) {
                    builder.setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (hanlder != null) {
                                hanlder.onDialogClick(AppDialogUserActions.CANCEL);
                            }
                        }
                    });
                }
                builder.create().show();
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean checkIsTablet(Activity acti) {
        String inputSystem;
        inputSystem = android.os.Build.ID;
        Log.d("hai", inputSystem);
        Display display = acti.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        Log.d("hai", width + "");
        Log.d("hai", height + "");
        DisplayMetrics dm = new DisplayMetrics();
        acti.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(width / dm.xdpi, 2);
        double y = Math.pow(height / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("hai", "Screen inches : " + screenInches + "");
        if (screenInches > 6.0) {
            return true;
        } else {
            return false;
        }
    }

//	public static void setAnalytics(Tracker mTracker, String mText){
//		try {
//			Log.i("Analytics", "Setting screen name: " + mText);
//			mTracker.setScreenName("" + mText);
//			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public static List<String> toLowerCase(List<String> strings) {
        ListIterator<String> iterator = strings.listIterator();
        while (iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
        return strings;
    }


    public static void googleAnalytics(String screenName) {
        Tracker tracker = MyApplication.getTracker();
        tracker.setScreenName("Android - " + screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }
}
