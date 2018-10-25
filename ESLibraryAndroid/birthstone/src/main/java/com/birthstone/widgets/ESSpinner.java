package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnItemSelectIndexChangeListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.parse.CollectController;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;
import com.birthstone.core.sqlite.SQLiteDatabase;

import java.util.LinkedList;


public class ESSpinner extends android.widget.Spinner implements ICollectible, IReleasable, IDataInitialize, ICellTitleStyleRequire, IDataQuery, AdapterView.OnItemSelectedListener
{
	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectName;
	protected String mCollectSign;
	protected String mSign = "ForQuery";
	protected Boolean mEmpty2Null = true;
	protected Boolean mIsEmpty = false;
	protected Activity mActivity;
	protected String mName;
	protected String mDisplayValue;
	protected String mBindValue;
	protected String mSql;
	protected Boolean mAutomatic = true;
	protected Object mSelectValue = "";
	protected String mSelectText = "";
	protected DataTable mDataTable;
	protected String mCharCode;

	protected String mDefaultValue = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";
	protected DataTable dataTable = new DataTable();
	//	protected Object[] displayArray = null;
	//	protected Object[] valueArray = null;
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
			String classnameString = mActivity.getPackageName() + ".R$id";
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
				new BindDataThread().start();
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
			new BindDataThread().start();
		}
		catch(Exception ex)
		{

			Log.v("DropDownList", ex.getMessage());
		}

	}

	/**
	 * 数据源绑定处理线程
	 */
	class BindDataThread extends Thread
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(true)
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	}

	/**
	 * 接收数据，并将数据绑定到适配器
	 **/
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			try
			{
				if(adapter == null)
				{
					adapter = new SpinnerItemAdapter(mActivity, dataTable);
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
		}
	};



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
				setDefaultValue(data.getValue().toString());
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
			ESSpinner.this.mOnItemSelectIndexChangeListener.selectIndexChange(mSelectValue.toString());
		}
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	private void startSwap(final int index, final int index2)
	{
		new Thread()
		{
			public void run()
			{
				Message msg = new Message();
				if(Looper.myLooper() == null)
				{
					Looper.prepare();
				}
				if(true)
				{
					msg.arg1 = index;
					msg.arg2 = index2;
					msg.what = 2;
					handler.sendMessage(msg);
				}
				Looper.loop();
			}
		}.start();
	}

	private void startSwap(final String operno)
	{
		try
		{
			new Thread()
			{
				public void run()
				{
					Message msg = new Message();
					if(Looper.myLooper() == null)
					{
						Looper.prepare();
					}
					if(setDefaultText(operno))
					{
						msg.what = 2;
						handler.sendMessage(msg);
					}
					Looper.loop();
				}
			}.start();
		}
		catch(Exception ex)
		{
			Log.e("startSwap", ex.getMessage());
		}
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

	/**
	 * 设置选中下标
	 */
	public void setSelection(int position)
	{
		startSwap(0, position);
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

	public void setDefaultValue(String value)
	{
		startSwap(value);
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

	@Override
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
				//				TextView Tv1=(TextView)convertView.findViewById(R.id.text1);
				//				Tv11.setText(mList.get(position).getPersonName());
			}
			return convertView;
		}
	}
}
