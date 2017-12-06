package demo.com.parallelspacewelecome.welcome;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * @author nzbao
 * @CreateTime 2017/11/28
 * @Desc 可以自定义是否可触摸(mTouchable),
 *            自定义滑动的快慢(setScrollDuration)
 */
public class MyViewPager extends ViewPager implements View.OnTouchListener {

    private boolean mTouchable = true;

    private boolean isTouching = false;

    public MyViewPager(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public void setTouchable(boolean mTouchable) {
        this.mTouchable = mTouchable;
    }

    public void setScrollDuration(int duration) {
        initViewPagerScroll(duration);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mTouchable) {
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mTouchable) {
            return !mTouchable;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }




    private void initViewPagerScroll(int duration) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            MyScroller scroller = new MyScroller(this.getContext());
            scroller.setmScrollDuration(duration);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching=false;
                break;

        }
        return false;
    }

    public boolean isTouching() {
        return isTouching;
    }

    private class MyScroller extends Scroller {

        private int mScrollDuration = 0;// 滑动速度

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        public void setmScrollDuration(int mScrollDuration) {
            this.mScrollDuration = mScrollDuration;
        }
    }
}
