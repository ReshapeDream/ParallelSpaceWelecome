package demo.com.parallelspacewelecome.welcome;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.com.parallelspacewelecome.R;
import demo.com.parallelspacewelecome.adapter.viewpager.BaseFragment;


/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public class PagerFragment1 extends BaseFragment {
    RelativeLayout rl;
    FrameLayout fl;
    ImageView lLogo;
    ImageView iv_lg_1;
    ImageView iv_lg_2;
    ImageView iv_sm_1;
    ImageView iv_sm_2;
    TextView lTitle;
    TextView lDes;

    private static final int color1 = 0xFF73C447;
    private static final int color2 = 0xFF88D65D;


    @Override
    public int setLayoutId() {
        return R.layout.welcome_pager_fragment_layout1;
    }

    @Override
    protected void initView(View view) {
        setBgColor(color1);
        iv_lg_1.setBackgroundColor(color1);
        iv_lg_2.setBackgroundColor(color2);
        lLogo.setImageResource(R.drawable.wechat1);
        lTitle.setText("工作圈与生活圈\n互不干扰");
        lDes.setText("通过平行空间同时运行两个微信账号，将你的\n工作与生活轻松分离");
    }

    @Override
    protected void findViewById(View view) {
        rl = view.findViewById(R.id.rl);
        fl = view.findViewById(R.id.fl);
        lLogo = view.findViewById(R.id.lLogo);
        lTitle = view.findViewById(R.id.lTitle);
        lDes = view.findViewById(R.id.lDes);
        iv_lg_1 = view.findViewById(R.id.iv_lg_1);
        iv_lg_2 = view.findViewById(R.id.iv_lg_2);
        iv_sm_1 = view.findViewById(R.id.iv_sm_1);
        iv_sm_2 = view.findViewById(R.id.iv_sm_2);

        Log.i(TAG,"findViewById:"+String.valueOf(iv_lg_1==null)+":"+iv_lg_1.toString());

        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float density = displayMetrics.density;
        int heightPixels = displayMetrics.heightPixels;
        ViewGroup.LayoutParams layoutParams = rl.getLayoutParams();
        layoutParams.height = (int) (heightPixels * 220 * density / 1280);
        layoutParams.width = (int) (heightPixels * 155.5 * density / 1280);

        ViewGroup.LayoutParams layoutParams1 = fl.getLayoutParams();
        layoutParams1.height = (int) (heightPixels * 192 * density / 1280);
        layoutParams1.width = (int) (heightPixels * 106.5 * density / 1280);

    }

    @Override
    public void onViewShow(View view, Runnable runnable) {
        //执行滚动动画
        int dur = 500;
        if(iv_lg_1==null){
            return;
        }
        ObjectAnimator.ofFloat(iv_lg_1, "translationX", 0, -iv_lg_1.getMeasuredWidth()).setDuration(dur).start();
        ObjectAnimator.ofFloat(iv_lg_2, "translationX", iv_lg_2.getMeasuredWidth(), 0).setDuration(dur).start();
        ObjectAnimator.ofFloat(iv_sm_1, "translationX", 0, -iv_sm_1.getMeasuredWidth()).setDuration(dur).start();
        ObjectAnimator.ofFloat(iv_sm_2, "translationX", iv_sm_2.getMeasuredWidth(), 0).setDuration(dur).start();
        iv_lg_2.setVisibility(View.VISIBLE);
        iv_sm_2.setVisibility(View.VISIBLE);
        new Handler().postDelayed(runnable, dur);
        new Handler().postDelayed(new ChangeLogo(), dur / 2);
        new Handler().postDelayed(new ChangeBgColor(), dur);
    }

    @Override
    public void onViewHide(View view, Runnable runnable) {
        if(lLogo==null){
            return;
        }
        lLogo.setImageResource(R.drawable.wechat1);
        iv_lg_1.setRotation(0);
        iv_lg_2.setRotation(0);
        iv_lg_1.setX(0);
        iv_sm_1.setX(0);
        iv_lg_2.setX(iv_lg_2.getMeasuredWidth());
        iv_sm_2.setX(iv_sm_2.getMeasuredWidth());
        iv_sm_2.setVisibility(View.INVISIBLE);
        iv_lg_2.setVisibility(View.INVISIBLE);
        setBgColor(color1);
    }

    class ChangeLogo implements Runnable {

        @Override
        public void run() {
            lLogo.setImageResource(R.drawable.wechat2);
        }
    }

    class ChangeBgColor implements Runnable {
        @Override
        public void run() {
            setBgColor(color2);
        }
    }
}
