package com.birthstone.base.parse;

import android.util.Log;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.interfaces.IValidator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorController implements IValidator, IControlSearcherHandler
{

	private IChildView childView;
	private Boolean result = true;
	private Boolean validatorResult = true;

	public ValidatorController(IChildView childView)
	{
		this.result = true;
		this.childView = childView;
	}

	public Boolean validator()
	{
		try
		{

			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(childView);
			Controllist.clear();
			Controllist = null;

		}
		catch(Exception ex)
		{
			Log.v("Validator", ex.getMessage());
		}
		return validatorResult;
	}

	public void handle(Object obj)
	{
		try
		{
			if (result)
			{
				IValidatible validatible = (IValidatible) obj;
				result = validatible.dataValidator();
				if (!result)
				{
					validatible.hint();
					validatorResult = false;
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
		return obj instanceof IValidatible;
	}

}
