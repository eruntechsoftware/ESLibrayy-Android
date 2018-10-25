package com.birthstone.base.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.birthstone.R;
import com.birthstone.annotation.ViewInjectUtils;
import com.birthstone.base.helper.ActivityHelper;
import com.birthstone.base.helper.ReleaseHelper;
import com.birthstone.base.helper.StatusBarUtil;
import com.birthstone.base.parse.*;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.ArrayList;
import java.util.List;

public class Fragment extends android.support.v4.app.Fragment implements IChildView, IUINavigationBar
{
	/**
	 * 变量声明
	 **/
	protected UINavigationBar mUINavigationBar;
	protected FragmentActivity mParentFragmentActivity;

	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private Bundle mSavedInstanceState;
	private View mView;

	protected android.content.Context mParentContext;

	public ArrayList<View> views = new ArrayList<View>();
	protected ArrayList<Data> mTransferParams = null;
	private DataCollection releaseParams, mReceiveDataParams, mTransferDataParams;

	private Boolean mParentRefresh = false, mIsParentStart = false;
	protected int index = 0;
	protected int mReleaseCount = 0;

	protected String mTitle, mRightButtonText, mLeftButtonText;
	private Boolean mShowBtnBack = false;

	private static List<String> FUNCTION_LIST = new ArrayList<String>();
	public static int LEFT_IMAGE_RESOURCE_ID;

	public static int RESULT_OK = 185324;
	public static int RESULT_CANCEL = 185816;

	public Fragment()
	{
		mShowBtnBack = false;
	}

	/**
	 * 初始化Fragment
	 *
	 * @param receiveDataParams 接收的参数集
	 **/
	public Fragment(DataCollection receiveDataParams)
	{
		this.mReceiveDataParams = receiveDataParams;
		this.releaseParams = mReceiveDataParams;
		mShowBtnBack = false;
	}

	/**
	 * 初始化Fragment
	 *
	 * @param receiveDataParams 接收的参数集
	 * @param mShowBtnBack      是否显示返回按钮
	 **/
	public Fragment(DataCollection receiveDataParams, Boolean mShowBtnBack)
	{
		this.mReceiveDataParams = receiveDataParams;
		this.releaseParams = mReceiveDataParams;
		mShowBtnBack = this.mShowBtnBack;
	}

	/**
	 * 初始化Fragment
	 *
	 * @param frgamentActivity  根布局frgamentActivity
	 * @param receiveDataParams 接收的参数集
	 **/
	public Fragment(FragmentActivity frgamentActivity, DataCollection receiveDataParams)
	{
		this.mParentFragmentActivity = frgamentActivity;
		this.mReceiveDataParams = receiveDataParams;
		this.releaseParams = mReceiveDataParams;
		mShowBtnBack = false;
	}

	/**
	 * 初始化Fragment
	 *
	 * @param frgamentActivity  根布局frgamentActivity
	 * @param receiveDataParams 接收的参数集
	 * @param mShowBtnBack      是否显示返回按钮
	 **/
	public Fragment(FragmentActivity frgamentActivity, DataCollection receiveDataParams, Boolean mShowBtnBack)
	{
		this.mParentFragmentActivity = frgamentActivity;
		this.mReceiveDataParams = receiveDataParams;
		this.releaseParams = mReceiveDataParams;
		this.mShowBtnBack = mShowBtnBack;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.mInflater = inflater;
		this.mContainer = container;
		this.mSavedInstanceState = savedInstanceState;
		ViewInjectUtils.inject(this);
		return mView;
	}

