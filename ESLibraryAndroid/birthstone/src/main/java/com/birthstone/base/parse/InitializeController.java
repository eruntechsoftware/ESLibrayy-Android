package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IDataInitialize;

import java.util.ArrayList;
import java.util.List;


public class InitializeController implements IControlSearcherHandler
{
	private IChildView childView;

	public InitializeController(IChildView childView )
	{
		this.childView = childView;
	}

	public void initialize() throws Exception
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
			if(obj instanceof IDataInitialize)
			{
				IDataInitialize Initidata = (IDataInitialize) obj;
				Initidata.setActivity(childView);
				Initidata.dataInitialize();
			}
		}
		catch(Exception ex)
		{
			Log.v("IDataInitialize", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IDataInitialize;
	}

}
