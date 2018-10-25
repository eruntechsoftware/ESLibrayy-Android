package com.birthstone.base.helper;

import android.support.v4.app.Fragment;

import java.util.Stack;

public class FragmentManager
{
    private static Stack<Fragment> FragmentStack;
    private static FragmentManager instance;

    public FragmentManager ()
    {

    }

    public static FragmentManager getFragmentManager ()
    {
        if (instance == null)
        {
            instance = new FragmentManager();
        }
        return instance;
    }

    public static Boolean pop ()
    {
        if(FragmentStack!=null && FragmentStack.size()>0)
        {
            Fragment fragment = FragmentStack.lastElement();
            if (fragment != null)
            {
                FragmentStack.remove(fragment);
                fragment = null;
                return true;
            }
        }
        return true;
    }

    public static void pop (Fragment fragment)
    {
        if (fragment != null)
        {
            if (FragmentStack == null)
            {
                FragmentStack = new Stack<Fragment>();
            }
            FragmentStack.remove(fragment);
            fragment = null;
        }
    }

    public static Fragment first ()
    {
        if (FragmentStack != null && FragmentStack.size() > 0)
        {
            Fragment fragment = FragmentStack.firstElement();
            return fragment;
        }
        return null;
    }

    public static Fragment last ()
    {
        if (FragmentStack != null && FragmentStack.size() > 0)
        {
            Fragment fragment = FragmentStack.lastElement();
            return fragment;
        }
        return null;
    }

    public static Fragment current ()
    {
        if (FragmentStack != null && FragmentStack.size() > 0)
        {
            Fragment fragment = FragmentStack.lastElement();
            return fragment;
        }
        return null;
    }

    public static Object getElement (int index)
    {
        return FragmentStack.get(index);
    }

    public static void push (Fragment fragment)
    {
        if (FragmentStack == null)
        {
            FragmentStack = new Stack<Fragment>();
        }
        FragmentStack.remove(fragment);
        FragmentStack.add(fragment);
    }

    // ջ
    public static void clear ()
    {
        if (FragmentStack == null)
        {
            FragmentStack = new Stack<Fragment>();
        }
        FragmentStack.clear();
    }

    public static int size ()
    {
        if (FragmentStack == null)
        {
            FragmentStack = new Stack<Fragment>();
        }
        return FragmentStack.size();
    }
}
