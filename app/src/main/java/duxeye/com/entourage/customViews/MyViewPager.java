package duxeye.com.entourage.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import duxeye.com.entourage.R;

/**
 * Created by Ondoor (Hasnain) on 7/26/2016.
 */
public class MyViewPager extends ViewPager {

    private final boolean swipeAble;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);
        try {
            swipeAble = a.getBoolean(R.styleable.MyViewPager_swipeable, true);
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return swipeAble ? super.onInterceptTouchEvent(event) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeAble ? super.onTouchEvent(event) : false;
    }
}