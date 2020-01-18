package com.birthstone.core.interfaces;

import android.view.View;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.List;

/**
 * 返回当前视图的子视图集合
 */
public interface IChildView
{

    /*
     * 初始化Activity
     * */
    public void initViewWithActivity();

    public void release();
    public void initDataWithView();
    public void initFunctionProtectedWithView();
    public void initStateControlWithView();

    public List<View> getViews();

    public DataCollection collect(String sign);

    public void release(DataCollection params);

    public void release(int tag,DataCollection params);

    public void release(int tag,DataTable table);

    public void finish();
}
