package com.example.sqlitedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by wangjinfa on 2017/6/6.
 */

public class ItemLayout extends RelativeLayout{

    public ItemLayout(Context context) {
        super(context);
        init(context);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
