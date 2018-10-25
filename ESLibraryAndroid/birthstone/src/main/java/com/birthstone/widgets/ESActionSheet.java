package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.birthstone.R;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

/***
 * 日期：2015年9月1日
 * 作者：杜明悦
 * 功能：底部弹出式列表
 */
public class ESActionSheet extends PopupWindow implements View.OnClickListener
{
	/**
	 * 声明控件
	 **/
	private View view, parent;
	private RelativeLayout dialog_rootView;
	private LinearLayout viewGroup;
	private LinearLayout divider;

	/**
	 * 私有变量声明
	 **/
	private Context context;
	private OnActionSheetClickListener onActionSheetClickListener;
	private String[] itemName;
	private String[] itemValue;
	private DataTable dataTable;
	private String cancel;

	/***
	 * ActionSheet
	 *
	 * @param context
	 * @param itemName 名称列表
	 */
	public ESActionSheet(Context context, View parent, String[] itemName)
	{
		super(context);
		this.context = context;
		this.parent = parent;
		setParams();
		this.itemName = itemName;
		if(this.itemName != null && this.itemName.length > 0)
		{
			createMenu();
		}
	}

	/***
	 * ActionSheet
	 *
	 * @param context
	 * @param itemName 名称列表
	 * @param itemValue 值列表
	 */
	public ESActionSheet(Context context, View parent, String[] itemName, String[] itemValue)
	{
		super(context);
		this.context = context;
		this.parent = parent;
		setParams();
		this.itemName = itemName;
		this.itemValue = itemValue;
		if(this.itemName != null && this.itemName.length > 0)
		{
			createMenu();
		}
	}

	/***
	 * ActionSheet
	 *
	 * @param context
	 * @param dataTable 数据源
	 */
	public ESActionSheet(Context context, View parent, DataTable dataTable, String itemName, String itemValue,String cancel)
	{
		super(context);
		this.context = context;
		this.parent = parent;
		this.cancel=cancel;
		setParams();
		this.dataTable = dataTable;
		DataCollection cancelParams = new DataCollection();
		cancelParams.add(new Data(itemName,cancel));
		cancelParams.add(new Data(itemValue,-10000));
		this.dataTable.add(cancelParams);
		if(this.dataTable != null && this.dataTable.size() > 0)
		{
			createMenu(dataTable, itemName, itemValue);
		}
	}

	/***
	 * 日期：2015年9月1日
	 * 作者：杜明悦
	 * 功能：设置必要参数
	 */
	protected void setParams()
	{
		view = View.inflate(context, R.layout.es_actionsheet, null);
		setContentView(view);

		viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);

