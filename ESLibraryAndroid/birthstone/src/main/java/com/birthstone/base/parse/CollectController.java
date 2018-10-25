package com.birthstone.base.parse;


import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.parse.DataCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据收集器
 */
public class CollectController implements ICollector, IControlSearcherHandler
{
	IChildView childView;
	DataCollection mResult;
	String mSign;

	/**
	 * 数据收集器
	 * @param childView 屏幕
	 */
	public CollectController(IChildView childView )
	{
		this.childView = childView;
	}

	/**
	 * 数据收集器
	 * @param childView 屏幕
	 * @param sign 收集标记
	 */
	public CollectController(IChildView childView, String sign )
	{
		this.childView = childView;
		this.mSign = sign;
	}

	/**
	 * 数据收集，返回DataCollection
	 * **/
	public DataCollection collect()
	{
		this.mResult = new DataCollection();
		try
		{
			if(childView != null)
			{
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(this.childView);
				Controllist.clear();
				Controllist=null;
			}
		}
		catch(Exception ex)
		{
			Log.v("CollectController", ex.getMessage());
		}
		return this.mResult;
	}


	public void handle(Object obj)
	{
		try
		{
			if(obj instanceof ICollectible)
			{
				ICollectible collect = (ICollectible) obj;
				DataCollection dataCollection = collect.collect();
				if((dataCollection != null) && (dataCollection.size() != 0))
				{
					this.mResult.addAll(dataCollection);
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("ռ '" + obj.toString() + "' ֹ˿ãԭ", ex.getMessage());
		}
	}

	/**
	 * 是否匹配收集接口
	 */
	public Boolean isPicked(Object obj)
	{
		if(obj instanceof ICollectible)
		{
			ICollectible Collectible = (ICollectible) obj;
			if(this.mSign == null)
			{
				return false;
			}
			else
			{
				return((Collectible != null) && (this.mSign.equals(null) || this.matchSign(this.mSign, Collectible.getCollectSign())));
			}
		}
		return false;
	}

	private Boolean matchSign(String target, String[] strings)
	{
		int size = strings.length;
		for(int i = 0; i < size; i++)
		{
			if(strings[i] != null && strings[i].equals(target)) { return true; }
		}
		return false;
	}

}
