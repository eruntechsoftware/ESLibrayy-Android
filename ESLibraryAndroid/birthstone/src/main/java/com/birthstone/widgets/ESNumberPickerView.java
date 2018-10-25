package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;

public class ESNumberPickerView extends LinearLayout implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize, View.OnClickListener, TextWatcher
{

	protected DataType mDataType;
	protected Boolean mIsRequired;
	protected String mCollectSign;
	protected Boolean mEmpty2Null = true;
	protected Boolean mached = true;
	protected Boolean isEmpty = true;
	protected Activity mActivity;
	protected String mName;
	protected String mIsRequiredTooltip = "";
	protected String mRegularExpression = "";
	protected String mRegularTooltip = "";
	protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";

	//当前输入框可输入的值（默认为不限制）
	private int maxValue = Integer.MAX_VALUE;

	//当前的库存量（默认为不限制）
	private int currentInventory = Integer.MAX_VALUE;

	//默认字体的大小
	private int textDefaultSize = 14;

	// 中间输入框的‘输入值
	private ESTextBoxNumber mNumText;

	//默认输入框最小值
	private int minValue = 1;

	private ImageView btn_num_add, btn_num_minus;

	// 监听事件(负责警戒值回调)
	private OnClickInputListener onClickInputListener;

	public ESNumberPickerView(Context context)
	{
		super(context);
	}

