package com.birthstone.annotation;

import android.util.Log;
import android.view.View;

import com.birthstone.base.activity.Activity;
import com.birthstone.base.activity.Fragment;
import com.birthstone.base.activity.FragmentActivity;
import com.birthstone.widgets.ESViewHolder;

import java.lang.reflect.Field;


public class ViewInjectUtils
{

    /**
     * 绑定Activity
     *
     * @param activity
     **/
    public static void inject (Activity activity)
    {
        if (activity != null)
        {
            injectContentView(activity);
            injectViews(activity);
            // injectEvents(activity);
        }
    }

    /**
     * 绑定Activity
     *
     * @param activity
     */
    private static void injectViews (Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
            Log.e("TAG", field.getName() + "");
            BindView viewInjectAnnotation = field.getAnnotation(BindView.class);

            if (viewInjectAnnotation != null)
            {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1)
                {
                    Log.e("TAG", viewId + "");
                    try
                    {
                        Object resView = activity.findViewById(viewId);
                        field.setAccessible(true);
                        field.set(activity, resView);
                    }
                    catch (Exception ex)
                    {
                        Log.e("BindView",ex.getMessage());
                    }

                }
            }

        }

    }

    /**
     * 绑定Activity布局视图
     *
     * @param activity
     */
    private static void injectContentView (Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();
        SetContentView contentView = clazz.getAnnotation(SetContentView.class);
        if (contentView != null)
        {
            int contentViewLayoutId = contentView.value();
            try
            {
                activity.setContentView(contentViewLayoutId);
            }
            catch (Exception ex)
            {
                Log.e("ContentView",ex.getMessage());
            }
        }
    }

    /**
     * 绑定FragmentActivity
     *
     * @param fragmentActivity
     **/
    public static void inject (FragmentActivity fragmentActivity)
    {
        if (fragmentActivity != null)
        {
            injectContentView(fragmentActivity);
            injectViews(fragmentActivity);
        }
    }

    /**
     * 绑定FragmentActivity
     *
     * @param fragmentActivity
     */
    private static void injectViews (FragmentActivity fragmentActivity)
    {
        Class<? extends FragmentActivity> clazz = fragmentActivity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
            Log.e("TAG", field.getName() + "");
            BindView viewInjectAnnotation = field.getAnnotation(BindView.class);
            if (viewInjectAnnotation != null)
            {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1)
                {

                    try
                    {
                        Object resView = fragmentActivity.findViewById(viewId);
                        field.setAccessible(true);
                        field.set(fragmentActivity, resView);
                    }
                    catch (Exception ex)
                    {
                        Log.e("BindView",ex.getMessage());
                    }

                }
            }

        }

    }

    /**
     * 绑定FragmentActivity布局视图
     *
     * @param fragmentActivity
     */
    private static void injectContentView (FragmentActivity fragmentActivity)
    {
        Class<? extends FragmentActivity> clazz = fragmentActivity.getClass();

        SetContentView contentView = clazz.getAnnotation(SetContentView.class);
        if (contentView != null)
        {
            int contentViewLayoutId = contentView.value();
            try
            {
                fragmentActivity.setContentView(contentViewLayoutId);
//				Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
//				method.setAccessible(true);
//				method.invoke(fragmentActivity, contentViewLayoutId);
            }
            catch (Exception ex)
            {
                Log.e("ContentView",ex.getMessage());
            }
        }
    }


    /**
     * 绑定Fragment
     *
     * @param fragment
     **/
    public static void inject (Fragment fragment)
    {
        if (fragment != null)
        {
            injectContentView(fragment);
            injectViews(fragment);
            // injectEvents(activity);
        }
    }

    /**
     * 绑定Fragment
     *
     * @param fragment
     */
    private static void injectViews (Fragment fragment)
    {
        Class<? extends Fragment> clazz = fragment.getClass();
        Field[] fields = clazz.getDeclaredFields();
        View view = fragment.getRootView();

        for (Field field : fields)
        {
            Log.e("TAG", field.getName() + "");
            BindView viewInjectAnnotation = field.getAnnotation(BindView.class);
            if (viewInjectAnnotation != null)
            {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1)
                {
                    Log.e("TAG", viewId + "");
                    try
                    {
                        Object resView = view.findViewById(viewId);
                        fragment.views.add((View) resView);
                        field.setAccessible(true);
                        field.set(fragment, resView);
                    }
                    catch (Exception ex)
                    {
                        Log.e("BindView",ex.getMessage());
                    }

                }
            }

        }

    }

    /**
     * 绑定Fragment布局视图
     *
     * @param fragment
     */
    private static void injectContentView (Fragment fragment)
    {
        Class<? extends Fragment> clazz = fragment.getClass();
        SetContentView contentView = clazz.getAnnotation(SetContentView.class);
        if (contentView != null)
        {
            int contentViewLayoutId = contentView.value();
            try
            {
                fragment.setCreateView(contentViewLayoutId);
//				Method method = clazz.getMethod("setCreateView", int.class);
//				method.setAccessible(true);
//				method.invoke(fragment, contentViewLayoutId);
            }
            catch (Exception ex)
            {
                Log.e("ContentView",ex.getMessage());
            }
        }
    }


    /**
     * 绑定RecyclerView.Adapter
     * @param viewHolder
     * @param view
     **/
    public static void inject (ESViewHolder viewHolder,View view)
    {
        if (view != null)
        {
            injectViews(viewHolder,view);
        }
    }


    /**
     * 绑定RecyclerView.ViewHolder
     * @param viewHolder
     * @param convertView
     */
    private static void injectViews (ESViewHolder viewHolder,View convertView)
    {
        Class<? extends View> clazz = convertView.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields)
        {
            BindView viewInjectAnnotation = field.getAnnotation(BindView.class);
            if (viewInjectAnnotation != null)
            {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1)
                {
                    try
                    {
                        View resView = convertView.findViewById(viewId);
                        field.setAccessible(true);
                        field.set(convertView, resView);
                        viewHolder.addView(resView);
                    }
                    catch (Exception ex)
                    {
                        Log.e("BindView",ex.getMessage());
                    }
                }
            }
        }
    }
}
