package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickedListener;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.parse.CollectController;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IFunctionProtected;
import com.birthstone.core.parse.DataCollection;


@SuppressLint("HandlerLeak")
public class ESButtonClose extends ESButton implements IDataInitialize, IFunctionProtected
{
	protected Activity mActivity;
	protected OnClickingListener onClickingListener;
	protected OnClickedListener onClickedListener;
	//protected ESProgressDialog alter;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESButtonClose(Context context, AttributeSet attrs )
	{
		super(context, attrs);
	}

	public ESButtonClose(Context context, AttributeSet attrs, int defStyle)
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
			if(onClicking()) { return; }
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
		if (mActivity != null) {
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
	}


	public void click()
	{
		try
		{
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
			Log.v("ButtonOpen", ex.getMessage());
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
				if(mActivity.getParentActivity() != null)
				{
					mActivity.getParentActivity().release(dataCollection);
					return true;
				}
				return false;
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
			/*
			 * if(Looper.myLooper()==null) { Looper.prepare(); }
			 */
			if(true)
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			// Looper.loop();
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
					close();
					mActivity.finish();
					break;
				case 0:
					//alter.dismiss();
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

	public void setFuncSign(String funcSign)
	{
		this.mFuncSign = funcSign;
	}
}
