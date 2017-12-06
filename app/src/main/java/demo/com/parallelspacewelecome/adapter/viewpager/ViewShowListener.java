package demo.com.parallelspacewelecome.adapter.viewpager;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public interface ViewShowListener {
    /**
     * 展示出来的时候需要的操作
     * @param view
     * @param runnable 执行完成后可以执行的一些操作
     */
    void onViewShow(@Nullable View view, @Nullable Runnable runnable);

    /**
     * 隐藏起来的时候需要执行的操作
     * @param view
     * @param runnable 执行完成后可以执行的一些操作
     */
    void onViewHide(@Nullable View view, @Nullable Runnable runnable);

}
