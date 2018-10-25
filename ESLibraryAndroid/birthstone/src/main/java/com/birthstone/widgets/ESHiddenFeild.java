package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.parse.Data;

/**
 * 隐藏字段控件，用于非可见的字段绑定
 * */
public class ESHiddenFeild extends ESTextBox
{
	public ESHiddenFeild(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		this.setVisibility(View.GONE);
	}

	@Override
	public void release (String dataName, Data data)
	{
		super.release(dataName, data);
	}
}
