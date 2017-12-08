package demo.com.parallelspacewelecome.welcome;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import demo.com.parallelspacewelecome.ImmersiveHelper;
import demo.com.parallelspacewelecome.R;
import demo.com.parallelspacewelecome.adapter.viewpager.BaseFragment;
import demo.com.parallelspacewelecome.adapter.viewpager.IPagerChangeListener;
import demo.com.parallelspacewelecome.adapter.viewpager.MFragmentStatePagerAdapter;


/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public class MainPagerActivity extends AppCompatActivity implements IPagerChangeListener<Fragment> {
    ArrayList<BaseFragment> fragments;
    private MFragmentStatePagerAdapter mainPagerAdapter;
    private MyViewPager mainContentViewpager;
    private String TAG = "MainPagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersiveHelper.setStatusTranslucent(this);
        setContentView(R.layout.welcome_main_activity);
        createFragments();
        initViewPager();
    }

    private void createFragments() {
        fragments = new ArrayList<>();
        fragments.add(new PagerFragment1());
        fragments.add(new PagerFragment2());
        fragments.add(new PagerFragment3());
    }

    private void initViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        mainPagerAdapter = new MFragmentStatePagerAdapter(fm, fragments);
        mainContentViewpager = findViewById(R.id.main_viewpager);
        mainContentViewpager.setAdapter(mainPagerAdapter);
        mainContentViewpager.setScrollDuration(1000);
