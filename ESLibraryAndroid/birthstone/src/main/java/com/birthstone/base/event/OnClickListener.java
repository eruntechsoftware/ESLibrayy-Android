package com.birthstone.base.event;

import android.view.View;

public interface OnClickListener extends View.OnClickListener
{
	public Boolean cancel = false;

	Boolean onClicking();

	public void onClick(View v);

	public void onClicked();
}
