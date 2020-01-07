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

	@Override
	public void setChecked(boolean checked)
	{
		this.mChecked = super.isChecked();
		if (this.mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();
			if ( mOnCheckedChangeListener!= null) {
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}
		}
	}

	@Override
	public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener)
	{
//		super.setOnCheckedChangeListener(listener);
		mOnCheckedChangeListener = listener;
	}
}
