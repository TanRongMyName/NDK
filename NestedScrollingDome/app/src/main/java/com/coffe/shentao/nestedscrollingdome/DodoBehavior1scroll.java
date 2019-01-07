package com.coffe.shentao.nestedscrollingdome;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

public class DodoBehavior1scroll extends CoordinatorLayout.Behavior<View> {

    public DodoBehavior1scroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    boolean onStartNestedScroll
//    判断是否接收后续事件
//    我们的例子由于是竖直方向的滑动监听（直接true包含横向也行，后面不会获取对应的值）
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
//        return false;
    }

//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        int followScrolled = target.getScrollY();
//        child.setScrollY(followScrolled);
//    }
//    void onNestedScroll
//    对应滑动的时候，处理的事情
//    当然，这里换成void onNestedPreScroll 效果是差不多的， 具体只是2个方法有先后顺序而已
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        int followScrolled = target.getScrollY();
        child.setScrollY(followScrolled);
    }
//    boolean onNestedFling
//    对应的滑动较快，也就是fling事件触发的时候调用
//    这里不能换成 onNestedPreFling，替换后，会有卡顿，暂时不纠结为什么
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        if(child instanceof NestedScrollView){
            ((NestedScrollView) child).fling((int)velocityY);
        }
        return true;
    }
}


