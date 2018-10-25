package com.birthstone.base.security;

import com.birthstone.base.activity.Activity;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.interfaces.IControlSearcherHandler;

import java.util.ArrayList;
import java.util.List;


public class ControlSearcher
{
    private ArrayList<IControlSearcherHandler> handlers;

    public ControlSearcher ()
    {
        this.handlers = new ArrayList<IControlSearcherHandler>();
    }

    public ControlSearcher (List<IControlSearcherHandler> handlers)
    {
        this.handlers = new ArrayList<IControlSearcherHandler>(handlers);
    }

    public void search (Object view) throws Exception
    {
        try
        {
            int size = this.handlers.size();
            // Log.v("Controls",String.valueOf(size));
            if (view instanceof IChildView)
            {
                IChildView iChildView = ((IChildView) view);
                if (iChildView.getViews().size() > 0)
                {
                    int len = iChildView.getViews().size();
                    for (int i = 0; i < len; i++)
                    {
                        search(iChildView.getViews().get(i));
                    }
                }
            }
            else
            {
                for (int i = 0; i < size; i++)
                {
                    if (handlers.get(i).isPicked(view))
                    {
                        handlers.get(i).handle(view);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
