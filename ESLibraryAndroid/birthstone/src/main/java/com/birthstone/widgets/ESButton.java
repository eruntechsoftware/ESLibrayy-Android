package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnClickedListener;
import com.birthstone.base.event.OnClickingListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.ModeType;
import com.birthstone.core.helper.ModeTypeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;

import java.util.LinkedList;


@SuppressLint("DefaultLocale")
/**
 * @author 杜明悦
 * Button基类，包含常用属性方法
 * */ public class ESButton extends android.widget.Button implements IFunctionProtected, IReleasable, IStateProtected, IDataInitialize
{

	public String mFuncSign;
	protected String mSign;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected ModeType mModeType;
	protected String mOpen;
	protected Boolean mIsClosed = false;
	protected String mName;
	protected IChildView mActivity;
	/**
	 * 单击事件执行前执行事件，并返回是否终止单击事件的执行参数
	 **/
	public OnClickingListener onClickingListener;
	/**
	 * 单击事件执行完成后执行的事件
	 **/
	public OnClickedListener onClickedListener;
	public String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		try
		{
			setOnClickListener(clickListener);
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESButton);
			this.mFuncSign = a.getString(R.styleable.ESButton_funcSign);
			this.mSign = a.getString(R.styleable.ESButton_sign);
			this.mStateHiddenId = a.getString(R.styleable.ESButton_stateHiddenId);
			this.mWantedStateValue = a.getString(R.styleable.ESButton_wantedStateValue);
			this.mModeType = ModeTypeHelper.valueOf(a.getInt(R.styleable.ESButton_modeType, 0));
			this.mOpen = a.getString(R.styleable.ESButton_open);
			this.mIsClosed = a.getBoolean(R.styleable.ESButton_isClosed, false);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e(ESButton.this.toString(), ex.getMessage());
		}
	}

	public ESButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void click()
	{
	}

	protected OnClickListener clickListener = new OnClickListener()
	{
		public Boolean onClicking()
		{
			if (onClickingListener != null)
			{
				return onClickingListener.onClicking();
			}
			return false;
		}

		public void onClick(View v)
		{
			if (onClicking())
			{
				return;
			}
			click();
			onClicked();
		}

		public void onClicked()
		{
			if (onClickedListener != null)
			{
				onClickedListener.onClicked();
			}
		}
	};

	public void dataInitialize()
	{
		String classnameString = this.getContext().getPackageName() + ".R$id";
		mName = InitializeHelper.getName(classnameString, getId());
	}

	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	/**
	 *   发布数据到UIView
	 *
	 * @param dataName 数据名称
	 * @param data     数据对象  *
	 **/
	public void release(String dataName, Data data)
	{
		if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if (data.getValue() == null)
			{
				this.setText("");
				return;
			}
			else
			{
				this.setText(data.getValue().toString());
			}
		}
	}

	public String getName()
	{
		return mName;
	}

	public Object getChildView()
	{
		return mActivity;
	}

	public void setChildView(Object arg0)
	{
		mActivity = (IChildView) arg0;
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


	public void setVisible(Boolean arg0)
	{
		if (arg0)
		{
			setVisibility(View.VISIBLE);
		}
		else
		{
			setVisibility(View.GONE);
		}
	}

	public String getStateHiddenId() {
		return mStateHiddenId;
	}

	public String getWantedStateValue() {
		return mWantedStateValue;
	}

	public void setStateHiddenId(String stateHiddenId)
	{
		this.mStateHiddenId = stateHiddenId;
	}

	public void setWantedStateValue(String wantedStateValue)
	{
		this.mWantedStateValue = wantedStateValue;
	}

	public void protectState(Boolean arg0)
	{
		if (arg0)
		{
			this.setVisibility(View.VISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}

	public void setModeType(ModeType modeType)
	{
		this.mModeType = modeType;
	}

	public ModeType getModeType()
	{
		return mModeType;
	}

	public String[] getFuncSign()
	{
		return StringToArray.stringConvertArray(this.mFuncSign);
	}

	public void setFuncSign(String funcSign)
	{
		this.mFuncSign = funcSign;
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



}
