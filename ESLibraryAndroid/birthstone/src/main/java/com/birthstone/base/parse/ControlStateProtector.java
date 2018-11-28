package com.birthstone.base.parse;

import android.util.Log;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.security.ControlSearcher;
import com.birthstone.core.helper.ModeType;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IStateProtected;
import com.birthstone.widgets.ESHiddenFeild;

import java.util.ArrayList;
import java.util.List;

public class ControlStateProtector implements IControlSearcherHandler
{
	private IChildView childView;
	private ArrayList<String> hiddenFeilds = null;

	public ControlStateProtector()
	{
	}

	public static ControlStateProtector createControlStateProtector()
	{
		ControlStateProtector ControlStateProtector = new ControlStateProtector();
		return ControlStateProtector;
	}

	public void stateProtector(Object activity)
	{
		try
		{
			this.childView = (IChildView) activity;
			List<IControlSearcherHandler> Controllist = new ArrayList<IControlSearcherHandler>();
			Controllist.add(this);
			new ControlSearcher(Controllist).search(activity);
			Controllist.clear();
			Controllist = null;
		}
		catch(Exception ex)
		{
			Log.e("SetStateControl", ex.getMessage());
		}
	}

	public void handle(Object obj)
	{
		try
		{
			if (childView == null)
			{
				if (obj instanceof IChildView)
				{
					childView = (IChildView) obj;
				}
			}
			if (obj instanceof IStateProtected)
			{
				IStateProtected aprotected = (IStateProtected) obj;
				if (aprotected != null)
				{
					String[] getValue = aprotected.getWantedStateValue().replace("|!", "!").split("!");
					if (getValue != null)
					{
						if (aprotected.getStateHiddenId().trim().length() > 0)
						{
							hiddenFeilds = getHiddenFeildList(aprotected.getStateHiddenId().replace("|!", "!").split("!"));
							if (hiddenFeilds != null && hiddenFeilds.size() > 0)
							{
								aprotected.protectState(this.stateIsMatched(aprotected.getModeType(),getValue, hiddenFeilds));
							}
						}
					}
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
		if (obj instanceof Activity)
		{
			return true;
		}
		return (obj instanceof IStateProtected);
	}

	public Boolean stateIsMatched(ModeType modeType,String[] wanted, ArrayList<String> current)
	{
		Boolean result = false;
		if (wanted.equals(null))
		{
			return true;
		}
		int size = wanted.length;
		if(modeType==ModeType.AND)
		{
			for (int i = 0; i < size; i++)
			{
				if (current.contains(wanted[i]))
				{
					result = true;
				}
				else
				{
					result = false;
					break;
				}
			}
		}
		if(modeType==ModeType.OR)
		{
			for (int i = 0; i < size; i++)
			{
				if (current.contains(wanted[i]))
				{
					result = true;
					break;
				}
			}
		}
		return result;
	}

	private ArrayList<String> getHiddenFeildList(String[] nameList)
	{
		ArrayList<String> hiddenFeilds = new ArrayList<>();
		try
		{
			if (nameList != null && nameList.length > 0)
			{
				for (String name : nameList)
				{
					int size = childView.getViews().size();
					for (int i = 0; i < size; i++)
					{
						if (childView.getViews().get(i) instanceof IReleasable)
						{
							IReleasable release = (IReleasable) childView.getViews().get(i);
							if (release.getName().equals(name))
							{
								ESHiddenFeild hidden = (ESHiddenFeild) childView.getViews().get(i);
								hiddenFeilds.add(hidden.getText().toString());
								break;
							}
						}
					}
				}

			}
		}
		catch(Exception ex)
		{

		}
		return hiddenFeilds;
	}

}
