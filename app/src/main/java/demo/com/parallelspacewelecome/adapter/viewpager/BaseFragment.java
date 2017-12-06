package demo.com.parallelspacewelecome.adapter.viewpager;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public abstract class BaseFragment extends Fragment implements ViewShowListener{
    private View mLayoutView;
    protected  String TAG=this.getClass().getSimpleName();
    private int bgColor;

    private BaseFragmentViewListener baseFragmentViewListener;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }


    public void setBaseFragmentViewListener(BaseFragmentViewListener baseFragmentViewListener){
        this.baseFragmentViewListener=baseFragmentViewListener;
    }

    public BaseFragmentViewListener getBaseFragmentViewListener(){
        return this.baseFragmentViewListener;
    }

    /**
     * 设置布局文件
     * @return
     */
    @LayoutRes
    public abstract  int setLayoutId();

    /**
     * 初始化View
     * @param view
     */
    protected abstract void initView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView"+String.valueOf(baseFragmentViewListener==null));
        mLayoutView=inflater.inflate(setLayoutId(),container,false);
        if(baseFragmentViewListener!=null) {
            baseFragmentViewListener.onViewCreated(mLayoutView);
        }
        return mLayoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreated");
        findViewById(view);
        initView(view);
    }

    public View getLayoutView(){
        return mLayoutView;
    }

    protected abstract void findViewById(View view);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
