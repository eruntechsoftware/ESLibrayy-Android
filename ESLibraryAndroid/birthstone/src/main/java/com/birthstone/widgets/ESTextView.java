package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.helper.ModeType;
import com.birthstone.core.helper.ModeTypeHelper;

import java.util.LinkedList;


public class ESTextView extends android.widget.TextView implements ICollectible, IValidatible, IReleasable, IDataInitialize
{
	protected Boolean mIsRequired;
	protected DataType mDataType;
	protected String mCollectSign;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected ModeType mModeType;
	protected Boolean mEmpty2Null = true;
	protected IChildView mActivity;
	protected String mName;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESTextView(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESTextView);
			this.mIsRequired = a.getBoolean(R.styleable.ESTextView_isRequired, false);
			this.mCollectSign = a.getString(R.styleable.ESTextView_collectSign);
			this.mEmpty2Null = a.getBoolean(R.styleable.ESTextView_empty2Null,true);
			this.mDataType = DataTypeHelper.valueOf(a.getInt(R.styleable.ESTextView_dataType,0));
			this.mStateHiddenId = a.getString(R.styleable.ESTextView_stateHiddenId);
			this.mWantedStateValue = a.getString(R.styleable.ESTextView_wantedStateValue);
			this.mModeType = ModeTypeHelper.valueOf(a.getInt(R.styleable.ESTextView_modeType, 0));
			a.recycle();
		}
		catch(Exception e)
		{
			Log.e("TextView", e.getMessage());
		}
	}

	public ESTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = ((Context)mActivity).getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
	}

	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	public void release(String dataName, Data data)
	{
		if(dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(data.getValue() == null)
			{
				this.setText("");
				return;
			}
			if(this.mDataType == null)
			{
				this.setText(data.getValue().toString());
			}
			else if(this.mDataType.equals(DataType.Date))
			{
				this.setText(DateTimeHelper.getDateString(data.getValue().toString(), DateTimeHelper.getDateFormat()));
			}
			else if(this.mDataType.equals(DataType.DateTime))
			{
				this.setText(DateTimeHelper.getDateString(data.getValue().toString(), DateTimeHelper.getDateTimeFormat()));
			}
			else
			{
				this.setText(data.getValue().toString());
			}
		}
	}
	
	public void setName(String name)
	{
		mName = name;
	}

	public String getName()
	{
		return mName;
	}

	public DataCollection collect()
	{

		DataCollection datas = new DataCollection();
		if(this.getText().equals("") && mEmpty2Null.equals(true))
		{
			Log.v("TextView--Collect", "");
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, getText().toString(), mDataType));
		}
		return datas;
	}

	public Object getChildView()
	{
		return mActivity;
	}

	public void setChildView(Object arg0)
	{

		if(arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
		}
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public DataType getDataType()
	{
		return mDataType;
	}

	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public Boolean dataValidator ()
	{
		try
		{
			if (mIsRequired)
			{
				if(getText().toString().trim().equals(""))
				{
					return false;
				}
				return true;
			}
			return true;
		}
		catch (Exception ex)
		{
			Log.e("Validator", ex.getMessage());
		}
		return true;
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
		ToastHelper.toastShow(this.getContext(),getHint().toString());
	}

	private void shakeAnimation ()
	{
		Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.es_shake);
		this.startAnimation(shake);
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
}
