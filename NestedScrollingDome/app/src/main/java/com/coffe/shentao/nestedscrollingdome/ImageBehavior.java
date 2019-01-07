package com.coffe.shentao.nestedscrollingdome;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.coffe.shentao.nestedscrollingdome.design.Behavior;


/**
 * 现在意味着这边的方法是能够被调用的
 */
public class ImageBehavior extends Behavior {


    private int maxHeight = 400;
    private int originHeight;

    @Override
    public void onLayoutFinish(View parent, View child) {
        if(originHeight == 0){
            originHeight = child.getHeight();
        }
    }

    public ImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//    当子view调用dispatchNestedPreScroll方法是,会调用该方法.
//            该方法的会传入内部View移动的dx,dy，如果你需要消耗一定的dx,dy，就通过最后一个参数
//    consumed进行指定，例如我要消耗一半的dy，就可以写consumed[1]=dy/2
    // 参数target:同上
    // 参数dxConsumed:表示target已经消费的x方向的距离
    // 参数dyConsumed:表示target已经消费的y方向的距离
    // 参数dxUnconsumed:表示x方向剩下的滑动距离
    // 参数dyUnconsumed:表示y方向剩下的滑动距离
    @Override
    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        Log.i("barry","test..onNestedScroll--------"+scrollView.getScrollY());


        if(scrollView.getScrollY() > 0){//向上滑动 ---
            ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            layoutParams.height = layoutParams.height - Math.abs(dyConsumed);
            if(layoutParams.height < originHeight){
                layoutParams.height = originHeight;
            }
            target.setLayoutParams(layoutParams);
        }else if(scrollView.getScrollY() == 0){//滑动停止的时候 ----改变的宽度变化
            ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            //为0
            layoutParams.height = layoutParams.height + Math.abs(dyUnconsumed);
            if(layoutParams.height >= maxHeight){//
                layoutParams.height = maxHeight;
            }
            target.setLayoutParams(layoutParams);
        }

    }
}
