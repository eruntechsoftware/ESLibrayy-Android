package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnCheckedChangedListener;
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

public class ESRadioGroup extends android.widget.RadioGroup implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize
{

	private DataType mDataType;
	private Boolean mIsRequired;
	private String mCollectSign;
	private Boolean mEmpty2Null = true;
	private Activity mActivity;
	private String mName;
	private Object mSelectItemValue = null;
	private Object mSelectItemText = null;
	private String mTipText = "";
	private RadioButton mSelectedRadioButton;
	private OnCheckedChangedListener onCheckedChangedListener;
	public String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

	public ESRadioGroup(Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESRadioGroup);
			String dataType = a.getString(R.styleable.ESRadioGroup_dataType);
			this.mDataType = DataTypeHelper.valueOf(a.getInt(R.styleable.ESRadioGroup_dataType,0));
			mIsRequired = a.getBoolean(R.styleable.ESRadioGroup_isRequired,false);
			mEmpty2Null = a.getBoolean(R.styleable.ESRadioGroup_empty2Null, true);
			mCollectSign = a.getString(R.styleable.ESRadioGroup_collectSign);
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
			RadioButton radioButton;
			int size = group.getChildCount();
			//ѭа
			for(int i=0; i<size; i++)
			{
				//ʵ
				radioButton = (RadioButton)group.getChildAt(i);
				//жǷѡеİ
				if(radioButton.getId() == checkedId)
				{
					//ʵѡа
					mSelectedRadioButton = (RadioButton)group.getChildAt(i);
					radioButton.setChecked(true);
					//ѡаֵ
					mSelectItemValue = radioButton.getTag();
					mSelectItemText = radioButton.getText();
					Log.v("value", String.valueOf(mSelectItemText + ":::::" + mSelectItemValue));
				}
				else
				{
					radioButton.setChecked(false);
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
			String classnameString = mActivity.getPackageName() + ".R$id";
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
				int size = this.getChildCount();
				for(int i = 0; i < size; i++)
				{
					RadioButton radioButton = (RadioButton) this.getChildAt(i);
					if(radioButton != null)
					{
						if(radioButton.getTag().toString().equals(data.getValue().toString()))
						{
							radioButton.setTag(data.getValue().toString());
							radioButton.setChecked(true);
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
	public Boolean dataValidator()
	{
		try
		{
			if(mIsRequired)
			{
				if(this.getCheckedRadioButtonId() <= 0)
				{
					mTipText = "ѡ!";
				}
			}
			invalidate();
			if(mTipText.length() != 0) { return false; }
		}
		catch(Exception ex)
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
	
	/**
	 * ѡеİ
	 * @return ѡеİ
	 */
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
