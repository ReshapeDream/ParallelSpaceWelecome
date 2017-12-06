package demo.com.parallelspacewelecome.adapter.recyler.listener;


import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * @author nzbao
 * @CreateTime 2017/11/14
 * @Desc
 */
public interface ItemTouchMoveListener <T extends RecyclerView.ViewHolder>{

    /**
     * 选中时
     * @param selectHolder
     */
    void onSelected(T selectHolder);

    /**
     * 移动时
     * @param from
     * @param to
     * @return
     */
    boolean onMove(T from, T to);

    /**
     * 侧滑时
     * @param selectHolder
     * @return
     */
    boolean onSwiped(T selectHolder);

    /**
     * 取消选中后
     * @param selectHolder
     */
    void unSelected(T selectHolder);


    void onChildDraw(Canvas c, RecyclerView recyclerView, T viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

    void onChildDrawOver(Canvas c, RecyclerView recyclerView, T viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);
}
