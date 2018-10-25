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
    public List<View> getViews();

    public void release(DataCollection params);

    public void release(int tag,DataCollection params);

    public void release(int tag,DataTable table);
}
