package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IRefresh;

import java.util.ArrayList;
import java.util.List;


public class RefreshController implements IControlSearcherHandler
{
	public RefreshController( )
	{
	}

	public static RefreshController createRefreshController()
	{
		RefreshController Ref = new RefreshController();
		return Ref;
	}

	public void refresh(IChildView childView) throws Exception
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(childView);
			Controllist.clear();
			Controllist=null;
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	public void handle(Object obj)
	{
		try
		{
			if(obj instanceof IRefresh)
			{
				IRefresh Refresh = (IRefresh) obj;
				Refresh.refreshData();
			}
		}
		catch(Exception ex)
		{
			Log.v("IRefresh", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IRefresh;
	}

}
