package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


public class ESTimePicker extends android.widget.TimePicker implements ICollectible, IReleasable, ICellTitleStyleRequire, IValidatible, IDataInitialize
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectSign;
	protected Boolean mEmpty2Null = true;
	protected Activity mActivity;
	protected String mName;
	protected String mTime;
	protected String mTipText = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESTimePicker(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESTimePicker);
			mIsRequired = a.getBoolean(R.styleable.ESTimePicker_isRequired,false);
			mCollectSign = a.getString(R.styleable.ESTimePicker_collectSign);
			mEmpty2Null = a.getBoolean(R.styleable.ESTimePicker_empty2Null,true);
			mDataType = DataType.DateTime;
			setIs24HourView(true);
		}
		catch(Exception ex)
		{
			Log.e("TimePicker", ex.getMessage());
		}
	}

	public ESTimePicker(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 数据收集，返回DataCollection
	 **/
	public DataCollection collect()
	{

		DataCollection datas = new DataCollection();
		mTime = this.getCurrentHour().toString() + ":" + this.getCurrentMinute().toString();
		if(mTime.equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, mTime, mDataType));
		}
		return datas;
	}

	public LinkedList<String> getRequest()
	{

		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	/**
	 * 发布数据到UIView 
	 *@param dataName 数据名称 
	 *@param data 数据对象 
	 **/
	public void release(String dataName, Data data)
	{

		if(dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(data.getValue() == null)
			{
				return;
			}
			if(this.mDataType == null)
			{
				return;
			}
			else
			{
				String[] times = data.getValue().toString().split(":");
				if(times != null && times.length > 0)
				{
					this.setCurrentHour(Integer.valueOf(times[0]));
					this.setCurrentHour(Integer.valueOf(times[1]));
				}
			}
		}
	}

	public void dataInitialize()
	{
		Date date = Calendar.getInstance().getTime();
		this.setCurrentHour(date.getHours());
		this.setCurrentMinute(date.getMinutes());
		if(mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
	}

	/** 
	 * 数据类型校验，并返回是否成功 
	 * @return 是否校验成功 
	 * **/
	public Boolean dataValidator()
	{
		try
		{
			mTipText = ValidatorHelper.dataTypeValidator(mDataType, mTime);
			Log.v("DataTypeValidator", mTipText);
//			if(mIsRequired)
//			{
//				Log.v("IsRequiredValidator", mTipText);
//				mTipText = ValidatorHelper.createRequiredValidator(mTime);
//			}
			invalidate();
			if(mTipText.length() != 0) { return false; }
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
		return true;
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
//		ToastHelper.toastShow(this.getContext(), getHint().toString());
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.RED);

		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// ַ
		canvas.drawText(mTipText, 8, this.getHeight() / 2 + 5, mPaint);
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName()
	{

		return mName;
	}

	public Object getActivity()
	{
		return mActivity;
	}

	public void setActivity(Object obj)
	{
		if(obj instanceof Activity)
		{
			mActivity = (Activity) obj;
		}
	}

	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
	}

	public DataType getDataType()
	{
		return mDataType;
	}

	public void setIsRequired(Boolean isRequired)
	{
		this.mIsRequired = isRequired;
	}

	public Boolean getIsRequired()
	{
		return mIsRequired;
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public String getTime()
	{
		return mTime;
	}

	public void setTime(String time)
	{
		this.mTime = time;
	}

	public String getTipText()
	{
		return mTipText;
	}

	public void setTipText(String tipText)
	{
		this.mTipText = tipText;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

}
