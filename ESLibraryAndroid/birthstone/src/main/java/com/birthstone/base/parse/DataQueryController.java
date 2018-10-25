package com.birthstone.base.parse;


import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataQuery;

import java.util.ArrayList;
import java.util.List;

public class DataQueryController implements IControlSearcherHandler
{
	private IChildView childView;

	public DataQueryController(IChildView childView )
	{
		this.childView = childView;
	}

	public void query()
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
			Log.v("IDataQuery", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{

		try
		{
			if(obj instanceof IDataQuery)
			{
				IDataQuery Initidata = (IDataQuery) obj;
				Initidata.query();
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{

		return obj instanceof IDataQuery;
	}

}
