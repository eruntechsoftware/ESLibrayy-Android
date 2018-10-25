package com.birthstone.widgets.tabHost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.ActivityHelper;

import java.util.ArrayList;

/**
 * 时 间: 2016年04月14日
 * <p>
 * 作者：杜明悦
 * <p>
 * 功 能: 滑动MaterialTabHost
 */
public class ESMaterialTabHost extends LinearLayout implements View.OnClickListener
{

	/**
	 * 声明控件
	 **/
	private LinearLayout titleLayout;
	private LinearLayout cursor;
	private LinearLayout divider;
	private ViewPager viewPager;
	//    private View parentView;

	/**变量声明**/
	/**
	 * Fragment适配器
	 **/
	private FragmentAdapter adapter;
	/**
	 * Fragment集合
	 **/
	private ArrayList<Fragment> fragmentList;
	/**
	 * 标题集合
	 **/
	private ArrayList<ESMaterialTab> materialTabList;
	private Boolean mDisplayTabar = true;
	/**
	 * Fragment索引
	 **/
	private int mIndex = 0;
	/**
	 * 标题布局高度
	 **/
	private float mLayoutTitleHeight = 40f;
	/**
	 * 游标宽度
	 **/
	private int mIndexerWidth = 60;
	/**
	 * 游标高度
	 **/
	private float mIndexerHeight = 2;
	/**
	 * 游标位置
	 **/
	private int offset;
	/**
	 * 游标起始位置
	 **/
	private float cursorStartx;
	/**
	 * 标题栏背景色
	 **/
	private int mTabTitleBackgroundColor = Color.WHITE;
	/**
	 * 标题文字默认颜色
	 **/
	private int mTabTitleTextDefaultColor = Color.BLACK;
	/**
	 * 标题文字选中时颜色
	 **/
	private int mTabTitleTextActiveColor = Color.WHITE;
	/**
	 * 标题文字大小
	 **/
	private float mTabTitleTextSize = 14;
	/**
	 * 游标颜色
	 **/
	private int mTabIndexerColor = Color.RED;

	/**
	 * 分割线颜色
	 * **/
	private int mDividerColor = Color.rgb(245,245,245);

	/**
	 * 字体是否加粗
	 **/
	private boolean mboldText = true;

	/**
	 * 上下文
	 **/
	private Context mContext;
	private Animation animation = null;
	private Fragment mFragment;
	private FragmentActivity mFragmentActivity;
	private OnChangIndexListener mOnChangIndexListener;

	public ESMaterialTabHost(Context context)
	{
		this(context, null);

	}

	public ESMaterialTabHost(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESMaterialTabHost);
		//tabhost高度
		mLayoutTitleHeight = a.getDimension(R.styleable.ESMaterialTabHost_tabHost_height, 40);
		//标题栏背景色
		mTabTitleBackgroundColor = a.getColor(R.styleable.ESMaterialTabHost_tabHost_BackgroundColor, Color.WHITE);
		//tab默认状态时的颜色
		mTabTitleTextDefaultColor = a.getColor(R.styleable.ESMaterialTabHost_tabHost_titleDefaultColor, getResources().getColor(R.color.es_shenhui));
		//tab选中状态时的颜色
		mTabTitleTextActiveColor = a.getColor(R.styleable.ESMaterialTabHost_tabHost_titleActiveColor, Color.BLUE);
		//标题文字字号
		mTabTitleTextSize = a.getFloat(R.styleable.ESMaterialTabHost_tabHost_titleSize, 16);
		//		a.getDimensionPixelSize(R.styleable.ESMaterialTabHost_tabHost_titleSize,
		//								(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

		setBackgroundColor(mTabTitleBackgroundColor);

		//字体是否加粗
		mboldText = a.getBoolean(R.styleable.ESMaterialTabHost_tabHost_boldText, true);
		//游标高度
		mIndexerHeight = a.getDimension(R.styleable.ESMaterialTabHost_tabHost_indexerHeight, 4);
		//游标颜色
		mTabIndexerColor = a.getColor(R.styleable.ESMaterialTabHost_tabHost_indexerColor, getResources().getColor(R.color.es_shenhui));

		fragmentList = new ArrayList<Fragment>();
		materialTabList = new ArrayList<ESMaterialTab>();