	public void setCreateView(int resID)
	{
		try
		{
			mView = mInflater.inflate(resID, mContainer, false);
			if(Build.VERSION.SDK_INT > 13)
			{
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().penaltyLog().penaltyDeath().build());
			}
			initView();
		}
		catch(Exception ex)
		{
			Log.e("onCreate", ex.getMessage());
		}
	}

	/**
	 * 初始化UIView参数
	 **/
	public void initView()
	{
		try
		{
			initalizeNavigationBar();
			initViewWithActivity();
			release();
			initDataWithView();
			initFunctionProtectedWithView();
			initStateControlWithView();
		}
		catch(Exception ex)
		{
			Log.e("getInitialize", ex.getMessage());
		}
	}

	/**
	 * 获取根视图
	 **/
	public View getRootView()
	{
		return mView;
	}

	/**
	 * 是否完成初始化
	 */
	public Boolean getInitialize()
	{
		try
		{
			initViewWithActivity();
			return true;
		}
		catch(Exception ex)
		{
			Log.e("getInitialize", ex.getMessage());
		}
		return false;
	}

	/**
	 * 初始化Activity
	 */
	public void initViewWithActivity()
	{
		try
		{
			InitializeController initializeController = new InitializeController(this);
			initializeController.initialize();
			initializeController = null;
		}
		catch(Exception ex)
		{
			Log.v("InitializeController", ex.getMessage());
		}
	}

	/**
	 * 初始化NavigationBar
	 **/
	public void initalizeNavigationBar()
	{
		View rootView = mView;
		if(rootView instanceof ViewGroup)
		{
			ViewGroup viewGroup = (ViewGroup) rootView;
			mUINavigationBar = new UINavigationBar(this.getActivity(), true);
			mUINavigationBar.setId(R.id.uiNavigationBar);
			mUINavigationBar.UINavigationBarDelegat = this;
			viewGroup.addView(mUINavigationBar);

			if(mRightButtonText != null)
			{
				mUINavigationBar.setRightText(mRightButtonText);
			}

			if(mLeftButtonText != null)
			{
				mUINavigationBar.setLeftText(mRightButtonText);
			}

			if(mTitle != null)
			{
				mUINavigationBar.setTitle(mTitle);
			}

			if(!mShowBtnBack)
			{
				mUINavigationBar.setLeftButtonVisibility(View.GONE);
			}
		}
	}

	/**
	 * 发布数据集到当前屏幕
	 */
	public void release()
	{
		ReleaseHelper releaseHelper;
		try
		{
			//调用数据发布前处理方法
			releaseing();

			if(mReceiveDataParams != null && mReceiveDataParams.size() > 0)
			{
				releaseHelper = new ReleaseHelper(mReceiveDataParams, this);
				releaseHelper.release(null);
			}

			//数据发布完成后处理方法
			released();
		}
		catch(Exception ex)
		{
			Log.e("", ex.getMessage());
		}
	}

	/**
	 * 发布数据集到当前屏幕
	 *
	 * @param params 数据集
	 */
	public void release(DataCollection params)
	{
		releaseParams = (DataCollection) params.clone();
		if(releaseParams != null && releaseParams.size() > 0)
		{
			ReleaseHelper releaseHelper;
			try
			{
				//数据发布前处理方法
				releaseing();

				releaseHelper = new ReleaseHelper(releaseParams, this);
				releaseHelper.release(null);

				//数据发布完成后处理方法
				released();

				releaseParams.clear();
			}
			catch(Exception ex)
			{
				Log.e("", ex.getMessage());
			}
		}
	}

	/**
	 * 发布数据集到当前屏幕
	 * @param tag 发布数据时用以区分标识
	 * @param params 数据集
	 */
	public void release (int tag,DataCollection params)
	{
		releaseParams = (DataCollection) params.clone();
		if (releaseParams != null && releaseParams.size() > 0)
		{
			ReleaseHelper releaseHelper;
			try
			{
				//数据发布前处理方法
				releaseing();

				releaseHelper = new ReleaseHelper(releaseParams, this);
				releaseHelper.release(null);

				//数据发布完成后处理方法
				released();

				releaseParams.clear();
			}
			catch (Exception ex)
			{
				Log.e("", ex.getMessage());
			}
		}
	}

	/**
	 * 发布数据集到当前屏幕
	 * @param tag 发布数据时用以区分标识
	 * @param dataTable 数据集
	 */
	public void release(int tag,DataTable dataTable)
	{
		if(dataTable==null)
		{
			return;
		}
		releaseParams = (DataCollection) dataTable.getFirst().clone();
		if (releaseParams != null && releaseParams.size() > 0)
		{
			ReleaseHelper releaseHelper;
			try
			{
				//数据发布前处理方法
				releaseing();

				releaseHelper = new ReleaseHelper(releaseParams, this);
				releaseHelper.release(null);

				//数据发布完成后处理方法
				released();

				releaseParams.clear();
			}
			catch (Exception ex)
			{
				Log.e("", ex.getMessage());
			}
		}
	}

	/**
	 * 数据发布前处理方法
	 */
	public void releaseing()
	{

	}

	/**
	 * 数据发布后处理方法
	 */
	public void released()
	{
	}

	/**
	 * 收集当前Activity数据，并指定收集标签
	 *
	 * @param collectSign 收集标签
	 *
	 * @return DataCollection数据集
	 */
	public DataCollection collect(String collectSign)
	{
		CollectController collecter = new CollectController(this, collectSign);
		return collecter.collect();
	}

	/**
	 * 设置权限状态
	 */
	private void initFunctionProtectedWithView()
	{
		try
		{
			FunctionProtected function = new FunctionProtected();
			function.setStateControl(this);
		}
		catch(Exception ex)
		{
			Log.e("", ex.getMessage());
		}
	}

	/**
	 * 执行查询相关接口
	 */
	public void initDataWithView()
	{
		DataQueryController dataQueryController;
		try
		{
			if(this != null)
			{
				dataQueryController = new DataQueryController(this);
				dataQueryController.query();
			}
		}
		catch(Exception ex)
		{
			Log.e("query", ex.getMessage());
		}
	}

	/**
	 * 校验Activity相关UIView是否合法
	 *
	 * @return 是否合法输入
	 */
	public Boolean validator()
	{
		ValidatorController validatorController = new ValidatorController(this);
		try
		{
			return validatorController.validator();
		}
		catch(Exception ex)
		{
			Log.e("validator", ex.getMessage());
		}
		return false;
	}

	/**
	 * 设置权限代码
	 *
	 * @param funStr 权限代码，以逗号分隔
	 */
	@SuppressLint("DefaultLocale")
	public static void setFunction(String funStr)
	{
		try
		{
			String str = "";
			FUNCTION_LIST.clear();
			while(funStr.indexOf(",") > 0)
			{
				str = funStr.substring(0, funStr.indexOf(","));
				funStr = funStr.substring(funStr.indexOf(",") + 1);
				if(!str.equals("") && !FUNCTION_LIST.contains(str))
				{
					FUNCTION_LIST.add(str.trim().toLowerCase());
				}
			}
			if(funStr.length() > 0 && !FUNCTION_LIST.contains(funStr))
			{
				FUNCTION_LIST.add(funStr.trim().toLowerCase());
			}
		}
		catch(Exception ex)
		{
			Log.e("", ex.getMessage());
		}
	}

	/**
	 * 设置UIView状态
	 */
	public void initStateControlWithView()
	{
		ControlStateProtector.createControlStateProtector().setStateControl(this);
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(resultCode)
		{
			case 185324:
				if(data.getBooleanExtra("isRefresh", false))
				{
					onRefresh(getTransferDataParams());
				}
				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Activity关闭时，通知父级Activity调用此方法，用于页面刷新
	 *
	 * @param mTransferDataParams 参数集
	 **/
	public void onRefresh(DataCollection mTransferDataParams)
	{

	}


	public ArrayList<View> getViews()
	{
		return views;
	}

	public void setViews(ArrayList<View> views)
	{
		this.views = views;
	}

	/**
	 * 获取当前Activity接收父级屏幕传递的参数集
	 *
	 * @return DataCollection类型数据集合
	 **/
	public DataCollection getReceiveDataParams()
	{
		return mReceiveDataParams;
	}

	/**
	 * 设置当前Activity接收父级屏幕传递的参数集
	 *
	 * @param receiveDataParams 接收参数集合
	 **/
	public void setReceiveDataParams(DataCollection receiveDataParams)
	{
		this.mReceiveDataParams = receiveDataParams;
	}


	/**
	 * 获取当前Activity向下级屏幕传递的参数集
	 *
	 * @return DataCollection类型数据集合
	 **/
	public DataCollection getTransferDataParams()
	{
		return mTransferDataParams;
	}


	/**
	 * 设置当前Activity向下级屏幕传递的参数集
	 *
	 * @param transferDataParams 数据集合
	 **/
	public void setTransferDataParams(DataCollection transferDataParams)
	{
		this.mTransferDataParams = transferDataParams;
	}

	/**
	 * 获取当前Activity的父对象，有FragmentActivity
	 *
	 * @return FragmentActivity
	 */
	public IChildView getParentView ()
	{
		if(mParentFragmentActivity!=null)
		{
			return mParentFragmentActivity;
		}
		return null;
	}

	/**
	 * 获取当前Activity的FragmentActivity
	 *
	 * @return FragmentActivity
	 */
	public FragmentActivity getParentFragmentActivity()
	{
		return mParentFragmentActivity;
	}

	/**
	 * 设置Fragment的FragmentActivity
	 *
	 * @param mFragmentActivity
	 **/
	public void setFragmentActivity(FragmentActivity mFragmentActivity)
	{
		this.mParentFragmentActivity = mFragmentActivity;
	}

	/**
	 * 获取权限代码列表
	 *
	 * @return 权限代码列表
	 */
	public static List<String> getFunctionList()
	{
		return FUNCTION_LIST;
	}

	/**
	 * 设置权限代码列表
	 *
	 * @param functionList 权限代码列表
	 **/
	public static void setFunctionList(List<String> functionList)
	{
		Fragment.FUNCTION_LIST = functionList;
	}

	/**
	 * 设置父级页面是否执行刷新方法
	 *
	 * @param mParentRefresh 是否刷新
	 **/
	public void setParentRefresh(boolean mParentRefresh)
	{
		this.mParentRefresh = mParentRefresh;
	}

	/**
	 * 跳转到目标屏幕
	 *
	 * @param targetViewController 目标屏幕
	 * @param navigationbar        是否显示导航栏
	 **/
	public void pushViewController(String targetViewController, Boolean navigationbar)
	{
		try
		{
			ActivityHelper open = new ActivityHelper();
			open.open(this, targetViewController, new DataCollection(), true, navigationbar);
		}
		catch(Exception ex)
		{

		}
	}

	/**
	 * 跳转到目标屏幕并传递参数
	 *
	 * @param targetViewController 目标屏幕
	 * @param params               参数集合
	 * @param navigationbar        是否显示导航栏
	 **/
	public void pushViewController(String targetViewController, DataCollection params, Boolean navigationbar)
	{
		try
		{
			ActivityHelper open = new ActivityHelper();
			open.open(this, targetViewController, params, true, navigationbar);
		}
		catch(Exception ex)
		{

		}
	}

	@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		for(View view : views)
		{
			if(view != null && view.hasOnClickListeners())
			{
				view.setOnClickListener(null);
			}
			view = null;
		}
		views.clear();
		views = null;

		if(mTransferParams != null)
		{
			mTransferParams.clear();
			mTransferParams = null;
		}

		if(releaseParams != null)
		{
			releaseParams.clear();
			releaseParams = null;
		}

		if(mReceiveDataParams != null)
		{
			mReceiveDataParams.clear();
			mReceiveDataParams = null;
		}

		if(mTransferDataParams != null)
		{
			mTransferDataParams.clear();
			mTransferDataParams = null;
		}

		mParentFragmentActivity = null;
		mParentContext = null;

	}

	/**
	 * 获取导航栏
	 *
	 * @return UINavigationBar
	 */
	public UINavigationBar getNavigationBar()
	{
		return mUINavigationBar;
	}

	/**
	 * 设置导航栏背景色
	 *
	 * @param color 背景色
	 */
	public void setUINavigationBarBackgroundColor(int color)
	{
		if(mUINavigationBar != null)
		{
			mUINavigationBar.setBackgroundColor(color);
		}
		UINavigationBar.BACKGROUND_COLOR = color;
		StatusBarUtil.setTranslucent(this.getActivity());
		//设置状态栏和标题栏颜色一致，实现沉浸式状态栏
		StatusBarUtil.setColorNoTranslucent(this.getActivity(), color);
	}

	/**
	 * 设置导航栏背景色
	 *
	 * @param color         背景色
	 * @param isTranslucent 是否半透明状态栏
	 */
	public void setUINavigationBarBackgroundColor(int color, boolean isTranslucent)
	{
		if(mUINavigationBar != null)
		{
			mUINavigationBar.setBackgroundColor(color);
		}
		UINavigationBar.BACKGROUND_COLOR = color;
		if(isTranslucent)
		{
			StatusBarUtil.setTranslucent(this.getActivity());
			//设置状态栏和标题栏颜色一致，实现沉浸式状态栏
			StatusBarUtil.setColorNoTranslucent(this.getActivity(), color);
		}
	}

	/**
	 * 设置NavigationBar左侧按钮是否可见
	 *
	 * @param visible 设置可见性
	 **/
	public void setUINavigationBarLeftButtonVisibility(int visible)
	{
		if(mUINavigationBar != null)
		{
			mUINavigationBar.setLeftButtonVisibility(visible);
		}

	}

	/**
	 * 设置NavigationBar右侧按钮是否可见
	 *
	 * @param visible 设置可见性
	 **/
	public void setUINavigationBarRightButtonVisibility(int visible)
	{
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setRightButtonVisibility(visible);
		}
	}

	/**
	 * 设置NavigationBar是否可见
	 *
	 * @param visible 设置可见性
	 **/
	public void setUINavigationBarVisibility(int visible)
	{
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setVisibility(visible);
		}
	}

	/**
	 * 设置导航栏标题文本
	 *
	 * @param title 标题文本
	 **/
	public void setTitleText(String title)
	{
		this.mTitle = title;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setTitle(mTitle);
		}
	}

	/**
	 * 设置左侧按钮图片
	 *
	 * @param resouceid 图片资源
	 **/
	public void setLeftButtonImage(int resouceid)
	{
		LEFT_IMAGE_RESOURCE_ID = resouceid;
		Activity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		FragmentActivity.LEFT_IMAGE_RESOURCE_ID = resouceid;
		if(getNavigationBar() != null)
		{
			this.getNavigationBar().setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
		}
	}

	/**
	 * 设置导航栏右侧按钮文本
	 *
	 * @param buttonText 按钮文本
	 **/
	public void setRightText(String buttonText)
	{
		this.mRightButtonText = buttonText;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setRightText(buttonText);
		}
	}

	/**
	 * 设置导航栏左侧按钮文本
	 *
	 * @param buttonText 按钮文本
	 **/
	public void setLeftText(String buttonText)
	{
		this.mLeftButtonText = buttonText;
		if(this.getNavigationBar() != null)
		{
			this.getNavigationBar().setLeftText(buttonText);
		}
	}

	/**
	 * 设置右侧按钮图片
	 *
	 * @param resouceid 图片资源
	 **/
	public void setRightButtonImage(int resouceid)
	{
		if(getNavigationBar() != null)
		{
			this.getNavigationBar().setRightButtonImage(resouceid);
		}
	}

	@Override
	public void onLeftClick()
	{

	}

	@Override
	public void onRightClick()
	{

	}
}
