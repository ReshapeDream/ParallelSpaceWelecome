package demo.com.parallelspacewelecome.adapter.viewpager;

import android.view.View;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc 当BaseFragment 创建View之后 回调
 */
public interface BaseFragmentViewListener {

    void onViewCreated(View view);
}
