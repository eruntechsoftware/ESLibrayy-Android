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
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ESRadioGroup extends android.widget.RadioGroup implements RadioGroup.OnCheckedChangeListener, ICollectible, IValidatible, IReleasable, IStateProtected, ICellTitleStyleRequire, IDataInitialize
{

	private DataType mDataType;
	private Boolean mIsRequired;
	private String mCollectSign;
	private Boolean mEmpty2Null = true;
	protected String mStateHiddenId;
	protected String mWantedStateValue;
	protected boolean multiple;
	protected String check_tag;
	protected ModeType mModeType;
	private IChildView mActivity;
	protected String mMessage = "";
	private String mName;
	private Object mSelectItemValue = null;
	private Object mSelectItemText = null;
	private CompoundButton mSelectedButton;
	private OnCheckedChangedListener onCheckedChangedListener;
	public String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	private boolean isEmpty;

	private Map<String,View> views = new HashMap<>();

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
			this.multiple = a.getBoolean(R.styleable.ESRadioGroup_multiple, false);
			this.check_tag = a.getString(R.styleable.ESRadioGroup_check_tag);
			this.mMessage = a.getString(R.styleable.ESRadioGroup_message);
			this.mCollectSign = a.getString(R.styleable.ESRadioGroup_collectSign);
			this.mStateHiddenId = a.getString(R.styleable.ESRadioGroup_stateHiddenId);
			this.mWantedStateValue = a.getString(R.styleable.ESRadioGroup_wantedStateValue);

			this.mModeType = ModeTypeHelper.valueOf(a.getInt(R.styleable.ESRadioGroup_modeType, 0));
			this.setOnCheckedChangeListener(this);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("RadioGroup", ex.getMessage());
		}
	}

	public void onCheckedChanged(android.widget.RadioGroup group, int checkedId)
	{
		for(View childView:views.values())
		{
			if(childView instanceof ESSpinner)
			{

				ESSpinner spinner = (ESSpinner) childView;
				if (spinner.getId() == checkedId)
				{
					mSelectItemValue = spinner.getSelectValue();
					mSelectItemText = spinner.getSelectText();
					Log.v("value", String.valueOf(mSelectItemText + ":::::" + mSelectItemValue));
				}
				continue;
			}
			if(childView instanceof CompoundButton)
			{
				CompoundButton radioButton = (CompoundButton) childView;
				if (radioButton.getId() == checkedId)
				{
					mSelectedButton = radioButton;
					//radioButton.setChecked(!radioButton.isChecked());
					mSelectItemValue = radioButton.getTag();
					mSelectItemText = radioButton.getText();
					Log.v("value", String.valueOf(mSelectItemText + ":::::" + mSelectItemValue));
					if (getOnCheckedChangedListener() != null)
					{
						this.post(new Runnable(){
							@Override
							public void run()
							{
								onCheckedChangedListener.onCheckedChanged(mSelectedButton);
							}
						});
					}
				}
				else
				{
					if (!multiple)
					{
						radioButton.setChecked(false);
					}
				}
			}
		}
		collectChildView(this);
	}



	/**
	 *
	 */
	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = this.getContext().getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
			initChildViews(this);
			setChecked(check_tag);
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
			if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
			{
				if (data != null)
				{
					if(!multiple)
					{
						View view;
						int size = this.getChildCount();
						for (int i = 0; i < size; i++)
						{
							view = this.getChildAt(i);
							if (view instanceof CompoundButton)
							{
								CompoundButton radioButton = (CompoundButton) view;
								if (radioButton != null)
								{
									if (radioButton.getTag().toString().equals(data.getValue().toString()))
									{
										radioButton.setTag(data.getValue().toString());
										radioButton.setChecked(true);
									}
								}
							}
							if (view instanceof ESSpinner)
							{

								ESSpinner spinner = (ESSpinner) view;
								if (spinner != null)
								{
									spinner.setItemSelectedByValue(data.getStringValue());
								}
							}
						}
					}
					else
					{
						//多选的情况，全部用逗号分隔
						String[] values = data.getStringValue().split(",");
						if(values.length>0)
						{
							for(String v:values)
							{
								releaseChildView(v,this);
							}
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

	private void releaseChildView(String value,View parentView)
	{
		for(View childView : views.values())
		{
			if (childView instanceof CompoundButton)
			{
				CompoundButton radioButton = (CompoundButton) childView;
				if (radioButton != null)
				{
					if (radioButton.getTag().toString().equals(value))
					{
						radioButton.setTag(value);
						radioButton.setChecked(true);
					}
				}
				continue;
			}
			if(childView instanceof ESSpinner)
			{
				ESSpinner spinner = (ESSpinner) childView;
				if (spinner != null)
				{
					spinner.setItemSelectedByValue(value);
				}
				continue;
			}
		}
	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		if(!multiple)
		{
			if (mSelectItemValue == null && mEmpty2Null.equals(true))
			{
				datas.add(new Data(this.mName, null, mDataType));
			}
			else
			{
				datas.add(new Data(mName, mSelectItemValue, mDataType));
			}
		}
		else
		{
			String value = collectChildView(this);
			if(value.length()>0)
			{
				mSelectItemValue = value.substring(0, value.length() - 1);
			}
			datas.add(new Data(mName, mSelectItemValue, mDataType));
		}
		return datas;
	}

	private String collectChildView(View parentView)
	{
		StringBuffer stringBufferValue = new StringBuffer(200);
		StringBuffer stringBufferText = new StringBuffer(200);
		for(View childView : views.values())
		{
			if(childView instanceof CompoundButton)
			{
				CompoundButton radioButton = (CompoundButton) childView;
				if (radioButton != null && radioButton.isChecked()==true)
				{
					stringBufferValue.append(radioButton.getTag()).append(",");
					stringBufferText.append(radioButton.getText()).append(",");
				}
				continue;
			}
			if(childView instanceof ESSpinner)
			{
				ESSpinner spinner = (ESSpinner) childView;
				if (spinner != null && !spinner.getSelectValue().trim().equals(""))
				{
					stringBufferValue.append(spinner.getSelectValue()).append(",");
				}
				continue;
			}
		}
		if(stringBufferValue.length()>0)
		{
			mSelectItemValue = stringBufferValue.substring(0,stringBufferValue.length()-1);
			mSelectItemText = stringBufferText.substring(0,stringBufferText.length()-1);
			return mSelectItemValue.toString();
		}
		return "";
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
				if(this.getCheckedButtonId() <= 0)
				{
					//空的，什么也没选
					isEmpty=true;
				}
				else
				{
					isEmpty=false;
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

	/**
	 * 设置选中值
	 * */
	public void setChecked(Object value)
	{
		if(views!=null && views.containsKey(value.toString()))
		{
			View view = views.get(value.toString());
			if(view instanceof CompoundButton)
			{
				CompoundButton compoundButton = (CompoundButton)view;
				compoundButton.setChecked(true);
			}
			if(view instanceof ESSpinner)
			{
				ESSpinner spinner = (ESSpinner)view;
				spinner.setItemSelectedByValue(value.toString());
			}
		}

	}

	private void initChildViews(View parentView)
	{
		if(parentView!=null)
		{
			if(parentView instanceof ViewGroup)
			{
				ViewGroup viewGroup = (ViewGroup)parentView;
				int size = viewGroup.getChildCount();
				View childView;
				for(int i=0;i<size; i++)
				{
					childView = viewGroup.getChildAt(i);

					if(childView instanceof CompoundButton)
					{
						CompoundButton radioButton = (CompoundButton) childView;
						views.put(radioButton.getTag().toString(),radioButton);
					}
					if(childView instanceof ESSpinner)
					{
						ESSpinner spinner = (ESSpinner) childView;
						views.put("spinner",spinner);
					}
					else if(childView instanceof ViewGroup)
					{
						initChildViews(childView);
					}
				}
			}
		}
	}

	public void clearAllCheck()
	{
		for(View childView : views.values())
		{
			if(childView instanceof CompoundButton)
			{
				CompoundButton radioButton = (CompoundButton) childView;
				radioButton.setChecked(false);
				continue;
			}
			if(childView instanceof ESSpinner)
			{

				ESSpinner spinner = (ESSpinner) childView;
				if (spinner != null)
				{
					spinner.setSelection(0);
				}
				continue;
			}
		}
	}

	/**
	 * 设置默认选中的值
	 * */
	public void checkDefault()
	{
		this.setChecked(this.check_tag);
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
//		ToastHelper.toastShow(this.getContext(), getHint().toString());
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

	public boolean isMultiple()
	{
		return multiple;
	}

	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}

	@Override
	public Boolean getIsEmpty() {
		try
		{
			if(mIsRequired)
			{
				//如果没选中，返回-1
				if(getCheckedButtonId()<0)
				{
					//空的，什么也没选
					isEmpty=true;
				}
				else
				{
					isEmpty=false;
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("Validator", ex.getMessage());
		}
		return isEmpty;
	}

	public int getCheckedButtonId()
	{
		int id = -1;
		for(View childView : views.values())
		{
			if (childView instanceof CompoundButton)
			{
				CompoundButton radioButton = (CompoundButton) childView;
				if (radioButton.isChecked())
				{
					id = radioButton.getId();
				}
				continue;
			}
			if (childView instanceof ESSpinner)
			{

				ESSpinner spinner = (ESSpinner) childView;
				if (spinner.getSelectValue() != null)
				{
					id = spinner.getId();
				}
				continue;
			}
		}
		return id;
	}

	/**
	 * 提示校验错误
	 * **/
	public void message()
	{
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

	public String getCheck_tag()
	{
		return check_tag;
	}

	public void setCheck_tag(String check_tag)
	{
		this.check_tag = check_tag;
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
	

	public CompoundButton getSelectedButton()
	{
		return mSelectedButton;
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
