package com.coffe.shentao.nestedscrollingdome;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.coffe.shentao.nestedscrollingdome.design.Behavior;


public class ToolBarBehavior extends Behavior {

    private float maxHeight = 400;

    public ToolBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        if(scrollView.getScrollY() <= maxHeight){
            target.setAlpha(scrollView.getScrollY() * 1.0f/maxHeight);
        }else if(scrollView.getScrollY() == 0){
            target.setAlpha(0);
        }



    }
}
