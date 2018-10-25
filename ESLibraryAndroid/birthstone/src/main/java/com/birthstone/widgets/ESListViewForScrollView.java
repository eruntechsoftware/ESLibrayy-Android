package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 时间：2016年01月06日
 *
 * 作者：张景瑞
 *
 * 功能：在ScrollView中嵌套的ListView
 */
public class ESListViewForScrollView extends ListView {

    public ESListViewForScrollView(Context context) {
        super(context);
    }
    public ESListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ESListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
