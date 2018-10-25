package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickedListener;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.base.parse.CollectController;
import com.birthstone.core.helper.DateTimeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;


@SuppressLint("HandlerLeak")
public class ESButtonOK extends ESButton implements IDataInitialize, IFunctionProtected
{
	protected ProgressDialog mAlter;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESButtonOK(Context context, AttributeSet attrs )
	{
		super(context, attrs);
	}

	public ESButtonOK(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	protected OnClickListener clickListener = new OnClickListener()
	{
		public Boolean onClicking()
		{
			if(onClickingListener != null)
			{
				return onClickingListener.onClicking();
			}
			return false;
		}

		public void onClick(View v)
		{
			if(onClicking()) 
			{ 
				return; 
			}
			click();
			onClicked();
		}

		public void onClicked()
		{
			if(onClickedListener != null)
			{
				onClickedListener.onClicked();
			}
		}
	};

	public void dataInitialize()
	{

	}


	public void click()
	{
		try
		{
			mAlter = ProgressDialog.show(mActivity, "ر", "ڹرմ壬Ժ...", true);
			if(mThread.getState().equals(Thread.State.WAITING))
			{
				mThread.run();
			}
			else if(mThread.getState().equals(Thread.State.NEW))
			{
				mThread.start();
			}
			else if(mThread.getState().equals(Thread.State.TERMINATED))
			{
				mThread.run();
			}
		}
		catch(Exception ex)
		{
			Log.v("ButtonOK", ex.getMessage());
		}
	}

	private Boolean close()
	{
		try
		{
			if(mSign != null)
			{
				CollectController collectController = new CollectController(mActivity, mSign);
				DataCollection dataCollection = collectController.collect();
				dataCollection.add(new Data("BtnQuery", 1));
				if(mActivity.getParentActivity() != null)
				{
					mActivity.getParentActivity().release(dataCollection);
					return true;
				}
				return false;
			}
			else
			{
				DataCollection dataCollection = new DataCollection();
				dataCollection.add(new Data("noBill", DateTimeHelper.getNow()));
				dataCollection.add(new Data("BtnQuery", 1));
				if(mActivity.getParentActivity() != null)
				{
					mActivity.getParentActivity().release(dataCollection);
					return true;
				}
			}
			return true;
		}
		catch(Exception ex)
		{
			Log.e("button-close", ex.getMessage());
		}
		return true;
	}

	Thread mThread = new Thread(new Runnable()
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(true)
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	});

	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			try
			{

				Log.v("msg", String.valueOf(msg.what));
				switch(msg.what)
				{
				case 1:
					mAlter.dismiss();
					close();
					mActivity.finish();
					break;
				case 0:
					mAlter.dismiss();
					mActivity.finish();
					break;
				}
			}
			catch(Exception ex)
			{
				Log.v("handleMessage", ex.getMessage());
			}
		}
	};

	/*
	 *
	 */
	public String[] getFuncSign()
	{
		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	/*
	 *
	 */
	public void setVisible(Boolean arg0)
	{
		if(arg0)
		{
			this.setVisibility(View.VISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}

	public Object getActivity()
	{
		return mActivity;
	}

	public void setActivity(Object arg0)
	{

		if(arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
		}
	}

	public OnClickingListener getOnClickingListener()
	{
		return onClickingListener;
	}

	public void setOnClickingListener(OnClickingListener onClickingListener)
	{
		this.onClickingListener = onClickingListener;
	}

	public OnClickedListener getOnClickedListener()
	{
		return onClickedListener;
	}

	public void setOnClickedListener(OnClickedListener onClickedListener)
	{
		this.onClickedListener = onClickedListener;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public OnClickListener getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(OnClickListener clickListener)
	{
		this.clickListener = clickListener;
	}

	public void setFuncSign(String funcSign)
	{
		this.mFuncSign = funcSign;
	}

}
