package com.lhc.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：lhc
 * 时间：2018/5/11.
 */
public class HeadBehavior extends Behavior {
    private static final String TAG = "headBehavior";
    private float originY;

    public HeadBehavior(Context context, AttributeSet set) {
        super(context, set);

    }

    @Override
    void onLayoutFinish(View parent, View child) {
        super.onLayoutFinish(parent, child);
        if (originY == 0) {
            originY = child.getY();
        }
    }


    @Override
    void onNestedPreScroll(View scrollView, View target, int dx, int dy, int[] consumed) {
        if (scrollView.getScrollY() <= originY && dy > 0) {
            if (scrollView.getScrollY() + dy >= originY) {
                dy = (int) (originY - scrollView.getScrollY());
            }
            consumed[1] = dy;
            scrollView.scrollBy(0, dy);
        } else if (scrollView.getScrollY() >= 0 && dy < 0 && target.getScrollY() == 0) {
            if (scrollView.getScrollY() + dy < 0) {
                dy = -scrollView.getScrollY();
            }
            consumed[1] = dy;
            scrollView.scrollBy(0, dy);
        }
    }

    @Override
    void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }
}
