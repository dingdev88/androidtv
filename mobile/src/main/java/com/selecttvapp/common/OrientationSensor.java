package com.selecttvapp.common;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import com.selecttvapp.callbacks.OrientationListener;

/**
 * Created by Appsolute dev on 02-Dec-17.
 */

public class OrientationSensor {
    private OrientationEventListener mOrientationEventListener;
    private Activity activity;
    private OrientationListener mListener;
    private int currentOrientation;

    public void setOrientationListener(OrientationListener mListener) {
        this.mListener = mListener;
    }

    public OrientationSensor(Activity activity) {
        this.activity = activity;
    }

    public static OrientationSensor getInstance(Activity activity) {
        return new OrientationSensor(activity);
    }

    public void senseOrientation() {
        mOrientationEventListener = new OrientationEventListener(activity,
                SensorManager.SENSOR_DELAY_NORMAL) {
            public void onOrientationChanged(int arg0) {
                int localOrientation = 0;
                if (arg0 >= 315 || arg0 < 45) {
                    localOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else if (arg0 >= 45 && arg0 < 135) {
                    localOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else if (arg0 >= 135 && arg0 < 225) {
                    localOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else if (arg0 >= 225 && arg0 < 315) {
                    localOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }

                if (currentOrientation != localOrientation) {
                    currentOrientation = localOrientation;
                    if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        if (mListener != null)
                            mListener.onPortraitView();
                    } else {
                        if (mListener != null)
                            mListener.onLandscapeView();
                    }
                }
            }
        };

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    public void stopSenseOrientation() {
        if (mOrientationEventListener != null)
            mOrientationEventListener.disable();
    }
}
