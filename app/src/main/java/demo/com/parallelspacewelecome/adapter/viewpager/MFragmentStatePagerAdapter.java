package demo.com.parallelspacewelecome.adapter.viewpager;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc Bug 横竖屏切换导致崩溃 需要锁定屏幕方向
 */
public class MFragmentStatePagerAdapter<T extends BaseFragment> extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private static final boolean DEBUG = true;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    private ArrayList<T.SavedState> mSavedState = new ArrayList<>();
    private ArrayList<T> mFragments = new ArrayList<>();
    private List<T> fragments;
    private Fragment mCurrentPrimaryItem = null;

    /*滚动参数*/
    private IPagerChangeListener<T> iPagerChangeListener;

    private LinkedList<ViewNode> viewNodes = new LinkedList<>();
    private ViewNode selectedNode;
    private int lastSelectedPosition;

    /**
     * 正在消失的页面
     */
    private ViewNode fadingOutNode;
    private int fadingOutPosition;
    /**
     * 开始滑动的某个页面
     */
    private ViewNode scrollFromNode;
    /**
     * 滑动后的新页面
     */
    private ViewNode scrollToNode;
    /**
     * 开始滑动的位置
     */
    private int scrollFromPosition;
    /**
     * 滑动后的新位置 其实就是lastSelectedPosition
     */
    private int scrollToPosition;
    private String TAG = this.getClass().getSimpleName();
    /**/

    public MFragmentStatePagerAdapter(FragmentManager fm, List<T> fragments) {
        mFragmentManager = fm;
        this.fragments = fragments;
    }

    public MFragmentStatePagerAdapter setPagerChangeListener(ViewPager viewPager, IPagerChangeListener<T> pageChangeListener) {
        viewPager.addOnPageChangeListener(this);
        this.iPagerChangeListener = pageChangeListener;
        return this;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int p = position;
        // If we already have this item instantiated, there is nothing
        // to do.  This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        if (mFragments.size() > position) {
            T f = mFragments.get(position);
            if (f != null) {
                addViewNodeToLinkedList(f.getLayoutView(), p);
                if (DEBUG)
                    Log.i(TAG, "Adding item #" + position + ": f=" + f + String.valueOf(f.getView() == null));
                return f;
            }
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        T fragment = getItem(position);
        if (mSavedState.size() > position) {
            T.SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        while (mFragments.size() <= position) {
            mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        mFragments.set(position, fragment);
        mCurTransaction.add(container.getId(), fragment);

        fragment.setBaseFragmentViewListener(new BaseFragmentViewListener() {

            @Override
            public void onViewCreated(View view) {
                addViewNodeToLinkedList(view, p);
                if (DEBUG) Log.e(TAG, "Adding item #" + p + ": f=" + String.valueOf(view == null));
            }
        });
//        if (DEBUG) Log.e(TAG, "Adding item #" + position + ": f=" + fragment+String.valueOf(fragment.getView()==null));
        return fragment;
    }


    private void addViewNodeToLinkedList(View view, int addPosition) {

        if (DEBUG) Log.e(TAG, "addViewNodeToLinkedList item #" + addPosition);

        if (addPosition > lastSelectedPosition) {//末尾添加
            ViewNode viewNode;
            ViewNode lastNode = null;
            try {
                lastNode = viewNodes.getLast();//NoSuchElementException
            } catch (Exception e) {
            }

            if (lastNode != null) {
                viewNode = new ViewNode(view, null, lastNode);
                lastNode.setNextNode(viewNode);
            } else {
                viewNode = new ViewNode(view, null, null);
            }
            viewNodes.addLast(viewNode);
        } else if (addPosition < lastSelectedPosition) {//头部添加
            ViewNode viewNode;
            ViewNode firstNode = null;
            try {
                firstNode = viewNodes.getFirst();//NoSuchElementException
            } catch (Exception e) {
            }
            if (firstNode != null) {
                viewNode = new ViewNode(view, firstNode, null);
                firstNode.setPreNode(viewNode);
            } else {
                viewNode = new ViewNode(view, null, null);
            }
            viewNodes.addFirst(viewNode);
        } else {//addPostion==0 and lastPositon ==0
            ViewNode viewNode = new ViewNode(view, null, null);
            selectedNode = viewNode;
            viewNodes.addFirst(viewNode);
            if (iPagerChangeListener != null) {
                iPagerChangeListener.onPagerSelected(view, fragments.get(addPosition), addPosition, true);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        T fragment = (T) object;
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Removing item #" + position + ": f=" + object
                + " v=" + ((Fragment) object).getView());
        while (mSavedState.size() <= position) {
            mSavedState.add(null);
        }
        mSavedState.set(position, fragment.isAdded()
                ? mFragmentManager.saveFragmentInstanceState(fragment) : null);
        mFragments.set(position, null);

        mCurTransaction.remove(fragment);

        if (position > lastSelectedPosition) {
            viewNodes.removeLast();
        } else {
            viewNodes.removeFirst();
        }
    }

    @Override
    @SuppressWarnings("ReferenceEquality")
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        T fragment = (T) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((T) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new Bundle();
            T.SavedState[] fss = new T.SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            T f = mFragments.get(i);
            if (f != null && f.isAdded()) {
                if (state == null) {
                    state = new Bundle();
                }
                String key = "f" + i;
                mFragmentManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            Parcelable[] fss = bundle.getParcelableArray("states");
            mSavedState.clear();
            mFragments.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSavedState.add((T.SavedState) fss[i]);
                }
            }
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    T f = (T) mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        }
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }

    private float lastPositionOffset = 0f;

    private boolean slidingDirectionConfirmed = false;
    private boolean slidingToRight = false;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //positionOffset 从小到大，表明从左往右滑动 SelectedPosition 0->1->2->3
        //positionOffset 从大到小，表明从右往左滑动 SelectedPosition 3->2->1->0
        if (positionOffset == 0f || (lastPositionOffset - positionOffset > 0.9f || lastPositionOffset - positionOffset < -0.9f)) {//刚开始滑动或者改变方向
            lastPositionOffset = positionOffset;
            slidingDirectionConfirmed = false;
            slidingToRight = false;
            //松手后 selectedPosition已经变化，但是滑动并未停止
            fadingOutNode = selectedNode;
            fadingOutPosition = lastSelectedPosition;
            return;
        }
        if(fadingOutNode==null){
            return;
        }

        if (!slidingDirectionConfirmed && lastPositionOffset != 0) {
            if (lastPositionOffset > positionOffset) {
                slidingDirectionConfirmed = true;
                slidingToRight = false;
            }

            if (lastPositionOffset < positionOffset) {
                slidingDirectionConfirmed = true;
                slidingToRight = true;
            }
        }

        if (slidingDirectionConfirmed) {
            if (slidingToRight) {//向右滑动
                if (iPagerChangeListener != null && fadingOutNode.getNextNode() != null && fadingOutPosition < (getCount() - 1)) {
                    this.iPagerChangeListener.onPagerScrolling(
                            fadingOutNode.getmView(),
                            fadingOutNode.getNextNode().getmView(),
                            fadingOutPosition,
                            fadingOutPosition + 1,
                            positionOffset);
                }
            } else {//向左滑动
                if (iPagerChangeListener != null && fadingOutNode.getPreNode() != null && fadingOutPosition > 0) {
                    this.iPagerChangeListener.onPagerScrolling(
                            fadingOutNode.getmView(),
                            fadingOutNode.getPreNode().getmView(),
                            fadingOutPosition,
                            fadingOutPosition - 1,
                            1 - positionOffset);//让滑入滑出的比例都是由大到小
                }
            }
        }
        lastPositionOffset = positionOffset;
    }

    /**
     * 判断滚动结束后是否新的页面
     */
    boolean isNewPager = false;

    /**
     * 横竖屏切换时先执行 onPageSelected，再执行instantiateItem,selectedNode为空
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        if (selectedNode != null) {
            scrollFromNode = selectedNode;
            if (position < lastSelectedPosition) {
                selectedNode = selectedNode.getPreNode();
            } else {
                selectedNode = selectedNode.getNextNode();
            }
            if (iPagerChangeListener != null&&selectedNode!=null) {
                iPagerChangeListener.onPagerSelected(selectedNode.getmView(), fragments.get(position), position, false);
            }
        }else{
            return;
        }
        scrollToNode = selectedNode;
        isNewPager = true;
        scrollFromPosition = lastSelectedPosition;
        scrollToPosition = position;
        lastSelectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://空闲状态  滑动结束
//                onPageScrolled(lastSelectedPosition, 0, 0);
                if (iPagerChangeListener != null) {
                    if (scrollFromNode != null && scrollToNode != null)
                        iPagerChangeListener.onScrollFinish(scrollFromNode.getmView(), scrollToNode.getmView(), scrollFromPosition, scrollToPosition, isNewPager);
                }
                isNewPager = false;
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://滑动中 ，手指拖拽
                break;
            case ViewPager.SCROLL_STATE_SETTLING://滑动中 手里离开
                break;
        }
    }
}
