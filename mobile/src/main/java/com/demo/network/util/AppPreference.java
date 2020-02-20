package com.demo.network.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.demo.network.common.DebugHelper;
import com.selecttvapp.RabbitTvApplication;

public class AppPreference {
	
	private static String TAG = AppPreference.class.getName();
	
	private static SharedPreferences usrPrefrence = null;
	
	protected static void initPrefrence(final Context mContext) {
		try {
			AppPreference.usrPrefrence = PreferenceManager.getDefaultSharedPreferences(mContext);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}
	
	public static void saveInt(final Context context, final int value, final String key) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				editIntPrefrenceValue(value, key);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}

	protected static void editIntPrefrenceValue(final int value, final String key) {
		final Editor editing = AppPreference.usrPrefrence.edit();
		try {
			editing.remove(key);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		editing.putInt(key, value);
		editing.commit();
	}
	
	public static void saveLong(final Context context, final long value, final String key) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				editLongPrefrenceValue(value, key);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}

	protected static void editLongPrefrenceValue(final long value, final String key) {
		final Editor editing = AppPreference.usrPrefrence.edit();
		try {
			editing.remove(key);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		editing.putLong(key, value);
		editing.commit();
	}
	
	public static void saveFloat(final Context context, final float value, final String key) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				editFloatPrefrenceValue(value, key);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}

	protected static void editFloatPrefrenceValue(final float value, final String key) {
		final Editor editing = AppPreference.usrPrefrence.edit();
		try {
			editing.remove(key);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		editing.putFloat(key, value);
		editing.commit();
	}
	
	public static int getInt(final Context context, final String key) {
		
		return getInt(context, key, 0);
	}
	
	public static int getInt(final Context context, final String key, int defult) {
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			return AppPreference.usrPrefrence.getInt(key, defult);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		return defult;
	}
	
	public static long getLong(final Context context, final String key) {
		
		return getLong(context, key, 0);
	}
	
	public static long getLong(final Context context, final String key, long defult) {
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			return AppPreference.usrPrefrence.getLong(key, defult);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		return defult;
	}
	
	public static float getFloat(final Context context, final String key, float defult) {
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			return AppPreference.usrPrefrence.getFloat(key, defult);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		return defult;
	}
	
	public static void saveString(final Context context, final String value, final String key) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				editStringPrefrenceValue(value, key);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}

	protected static void editStringPrefrenceValue(final String value, final String key) {
		final Editor editing = AppPreference.usrPrefrence.edit();
		try {
			editing.remove(key);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		editing.putString(key, value);
		editing.commit();
	}
	
	public static String getString(final Context context, final String key) {
		return getString(context, key, null);
	}
	
	public static String getString(final Context context, final String key, String defult) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			return AppPreference.usrPrefrence.getString(key, defult);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		return null;
	}
	
	public static void saveBoolean(final Context context, final boolean values, final String key) {
		
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				editBooleanPrefrenceValue(values, key);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}

	protected static void editBooleanPrefrenceValue(final boolean values, final String key) {
		final Editor editing = AppPreference.usrPrefrence.edit();
		try {
			editing.remove(key);
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		editing.putBoolean(key, values);
		editing.commit();
	}
	
	public static boolean getBoolean(final Context context, final String key, boolean defult) {
		
		boolean flag = false;
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(context);
			}
			if (AppPreference.usrPrefrence != null) {
				flag = AppPreference.usrPrefrence.getBoolean(key, defult);
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
		return flag;
	}
	
	public static boolean getBoolean(final Context context, final String key) {
		
		return getBoolean(context, key, false);
	}
	
	public static void clear() {
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(RabbitTvApplication.getInstance());
			}
			if (AppPreference.usrPrefrence != null) {
				final Editor editing = AppPreference.usrPrefrence.edit();
				editing.clear();
				editing.commit();
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}
	
	public static void remove(String key) {
		try {
			if (AppPreference.usrPrefrence == null) {
				AppPreference.initPrefrence(RabbitTvApplication.getInstance());
			}
			if (AppPreference.usrPrefrence != null) {
				final Editor editing = AppPreference.usrPrefrence.edit();
				editing.remove(key).commit();
			}
		} catch (final Exception exception) {
			DebugHelper.trackException(AppPreference.TAG, exception);
		}
	}
}
