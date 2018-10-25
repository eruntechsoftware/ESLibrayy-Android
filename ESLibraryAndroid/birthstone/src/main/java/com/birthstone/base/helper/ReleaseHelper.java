package com.birthstone.base.helper;

import android.util.Log;

import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IReleaser;
import com.birthstone.core.parse.DataCollection;

import java.util.ArrayList;
import java.util.List;


public class ReleaseHelper implements IReleaser, IControlSearcherHandler
{
	private DataCollection dataCollection;
	private IChildView childView = null;

	public ReleaseHelper( DataCollection datas, IChildView childView )
	{
		this.dataCollection = datas;
		this.childView = childView;
	}

	public void release(Object data)
	{
		try
		{
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(childView);
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{
		try
		{
			if(obj instanceof IReleasable)
			{
				IReleasable Release = (IReleasable) obj;
				if(!Release.equals(null))
				{
					String name = Release.getName();
					Release.release(name, dataCollection.get(name));
				}
			}
		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
	}

	public Boolean isPicked(Object obj)
	{
		return obj instanceof IReleasable;
	}

	public DataCollection getDataCollection()
	{
		return dataCollection;
	}

	public void setDataCollection(DataCollection dataCollection)
	{
		this.dataCollection = dataCollection;
	}

	public IChildView getForm()
	{
		return childView;
	}

	public void setForm(IChildView childView)
	{
		this.childView = childView;
	}

}