//        mainContentViewpager.setCurrentItem(0);
        mainPagerAdapter.setPagerChangeListener(mainContentViewpager, this);
    }

    @Override
    public void onStartScroll(View fadeOutView, @Nullable View fadeInView, int fadeOutPosition, int fadeInPosition) {

    }

    @Override
    public void onPagerSelected(View view, Fragment fragment, int position, boolean init) {
        Log.i(TAG, "onPagerSelected");
        if (init && view != null) {
            setFideInViewTrans(view, position);
        }
        if (!init) {
            mainContentViewpager.setTouchable(false);
        }
    }

    @Override
    public void onPagerScrolling(View fadeOutView, @Nullable View fadeInView, int fadeOutPosition, int fadeInPosition, float fadeOutTransOffset) {
        BaseFragment fadeOutFragment = fragments.get(fadeOutPosition);
        BaseFragment fadeInFragment = fragments.get(fadeInPosition);

        if (fadeOutView == null || fadeInView == null) {
            return;
        }

        //设置背景色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int immersiveColor = (int) evaluator.evaluate(fadeOutTransOffset, fadeOutFragment.getBgColor(), fadeInFragment.getBgColor());
        int fadeOutColor = (int) evaluator.evaluate(fadeOutTransOffset, fadeOutFragment.getBgColor(), 0);
        int fadeInColor = (int) evaluator.evaluate(fadeOutTransOffset, 0, fadeInFragment.getBgColor());
        fadeInView.setBackgroundColor(fadeInColor);
        fadeOutView.setBackgroundColor(fadeOutColor);


        //设置背景图片旋转
        ImageView fadeInIv = fadeInView.findViewById(R.id.iv_lg_1);
        ImageView fadeOutIv = fadeOutView.findViewById(R.id.iv_lg_2);
        float degreeIn = 15f;
        float degreeOut = 40f;
        float rotationIn = fadeInPosition > fadeOutPosition ? (1 - fadeOutTransOffset) * degreeIn : (1 - fadeOutTransOffset) * -degreeIn;
        float rotationOut = fadeInPosition > fadeOutPosition ? fadeOutTransOffset * -degreeOut : fadeOutTransOffset * degreeOut;
        fadeOutIv.setPivotX(fadeOutView.getWidth() / 2);
        fadeOutIv.setPivotY(fadeOutView.getHeight());
        fadeOutIv.setRotation(rotationOut);

        fadeInIv.setPivotX(fadeInView.getWidth() / 2);
        fadeInIv.setPivotY(fadeInView.getHeight());
        fadeInIv.setRotation(rotationIn);

        //设置ViewPager背景色
        ViewParent parent = fadeInView.getParent();
        while (parent != null) {
            if (parent instanceof ViewPager) {
                ((ViewPager) parent).setBackgroundColor(immersiveColor);
                break;
            } else {
                parent = parent.getParent();
            }
        }
        //设置文字和其他
        //进入
        ImageView iLogo = fadeInView.findViewById(R.id.lLogo);
        TextView iTitle = fadeInView.findViewById(R.id.lTitle);
        TextView iDes = fadeInView.findViewById(R.id.lDes);
        RelativeLayout iRl = fadeInView.findViewById(R.id.rl);

        //退出
        ImageView oLogo = fadeOutView.findViewById(R.id.lLogo);
        TextView oTitle = fadeOutView.findViewById(R.id.lTitle);
        TextView oDes = fadeOutView.findViewById(R.id.lDes);
        RelativeLayout oRl = fadeOutView.findViewById(R.id.rl);

        degreeIn = 10f;
        degreeOut = 60f;
        rotationIn = fadeInPosition > fadeOutPosition ? (1 - fadeOutTransOffset) * degreeIn : (1 - fadeOutTransOffset) * -degreeIn;
        rotationOut = fadeInPosition > fadeOutPosition ? fadeOutTransOffset * -degreeOut : fadeOutTransOffset * degreeOut;
        oRl.setPivotX(oRl.getWidth() / 2);
        oRl.setPivotY(oRl.getHeight());
        oRl.setRotation(rotationOut);

        iRl.setPivotX(iRl.getWidth() / 2);
        iRl.setPivotY(iRl.getHeight());
        iRl.setRotation(rotationIn);

        float logoOutRatio;
        float titleOutRatio;
        float desOutRatio;

        float logoInRatio;
        float titleInRatio;
        float desInRatio;

        if (fadeInPosition > fadeOutPosition) {//右侧滑入 左侧滑出
            logoInRatio = 0.5f * (1 - fadeOutTransOffset);
            titleInRatio = 0.6f * (1 - fadeOutTransOffset);
            desInRatio = 0.8f * (1 - fadeOutTransOffset);

            logoOutRatio = -0.5f * fadeOutTransOffset;
            titleOutRatio = -0.6f * fadeOutTransOffset;
            desOutRatio = -0.8f * fadeOutTransOffset;
        } else {//左侧滑入 右侧滑出
            logoInRatio = -0.5f * (1 - fadeOutTransOffset);
            titleInRatio = -0.6f * (1 - fadeOutTransOffset);
            desInRatio = -0.8f * (1 - fadeOutTransOffset);

            logoOutRatio = 0.5f * fadeOutTransOffset;
            titleOutRatio = 0.6f * fadeOutTransOffset;
            desOutRatio = 0.8f * fadeOutTransOffset;
        }


        oLogo.setTranslationX(logoOutRatio * oLogo.getX());
        oTitle.setTranslationX(titleOutRatio * oTitle.getMeasuredWidth());
        oDes.setTranslationX(desOutRatio * oDes.getMeasuredWidth());

        iLogo.setTranslationX(logoInRatio * iLogo.getX());
        iTitle.setTranslationX(titleInRatio * iTitle.getMeasuredWidth());
        iDes.setTranslationX(desInRatio * iDes.getMeasuredWidth());
    }

    @Override
    public void onScrollFinish(View fadeOutView, View fadeInView, int fadeOutPosition, int fadeInPosition, boolean pagerChanged) {
        if (pagerChanged) {
            Log.i(TAG, "从" + fadeOutPosition + "页到" + fadeInPosition + "页,同一页面吗：" + String.valueOf(fadeInView == fadeOutView));
            resetFadeOutView(fadeOutView, fadeOutPosition);
            setFideInViewTrans(fadeInView, fadeInPosition);
        }
    }

    boolean isFirstShow=true;
    private void setFideInViewTrans(View fadeInView, int position) {
        in = position;
        int delay=100;
        switch (position) {
            case 0:
                if(isFirstShow){//首次展示没有滚动，会导致切换过快，delay时间加长
                    delay=700;
                    isFirstShow=false;
                }
                break;
            case 1:
                break;
            case 2:
                break;
        }
        handler.postDelayed(runShow, delay);
    }


    private void resetFadeOutView(View fadeOutView, int position) {
        out = position;
        int delay=100;
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
        handler.postDelayed(runHide, delay);
    }

    Handler handler = new Handler();
    RunShow runShow = new RunShow();
    RunHide runHide = new RunHide();
    int in;
    int out;

    class RunShow implements Runnable {

        @Override
        public void run() {
            fragments.get(in).onViewShow(null, new SetTouchableRun());
        }
    }

    class RunHide implements Runnable {

        @Override
        public void run() {
            fragments.get(out).onViewHide(null, null);
        }
    }

    class SetTouchableRun implements Runnable {

        @Override
        public void run() {
            mainContentViewpager.setTouchable(true);
        }
    }
}
