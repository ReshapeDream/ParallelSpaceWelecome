package demo.com.parallelspacewelecome.adapter.recyler;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import demo.com.parallelspacewelecome.adapter.recyler.listener.ItemTouchMoveListener;


/**
 * @author nzbao
 * @CreateTime 2017/11/14
 * @Desc
 */
public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private String TAG = "ItemTouchHelperCallBack";

    private ItemTouchMoveListener itemTouchMoveListener;

    public ItemTouchHelperCallBack(ItemTouchMoveListener itemTouchMoveListener) {
        this.itemTouchMoveListener = itemTouchMoveListener;
    }

    /**
     * 用来判断 动作类型及方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int up = ItemTouchHelper.UP;
        int down = ItemTouchHelper.DOWN;
        int left = ItemTouchHelper.LEFT;
        int right = ItemTouchHelper.RIGHT;
        int start = ItemTouchHelper.START;
        int end = ItemTouchHelper.END;
        int dragFlag = up | down | left | right;
        int swipFlag=up|down|left|right;
        return makeMovementFlags(dragFlag, swipFlag);
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //在 move 中调用适配器的 adapter.notifyItemMoved(fromPosition,toPosition)
        itemTouchMoveListener.onMove(viewHolder, target);
        return true;
    }

    /**
     * 侧滑
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchMoveListener.onSwiped(viewHolder);
    }

    RecyclerView.ViewHolder srcHolder;

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder,actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {//
            srcHolder = viewHolder;
            itemTouchMoveListener.onSelected(srcHolder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView,viewHolder);
        itemTouchMoveListener.unSelected(srcHolder);
    }

    /**
     * 拖拽或侧滑时重绘child
     *
     * onSelectedChanged  之后会执行多次执行onChildDraw  onChildDrawOver
     * clearView 之后会执行一次 onChildDraw  onChildDrawOver
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        itemTouchMoveListener.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
    }

    /**
     * 重绘完成
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        itemTouchMoveListener.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
