package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeHelper;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;


public class ESCheckBox extends android.widget.CheckBox implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected Boolean mEmpty2Null = true;
	protected String mCollectSign;
	protected Activity mActivity;
	protected String mName;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";

	public ESCheckBox(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESCheckBox);
			int value = a.getInt(R.styleable.ESCheckBox_dataType,1);
			this.mDataType = DataTypeHelper.valueOf(value);

			mIsRequired = a.getBoolean(R.styleable.ESCheckBox_isRequired,false);
			mEmpty2Null = a.getBoolean(R.styleable.ESCheckBox_empty2Null, true);
			mCollectSign = a.getString(R.styleable.ESCheckBox_collectSign);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("CheckBox", ex.getMessage());
		}
	}

	public ESCheckBox(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void dataInitialize()
	{
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
		return true;
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
		ToastHelper.toastShow(this.getContext(), getHint().toString());
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
			else
			{
				if(data.getValue().equals("1") || data.getValue().toString().toLowerCase().equals("true"))
				{
					this.setChecked(true);
				}
				if(data.getValue().equals("0") || data.getValue().toString().toLowerCase().equals("false"))
				{
					this.setChecked(false);
				}
			}
		}
	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		Log.v("CheckBox--Collect", getText().toString());
		if(this.isChecked())
		{
			datas.add(new Data(this.mName, 1, mDataType));
		}
		else
		{
			datas.add(new Data(mName, 0, mDataType));
		}
		return datas;
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

	public String getName()
	{
		return mName;
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

	public void setName(String name)
	{
		this.mName = name;
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
