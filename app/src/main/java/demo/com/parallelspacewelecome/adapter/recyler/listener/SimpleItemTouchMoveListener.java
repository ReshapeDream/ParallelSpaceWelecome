package demo.com.parallelspacewelecome.adapter.recyler.listener;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * @author nzbao
 * @CreateTime 2017/11/14
 * @Desc
 */
public abstract class SimpleItemTouchMoveListener implements ItemTouchMoveListener {

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }
}
