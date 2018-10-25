package com.birthstone.base.activity;


import com.birthstone.base.helper.ActivityHelper;
import com.birthstone.core.parse.DataCollection;

public abstract class Context extends android.content.Context
{

	/**
	 * ͼ
	 * 
	 * @param targetViewController
	 * @param params ͼĲ
	 * @param navigationbar ǷĬ
	 * **/
	public static void pushViewController(android.content.Context context, String targetViewController, DataCollection params, Boolean navigationbar)
	{
		try
		{
			ActivityHelper open = new ActivityHelper();
			open.open(context, targetViewController, params, true, navigationbar);
		}
		catch(Exception ex)
		{

		}
	}
}
