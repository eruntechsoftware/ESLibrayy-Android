package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DateTimeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;


public class ESDateTimeView extends EditText implements ICollectible, IDataInitialize
{
	protected DataType mDataType;
	protected String mCollectSign;
	protected Boolean mEmpty2Null = true;
	protected Activity mActivity;
	protected String mName;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESDateTimeView(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESDateTimeView);
			mEmpty2Null = a.getBoolean(R.styleable.ESDateTimeView_empty2Null, true);
			mCollectSign = a.getString(R.styleable.ESDateTimeView_collectSign);
			this.setEnabled(false);
			this.setText("");
			this.mDataType = DataType.String;
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("DateTimeView", ex.getMessage());
		}
	}

	public ESDateTimeView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		Log.v("DateTimeView--Collect", getText().toString());
		if(this.getText().equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, DateTimeHelper.getNow(), mDataType));
		}
		return datas;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		try
		{
			Thread.sleep(1000);
			super.onDraw(canvas);
			Paint mPaint = new Paint();
			// mPaint.setColor(Color.RED);
			mPaint.setTextSize(this.getTextSize());

			mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
			// ַ
			// this.setWidth((int) mPaint.measureText(TipText));
			canvas.drawText(DateTimeHelper.getNow(), 8, this.getHeight() / 2 + 5, mPaint);
			// canvas.drawText(String.valueOf(DateValue.getYear())+"-"+String.valueOf(DateValue.getMonth())+"-"+String.valueOf(DateValue.getDay())+" "+String.valueOf(DateValue.getHours())+":"+String.valueOf(DateValue.getMinutes())+":"+String.valueOf(DateValue.getSeconds()),
			// 8, this.getHeight()/2+5, mPaint);
			invalidate();
		}
		catch(Exception e)
		{
			Log.e("ƴ", e.getMessage());
		}
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

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
			invalidate();
		}
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public String getNameSpace()
	{
		return mNameSpace;
	}

	public void setNameSpace(String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}
}
