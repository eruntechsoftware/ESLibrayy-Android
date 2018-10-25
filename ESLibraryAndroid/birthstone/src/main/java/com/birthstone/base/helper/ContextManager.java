package com.birthstone.base.helper;

import android.content.Context;

import com.birthstone.base.activity.Activity;

import java.util.Stack;


public class ContextManager
{
	private static Stack<Context> activityStack;
	private static ContextManager instance;

	public ContextManager( )
	{

	}

	public static ContextManager getActivityManager()
	{
		if(instance == null)
		{
			instance = new ContextManager();
		}
		return instance;
	}

	public static Boolean pop()
	{
		Context activity = activityStack.lastElement();
		if(activity != null)
		{
			activityStack.remove(activity);
			return true;
		}
		return true;
	}

	public static void pop(Activity activity)
	{
		if(activity != null)
		{
			if(activityStack == null)
			{
				activityStack = new Stack<Context>();
			}
			activityStack.remove(activity);
		}
	}

	public static Context first()
	{
		Context activity = activityStack.firstElement();
		return activity;
	}

	public static Context last()
	{
		Context activity = activityStack.lastElement();
		return activity;
	}

	public static Context current()
	{
		Context activity = activityStack.lastElement();
		return activity;
	}

	public static Object getElement(int index)
	{
		return activityStack.get(index);
	}

	public static void push(Context activity)
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Context>();
		}
		activityStack.remove(activity);
		activityStack.add(activity);
//		activity.setIndex(activityStack.size() - 1);
	}

	//
	public static void clear()
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Context>();
		}
		activityStack.clear();
	}

	public static int size()
	{
		if(activityStack == null)
		{
			activityStack = new Stack<Context>();
		}
		return activityStack.size();
	}
}
