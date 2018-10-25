package com.birthstone.base.activity;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * ֧ƻActivity
 * 
 * @author MinG
 * 
 */
public class SlideActivity extends Activity implements OnTouchListener
{
	VelocityTracker velocityTracker;
	static final int FLING_MIN_DISTANCE = 150;
	static final int FLING_MIN_VELOCITY = 400;
	float x1 = 0, x2 = 0;
	float y1, y2;
	/**
	 * 󻬶
	 */
	protected void leftSlide()
	{

	}

	/**
	 * һ
	 */
	protected void rightSlide()
	{

	}

	public boolean onTouch(View v, MotionEvent event)
	{
		return onTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();

		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
			if(velocityTracker == null)
			{
				velocityTracker = VelocityTracker.obtain();// ٶ
				velocityTracker.addMovement(event);
			}
			x1 = event.getX();
			y1 = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			if(velocityTracker != null)
			{
				velocityTracker.addMovement(event);
			}
			break;

		case MotionEvent.ACTION_UP:
			x2 = event.getX();
			y2 = event.getY();
			float velocityX = 0,
			velocityY = 0;
			if(velocityTracker != null)
			{
				velocityTracker.addMovement(event);
				velocityTracker.computeCurrentVelocity(1000);
				velocityX = velocityTracker.getXVelocity();
				velocityY = velocityTracker.getYVelocity();
			}
			if(x1 - x2 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY)
			{
				leftSlide();
			}
			if(x2 - x1 > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY)
			{
				rightSlide();
			}

			if(velocityTracker != null)
			{
				velocityTracker.recycle();
				velocityTracker = null;
			}
			break;
		}
		return true;
	}
}
