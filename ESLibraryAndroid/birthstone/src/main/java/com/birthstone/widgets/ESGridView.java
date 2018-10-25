package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 作  者： 潘跃瑞
 * <p>
 * 功  能： 在scrollview中嵌套的gridview
 * <p>
 * 时  间： 2014年11月8日
 */
public class ESGridView extends android.widget.GridView
{

    public ESGridView (Context context)
    {
        super(context);
    }

    public ESGridView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ESGridView (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
