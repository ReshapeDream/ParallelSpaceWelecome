package demo.com.parallelspacewelecome.adapter.viewpager;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public interface IPagerChangeListener<T> {
    /**
     * 开始滑动
     * @param fadeOutView
     * @param fadeInView
     * @param fadeOutPosition
     * @param fadeInPosition
     */
    void onStartScroll(View fadeOutView, @Nullable View fadeInView, int fadeOutPosition, int fadeInPosition);

    /**
     * 页面选中
     * 手指离开屏幕
     * @param view
     * @param t
     * @param position
     * @param init
     */
    void onPagerSelected(View view, T t, int position, boolean init);

    /**
     * 滑动中
     * @param fadeOutView
     * @param fadeInView
     * @param fadeOutPosition
     * @param fadeInPosition
     * @param fadeOutTransOffset 0~1 越来越大，但最终不会等于1，0.99991212
     */
    void onPagerScrolling(View fadeOutView, @Nullable View fadeInView, int fadeOutPosition, int fadeInPosition, float fadeOutTransOffset);

    /**
     * 滑动结束
     * @param fadeOutView
     * @param fadeInView
     * @param fadeOutPosition
     * @param fadeInPosition
     * @param pagerChanged
     */
    void onScrollFinish(View fadeOutView, View fadeInView, int fadeOutPosition, int fadeInPosition, boolean pagerChanged);
}
