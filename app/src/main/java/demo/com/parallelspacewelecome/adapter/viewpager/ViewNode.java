package demo.com.parallelspacewelecome.adapter.viewpager;

import android.view.View;

/**
 * @author nzbao
 * @CreateTime 2017/11/30
 * @Desc
 */
public class ViewNode {
    private View mView;
    private ViewNode nextNode;
    private ViewNode preNode;

    public ViewNode(View mView, ViewNode nextNode, ViewNode preNode) {
        this.mView = mView;
        this.nextNode = nextNode;
        this.preNode = preNode;
    }

    public View getmView() {
        return mView;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    public ViewNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(ViewNode nextNode) {
        this.nextNode = nextNode;
    }

    public ViewNode getPreNode() {
        return preNode;
    }

    public void setPreNode(ViewNode preNode) {
        this.preNode = preNode;
    }
}
