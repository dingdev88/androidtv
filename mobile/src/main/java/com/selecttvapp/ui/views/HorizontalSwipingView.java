/*
 * Copyright (c) 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.selecttvapp.ui.views;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.selecttvapp.R;


public class HorizontalSwipingView extends RelativeLayout {
    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
    private int mDraggingState = 0;
    private LinearLayout main_layout;
    private ViewDragHelperNew mDragHelper;
    private int mDraggingBorder;
    private int mVerticalRange;

    private int mHorizontalRange;
    private boolean mIsOpen;
    onSwipechangedListener monSwipechangedListener;
    private Context mContext;


    public class DragHelperCallback extends ViewDragHelperNew.Callback {
        @Override
        public void onViewDragStateChanged(int state) {
            if (state == mDraggingState) { // no change
                return;
            }
            if ((mDraggingState == ViewDragHelperNew.STATE_DRAGGING || mDraggingState == ViewDragHelperNew.STATE_SETTLING) &&
                    state == ViewDragHelperNew.STATE_IDLE) {
                // the view stopped from moving.

                if (mDraggingBorder == convertDpToPixels(getContext())) {
                    mIsOpen = false;
                    onStopDraggingToClosed();
                } else if (mDraggingBorder == mHorizontalRange) {
                    mIsOpen = true;
                }
            }
            if (state == ViewDragHelperNew.STATE_DRAGGING && state == ViewDragHelperNew.DIRECTION_HORIZONTAL) {
                onStartDragging();
            }
            if (state == ViewDragHelperNew.STATE_IDLE || state == ViewDragHelperNew.STATE_SETTLING) {
                onStopDraggingToClosed();
            }
            mDraggingState = state;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mDraggingBorder = left;
        }

        public int getViewVerticalDragRange(View child) {
            return mVerticalRange;
        }

        public int getViewHorizontalDragRange(View child) {
            return mHorizontalRange;
        }

        @Override
        public boolean tryCaptureView(View view, int i) {
            return (view.getId() == R.id.rightSliderLayout);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = mHorizontalRange;
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = mVerticalRange;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            final float rangeToCheck = mHorizontalRange;
            if (mDraggingBorder == convertDpToPixels(getContext())) {
                mIsOpen = false;
                return;
            }
            if (mDraggingBorder == rangeToCheck) {
                mIsOpen = true;
                return;
            }
            boolean settleToOpen = false;
            if (xvel > AUTO_OPEN_SPEED_LIMIT) { // speed has priority over position
                settleToOpen = true;
            } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = false;
            } else if (mDraggingBorder > rangeToCheck / 2) {
                settleToOpen = true;
            } else if (mDraggingBorder < rangeToCheck / 2) {
                settleToOpen = false;
            }

            final int settleDestX = settleToOpen ? mHorizontalRange : convertDpToPixels(getContext());

            if (mDragHelper.settleCapturedViewAt(settleDestX, 0)) {
                ViewCompat.postInvalidateOnAnimation(HorizontalSwipingView.this);
            }
        }
    }

    public HorizontalSwipingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsOpen = false;
    }

    @Override
    protected void onFinishInflate() {
        main_layout = (LinearLayout) findViewById(R.id.rightSliderLayout);
        mDragHelper = ViewDragHelperNew.create(this, 1.0f, new DragHelperCallback());
        mIsOpen = false;
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mHorizontalRange = (int) (main_layout.getMeasuredWidth() / 2);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void onStopDraggingToClosed() {
        // To be implemented
        monSwipechangedListener.onSwipeRealesed();
    }

    private void onStartDragging() {
        monSwipechangedListener.onSwipeStarted();
    }

    private boolean isQueenTarget(MotionEvent event) {
        int[] queenLocation = new int[2];
        main_layout.getLocationOnScreen(queenLocation);
        int leftLimit = queenLocation[0] + main_layout.getMeasuredWidth();
        int rightLimit = queenLocation[0];
        int X = (int) event.getRawX();
        return (X > rightLimit && X < leftLimit);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isQueenTarget(event) && mDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isQueenTarget(event) || isMoving()) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void computeScroll() { // needed for automatic settling.
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean isMoving() {
        return (mDraggingState == ViewDragHelperNew.STATE_DRAGGING ||
                mDraggingState == ViewDragHelperNew.STATE_SETTLING);
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void closeSlider() {

        final float rangeToCheck = mHorizontalRange;
        boolean settleToOpen = true;


        //mDraggingBorder=main_layout.getMeasuredWidth()/2;
        performTouch();
        if (mDragHelper.settleCapturedViewAt(main_layout.getMeasuredWidth() / 2, 0)) {
            ViewCompat.postInvalidateOnAnimation(HorizontalSwipingView.this);
        }
        //performTouch();
    }

    private void performTouch() {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 0;
        float x = 100.0f;
        float y = 0.0f;
// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );
        motionEvent.getPointerId(0);

// Dispatch touch event to view
        mDragHelper.shouldInterceptTouchEvent(motionEvent);

        MotionEvent motionEvent1 = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_MOVE,
                x,
                y,
                metaState
        );
        mDragHelper.shouldInterceptTouchEvent(motionEvent1);

        MotionEvent motionEvent2 = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        mDragHelper.shouldInterceptTouchEvent(motionEvent2);
    }

    public boolean isSliding() {
        return (mDraggingState == ViewDragHelperNew.STATE_DRAGGING ||
                mDraggingState == ViewDragHelperNew.STATE_SETTLING);
    }

    public interface onSwipechangedListener {
        public void onSwipeStarted();

        public void onSwipeRealesed();
    }

    public void setListener(onSwipechangedListener listener) {
        monSwipechangedListener = listener;
    }

    public void open() {
        mDragHelper.smoothSlideViewTo(main_layout, convertDpToPixels(getContext()), 0);
    }

    public static int convertDpToPixels(Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        return px;
    }

}