		/**设置根布局宽、高**/
		LinearLayout.LayoutParams rootLinearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		rootLinearParams.gravity = Gravity.CENTER;
		this.setOrientation(LinearLayout.VERTICAL);

		/**设置title布局方式(宽、高)**/
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Activity.px2dip(mContext, Activity.dip2px(mContext, (int) mLayoutTitleHeight)));
		linearParams.gravity = Gravity.CENTER;
		titleLayout = new LinearLayout(context);
		titleLayout.setGravity(Gravity.CENTER);
		titleLayout.setLayoutParams(linearParams);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setBackgroundColor(mTabTitleBackgroundColor);
		this.addView(titleLayout);



		/**实例化游标**/
		/**设置游标布局宽、高**/
		mIndexerWidth = Activity.dip2px(mContext, 60);
		LinearLayout.LayoutParams cursorLinearParams = new LayoutParams(Activity.dip2px(mContext, 60), Activity.px2dip(mContext, Activity.dip2px(mContext, (int) mIndexerHeight)));
		cursor = new LinearLayout(context);
		cursor.setBackgroundColor(mTabIndexerColor);
		cursor.setLayoutParams(cursorLinearParams);
		this.addView(cursor);

		/**设置分割线布局方式(宽、高)**/
		LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Activity.px2dip(mContext, 1));
		linearParams.gravity = Gravity.CENTER;
		divider = new LinearLayout(context);
		divider.setGravity(Gravity.CENTER);
		divider.setLayoutParams(dividerParams);
		divider.setOrientation(LinearLayout.HORIZONTAL);
		divider.setBackgroundColor(mDividerColor);
		this.addView(divider);

		/**实例化viewPager**/
		viewPager = new ViewPager(context);
		viewPager.setId(R.id.IDCard);
		viewPager.setBackgroundColor(Color.WHITE);
		viewPager.setLayoutParams(rootLinearParams);
		this.addView(viewPager);

	}

	/**
	 * 修改时间：2016年04月14日 作者：杜明悦 功能：添加Fragment及标题
	 *
	 * @param title    标题
	 * @param fragment fragment
	 */
	public void addFragment(Fragment parmentFragment, String title, Fragment fragment)
	{

		this.mFragment = parmentFragment;

		//添加Fragment
		if (fragmentList != null)
		{
			fragmentList.add(fragment);
		}
		//添加tab标签页
		if (materialTabList != null)
		{
			ESMaterialTab subTab = new ESMaterialTab(mContext);
			subTab.setTitleText(title);
			subTab.setTitleSize(mTabTitleTextSize);
			subTab.setFakeBoldText(mboldText);
			materialTabList.add(subTab);
			subTab.setIndex(materialTabList.size() - 1);
			subTab.setOnClickListener(this);
			materialTabList.get(0).setTextColor(mTabTitleTextActiveColor);
			titleLayout.addView(subTab);
		}
		initWidth();
		initViewPager();
	}



	/**
	 * 修改时间：2016年04月14日 作者：杜明悦 功能：添加Fragment及标题
	 *
	 * @param title    标题
	 * @param fragment fragment
	 */
	public void addFragment(FragmentActivity parmentFragmentActivity, String title, Fragment fragment)
	{

		this.mFragmentActivity = parmentFragmentActivity;

		//添加Fragment
		if (fragmentList != null)
		{
			fragmentList.add(fragment);
		}
		//添加tab标签页
		if (materialTabList != null)
		{
			ESMaterialTab subTab = new ESMaterialTab(mContext);
			subTab.setTitleText(title);
			subTab.setTitleSize(mTabTitleTextSize);
			materialTabList.add(subTab);
			subTab.setIndex(materialTabList.size() - 1);
			subTab.setOnClickListener(this);
			materialTabList.get(0).setTextColor(mTabTitleTextActiveColor);
			titleLayout.addView(subTab);
		}
		initWidth();
		initViewPager();
	}

	/**
	 * 修改时间：2016年04月16日 作者：杜明悦 功能：添加Fragment及标题
	 */
	public void initViewPager()
	{
		if (adapter == null)
		{
			if (mFragment != null)
			{
				adapter = new FragmentAdapter(mFragment.getChildFragmentManager(), fragmentList);
			}
			if (mFragmentActivity != null)
			{
				adapter = new FragmentAdapter(mFragmentActivity.getSupportFragmentManager(), fragmentList);
			}
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(0);
			viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 修改时间：2016年04月16日
	 * <p>
	 * 作者：杜明悦
	 * <p>
	 * 功能：初始化获取屏幕以及游标宽度
	 */
	private void initWidth()
	{
		//先获取屏幕宽度
		int screenW = 0;
		if (this.mFragment != null)
		{
			screenW = ActivityHelper.getActivityWidth(this.mFragment.getActivity());
		}
		if (this.mFragmentActivity != null)
		{
			screenW = ActivityHelper.getActivityWidth(this.mFragmentActivity);
		}
		//每段title的宽度
		offset = screenW / fragmentList.size();

		mIndexerWidth = offset / 2;
		cursor.getLayoutParams().width = mIndexerWidth;

		cursorStartx = mIndexerWidth / 2;
		animation = new TranslateAnimation(0, cursorStartx, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(0);
		cursor.startAnimation(animation);
	}

	/**
	 * 添加OnPageChangeListener事件
	 *
	 * @param onPageChangeListener viewpager监听对象
	 **/
	public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener)
	{
		if (onPageChangeListener != null && viewPager != null)
		{
			viewPager.addOnPageChangeListener(onPageChangeListener);
		}
	}

	public void onClick(View v)
	{
		if (viewPager != null)
		{
			viewPager.setCurrentItem(((ESMaterialTab) v).getIndex());
		}
	}

	/**
	 * 修改时间：2016年03月21日
	 * <p>
	 * 作者：张景瑞
	 * <p>
	 * 功能：页面切换监听事件处理
	 */
	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
	{
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{

		}

		@Override
		public void onPageSelected(int position)
		{
			if (mOnChangIndexListener != null)
			{
				mOnChangIndexListener.changed(position);
			}
			if (mDisplayTabar == true)
			{
				final int index = mIndex;
				animation = new TranslateAnimation(cursorStartx + offset * mIndex, offset * position + cursorStartx, 0, 0);//这个比较简洁，只有一行代码。
				mIndex = position;
				animation.setFillAfter(true);
				animation.setDuration(300);
				cursor.startAnimation(animation);

				if (materialTabList != null && materialTabList.size() > 0)
				{
					//先设置为默认色
					materialTabList.get(index).setTextColor(mTabTitleTextDefaultColor);
					//设置为默认字体大小
					materialTabList.get(index).setTitleSize(mTabTitleTextSize);
					//设置为选中颜色
					materialTabList.get(position).setTextColor(mTabTitleTextActiveColor);
					//设置为选中字体大小
					materialTabList.get(position).setTitleSize(mTabTitleTextSize + 2);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{

		}
	}

	//	/**
	//	 * 中断事件
	//	 */
	//	@Override
	//	public boolean onInterceptTouchEvent(MotionEvent ev)
	//	{
	//		return true;
	//	}
	//
	//	/**
	//	 * 分发事件
	//	 */
	//	@Override
	//	public boolean dispatchTouchEvent(MotionEvent ev)
	//	{
	//		return super.dispatchTouchEvent(ev);
	//	}
	//
	//	/**
	//	 * 实现多个ListView控件同时触发事件
	//	 */
	//	@Override
	//	public boolean onTouchEvent(MotionEvent event)
	//	{
	//
	//		int width = getWidth() / getChildCount();
	//		int height = getHeight();
	//		int count = getChildCount();
	//
	//		float eventX = event.getX();
	//
	//		if (eventX < width)
	//		{    // 滑动左边的Fragment
	//			event.setLocation(width / 2, event.getY());
	//			getChildAt(0).dispatchTouchEvent(event);//移动位置后，分发事件
	//			return true;
	//
	//		}
	//		else if (eventX > width && eventX < 2 * width)
	//		{ //滑动中间的 listView
	//			float eventY = event.getY();
	//			if (eventY < height / 2)
	//			{
	//				event.setLocation(width / 2, event.getY());
	//				for (int i = 0; i < count; i++)
	//				{
	//					View child = getChildAt(i);
	//					try
	//					{
	//						child.dispatchTouchEvent(event);
	//					}
	//					catch(Exception e)
	//					{
	//						e.printStackTrace();
	//					}
	//
	//				}
	//				return true;
	//			}
	//			else if (eventY > height / 2)
	//			{
	//				event.setLocation(width / 2, event.getY());
	//				try
	//				{
	//					getChildAt(1).dispatchTouchEvent(event);
	//				}
	//				catch(Exception e)
	//				{
	//					e.printStackTrace();
	//				}
	//				return true;
	//			}
	//		}
	//		else if (eventX > 2 * width)
	//		{
	//			event.setLocation(width / 2, event.getY());
	//			getChildAt(2).dispatchTouchEvent(event);
	//			return true;
	//		}
	//
	//		return true;
	//	}

	/**
	 * 移除所有视图
	 **/
	public void removeAllViews()
	{
		titleLayout.removeAllViews();
		if (fragmentList != null && fragmentList.size() > 0)
		{
			fragmentList.clear();
		}

		if (materialTabList != null && materialTabList.size() > 0)
		{
			materialTabList.clear();
		}

		adapter.notifyDataSetChanged();
	}

	/**
	 * 修改时间：2016年05月12日 作者：杜明悦 功能：设置是否显示Tab栏
	 *
	 * @param displayTabar 是否显示
	 */
	public void setDisplayTabar(Boolean displayTabar)
	{
		this.mDisplayTabar = displayTabar;
		if (this.mDisplayTabar == false)
		{
			titleLayout.setVisibility(View.GONE);
			cursor.setVisibility(View.GONE);
		}
		else
		{
			titleLayout.setVisibility(View.VISIBLE);
			cursor.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 修改时间：2016年04月15日 作者：杜明悦 功能：设置标题栏背景色
	 *
	 * @param titleBackgroundColor 标题栏背景色
	 */
	public void setTitleBackgroundColor(int titleBackgroundColor)
	{
		this.mTabTitleBackgroundColor = titleBackgroundColor;
		if (this.titleLayout != null)
		{
			this.titleLayout.setBackgroundColor(mTabTitleBackgroundColor);
		}
	}

	/**
	 * 设置选项卡标题
	 *
	 * @param index 选项卡下标
	 * @param title 标题
	 */
	public void setTitleText(int index, String title)
	{
		if (materialTabList != null && materialTabList.size() > 0 && materialTabList.size() > index)
		{
			materialTabList.get(index).setTitleText(title);
		}
	}

	/**
	 * 修改时间：2016年04月15日 作者：杜明悦 功能：设置标题文本默认色
	 *
	 * @param titleTextDefaultColor 标题文本默认色
	 */
	public void setTitleTextDefaultColor(int titleTextDefaultColor)
	{
		this.mTabTitleBackgroundColor = titleTextDefaultColor;
		if (this.materialTabList != null && materialTabList.size() > 0)
		{
			for (ESMaterialTab tab : materialTabList)
			{
				tab.setTextColor(mTabTitleTextDefaultColor);
			}
		}
	}

	/**
	 * 修改时间：2016年04月15日 作者：杜明悦 功能：设置标题栏背景色
	 *
	 * @param titleTextActiveColor 标题栏背景色
	 */
	public void setTabTitleTextActiveColor(int titleTextActiveColor)
	{
		this.mTabTitleTextActiveColor = titleTextActiveColor;
	}

	/**
	 * 修改时间：2016年04月15日 作者：杜明悦 功能：设置游标背景色
	 *
	 * @param currorColor 标题栏背景色
	 */
	public void setTabIndexerColor(int currorColor)
	{
		this.mTabIndexerColor = currorColor;
		if (this.cursor != null)
		{
			this.cursor.setBackgroundColor(mTabIndexerColor);
		}
	}

	/**
	 * 设置滑动处理事件监听
	 *
	 * @param onChangIndexListener 事件监听接口
	 **/
	public void setOnChangIndexListener(OnChangIndexListener onChangIndexListener)
	{
		this.mOnChangIndexListener = onChangIndexListener;
	}

	/**
	 * 修改时间：2016年04月26日 作者：杜明悦 功能：获取当前选择的索引
	 */
	public int getIndex()
	{
		return mIndex;
	}

	/**
	 * 修改时间：2016年07月1日 作者：杜明悦 功能：设置当前选择的索引
	 */
	public void setIndex(int mIndex)
	{
		if (viewPager != null)
		{
			viewPager.setCurrentItem(mIndex, true);
		}
	}

}
