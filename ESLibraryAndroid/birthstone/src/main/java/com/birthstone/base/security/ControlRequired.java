package com.birthstone.base.security;

import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.IControlSearcherHandler;

import java.util.ArrayList;
import java.util.List;



public class ControlRequired implements IControlSearcherHandler
{
	private Boolean isRequeired;

	public ControlRequired( )
	{
		isRequeired = true;
	}

	public static ControlRequired createObjectRequire()
	{
		ControlRequired ObjectRequire = new ControlRequired();
		return ObjectRequire;
	}

	public Boolean isMatched(Object form) throws Exception
	{
		try
		{
			if(form != null)
			{
				List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
				Controllist.add(this);
				new ControlSearcher(Controllist).search(form);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return isRequeired;
	}

	public void handle(Object object)
	{
		try
		{
			if(object instanceof ICellTitleStyleRequire)
			{
				/*
				 * ICellTitleStyleRequire Required =
				 * (ICellTitleStyleRequire)object; if
				 * (ValidatorHelper.CreateDataTypeValidator(Required.DataType,
				 * Object)) { if (Required.getIsRequired() == true &&
				 * string.IsNullOrEmpty(Required.ReturnValue() as string))// {
				 * ValidatorHelper.CreateRequiredValidator(Object); IsRequeired
				 * = false; } }
				 */
			}
			else
			{
				isRequeired = false;
			}
		}
		catch(Exception ex)
		{
			try
			{
				throw ex;
			}
			catch(Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Boolean isPicked(Object Object)
	{
		return(Object instanceof ICellTitleStyleRequire);
	}

}
