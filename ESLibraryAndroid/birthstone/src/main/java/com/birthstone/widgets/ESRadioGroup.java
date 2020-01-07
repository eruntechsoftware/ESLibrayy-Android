package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnCheckedChangedListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.helper.ModeType;
import com.birthstone.core.helper.ModeTypeHelper;

import java.util.LinkedList;

public class ESRadioGroup extends android.widget.RadioGroup implements ICollectible, IValidatible, IReleasable, IStateProtected, ICellTitleStyleRequire, IDataInitialize
{

	private DataType mDataType;
	private Boolean mIsRequired;
	private String mCollectSign;
	private Boolean mEmpty2Null = true;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected ModeType mModeType;
	private IChildView mActivity;
	protected String mMessage = "";
	private String mName;
	private Object mSelectItemValue = null;
	private Object mSelectItemText = null;
	private String mTipText = "";
	private RadioButton mSelectedRadioButton;
	private OnCheckedChangedListener onCheckedChangedListener;
	public String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	private boolean isEmpty;

	public ESRadioGroup(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESRadioGroup);
			String dataType = a.getString(R.styleable.ESRadioGroup_dataType);
			this.mDataType = DataTypeHelper.valueOf(a.getInt(R.styleable.ESRadioGroup_dataType,0));
			this.mIsRequired = a.getBoolean(R.styleable.ESRadioGroup_isRequired,false);
			this.mEmpty2Null = a.getBoolean(R.styleable.ESRadioGroup_empty2Null, true);
			this.mMessage = a.getString(R.styleable.ESRadioGroup_message);
			this.mCollectSign = a.getString(R.styleable.ESRadioGroup_collectSign);
			this.mStateHiddenId = a.getString(R.styleable.ESRadioGroup_stateHiddenId);
			this.mWantedStateValue = a.getString(R.styleable.ESRadioGroup_wantedStateValue);
			this.mModeType = ModeTypeHelper.valueOf(a.getInt(R.styleable.ESRadioGroup_modeType, 0));
			this.setOnCheckedChangeListener(onCheckedChangeListener);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("RadioGroup", ex.getMessage());
		}
	}

	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener()
	{
		public void onCheckedChanged(android.widget.RadioGroup group, int checkedId)
		{

			View view;
			int size = group.getChildCount();
			//ѭа
			for(int i=0; i<size; i++)
			{
				//ʵ
				view = group.getChildAt(i);
				if(view instanceof RadioButton)
				{
					RadioButton radioButton = (RadioButton) view;
					if (radioButton.getId() == checkedId)
					{
						//ʵѡа
						mSelectedRadioButton = (RadioButton) group.getChildAt(i);
						radioButton.setChecked(true);
						//ѡаֵ
						mSelectItemValue = radioButton.getTag();
						mSelectItemText = radioButton.getText();
						Log.v("value", String.valueOf(mSelectItemText + ":::::" + mSelectItemValue));
					}
					else {
						radioButton.setChecked(false);
					}
				}
				if(view instanceof ESSpinner)
				{
					ESSpinner spinner = (ESSpinner)view;
					if(spinner.getId()==checkedId)
					{
						mSelectItemValue = spinner.getSelectValue();
						mSelectItemText = spinner.getSelectText();
						Log.v("value", String.valueOf(mSelectItemText + ":::::" + mSelectItemValue));
					}
				}
			}
			//Ϊnullִ
			if(getOnCheckedChangedListener() != null)
			{
				onCheckedChangedListener.onCheckedChanged(mSelectedRadioButton);
			}
		}
	};

	/**
	 *
	 */
	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = this.getContext().getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
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
		try
		{
			if(data != null)
			{
				View  view;
				int size = this.getChildCount();
				for(int i = 0; i < size; i++)
				{
					view = this.getChildAt(i);
					if(view instanceof RadioButton)
					{
						RadioButton radioButton = (RadioButton) view;
						if (radioButton != null)
						{
							if (radioButton.getTag().toString().equals(data.getValue().toString()))
							{
								radioButton.setTag(data.getValue().toString());
								radioButton.setChecked(true);
							}
						}
					}
					if(view instanceof ESSpinner)
					{

						ESSpinner spinner = (ESSpinner) view;
						if (spinner != null)
						{
							spinner.setItemSelectedByValue(data.getStringValue());
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("RadioGroup", ex.getMessage());
		}

	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		if(mSelectItemValue == null && mEmpty2Null.equals(true))
		{
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, mSelectItemValue, mDataType));
		}
		return datas;
	}

	/** 
	 * 数据类型校验，并返回是否成功 
	 * @return 是否校验成功 
	 * **/
	@SuppressLint("ResourceType")
	public Boolean dataValidator()
	{
		try
		{
			if(mIsRequired)
			{
				//如果没选中，返回-1
				if(this.getCheckedRadioButtonId() <= 0)
				{
					//空的，什么也没选
					isEmpty=true;
				}
			}
			invalidate();
		}
		catch(Exception ex)
		{
			Log.e("Validator", ex.getMessage());
		}
		return !isEmpty;
	}

	public void clearCheck()
	{
		int size = this.getChildCount();
		View view;
		for(int i=0; i<size; i++)
		{
			view = this.getChildAt(i);
			if(view instanceof RadioButton)
			{
				RadioButton radioButton = (RadioButton) view;
				radioButton.setChecked(false);

			}
			if(view instanceof ESSpinner)
			{
				ESSpinner spinner = (ESSpinner)view;
				spinner.setSelection(0);
			}
		}
	}

	public void clearOtherCheck()
	{
		int size = this.getChildCount();
		View view;
		for(int i=0; i<size; i++)
		{
			view = this.getChildAt(i);
			if(view instanceof ESSpinner)
			{
				ESSpinner spinner = (ESSpinner)view;
				spinner.setSelection(0);
			}
		}
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
		// this.setWidth((int) mPaint.measureText(TipText));
		canvas.drawText(mTipText, 8, this.getHeight() / 2 + 5, mPaint);
	}

	public Object getChildView()
	{
		return mActivity;
	}

	public void setChildView(Object obj)
	{
		mActivity = (IChildView)obj;
	}

	public String getName()
	{
		return mName;
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

	@Override
	public Boolean getIsEmpty() {
		return isEmpty;
	}

	@Override
	public void message() {
		ToastHelper.toastShow(this.getContext(),mMessage);
	}

	@Override
	public void expressionMessage() {

	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public Boolean getEmpty2Null()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null(Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public Object getSelectItemValue()
	{
		return mSelectItemValue;
	}

	public void setSelectItemValue(Object selectItemValue)
	{
		this.mSelectItemValue = selectItemValue;
	}

	public Object getSelectItemText()
	{
		return mSelectItemText;
	}

	public void setSelectItemText(Object selectItemText)
	{
		this.mSelectItemText = selectItemText;
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

	public void setName(String name)
	{
		this.mName = name;
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
	

	public RadioButton getSelectedRadioButton()
	{
		return mSelectedRadioButton;
	}

	/**
	 * OnCheckedChangedListener
	 * @return OnCheckedChangedListener
	 */
	public OnCheckedChangedListener getOnCheckedChangedListener()
	{
		return onCheckedChangedListener;
	}

	/**
	 * OnCheckedChangedListener
	 * @param onCheckedChangedListener
	 */
	public void setOnCheckedChangedListener(OnCheckedChangedListener onCheckedChangedListener)
	{
		this.onCheckedChangedListener = onCheckedChangedListener;
	}
	
}