		dialog_rootView = (RelativeLayout) view.findViewById(R.id.dialog_rootView);
		dialog_rootView.setOnClickListener(this);


	}

	/***
	 * 日期：2015年9月1日
	 * 作者：杜明悦
	 * 功能：创建弹出菜单
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void createMenu(DataTable table, String itemName, String itemValue)
	{
		if(table != null && table.size() > 0)
		{
			//设置button布局
			ViewGroup.LayoutParams layoutParams;
			int size = table.size();
			for(int i = 0; i < size; i++)
			{
				//添加Button
				Button btnItem = new Button(context);
				//设置view的ID
				btnItem.setId(i);
				btnItem.setText(table.get(i).get(itemName).getStringValue());
				btnItem.setTag(table.get(i).get(itemValue).getStringValue());
				//设置字体样式
				btnItem.setTextColor(Color.BLUE);
				btnItem.setTextSize(16.0f);
				btnItem.setPadding(0, 30, 0, 30);
				//添加事件监听
				btnItem.setOnClickListener(this);

				//设置button布局
				layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				btnItem.setLayoutParams(layoutParams);

				if(i == 0 && i == size - 2)
				{

				}
				else if(i == 0)
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_top));
				}
				else if(i == size - 2)
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_bottom));
				}
				else if(i == size - 1)
				{
					//设置字体样式
					btnItem.setTextColor(Color.RED);
					btnItem.setTag(-10000);
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_background_selector));
				}
				else
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_item));
				}
				viewGroup.addView(btnItem);

				//添加间隔线
				if(i < size - 2)
				{
					divider = new LinearLayout(context);
					divider.setBackgroundColor(Color.LTGRAY);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 0, 0);
					lp.height = 1;
					divider.setLayoutParams(lp);

					viewGroup.addView(divider);

				}
				if(i == size - 2)
				{
					divider = new LinearLayout(context);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 0, 0);
					lp.height = 20;
					divider.setLayoutParams(lp);

					viewGroup.addView(divider);
				}
			}
		}
	}

	/***
	 * 日期：2015年9月1日
	 * 作者：杜明悦
	 * 功能：创建弹出菜单
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void createMenu()
	{
		if(this.itemName != null && this.itemName.length > 0)
		{
			int size = itemName.length;
			for(int i = 0; i < size; i++)
			{
				//添加Button
				Button btnItem = new Button(context);
				//设置view的ID
				btnItem.setId(i);
				btnItem.setText(itemName[i]);
				if(itemValue!=null && itemValue.length>i)
				{
					btnItem.setTag(itemValue[i]);
				}
				//设置字体样式
				btnItem.setTextColor(Color.BLUE);
				btnItem.setTextSize(16.0f);
				btnItem.setPadding(0, 30, 0, 30);
				//添加事件监听
				btnItem.setOnClickListener(this);

				//设置button布局
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				btnItem.setLayoutParams(layoutParams);

				if(i == 0 && i == size - 2)
				{

				}
				else if(i == 0)
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_top));
				}
				else if(i == size - 2)
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_bottom));
				}
				else if(i == size - 1)
				{
					//设置字体样式
					btnItem.setTextColor(Color.RED);
					btnItem.setTag(-10000);
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_background_selector));
				}
				else
				{
					btnItem.setBackground(context.getResources().getDrawable(R.drawable.es_actionsheet_item));
				}
				viewGroup.addView(btnItem);

				//添加间隔线
				if(i < size - 2)
				{
					divider = new LinearLayout(context);
					divider.setBackgroundColor(Color.LTGRAY);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 0, 0);
					lp.height = 1;
					divider.setLayoutParams(lp);

					viewGroup.addView(divider);

				}
				if(i == size - 2)
				{
					divider = new LinearLayout(context);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 0, 0);
					lp.height = 20;
					divider.setLayoutParams(lp);

					viewGroup.addView(divider);
				}
			}
		}
	}

	/**
	 * 修改时间：2016年04月25日
	 * <p>
	 * 作者：张景瑞
	 * <p>
	 * 功能：取得导航栏高度
	 *
	 * @param context
	 *
	 * @return
	 */
	public static int getNavigationBarHeight(Context context)
	{
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		//获取NavigationBar的高度
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	/**
	 * 修改时间：2016年04月25日
	 * <p>
	 * 作者：张景瑞
	 * <p>
	 * 功能：判断是否有导航栏
	 *
	 * @param context
	 *
	 * @return
	 */
	@SuppressLint("NewApi")
	public static boolean checkDeviceHasNavigationBar(Context context)
	{

		//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
		boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

		if(!hasMenuKey && !hasBackKey)
		{
			// 做任何你需要做的,这个设备有一个导航栏
			return true;
		}
		else
		{
			return false;
		}
	}

	@SuppressLint("ResourceType")
	public void onClick(View v)
	{
		if(v instanceof RelativeLayout)
		{
			dismiss();
			return;
		}
		if(v.getId() != -10000)
		{
			if(onActionSheetClickListener != null)
			{
				onActionSheetClickListener.onItemClick ((Button) v);
			}
		}
		dismiss();
	}

	/***
	 * 日期：2015年9月2日
	 * 作者：杜明悦
	 * 功能：显示ActionSheet
	 */
	public void show()
	{
		view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_actionsheet_backgroundview_show));
		viewGroup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_actionsheet_show));

		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		if(checkDeviceHasNavigationBar(context))
		{
			showAtLocation(parent, Gravity.BOTTOM, 0, getNavigationBarHeight(context));
		}
		else
		{
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		}

		update();
	}

	/***
	 * 日期：2015年9月2日
	 * 作者：杜明悦
	 * 功能：隐藏ActionSheet
	 */
	public void dismiss()
	{

		//设置动画效果
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.es_actionsheet_dismiss);
		anim.setAnimationListener(new Animation.AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				view.post(new Runnable()
				{
					@Override
					public void run()
					{
						ESActionSheet.super.dismiss();
					}
				});

			}
		});

		//弹出框列表下滑
		viewGroup.startAnimation(anim);

	}


	/***
	 * 日期：2015年9月2日
	 * 作者：杜明悦
	 * 功能：设置ActionSheet单击处理监听对象
	 * @param onActionSheetClickListener 监听器对象
	 */
	public void setOnActionSheetClickListener(OnActionSheetClickListener onActionSheetClickListener)
	{
		this.onActionSheetClickListener = onActionSheetClickListener;
	}

	/**
	 * 日期：2015年9月2日 作者：杜明悦 功能：ActionSheet单击事件监听器
	 */
	public interface OnActionSheetClickListener
	{

		public void onItemClick(Button view);
	}
}
