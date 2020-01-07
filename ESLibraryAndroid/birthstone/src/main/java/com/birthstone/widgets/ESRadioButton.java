package com.birthstone.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;

public class ESRadioButton extends android.widget.RadioButton
{
	private boolean mChecked;

	private OnCheckedChangeListener mOnCheckedChangeListener;
	public ESRadioButton(Context context, AttributeSet attrs )
	{
		super(context, attrs);
	}

}
