package com.birthstone.base.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;

import com.birthstone.core.helper.Reflection;
import com.birthstone.core.parse.DataCollection;

public class ActivityHelper
{
	//Activity
	/**
	 * Activity跳转到目标activity
	 * @param parentActivity 上下文Activity 上层屏幕对象
	 * @param targetViewController 目标屏幕路径
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController)
	{
		open(parentActivity, targetViewController, new DataCollection());
	}
	
	/**
	 * Activity跳转到目标activity
	 * @param parentActivity 上下文Activity 上层屏幕对象
	 * @param targetViewController 目标屏幕路径
	 * @param params 参数集
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController, DataCollection params)
	{
		open(parentActivity, targetViewController, params, false, false);
	}

	/**
	 * Activity跳转到目标activity
	 * 
	 * @param parentActivity 上下文Activity 上层屏幕对象
	 * @param targetViewController 目标屏幕路径
	 * @param params 参数集
	 * @param showBtnBack 是否显示返回按钮
	 * @param navigationbar 是否显示导航栏
	 */
	@SuppressLint("NewApi")
	public void open(Activity parentActivity, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
					ActivityManager.push(parentActivity);
					if(params == null)
					{
						params = new DataCollection();
					}
					Intent intent = new Intent();
					intent.putExtra("Parameter", (DataCollection)params.clone());
					intent.putExtra("ActivityType", "Activity");
					intent.putExtra("Navigationbar", navigationbar);
					intent.putExtra("ShowBtnBack", showBtnBack);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					intent.setClass(parentActivity.getApplicationContext(), obj.getClass());
					parentActivity.startActivityForResult(intent, 0);
				}
			obj = null;
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}

	
	//Fragment
	/**
	 * Fragment跳转到目标activity
	 * @param parentFragment 上下文Fragment 上层屏幕
	 * @param targetViewController 目标屏幕路径
	 * @param params 参数集
	 */
	@Deprecated
	public void open(Fragment parentFragment, String targetViewController, DataCollection params)
	{
		open(parentFragment, targetViewController, params, false, false);
	}
	
	/**
	 * Fragment跳转到目标activity
	 * @param parentFragment 上下文Fragment 上层屏幕
	 * @param targetViewController 目标屏幕路径
	 * @param params 参数集
	 * @param showBtnBack 是否显示返回按钮
	 * @param navigationbar 是否显示导航栏
	 */
	@Deprecated
	public void open(Fragment parentFragment, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
				if(params==null)
				{
					params = new DataCollection();
				}
				FragmentManager.push(parentFragment);
				Intent intent = new Intent();
				intent.putExtra("Parameter", params);
				intent.putExtra("ActivityType", "Fragment");
				intent.putExtra("Navigationbar", navigationbar);
				intent.putExtra("ShowBtnBack", showBtnBack);
				intent.setClass(parentFragment.getActivity(), obj.getClass());
				parentFragment.startActivityForResult(intent, 0);
			}
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}
	
	//FragmentActivityתƿ
	/**
	 * FragmentActivity跳转到目标屏幕
	 * @param parentFragment 上下文Fragment 上层屏幕
	 * @param targetFragment 目标屏幕路径
	 * @param params 参数集
	 * @param showBtnBack 是否显示返回按钮
	 * @param navigationbar 是否显示导航栏
	 */
	@SuppressLint("NewApi")
	public void open(FragmentActivity parentFragment, String targetFragment, DataCollection params, Boolean showBtnBack, Boolean navigationbar )
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetFragment);
			if(obj!=null)
			{
				if(params==null)
				{
					params = new DataCollection();
				}
				FragmentActivityManager.push(parentFragment);

				Intent intent = new Intent();
				intent.putExtra("Parameter", (DataCollection)params.clone());
				intent.putExtra("ActivityType", "FragmentActivity");
				intent.putExtra("Navigationbar", navigationbar);
				intent.putExtra("ShowBtnBack", showBtnBack);
	
				intent.setClass(parentFragment.getBaseContext(), obj.getClass());
				parentFragment.startActivityForResult(intent, 0);
			}
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}
	
	/**Context**/
	/**
	 * Context跳转到目标屏幕
	 * @param parent 上下文
	 * @param targetViewController 目标屏幕路径
	 * @param params 参数集
	 * @param showBtnBack 是否显示返回按钮
	 * @param navigationbar 是否显示导航栏
	 */
	@SuppressLint("NewApi")
	public void open(Context parent, String targetViewController, DataCollection params, Boolean showBtnBack, Boolean navigationbar)
	{
		try
		{
			Reflection reflection = new Reflection();
			Object obj = reflection.createInstance(targetViewController);
			if(obj != null)
			{
					if(params == null)
					{
						params = new DataCollection();
					}
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("Parameter", (DataCollection)params.clone());
					intent.putExtra("ActivityType", "Context");
					intent.putExtra("Navigationbar", navigationbar);
					intent.putExtra("ShowBtnBack", showBtnBack);
					intent.setClass(parent, obj.getClass());
					parent.startActivity(intent);
				}
			
		}
		catch(Exception ex)
		{
			Log.e("targetViewController", ex.getMessage());
		}
	}

	/**
	 * 获取屏幕宽度
	 * @param context 上下文
	 * @return 屏幕宽度
	 * */
	public static int getActivityWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 获取屏幕高度
	 * @param context 上下文
	 * @return 屏幕高度
	 * */
	public static int getActivityHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
	
}