	public ESNumberPickerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initNumberPickerView(context, attrs);
	}

	/**
	 * 初始化
	 *
	 * @param context
	 * @param attrs
	 */
	private void initNumberPickerView(final Context context, AttributeSet attrs)
	{
		//加载定义好的布局文件
		LayoutInflater.from(context).inflate(R.layout.es_numberpickerview, this);
		LinearLayout mRoot = (LinearLayout) findViewById(R.id.root);
		btn_num_add = (ImageView) findViewById(R.id.btn_num_add);
		btn_num_minus = (ImageView) findViewById(R.id.btn_num_minus);
		mNumText = (ESTextBoxNumber) findViewById(R.id.number);

		//添加监听事件
		btn_num_add.setOnClickListener(this);
		btn_num_minus.setOnClickListener(this);
		mNumText.setOnClickListener(this);
		mNumText.addTextChangedListener(this);

		//获取自定义属性的相关内容
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ESNumberPickerView);
		// 背景
		//		int resourceId = typedArray.getResourceId(R.styleable.ESNumberPickerView_Background, R.drawable.bg_number_button);
		int addResourceId = typedArray.getResourceId(R.styleable.ESNumberPickerView_button_add_img, R.mipmap.btn_add);
		int subResourceId = typedArray.getResourceId(R.styleable.ESNumberPickerView_button_minus_img, R.mipmap.btn_minus_per);
		btn_num_add.setImageResource(addResourceId);
		btn_num_minus.setImageResource(subResourceId);

		mCollectSign = typedArray.getString(R.styleable.ESNumberPickerView_collectSign);

		//最大值
		maxValue = typedArray.getInteger(R.styleable.ESNumberPickerView_maxValue, 99);
		//最小值
		minValue = typedArray.getInteger(R.styleable.ESNumberPickerView_minValue, 1);

		//中间的编辑框是否可编辑
		boolean editable = typedArray.getBoolean(R.styleable.ESNumberPickerView_editable, true);
		//		//+和-文本的宽度 geDiemension返回float getDimensionPixelSize四舍五入+  getDimensionPixeloffset四舍五入-
		//		int buttonWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_buttonWidth, -1);
		//		//+和-文本的颜色
		int textColor = typedArray.getColor(R.styleable.ESNumberPickerView_numberTextColor, 0xff000000);
		mNumText.setTextColor(textColor);
		//		//+和-文本的字体大小
		int textSize = typedArray.getDimensionPixelSize(R.styleable.ESNumberPickerView_numberTextSize, 12);
		mNumText.setTextSize(textSize);
		mNumText.setText(minValue+"");
		//		// 中间显示数量的按钮宽度
		//		final int editextWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_editextWidth, -1);
		//必须调用这个，因为自定义View会随着Activity创建频繁的创建array
		typedArray.recycle();

		//设置输入框是否可用
		setEditable(editable);
	}

	/**
	 * @param editable 设置输入框是否可编辑
	 */
	private void setEditable(boolean editable)
	{
		if(editable)
		{
			mNumText.setFocusable(true);
			mNumText.setKeyListener(new DigitsKeyListener());
		}
		else
		{
			mNumText.setFocusable(false);
			mNumText.setKeyListener(null);
		}
	}

	/**
	 * @return 获取输入框的最终数字值
	 */
	public int getNumText()
	{
		try
		{
			String textNum = mNumText.getText().toString().trim();
			return Integer.parseInt(textNum);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			mNumText.setText(String.valueOf(minValue));
			return minValue;
		}
	}

	/**
	 * 设置当前的最大值，即库存的上限
	 */
	public ESNumberPickerView setCurrentInventory(int maxInventory)
	{
		this.currentInventory = maxInventory;
		return this;
	}

	/**
	 * @return 获取当前的库存
	 */
	public int getCurrentInvventory()
	{
		return currentInventory;
	}

	public int getMinDefaultNum()
	{
		return minValue;
	}

	/**
	 * 设置默认的最小值
	 *
	 * @param minValue
	 *
	 * @return
	 */
	public ESNumberPickerView setMinDefaultNum(int minValue)
	{
		this.minValue = minValue;
		return this;
	}

	public int getMaxValue()
	{
		return maxValue;
	}

	/**
	 * 最大限制量
	 *
	 * @param maxValue
	 *
	 * @return
	 */
	public ESNumberPickerView setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
		return this;
	}

	/**
	 * @param currentNum 设置当前输入框的值
	 *
	 * @return NumPickerView
	 */
	public ESNumberPickerView setCurrentNum(int currentNum)
	{
		if(currentNum > minValue)
		{
			if(currentNum <= currentInventory)
			{
				mNumText.setText(String.valueOf(currentNum));
			}
			else if(currentNum > maxValue)
			{
				mNumText.setText(String.valueOf(maxValue));
			}
			else
			{
				mNumText.setText(String.valueOf(currentInventory));
			}
		}
		else
		{
			mNumText.setText(String.valueOf(minValue));
		}
		return this;
	}

	public ESNumberPickerView setmOnClickInputListener(OnClickInputListener mOnWarnListener)
	{
		this.onClickInputListener = mOnWarnListener;
		return this;
	}

	@Override
	public void onClick(View view)
	{
		int widgetId = view.getId();
		int numText = getNumText();
		if(widgetId == R.id.btn_num_minus)
		{
			if(numText > minValue + 1)
			{
				mNumText.setText(String.valueOf(numText - 1));
			}
			else
			{
				mNumText.setText(String.valueOf(minValue));
				//小于警戒值
				warningForMinInput();
				Log.d("NumberPicker", "减少已经到达极限");
			}
		}
		else if(widgetId == R.id.btn_num_add)
		{
			if(numText < Math.min(maxValue, currentInventory))
			{
				mNumText.setText(String.valueOf(numText + 1));
			}
			else if(currentInventory < maxValue)
			{
				mNumText.setText(String.valueOf(currentInventory));
				//库存不足
				warningForInventory();
				Log.d("NumberPicker", "增加已经到达极限");
			}
			else
			{
				mNumText.setText(String.valueOf(maxValue));
				// 超过限制数量
				warningForMaxInput();
				Log.d("NumberPicker", "达到已经限制的输入数量");
			}

		}
		mNumText.setSelection(mNumText.getText().toString().length());
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int start, int before, int count)
	{

	}

	@Override
	public void afterTextChanged(Editable editable)
	{
		try
		{
			mNumText.removeTextChangedListener(this);
			String inputText = editable.toString().trim();
			if(!TextUtils.isEmpty(inputText))
			{
				int inputNum = Integer.parseInt(inputText);
				if(inputNum < minValue)
				{
					mNumText.setText(String.valueOf(minValue));
					// 小于警戒值
					warningForMinInput();
				}
				else if(inputNum <= Math.min(maxValue, currentInventory))
				{
					mNumText.setText(inputText);
				}
				else if(inputNum >= maxValue)
				{
					mNumText.setText(String.valueOf(maxValue));
					//超过限量
					warningForMaxInput();
				}
				else
				{
					mNumText.setText(String.valueOf(currentInventory));
					//库存不足
					warningForInventory();
				}
			}
			mNumText.addTextChangedListener(this);
			mNumText.setSelection(mNumText.getText().toString().length());
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 超过的库存限制 Warning for inventory.
	 */
	private void warningForInventory()
	{
		if(onClickInputListener != null)
			onClickInputListener.onWarningForInventory(currentInventory);
	}

	/**
	 * 小于最小值回调 Warning for inventory.
	 */
	private void warningForMinInput()
	{
		if(onClickInputListener != null)
			onClickInputListener.onWarningMinInput(minValue);
	}

	/**
	 * 查过最大值值回调 Warning for inventory.
	 */
	private void warningForMaxInput()
	{
		if(onClickInputListener != null)
			onClickInputListener.onWarningMaxInput(maxValue);
	}

	/**
	 * 超过警戒值回调
	 */
	public interface OnClickInputListener
	{
		void onWarningForInventory(int inventory);

		void onWarningMinInput(int minValue);

		void onWarningMaxInput(int maxValue);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 *
	 * @param dipValue DisplayMetrics类中属性density）
	 *
	 * @return
	 */
	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue （DisplayMetrics类中属性scaledDensity）
	 *
	 * @return
	 */
	public static int sp2px(Context context, float spValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue （DisplayMetrics类中属性scaledDensity）
	 *
	 * @return
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public Boolean dataValidator ()
	{
		try
		{
			if (mIsRequired && getText().toString().trim().equals(""))
			{
				isEmpty = true;
				return false;
			}
			mached = ValidatorHelper.isMached(mRegularExpression, getText().toString());
			if (!mached)
			{
				invalidate();
			}
			return mached;
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

	}

	private void shakeAnimation ()
	{
		Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.es_shake);
		this.startAnimation(shake);
	}


	public DataType getDataType ()
	{
		return mDataType;
	}

	public Boolean getIsRequired ()
	{
		return mIsRequired;
	}

	public void setDataType (DataType arg0)
	{
		mDataType = arg0;
		switch (arg0)
		{
			case String:
				this.mRegularExpression = "*";
				break;
			case Integer:
				this.mRegularExpression = DataTypeExpression.integer();
				break;
			case Numeric:
				this.mRegularExpression = DataTypeExpression.numeric();
				break;
			case Date:
				this.mRegularExpression = DataTypeExpression.date();
				break;
			case DateTime:
				this.mRegularExpression = DataTypeExpression.dateTime();
				break;
			case EMail:
				this.mRegularExpression = DataTypeExpression.eMail();
				break;
			case URL:
				this.mRegularExpression = DataTypeExpression.URL();
				break;
			case IDCard:
				this.mRegularExpression = DataTypeExpression.idCard();
				break;
			case Phone:
				this.mRegularExpression = DataTypeExpression.phone();
				break;
			case Mobile:
				this.mRegularExpression = DataTypeExpression.mobile();
				break;
		}
	}

	public void setIsRequired (Boolean arg0)
	{
		mIsRequired = arg0;
	}

	public void dataInitialize ()
	{
		if (mActivity != null)
		{
			String classnameString = mActivity.getPackageName() + ".R$id";
			mName = InitializeHelper.getName(classnameString, getId());
		}
	}

	public Object getActivity ()
	{
		return mActivity;
	}

	public void setActivity (Object arg0)
	{
		if (arg0 instanceof Activity)
		{
			mActivity = (Activity) arg0;
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
		if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
		{
			if (data.getValue() == null)
			{
				this.setText(minValue+"");
				return;
			}
			if (TextUtils.isEmpty(data.getValue().toString().trim()))
			{
				this.setText(minValue+"");
				return;
			}
			{
				this.setText(data.getValue().toString());
			}
		}
	}

	public DataCollection collect()
	{

		DataCollection datas = new DataCollection();
		if (this.getText().equals("") && mEmpty2Null.equals(true))
		{
			datas.add(new Data(this.mName, null, mDataType));
		}
		else
		{
			datas.add(new Data(mName, getText().toString(), mDataType));
		}
		return datas;
	}

	public void setText(String text)
	{
		if(mNumText!=null)
		{
			mNumText.setText(text);
		}
	}

	public String getText()
	{
		if(mNumText!=null)
		{
			return mNumText.getText().toString();
		}
		return "0";
	}

	public String[] getCollectSign ()
	{
		return StringToArray.stringConvertArray(this.mCollectSign);
	}

	public Boolean getEmpty2Null ()
	{
		return mEmpty2Null;
	}

	public void setEmpty2Null (Boolean empty2Null)
	{
		this.mEmpty2Null = empty2Null;
	}

	public String getTipText ()
	{
		return mIsRequiredTooltip;
	}

	public void setTipText (String tipText)
	{
		this.mIsRequiredTooltip = tipText;
		this.mRegularExpression = tipText;
	}

	public String getNameSpace ()
	{
		return mNameSpace;
	}

	public void setNameSpace (String nameSpace)
	{
		this.mNameSpace = nameSpace;
	}

	public void setName (String name)
	{
		this.mName = name;
	}

	public void setCollectSign (String collectSign)
	{
		this.mCollectSign = collectSign;
	}

	public String getName ()
	{
		return mName;
	}
}
