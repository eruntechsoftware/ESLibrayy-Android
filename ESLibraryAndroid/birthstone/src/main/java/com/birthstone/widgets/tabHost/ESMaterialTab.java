package com.birthstone.widgets.tabHost;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 时 间: 2016年04月14日
 *
 * 作者：杜明悦
 *
 * 功 能: MaterialTab
 */
public class ESMaterialTab extends LinearLayout {

    /**声明控件**/
    /**标题**/
    private TextView mTitle;

    private float mTitleSize = 13;

    /**声明变量**/
    private Context mContext;
    private int mIndex;

    public ESMaterialTab(Context context) {
        super(context);
        /**设置根布局宽、高**/
        LinearLayout.LayoutParams rootLinearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootLinearParams.gravity = Gravity.CENTER;
        rootLinearParams.weight = 1;
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(rootLinearParams);

        this.mContext = context;
        mTitle = new TextView(context);
        mTitle.setTextSize(13);
        this.addView(mTitle);
    }

    /**
     * 设置标题文本
     * @param titleText 标题文本
     * **/
    public void setTitleText(String titleText){
        if(mTitle != null){
            this.mTitle.setText(titleText);
        }
    }

    /**
     * 设置标题文本颜色
     * @param color 文本颜色
     * **/
    public void setTextColor(int color){
        if(mTitle != null){
            this.mTitle.setTextColor(color);
        }
    }

    /**
     * 设置标题文本大小
     * @param titleSize 文本大小
     * **/
    public void setTitleSize(float titleSize)
    {
        if(mTitle!=null)
        {
            this.mTitle.setTextSize(titleSize);
        }
    }

    /**
     * 设置标题是否加粗字体
     * @param boldText 是否加粗字体
     * **/
    public void setFakeBoldText(boolean boldText)
    {
        if(mTitle!=null)
        {
            mTitle.getPaint().setFakeBoldText(boldText);
        }

    }

    /**
     * 设置标题索引
     * @param index 索引
     * **/
    public void setIndex(int index){
        this.mIndex = index;
    }

    /**
     * 获取标题索引
     * **/
    public int getIndex(){
        return this.mIndex;
    }
}
