package demo.com.parallelspacewelecome.adapter.recyler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * @author nzbao
 * @CreateTime 2017/11/13
 * @Desc 通用ItemDecoration,适用与LinearLayoutManager和GridLayoutManager(orientation=GridLayoutManager.VERTICAL)
 */
public class UniversalItemDecoration extends RecyclerView.ItemDecoration {
    private String TAG = "UniversalItemDecoration";
    private int spanCount = -1;
    private int rowCount = -1;
    private int orientation = -1;
    private boolean includeEdge = false;

    private Drawable horizontalDecoration;
    private Drawable verticalDecoration;

    /**
     * @param horizontalDecoration  水平分割线
     * @param verticalDecoration 垂直分割线
     * @param includeEdge 是否包含边界
     */
    public UniversalItemDecoration(Drawable horizontalDecoration, Drawable verticalDecoration, boolean includeEdge) {
        this.horizontalDecoration = horizontalDecoration;
        this.verticalDecoration = verticalDecoration;
        this.includeEdge = includeEdge;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Log.i(TAG, parent.toString());
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            orientation=((GridLayoutManager) layoutManager).getOrientation();
        }
        drawDecoderation(c, parent);
    }

    private void drawDecoderation(Canvas c, RecyclerView parent) {
        Log.i(TAG, orientation + "======================" + spanCount);
        int cc = parent.getChildCount();
        if (spanCount != -1) {
            rowCount = cc % spanCount == 0 ? cc / spanCount : (cc / spanCount + 1);
        }

        for (int i = 0; i < cc; i++) {
            boolean topEdge = false;
            boolean bottomEdge = false;
            boolean leftEdge = false;
            boolean rightEdge = false;
            if (spanCount != -1) {
                topEdge = i < spanCount;
                bottomEdge = (i >= (rowCount - 1) * spanCount);
                leftEdge = i % spanCount == 0;
                rightEdge = i % spanCount == (spanCount - 1) || i == (cc - 1);
            } else {
                if (orientation == 1) {
                    leftEdge = true;
                    rightEdge = true;
                    topEdge = i == 0;
                    bottomEdge = i == cc-1;
                } else if (orientation == 0) {
                    leftEdge = i == 0;
                    rightEdge = i == cc-1;
                    topEdge = true;
                    bottomEdge = true;
                }
            }

            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams cp = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            if (horizontalDecoration != null ) {
                drawHorizontal(c, parent, i, cp, topEdge, bottomEdge);
            }
            if (verticalDecoration != null ) {
                drawVertical(c, parent, i, cp, leftEdge, rightEdge);
            }
        }
    }

    /**
     * 画垂直分割线
     *
     * @param c
     * @param parent
     * @param child
     * @param cp
     * @param leftEdge
     * @param rightEdge
     */
    private void drawVertical(Canvas c, RecyclerView parent, int child, RecyclerView.LayoutParams cp, boolean leftEdge, boolean rightEdge) {
        View childAt = parent.getChildAt(child);
        View nextChild = null;
        if (child < parent.getChildCount() - 1) {
            nextChild = parent.getChildAt(child + 1);
        }
        if (includeEdge && leftEdge) {//画左边界
            int left = childAt.getLeft() - cp.leftMargin;
            int top = childAt.getTop() - cp.topMargin;
            int right = left + verticalDecoration.getIntrinsicWidth();
            int bottom = childAt.getBottom() + cp.bottomMargin;
            verticalDecoration.setBounds(left, top, right, bottom);
            verticalDecoration.draw(c);
        }

        if (includeEdge && rightEdge) {//画右边界
            int left = childAt.getRight() - cp.rightMargin;
            int top = childAt.getTop() - cp.topMargin;
            int right = left + verticalDecoration.getIntrinsicWidth();
            int bottom = childAt.getBottom() + cp.bottomMargin;
            verticalDecoration.setBounds(left, top, right, bottom);
            verticalDecoration.draw(c);
        }

        if (!rightEdge) {//画中间
            int left = 0;
            if (nextChild != null) {
                left = (childAt.getRight() + cp.rightMargin + nextChild.getLeft() - cp.leftMargin - verticalDecoration.getIntrinsicWidth()) / 2;
            } else {
                left = childAt.getRight() + cp.rightMargin;
            }
            int right = left + verticalDecoration.getIntrinsicWidth();
            int top = childAt.getTop() - cp.topMargin;
            int bottom = childAt.getBottom() + cp.bottomMargin;
            verticalDecoration.setBounds(left, top, right, bottom);
            verticalDecoration.draw(c);
        }
    }

    /**
     * 画水平分割线
     * @param c
     * @param parent
     * @param childIndex
     * @param cp
     * @param topEdge
     * @param bottomEdge
     */
    private void drawHorizontal(Canvas c, RecyclerView parent, int childIndex, RecyclerView.LayoutParams cp, boolean topEdge, boolean bottomEdge) {
        View childAt = parent.getChildAt(childIndex);
        View nextChild = null;
        if (spanCount == -1) {
            if (childIndex < parent.getChildCount() - 1)
                nextChild = parent.getChildAt(childIndex + 1);
        } else {
            if (childIndex < (parent.getChildCount() - spanCount)) {
                nextChild = parent.getChildAt(childIndex + spanCount);
            }
        }
        int left = childAt.getLeft() - cp.leftMargin;
        int right = childAt.getRight() + cp.rightMargin;
        if (includeEdge && topEdge) {//画顶部
            int top = childAt.getTop() - cp.topMargin;
            int bottom = top + horizontalDecoration.getIntrinsicHeight();
            horizontalDecoration.setBounds(left, top, right, bottom);
            horizontalDecoration.draw(c);
        }

        if (includeEdge && bottomEdge) {//画底部
            int top = childAt.getBottom() + cp.bottomMargin;
            int bottom = top + horizontalDecoration.getIntrinsicHeight();
            horizontalDecoration.setBounds(left, top, right, bottom);
            horizontalDecoration.draw(c);
        }

        if (!bottomEdge) {//画中间
            int top ;
            if (nextChild != null) {
                top = (childAt.getBottom() + cp.bottomMargin + nextChild.getTop() - cp.topMargin - horizontalDecoration.getIntrinsicHeight()) / 2;
            } else {
                top = childAt.getBottom() + cp.bottomMargin;
            }
            int bottom = top + horizontalDecoration.getIntrinsicHeight();
            horizontalDecoration.setBounds(left, top, right, bottom);
            horizontalDecoration.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

    }
}
