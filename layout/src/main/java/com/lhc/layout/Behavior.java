package com.lhc.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 作者：lhc
 * 时间：2018/5/11.
 */
public abstract class Behavior {

    Behavior(Context context, AttributeSet set) {

    }

    void onLayoutFinish(View parent, View child) {

    }

    void onTouchMove(View parent, View child, MotionEvent event, float x, float y, float oldx, float oldy) {

    }


    void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    void onNestedPreScroll(View scrollView, View target, int dx, int dy, int[] consumed) {

    }

    boolean onNestedPreFling(View scrollView, View target, Scroller scroller, float velocityX, float velocityY) {
        return false;
    }

    boolean onNestedFling(View scrollView, View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

}
