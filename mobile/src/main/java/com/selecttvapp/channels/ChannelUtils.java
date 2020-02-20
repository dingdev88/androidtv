package com.selecttvapp.channels;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.common.XmlDuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelUtils {


	public static void showAlert(Context context, String message){

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

	public static String stripHtml(String html) {
		return html.replaceAll("\\<[^>]*>","");
	}

	public static long GetUnixTime() {

		DateFormat sdfgmt = DateFormat.getDateTimeInstance();
		sdfgmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		DateFormat sdfmad = DateFormat.getDateTimeInstance();
		TimeZone tz = TimeZone.getTimeZone(TimeZone.getDefault().getID());
		sdfmad.setTimeZone(tz);
		Date inptdate = null;
		String gmtTime = "";
		try {
			gmtTime = sdfgmt.format(new Date());
			inptdate = sdfmad.parse(gmtTime);


			return inptdate.getTime();

		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int convertDpToPixels(Context context) {
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
		return px;
	}
	public static int convertDpToPixels(Context context, int val) {
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, context.getResources().getDisplayMetrics());
		return px;
	}
	public static long getDuration(String dur) {
		if (!TextUtils.isEmpty(dur)) {
			int days = 0, hours = 0, minutes = 0, months = 0, seconds = 0, years = 0;
			try {
				XmlDuration dtFactory = new XmlDuration(dur);
				days = dtFactory.getDays();
				hours = dtFactory.getHours();
				minutes = dtFactory.getMinutes();
				months = dtFactory.getMonth();
				seconds = dtFactory.getSeconds();
				years = dtFactory.getYears();

			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			long duration = TimeUnit.DAYS.toMillis(days) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.HOURS.toMillis(hours) + TimeUnit.SECONDS.toMillis(seconds);
			return duration;
		}
		return 0;

	}
	public static String getDate(long time) {
		try {

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
			Date netDate = (new Date(time));
			return sdf.format(netDate);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String parseDateToddMMyyyy(String time) {
		if (!TextUtils.isEmpty(time)) {

			String inputPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
			if (time.contains(".")) {
				inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			}
			String outputPattern = "hh:mm aa";
			SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
			inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
			SimpleDateFormat outputFormat2 = new SimpleDateFormat(inputPattern, Locale.getDefault());
			TimeZone tz = TimeZone.getTimeZone(TimeZone.getDefault().getID());
			outputFormat.setTimeZone(tz);
			outputFormat2.setTimeZone(tz);

			Date date = null;
			String str = null;

			try {

				String gmtTime = outputFormat2.format(new Date());
				date = inputFormat.parse(time);
				str = outputFormat.format(date);
				return str;
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return "";
	}

	public static long getDurationFromDate(String startDate) {
		if (!TextUtils.isEmpty(startDate)) {
			try {

				String inputPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
				if (startDate.contains(".")) {
					inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
				}
				DateFormat df = new SimpleDateFormat(inputPattern, Locale.getDefault());
				Date dateOne = df.parse(startDate);
				long timeDiff = dateOne.getTime();
				return timeDiff;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}
	public static int GetUniqueID() {
		return (int) (Math.random()+ SystemClock.currentThreadTimeMillis());
	}

	public static boolean checkIsTablet(Activity mActivity) {

		if(mActivity!=null)
		{
			try {
				String inputSystem;
				inputSystem = android.os.Build.ID;

				DisplayMetrics dm = new DisplayMetrics();
				mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
				double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
				double y = Math.pow(dm.heightPixels/ dm.ydpi, 2);
				double screenInches = Math.sqrt(x + y);
				Log.d("Size", "Screen inches : " + screenInches + "");
				return screenInches > 6.0;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}


		return false;
	}
	public static String getYoutubeId(String data){
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(data);

		if(matcher.find()){
			return matcher.group();
		}
		return "";
	}
	public static String getYoutubePlaylistId(String data){
		Uri uri = Uri.parse(data);
		Set<String> args = uri.getQueryParameterNames();
		String limit = uri.getQueryParameter("list");
		return limit;
	}

	public static boolean isViewOverlapping(View firstView, View secondView) {
		int[] firstPosition = new int[2];
		int[] secondPosition = new int[2];

		firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		firstView.getLocationOnScreen(firstPosition);
		secondView.getLocationOnScreen(secondPosition);

		int r = firstView.getMeasuredWidth() + firstPosition[0];
		int l = secondPosition[0];
		return r >= l && (r != 0 && l != 0);
	}
}