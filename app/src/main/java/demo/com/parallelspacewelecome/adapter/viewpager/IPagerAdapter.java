package demo.com.parallelspacewelecome.adapter.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author nzbao
 * @CreateTime 2017/11/7
 * @Desc 在
 */
public abstract class IPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener//, ViewPager.PageTransformer
{
    private String TAG = "Neil";
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    /**
     * 当前未被销毁的View集合
     * size 为2-4
     * 只能一页一页的过度，跳转页面可能导致崩溃
     */
    private LinkedList<ViewNode> viewNodes;
    private IPagerChangeListener<T> iPagerChangeListener;
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


    public IPagerAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        viewNodes = new LinkedList<>();
    }

    public IPagerAdapter setPagerChangeListener(ViewPager viewPager, IPagerChangeListener<T> pageChangeListener) {
        viewPager.addOnPageChangeListener(this);
        this.iPagerChangeListener = pageChangeListener;
        return this;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //初始化一个View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(TAG, "instantiateItem:" + position);
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, container, false);
        convertView(view, mDatas.get(position), position);
        addViewNodeToLinkedList(view, position);
        container.addView(view);
        return view;
    }

    private void addViewNodeToLinkedList(View view, int addPosition) {
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
                iPagerChangeListener.onPagerSelected(view, mDatas.get(addPosition), addPosition, true);
            }
        }
    }


    public abstract void convertView(View view, T t, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (position > lastSelectedPosition) {
            viewNodes.removeLast();
        } else {
            viewNodes.removeFirst();
        }
    }

    private float lastPositionOffset = 0f;

    private boolean slidingDirectionConfirmed = false;
    private boolean slidingToRight = false;

    /**
     * position 当手指未离开屏幕，position为当前两个页面的第一个！
     * 当手指离开屏幕，position为当前选中的页面
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //positionOffset 从小到大，表明从左往右滑动 SelectedPosition 0->1->2->3
        //positionOffset 从大到小，表明从右往左滑动 SelectedPosition 3->2->1->0
        if (positionOffset == 0f || (lastPositionOffset - positionOffset > 0.9f || lastPositionOffset - positionOffset < -0.9f)) {//刚开始滑动或者改变方向
            lastPositionOffset = 0;
            slidingDirectionConfirmed = false;
            slidingToRight = false;
            //松手后 selectedPosition已经变化，但是滑动并未停止
            fadingOutNode = selectedNode;
            fadingOutPosition = lastSelectedPosition;
            return;
        }

        if (fadingOutNode == null) {
            return;
        }

        if (lastPositionOffset == 0) {
            lastPositionOffset = positionOffset;
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

            if (slidingToRight) {//向右滑动
                if (iPagerChangeListener != null && fadingOutNode.getNextNode() != null && fadingOutPosition < (getCount() - 1)) {
                    this.iPagerChangeListener.onStartScroll(
                            fadingOutNode.getmView(),
                            fadingOutNode.getNextNode().getmView(),
                            fadingOutPosition,
                            fadingOutPosition + 1);
                }
            } else {//向左滑动
                if (iPagerChangeListener != null && fadingOutNode.getPreNode() != null && fadingOutPosition > 0) {
                    this.iPagerChangeListener.onStartScroll(
                            fadingOutNode.getmView(),
                            fadingOutNode.getPreNode().getmView(),
                            fadingOutPosition,
                            fadingOutPosition-1);
                }
            }
        } else {
            lastPositionOffset = positionOffset;
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
    }

    //
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
            if (iPagerChangeListener != null) {
                iPagerChangeListener.onPagerSelected(selectedNode.getmView(), mDatas.get(position), position, false);
            }
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
                onPageScrolled(lastSelectedPosition, 0, 0);
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
