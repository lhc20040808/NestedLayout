package com.lhc.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 作者：lhc
 * 时间：2018/5/11.
 */
public class NestedLinearLayout extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener, NestedScrollingParent {

    private static String TAG = "nested";
    private static Class[] CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
    private float lastX;
    private float lastY;
    private Scroller scroller;

    public NestedLinearLayout(Context context) {
        this(context, null);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (params.mBehavior != null) {
                params.mBehavior.onLayoutFinish(this, child);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX();
                float moveY = event.getRawY();
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    if (params.mBehavior != null) {
                        params.mBehavior.onTouchMove(this, child, event, moveX, moveY, lastX, lastY);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        boolean flag = true;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (params.mBehavior != null) {
                flag = flag & params.mBehavior.onNestedPreFling(this, target, scroller, velocityX, velocityY);
            }
        }
        return flag;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (params.mBehavior != null) {
                params.mBehavior.onNestedPreScroll(this, target, dx, dy, consumed);
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (params.mBehavior != null) {
                params.mBehavior.onNestedScroll(this, child, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            }
        }
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private static class LayoutParams extends LinearLayout.LayoutParams {

        private Behavior mBehavior;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = null;
            try {
                a = c.obtainStyledAttributes(attrs, R.styleable.NestedLayout);
                mBehavior = parseBehavior(c, attrs, a.getString(R.styleable.NestedLayout_layout_behaviour));
            } finally {
                if (a != null)
                    a.recycle();
            }
        }

        private Behavior parseBehavior(Context c, AttributeSet attrs, String name) {
            if (TextUtils.isEmpty(name))
                return null;

            try {
                Class clz = Class.forName(name, true, c.getClassLoader());
                Constructor constructor = clz.getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                return (Behavior) constructor.newInstance(c, attrs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
