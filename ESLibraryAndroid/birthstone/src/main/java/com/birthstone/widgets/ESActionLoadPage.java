package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.base.parse.CollectController;
import com.birthstone.base.parse.ReleaseController;
import com.birthstone.core.sqlite.SQLiteDatabase;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeHelper;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IDataQuery;
import com.birthstone.core.interfaces.IReleaser;
import com.birthstone.core.parse.DataTable;

/**
 * @author 杜明悦
 * SQL查询组件，用于显示详细信息
 * */
public class ESActionLoadPage extends TextView implements IDataInitialize, IDataQuery, IReleaser
{
	private SQLiteDatabase mSqlDb;
	protected Activity mActivity;
	protected Boolean mAutomatic = true;
	protected String mSql;
	protected String mSign = "ForQuery";
	protected DataType mDataType;
	protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";

	public ESActionLoadPage( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		try
		{
			this.setVisibility(View.GONE);
			try
			{
				TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESActionLoadPage);
				this.mDataType = DataTypeHelper.valueOf(a.getInt(R.styleable.ESActionLoadPage_dataType,0));
				this.mSign = a.getString(R.styleable.ESActionLoadPage_sign);
				this.mSql = a.getString(R.styleable.ESActionLoadPage_sql);
				this.mAutomatic = a.getBoolean(R.styleable.ESActionLoadPage_automatic,true);
				a.recycle();
			}
			catch(Exception ex)
			{
				Log.e("ʹ", ex.getMessage());
				this.mDataType = DataType.String;
			}
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	public ESActionLoadPage(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	private Boolean onInit()
	{
		mSqlDb = new SQLiteDatabase(mActivity.getApplicationContext());
		this.mSqlDb.getCollectors().add(new CollectController(mActivity, mSign));
		this.mSqlDb.getReleasers().add(new ReleaseController(mActivity));
		mSqlDb.setSql(mSql);
		return true;
	}

	/**
	 * 执行SQL查询
	 * */
	public void execute()
	{
		try
		{
			onInit();
			mSqlDb.executeTable();
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	/**
	 * 使用多线程执行sql查询，并将接收的数据集发布到当前的Activity
	 * */
	Thread mThread = new Thread(new Runnable()
	{
		public void run()
		{
			Message msg = new Message();
			if(Looper.myLooper() == null)
			{
				Looper.prepare();
			}
			if(onInit())
			{
				msg.what = 1;
				handler.sendMessage(msg);
			}
			else
			{
				msg.what = 0;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	});

	/**
	 * 处理数据集，并将数据集发布到Activity
	 * */
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			try
			{
				switch(msg.what)
				{
				case 1:
					mSqlDb.executeTable();
					mSqlDb.close();
					break;
				}
			}
			catch(Exception ex)
			{
				Log.v("ExecuteHandleMessage", ex.getMessage());
			}
		}
	};

	/**
	 * 发布数据集到Activity
	 * @param ds 数据集
	 * @param activity  屏幕
	 * */
	public void execute(DataTable ds, Activity activity)
	{
		try
		{
			mSqlDb = new SQLiteDatabase(activity);
			mSqlDb.getReleasers().add(new ReleaseController(activity));
			mSqlDb.execute(ds);
			mSqlDb.close();
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	/**
	 * Activity采集器方法
	 * */
	public void collector(ICollector Collector, Activity activity)
	{
		try
		{
			mSqlDb.getCollectors().add(Collector);
			mSqlDb.getReleasers().add(new ReleaseController(activity));
			mSqlDb.setSql(mSql);
			mSqlDb.executeTable();
			mSqlDb.close();
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	/**
	 * 数据发布到Activity
	 */
	public void release()
	{
		try
		{
			execute();
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}

	/**
	 * 初始化View的ID,并设置name
	 * */
	public void dataInitialize()
	{
		String classnameString = mActivity.getPackageName() + ".R$id";
		this.setText(InitializeHelper.getName(classnameString, getId()));
		// Release();
	}

	/**
	 * 执行sql查询
	 */
	public void query()
	{
		try
		{
			if(mAutomatic)
			{
				execute();
			}
		}
		catch(Exception ex)
		{
			Log.e("ActionLoadPage", ex.getMessage());
		}
	}
	
	@Override
	public void release(Object arg0)
	{
		query();
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

	public Boolean getAutomatic()
	{
		return mAutomatic;
	}

	public void setAutomatic(Boolean Automatic)
	{
		this.mAutomatic = Automatic;
	}

	public String getSql()
	{
		return mSql;
	}

	public void setSql(String sql)
	{
		this.mSql = sql;
	}

	public String getSign()
	{
		return mSign;
	}

	public void setSign(String sign)
	{
		this.mSign = sign;
	}

	public DataType getDataType()
	{
		return mDataType;
	}

	public void setDataType(DataType dataType)
	{
		this.mDataType = dataType;
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
