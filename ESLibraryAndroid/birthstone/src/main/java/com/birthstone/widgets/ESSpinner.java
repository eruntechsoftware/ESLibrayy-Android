package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnItemSelectIndexChangeListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.helper.AsyncTaskSQL;
import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.LinkedList;

@SuppressLint("AppCompatCustomView")
public class ESSpinner extends Spinner implements ICollectible, IReleasable, IDataInitialize, ICellTitleStyleRequire, IDataQuery, AdapterView.OnItemSelectedListener
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectName;
	protected String mCollectSign;
	protected String mSign = "ForQuery";
	protected Boolean mEmpty2Null = true;
	protected Boolean mIsEmpty = false;
	 protected IChildView mActivity;
	protected String mName;
	protected String mDisplayValue;
	protected String mBindValue;
	protected String mDefault_text;
	protected String mSql;
	protected Boolean mAutomatic = true;
	protected Object mSelectValue = "";
	protected String mSelectText = "";
	protected DataTable mDataTable;
	protected String mCharCode;
	private float mItem_text_size;

	protected String mDefaultValue = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	protected DataTable dataTable = new DataTable();
	//	protected Object[] displayArray = null;
	//	protected Object[] valueArray = null;
	private SQLiteDatabase mSqlDb;
	protected SpinnerItemAdapter adapter;
	protected OnItemSelectIndexChangeListener mOnItemSelectIndexChangeListener;

	public ESSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESSpinner);
			if(mCollectName == null)
			{
				mCollectName = a.getString(R.styleable.ESSpinner_collectName);
			}
			mDisplayValue = a.getString(R.styleable.ESSpinner_displayValue);

			mBindValue = a.getString(R.styleable.ESSpinner_bindValue);
			mDefault_text = a.getString(R.styleable.ESSpinner_default_text);
			mSql = a.getString(R.styleable.ESSpinner_sql);
			mIsRequired = a.getBoolean(R.styleable.ESSpinner_isRequired, false);
			mCollectSign = a.getString(R.styleable.ESSpinner_collectSign);
			mSign = a.getString(R.styleable.ESSpinner_sign);
			if(mSign == null)
			{
				mSign = "";
			}
			mEmpty2Null = a.getBoolean(R.styleable.ESSpinner_empty2Null, true);
			mIsEmpty = a.getBoolean(R.styleable.ESSpinner_isEmpty, true);
			mItem_text_size = a.getDimensionPixelSize(R.styleable.ESSpinner_item_text_size,12);
			mAutomatic = a.getBoolean(R.styleable.ESSpinner_automatic, true);
			mDataType = DataType.String;
			this.setOnItemSelectedListener(this);
			a.recycle();
		}
		catch(Exception ex)
		{
			Log.e("Spinner", ex.getMessage());
		}
	}

	public ESSpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void dataInitialize()
	{
		if(mActivity != null)
		{
			String classnameString = this.getContext().getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
			if(mCollectName == null || mCollectName.equals(""))
			{
				mCollectName = mName;
			}
		}
	}

	/**
	 * 执行sql查询并绑定到适配器
	 */
	public void query()
	{
		try
		{
			if(mAutomatic)
			{
				bind();
			}
		}
		catch(Exception ex)
		{
			Log.v("DropDownList", ex.getMessage());
		}
	}

	/**
	 * 绑定数据源
	 **/
	public void bind()
	{
		try
		{
			if(mSqlDb==null)
			{
				mSqlDb = new SQLiteDatabase(this.getContext());
			}
			dataTable = mSqlDb.executeTable(mSql,this.mActivity.collect(mSign));

			try
			{
				if(dataTable!=null && mDefault_text!=null)
				{
					DataCollection temp = (DataCollection) dataTable.get(0).clone();
					if(temp!=null)
					{
						temp.get(mDisplayValue).setValue(mDefault_text);
						temp.get(mBindValue).setValue(-100);
						dataTable.add(0,temp);
					}
				}
				if(adapter == null)
				{
					adapter = new SpinnerItemAdapter(ESSpinner.this.getContext(), dataTable);
					setAdapter(adapter);
				}
				else
				{
					adapter.notifyDataSetChanged();
				}
			}
			catch(Exception ex)
			{
				Log.v("ExecuteHandleMessage", ex.getMessage());
			}

//			new AsyncTaskSQL(this.getContext(),mSql,this.mActivity.collect(mSign)){
//
//				/**
//				 * 执行成功后的方法
//				 */
//				public void onSuccess(DataTable rs) throws Exception
//				{
//					try
//					{
//						dataTable = rs;
//						if(dataTable!=null && mDefault_text!=null)
//						{
//							DataCollection temp = (DataCollection) dataTable.get(0).clone();
//							if(temp!=null)
//							{
//								temp.get(mDisplayValue).setValue(mDefault_text);
//								temp.get(mBindValue).setValue(-100);
//								dataTable.add(0,temp);
//							}
//						}
//						if(adapter == null)
//						{
//							adapter = new SpinnerItemAdapter(ESSpinner.this.getContext(), dataTable);
//							setAdapter(adapter);
//						}
//						else
//						{
//							adapter.notifyDataSetChanged();
//						}
//					}
//					catch(Exception ex)
//					{
//						Log.v("ExecuteHandleMessage", ex.getMessage());
//					}
//				}
//
//				/**
//				 * 执行失败后的处理方法
//				 */
//				public void onFail () throws Exception
//				{
//
//				}
//
//			}.execute();
		}
		catch(Exception ex)
		{

			Log.v("DropDownList", ex.getMessage());
		}

	}


	/**
	 * 绑定指定数据到适配器
	 *
	 * @param dataTable   绑定数据数组
	 * @param activity     屏幕对象
	 **/
	public void bind(DataTable dataTable, Activity activity)
	{
		try
		{
			if(dataTable!=null)
			{
				if(dataTable!=null && mDefault_text!=null)
				{
					DataCollection temp = (DataCollection) dataTable.get(0).clone();
					if(temp!=null)
					{
						temp.get(mDisplayValue).setValue(mDefault_text);
						temp.get(mBindValue).setValue(-100);
						dataTable.add(0,temp);
					}
				}

				adapter = new SpinnerItemAdapter(activity, dataTable);
				this.setAdapter(adapter);

				mSelectValue = dataTable.getFirst().get(mBindValue).getStringValue();
				mSelectText = dataTable.getFirst().get(mDisplayValue).getStringValue();
			}

			// this.setPrompt("ѡ");
		}
		catch(Exception ex)
		{
			Log.v("DropDownList", ex.getMessage());
		}
	}

	/**
	 * 获取采集标签名称
	 *
	 * @return 标签名称
	 */
	public LinkedList<String> getRequest()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(mName);
		return list;
	}

	/**
	 * 发布数据到UIView
	 *
	 * @param dataName 数据名称
	 * @param data     数据对象
	 **/
	public void release(String dataName, Data data)
	{
		if(dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if(data.getValue() != null)
			{
				setItemSelectedByValue(data.getValue().toString());
			}
		}
	}

	/**
	 * 数据收集
	 *
	 * @return 数据集合对象
	 **/
	public DataCollection collect()
	{
		DataCollection datas = new DataCollection();
		if(mSelectText.equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(mCollectName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mCollectName, mSelectValue.toString().trim(), mDataType));
		}
		return datas;
	}

	public String[] getCollectSign()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		mSelectValue = dataTable.get(arg2).get(mBindValue).getStringValue();
		mSelectText = dataTable.get(arg2).get(mDisplayValue).getStringValue();

		if(ESSpinner.this.mOnItemSelectIndexChangeListener != null)
		{
			ESSpinner.this.mOnItemSelectIndexChangeListener.selectIndexChange(mSelectValue.toString(),mSelectText);
		}
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	private boolean setDefaultText(String mDefaultValue)
	{
		if(dataTable != null)
		{
			int size = dataTable.size();
			for(int i = 0; i < size; i++)
			{
				if(dataTable.get(i).get(mDisplayValue).getStringValue().equals(mDefaultValue) || dataTable.get(i).get(mDisplayValue).getStringValue().equals(mDefaultValue))
				{
					dataTable.addFirst(dataTable.get(i));
					return true;
				}
			}
		}
		return false;
	}

	public void setItemSelectedByValue(String value)
	{

		int k= dataTable.size();
		for(int i=0;i<k;i++)
		{
			if(value.equals(dataTable.get(i).get(mBindValue).getStringValue().trim()))
			{
//                spinner.setSelection(i,true);// 默认选中项
				this.setSelection(i);// 默认选中项
				break;
			}
		}
	}

	/**
	 * 获取选中值
	 *
	 * @return 选中值
	 */
	public String getSelectValue()
	{
		if(mSelectValue != null)
		{
			return mSelectValue.toString();
		}
		return "";
	}

	/**
	 * 设置选中值
	 *
	 * @param selectValue 选中值
	 */
	public void setSelectValue(Object selectValue)
	{
		this.mSelectValue = selectValue;
	}

	/**
	 * 获取选中文本
	 *
	 * @return 选中文本
	 */
	public String getSelectText()
	{
		return mSelectText;
	}

	/**
	 * 设置选中文本
	 *
	 * @param selectText 选中文本
	 */
	public void setSelectText(Object selectText)
	{
		this.mSelectText = mSelectText;
	}

	public String getDisplayValue()
	{
		return mDisplayValue;
	}

	public String getBindValue()
	{
		return mBindValue;
	}


	//	public Object[] getDisplayArray()
	//	{
	//		return displayArray;
	//	}
	//
	//	public void setDisplayArray(String[] displayArray)
	//	{
	//		this.displayArray = displayArray;
	//	}
	//
	//	public Object[] getValueArray()
	//	{
	//		return valueArray;
	//	}
	//
	//	public void setValueArray(String[] valueArray)
	//	{
	//		this.valueArray = valueArray;
	//	}

	public String getDefaultValue()
	{
		return mDefaultValue;
	}


	@Override
	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
	}

	@Override
	public DataType getDataType()
	{
		return mDataType;
	}

	@Override
	public void setIsRequired(Boolean isRequired)
	{
		this.mIsRequired = isRequired;
	}

	@Override
	public Boolean getIsRequired()
	{
		return mIsRequired;
	}

	public void setCollectSign(String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName()
	{
		return mName;
	}

	public float getItem_text_size() {
		return mItem_text_size;
	}

	public void setItem_text_size(float item_text_size) {
		this.mItem_text_size = item_text_size;
	}

	@Override
	public Object getChildView()
	{
		return mActivity;
	}

	public void setChildView(Object arg0)
	{
		if(arg0 instanceof IChildView)
		{
			mActivity = (IChildView) arg0;
		}
	}

	public void setName(String name)
	{
		this.mName = name;
	}

	public void setOnItemSelectIndexChangeListener(OnItemSelectIndexChangeListener onItemSelectIndexChangeListener)
	{
		this.mOnItemSelectIndexChangeListener = onItemSelectIndexChangeListener;
	}

	class SpinnerItemAdapter extends BaseAdapter
	{
		private DataTable dataTable;
		private Context mContext;

		public SpinnerItemAdapter(Context pContext, DataTable dataTable)
		{
			this.mContext = pContext;
			this.dataTable = dataTable;
		}

		@Override
		public int getCount()
		{
			return dataTable == null ? 0 : dataTable.size();
		}

		@Override
		public Object getItem(int position)
		{
			return dataTable.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/**
		 * 下面是重要代码
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
			DataCollection row = dataTable.get(position);
			convertView = _LayoutInflater.inflate(R.layout.es_simple_spinner_item, null);

			if(convertView != null)
			{
				TextView textView = (TextView)convertView.findViewById(R.id.text1);
				textView.setText(dataTable.get(position).get(mDisplayValue).getStringValue());
				textView.setTextSize(mItem_text_size);
				//RadioButton checkbox = (RadioButton)convertView.findViewById(R.id.checkbox);
			}
			return convertView;
		}
	}
}
