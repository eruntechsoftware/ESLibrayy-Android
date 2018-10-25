package com.birthstone.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.birthstone.R;
import com.birthstone.annotation.ViewInjectUtils;
import com.birthstone.base.helper.ActivityHelper;
import com.birthstone.base.helper.ActivityManager;
import com.birthstone.base.helper.FragmentActivityManager;
import com.birthstone.base.helper.FragmentManager;
import com.birthstone.base.helper.ReleaseHelper;
import com.birthstone.base.helper.StatusBarUtil;
import com.birthstone.base.parse.CollectController;
import com.birthstone.base.parse.ControlStateProtector;
import com.birthstone.base.parse.DataQueryController;
import com.birthstone.base.parse.FunctionProtected;
import com.birthstone.base.parse.InitializeController;
import com.birthstone.base.parse.ValidatorController;
import com.birthstone.core.interfaces.IChildView;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.ArrayList;
import java.util.List;


@SuppressLint({"DefaultLocale", "HandlerLeak"})
public class Activity extends android.app.Activity implements IUINavigationBar, IChildView
{
    /**
     * 变量声明
     **/
    protected UINavigationBar mUINavigationBar;
    protected FragmentActivity mParentFragmentActivity;
    protected Fragment mParentFragment;
    protected Activity mParentActivity;
    protected Context mParentContext;

    protected ArrayList<View> views = new ArrayList<View>();
    protected ArrayList<Data> mTransferParams = null;
    private DataCollection releaseParams, mReceiveDataParams, mTransferDataParams;
    protected String mTitle, mRightButtonText,mLeftButtonText;
    private Boolean mIsParentStart = false;
    private Boolean mShowBtnBack = false;
    protected int index = 0;
    protected PreferenceManager.OnActivityResultListener onActivityResultListener;

    protected int mReleaseCount = 0;

    private static List<String> FUNCTION_LIST = new ArrayList<String>();
    public static int LEFT_IMAGE_RESOURCE_ID;
    private static float DENSITY;
    public static int RESULT_OK = 185324;
    public static int RESULT_CANCEL = 185816;

    public Activity ()
    {

    }

    @SuppressLint("NewApi")
    protected void onCreate (Bundle savedInstanceState)
    {
        try
        {
            if(Build.VERSION.SDK_INT > 13)
            {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().penaltyLog().penaltyDeath().build());
            }
            super.onCreate(savedInstanceState);
            ViewInjectUtils.inject(this);
            initalizeNavigationBar();
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            DENSITY = metric.density;

            this.getFragment();
            Intent intent = getIntent();
            if(intent != null)
            {
                mShowBtnBack = intent.getBooleanExtra("ShowBtnBack", false);
                if(mShowBtnBack)
                {
                    mUINavigationBar.setLeftButtonVisibility(View.VISIBLE);
                }

                String activityType = intent.getStringExtra("ActivityType");
                mTransferParams = (ArrayList<Data>) intent.getSerializableExtra("Parameter");// intent.getStringExtra("Parameter");

                if(activityType != null && activityType.equals("Activity"))
                {
                    if(ActivityManager.last() instanceof Activity)
                    {
                        this.mParentActivity = (Activity) ActivityManager.last();
                    }
                    this.mIsParentStart = true;
                    if(mIsParentStart)
                    {
                        if(this.mReceiveDataParams == null)
                        {
                            this.mReceiveDataParams = new DataCollection();
                        }
                        for(Data data : mTransferParams)
                        {
                            this.mReceiveDataParams.add(data);
                        }
                    }
                }

                if(activityType != null && activityType.equals("Fragment"))
                {
                    android.support.v4.app.Fragment fragment = FragmentManager.last();
                    if(fragment instanceof Fragment)
                    {
                        this.mParentFragment = (Fragment) fragment;
                    }
                    this.mIsParentStart = true;
                    if(mIsParentStart)
                    {
                        if(this.mReceiveDataParams == null)
                        {
                            this.mReceiveDataParams = new DataCollection();
                        }
                        for(Data data : mTransferParams)
                        {
                            this.mReceiveDataParams.add(data);
                        }
                    }
                }

                if(activityType != null && activityType.equals("FragmentActivity"))
                {
                    android.support.v4.app.FragmentActivity fragmentActivity = FragmentActivityManager.last();
                    if(fragmentActivity instanceof FragmentActivity)
                    {
                        this.mParentFragmentActivity = (FragmentActivity) fragmentActivity;
                    }
                    this.mIsParentStart = true;
                    if(mIsParentStart)
                    {
                        if(this.mReceiveDataParams == null)
                        {
                            this.mReceiveDataParams = new DataCollection();
                        }
                        for(Data data : mTransferParams)
                        {
                            this.mReceiveDataParams.add(data);
                        }
                    }
                }

                if(activityType != null && activityType.equals("Context"))
                {
                    if(this.mReceiveDataParams == null)
                    {
                        this.mReceiveDataParams = new DataCollection();
                    }
                    for(Data data : mTransferParams)
                    {
                        this.mReceiveDataParams.add(data);
                    }
                }
            }
            releaseParams = mReceiveDataParams;
            initView();
            intent = null;
        }
        catch(Exception ex)
        {
            Log.e("onCreate",ex.getMessage());
        }
    }

    /**
     * 设置内容视图资源ID
     *
     * @param layoutResID 资源ID
     **/
    public void setContentView (int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    /**
     * 初始化UIView参数
     **/
    public void initView ()
    {
        try
        {
            initViewWithActivity();
            release();
            initDataWithView();
            initFunctionProtectedWithView();
            initStateControlWithView();

        }
        catch (Exception ex)
        {
            Log.e("getInitialize", ex.getMessage());
        }
    }

    /**
     * 初始化NavigationBar
     **/
    public void initalizeNavigationBar ()
    {

        View rootView = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        if(rootView==null)
        {
            rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        }
        if (rootView instanceof ViewGroup)
        {
            ViewGroup viewGroup = (ViewGroup) rootView;
            mUINavigationBar = new UINavigationBar(this, true);
            mUINavigationBar.setId(R.id.uiNavigationBar);
            mUINavigationBar.UINavigationBarDelegat = this;
            viewGroup.addView(mUINavigationBar);

            if (mRightButtonText != null)
            {
                mUINavigationBar.setRightText(mRightButtonText);
            }

            if (mLeftButtonText != null)
            {
                mUINavigationBar.setLeftText(mRightButtonText);
            }

            if (mTitle != null)
            {
                mUINavigationBar.setTitle(mTitle);
            }
        }
    }

    /**
     * 是否完成初始化
     */
    public Boolean getInitialize ()
    {
        try
        {
            initViewWithActivity();
            return true;
        }
        catch (Exception ex)
        {
            Log.e("getInitialize", ex.getMessage());
        }
        return false;
    }

    @Override
    public View findViewById (int id)
    {
        View view = super.findViewById(id);
        if (!views.contains(view))
        {
            views.add(view);
        }
        return view;
    }

    /**
     * 初始化Activity
     */
    public void initViewWithActivity ()
    {
        try
        {
            InitializeController initializeController = new InitializeController(this);
            initializeController.initialize();
            initializeController = null;
        }
        catch (Exception ex)
        {
            Log.v("InitializeController", ex.getMessage());
        }
    }

    /**
     * 发布数据集到当前屏幕
     */
    public void release ()
    {
        ReleaseHelper releaseHelper;
        try
        {
            //调用数据发布前处理方法
            releaseing();

            if (mReceiveDataParams != null && mReceiveDataParams.size() > 0)
            {
                releaseHelper = new ReleaseHelper(mReceiveDataParams, this);
                releaseHelper.release(null);
            }

            //数据发布完成后处理方法
            released();
        }
        catch (Exception ex)
        {
            Log.e("", ex.getMessage());
        }
    }

    /**
     * 发布数据集到当前屏幕
     *
     * @param params 数据集
     */
    public void release (DataCollection params)
    {
        releaseParams = (DataCollection) params.clone();
        if (releaseParams != null && releaseParams.size() > 0)
        {
            ReleaseHelper releaseHelper;
            try
            {
                //数据发布前处理方法
                releaseing();

                releaseHelper = new ReleaseHelper(releaseParams, this);
                releaseHelper.release(null);

                //数据发布完成后处理方法
                released();

                releaseParams.clear();
            }
            catch (Exception ex)
            {
                Log.e("", ex.getMessage());
            }
        }
    }

    /**
     * 发布数据集到当前屏幕
     * @param tag 发布数据时用以区分标识
     * @param params 数据集
     */
    public void release (int tag,DataCollection params)
    {
        releaseParams = (DataCollection) params.clone();
        if (releaseParams != null && releaseParams.size() > 0)
        {
            ReleaseHelper releaseHelper;
            try
            {
                //数据发布前处理方法
                releaseing();

                releaseHelper = new ReleaseHelper(releaseParams, this);
                releaseHelper.release(null);

                //数据发布完成后处理方法
                released();

                releaseParams.clear();
            }
            catch (Exception ex)
            {
                Log.e("", ex.getMessage());
            }
        }
    }

    /**
     * 发布数据集到当前屏幕
     * @param tag 发布数据时用以区分标识
     * @param dataTable 数据集
     */
    public void release(int tag,DataTable dataTable)
    {
        if(dataTable==null)
        {
            return;
        }
        releaseParams = (DataCollection) dataTable.getFirst().clone();
        if (releaseParams != null && releaseParams.size() > 0)
        {
            ReleaseHelper releaseHelper;
            try
            {
                //数据发布前处理方法
                releaseing();

                releaseHelper = new ReleaseHelper(releaseParams, this);
                releaseHelper.release(null);

                //数据发布完成后处理方法
                released();

                releaseParams.clear();
            }
            catch (Exception ex)
            {
                Log.e("", ex.getMessage());
            }
        }
    }

    /**
     * 数据发布前处理方法
     */
    public void releaseing ()
    {

    }

    /**
     * 数据发布后处理方法
     */
    public void released ()
    {
    }

    /**
     * 收集当前Activity数据，并指定收集标签
     *
     * @param collectSign 收集标签
     * @return DataCollection数据集
     */
    public DataCollection collect (String collectSign)
    {
        CollectController collecter = new CollectController(this, collectSign);
        return collecter.collect();
    }

    /**
     * 设置权限状态
     */
    private void initFunctionProtectedWithView ()
    {
        try
        {
            FunctionProtected function = new FunctionProtected();
            function.setStateControl(this);
        }
        catch (Exception ex)
        {
            Log.e("", ex.getMessage());
        }
    }

    /**
     * 执行查询相关接口
     */
    public void initDataWithView ()
    {
        DataQueryController dataQueryController;
        try
        {
            if (this != null)
            {
                dataQueryController = new DataQueryController(this);
                dataQueryController.query();
            }
        }
        catch (Exception ex)
        {
            Log.e("query", ex.getMessage());
        }
    }

    /**
     * 校验Activity相关UIView是否合法
     *
     * @return 是否合法输入
     */
    public Boolean validator ()
    {
        ValidatorController validatorController = new ValidatorController(this);
        try
        {
            return validatorController.validator();
        }
        catch (Exception ex)
        {
            Log.e("validator", ex.getMessage());
        }
        return false;
    }

    /**
     * 设置权限代码
     *
     * @param funStr 权限代码，以逗号分隔
     */
    @SuppressLint("DefaultLocale")
    public static void setFunction (String funStr)
    {
        try
        {
            String str = "";
            FUNCTION_LIST.clear();
            while (funStr.indexOf(",") > 0)
            {
                str = funStr.substring(0, funStr.indexOf(","));
                funStr = funStr.substring(funStr.indexOf(",") + 1);
                if (!str.equals("") && !FUNCTION_LIST.contains(str))
                {
                    FUNCTION_LIST.add(str.trim().toLowerCase());
                }
            }
            if (funStr.length() > 0 && !FUNCTION_LIST.contains(funStr))
            {
                FUNCTION_LIST.add(funStr.trim().toLowerCase());
            }
        }
        catch (Exception ex)
        {
            Log.e("", ex.getMessage());
        }
    }

    /**
     * 设置UIView状态
     */
    public void initStateControlWithView ()
    {
        ControlStateProtector.createControlStateProtector().setStateControl(this);
    }

    /*
    * 关闭当前Activity
    * */
    public void finish ()
    {
        Intent intent = new Intent();
        intent.putExtra("isRefresh", false);
        this.setResult(RESULT_OK, intent);
        intent = null;
        ActivityManager.pop(this);
        super.finish();
    }

    /*
    * 关闭当前Activity
    * @param isRefresh 是否通知父页面刷新
    * */
    public void finish (Boolean isRefresh)
    {
        Intent intent = new Intent();
        intent.putExtra("isRefresh", isRefresh);
        this.setResult(RESULT_OK, intent);
        intent = null;
        ActivityManager.pop(this);
        super.finish();
    }

    /*
     * 关闭当前Activity
     * @param isParentColosed 是否通知父页面关闭
     * */
    public void finishWithParent (Boolean isParentColosed)
    {
        Intent intent = new Intent();
        intent.putExtra("isParentColosed", isParentColosed);
        this.setResult(RESULT_OK, intent);
        intent = null;
        ActivityManager.pop(this);
        super.finish();
    }

    /*
    * 关闭当前Activity并传递参数
    * @param intent 参数集合
    * */
    public void finishWithRefresh (DataCollection params)
    {
        Intent intent = new Intent();
        intent.putExtra("isRefresh", true);
        this.setResult(RESULT_OK, intent);
        DataCollection transferParams = null;
        if (mParentFragmentActivity != null)
        {
            transferParams = mParentFragmentActivity.getTransferDataParams();
        }
        if (mParentFragment != null)
        {
            transferParams = mParentFragment.getTransferDataParams();
        }
        if (mParentActivity != null)
        {
            transferParams = mParentActivity.getTransferDataParams();
        }
        if(params!=null)
        {
            if (transferParams != null)
            {
                if(params!=null)
                {
                    transferParams.addAll((DataCollection) params.clone());
                }
            }
            else
            {
                if(params!=null)
                {
                    transferParams = (DataCollection) params.clone();
                }
            }
        }
        if (mParentFragmentActivity != null)
        {
            mParentFragmentActivity.setTransferDataParams(transferParams);
        }
        if (mParentFragment != null)
        {
            mParentFragment.setTransferDataParams(transferParams);
        }
        if (mParentActivity != null)
        {
            mParentActivity.setTransferDataParams(transferParams);
        }
        ActivityManager.pop(this);
        super.finish();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if(onActivityResultListener!=null)
        {
            onActivityResultListener.onActivityResult(requestCode,resultCode,data);
        }
        switch (resultCode)
        {
            case 185324:

                if (data.getBooleanExtra("isParentColosed", false))
                {
                    onRefresh(getTransferDataParams());
                    finishWithParent(true);
                }
                if (data.getBooleanExtra("isRefresh", false))
                {
                    onRefresh(getTransferDataParams());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Activity关闭时，通知父级Activity调用此方法，用于页面刷新
     *
     * @param mTransferDataParams 参数集
     **/
    public void onRefresh (DataCollection mTransferDataParams)
    {

    }

    @Override
    public void onBackPressed ()
    {
        new Thread()
        {
            @Override
            public void run ()
            {
                if (Looper.myLooper() == null)
                {
                    Looper.prepare();
                }
                finish();
                Looper.loop();
            }
        }.start();

    }

    /**
     * 设备像素转换为标准像素
     *
     * @param context 上下文
     * @param dip     设备像素
     **/
    public static int dip2px (Context context, int dip)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 标准像素转换为设备像素
     *
     * @param context 上下文
     * @param px      标准像素
     **/
    public static int px2dip (Context context, int px)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public ArrayList<View> getViews ()
    {
        return views;
    }

    public void setViews (ArrayList<View> views)
    {
        this.views = views;
    }

    /**
     * 获取当前Activity接收父级屏幕传递的参数集
     *
     * @return DataCollection类型数据集合
     **/
    public DataCollection getReceiveDataParams ()
    {
        return mReceiveDataParams;
    }

    /**
     * 设置当前Activity接收父级屏幕传递的参数集
     *
     * @param receiveDataParams 接收参数集合
     **/
    public void setReceiveDataParams (DataCollection receiveDataParams)
    {
        this.mReceiveDataParams = receiveDataParams;
    }


    /**
     * 获取当前Activity向下级屏幕传递的参数集
     *
     * @return DataCollection类型数据集合
     **/
    public DataCollection getTransferDataParams ()
    {
        return mTransferDataParams;
    }

    /**
     * 设置当前Activity向下级屏幕传递的参数集
     *
     * @param transferDataParams 数据集合
     **/
    public void setTransferDataParams (DataCollection transferDataParams)
    {
        this.mTransferDataParams = transferDataParams;
    }

    /**
     * 获取当前屏幕Fragment
     *
     * @return Fragment
     */
    public Fragment getFragment ()
    {
        return mParentFragment;
    }

    /**
     * 设置当前Activity的Fragment
     *
     * @param fragment
     */
    public void setFragment (Fragment fragment)
    {
        this.mParentFragment = fragment;
    }

    /**
     * 获取当前Activity的父对象，有Activity、Fragment、FragmentActivity
     *
     * @return Activity、Fragment、FragmentActivity
     */
    public IChildView getParentView ()
    {
        if(mParentActivity!=null)
        {
            return mParentActivity;
        }
        if(mParentFragment!=null)
        {
            return mParentFragment;
        }
        if(mParentFragmentActivity!=null)
        {
            return mParentFragmentActivity;
        }
        return null;
    }

    /**
     * 获取当前Activity的父级Activity
     *
     * @return Activity
     */
    public Activity getParentActivity ()
    {
        return mParentActivity;
    }

    /**
     * 获取当前Activity的Fragment
     *
     * @return Fragment
     */
    public Fragment getParentFragment ()
    {
        return mParentFragment;
    }

    /**
     * 获取当前Activity的FragmentActivity
     *
     * @return FragmentActivity
     */
    public FragmentActivity getParentFragmentActivity ()
    {
        return mParentFragmentActivity;
    }

    /**
     * 设置当前Activity的父级Activity
     *
     * @param parentActivity 父级Activity
     */
    public void setParentActivity (Activity parentActivity)
    {
        this.mParentActivity = parentActivity;
    }

    /**
     * 获取权限代码列表
     *
     * @return 权限代码列表
     */
    public static List<String> getFunctionList ()
    {
        return FUNCTION_LIST;
    }

    /**
     * 设置权限代码列表
     *
     * @param functionList 权限代码列表
     **/
    public static void setFunctionList (List<String> functionList)
    {
        Activity.FUNCTION_LIST = functionList;
    }

    /**
     * 获取屏幕像素密度
     **/
    public static float getDensity ()
    {
        return DENSITY;
    }

    /**
     * 设置屏幕像素密度
     **/
    public static void setDensity (float DENSITY)
    {
        Activity.DENSITY = DENSITY;
    }

    /**
     * 获取导航栏
     *
     * @return UINavigationBar
     */
    public UINavigationBar getNavigationBar ()
    {
        return mUINavigationBar;
    }

    /**
     * 设置导航栏背景色
     *
     * @param color 背景色
     */
    public void setUINavigationBarBackgroundColor (int color)
    {
        if (mUINavigationBar != null)
        {
            mUINavigationBar.setBackgroundColor(color);
        }
        UINavigationBar.BACKGROUND_COLOR = color;
        StatusBarUtil.setTranslucent(this);
        //设置状态栏和标题栏颜色一致，实现沉浸式状态栏
        StatusBarUtil.setColorNoTranslucent(this, color);
    }

    /**
     * 设置导航栏背景色
     *
     * @param color         背景色
     * @param isTranslucent 是否半透明状态栏
     */
    public void setUINavigationBarBackgroundColor (int color, boolean isTranslucent)
    {
        if (mUINavigationBar != null)
        {
            mUINavigationBar.setBackgroundColor(color);
        }
        UINavigationBar.BACKGROUND_COLOR = color;
        if (isTranslucent)
        {
            StatusBarUtil.setTranslucent(this);
            //设置状态栏和标题栏颜色一致，实现沉浸式状态栏
            StatusBarUtil.setColorNoTranslucent(this, color);
        }
    }

    /**
     * 设置NavigationBar左侧按钮是否可见
     *
     * @param visible 设置可见性
     **/
    public void setUINavigationBarLeftButtonVisibility (int visible)
    {
        if (mUINavigationBar != null)
        {
            mUINavigationBar.setLeftButtonVisibility(visible);
        }

    }

    /**
     * 设置NavigationBar右侧按钮是否可见
     *
     * @param visible 设置可见性
     **/
    public void setUINavigationBarRightButtonVisibility (int visible)
    {
        if (this.getNavigationBar() != null)
        {
            this.getNavigationBar().setRightButtonVisibility(visible);
        }
    }

    /**
     * 设置NavigationBar是否可见
     *
     * @param visible 设置可见性
     **/
    public void setUINavigationBarVisibility (int visible)
    {
        if (this.getNavigationBar() != null)
        {
            this.getNavigationBar().setVisibility(visible);
        }
    }

    /**
     * 设置导航栏标题文本
     *
     * @param title 标题文本
     **/
    public void setTitleText (String title)
    {
        this.mTitle = title;
        if (this.getNavigationBar() != null)
        {
            this.getNavigationBar().setTitle(mTitle);
        }
    }

    @Override
    public void setTitle(int titleId)
    {
        setTitleText (getResources().getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title)
    {
        setTitleText (title.toString());
    }

    /**
     * 设置左侧按钮图片
     *
     * @param resouceid 图片资源
     **/
    public void setLeftButtonImage (int resouceid)
    {
        LEFT_IMAGE_RESOURCE_ID = resouceid;
        Activity.LEFT_IMAGE_RESOURCE_ID = resouceid;
        FragmentActivity.LEFT_IMAGE_RESOURCE_ID = resouceid;
        if (getNavigationBar() != null)
        {
            this.getNavigationBar().setLeftButtonImage(LEFT_IMAGE_RESOURCE_ID);
        }
    }

    /**
     * 设置导航栏右侧按钮文本
     *
     * @param buttonText 按钮文本
     **/
    public void setRightText (String buttonText)
    {
        this.mRightButtonText = buttonText;
        if (this.getNavigationBar() != null)
        {
            this.getNavigationBar().setRightText(buttonText);
        }
    }

    /**
     * 设置导航栏左侧按钮文本
     *
     * @param buttonText 按钮文本
     **/
    public void setLeftText (String buttonText)
    {
        this.mLeftButtonText = buttonText;
        if (this.getNavigationBar() != null)
        {
            this.getNavigationBar().setLeftText(buttonText);
        }
    }


    /**
     * 设置右侧按钮图片
     *
     * @param resouceid 图片资源
     **/
    public void setRightButtonImage (int resouceid)
    {
        if (getNavigationBar() != null)
        {
            this.getNavigationBar().setRightButtonImage(resouceid);
        }
    }

    /**
     * 左侧按钮单击事件
     **/
    public void onLeftClick ()
    {
        try
        {
            finish();
        }
        catch (Exception ex)
        {
            Log.e("Activity", ex.getMessage());
        }
    }

    /**
     * 右侧按钮单击事件
     **/
    public void onRightClick ()
    {

    }

    /**
     * 设置屏幕回调事件接口对象
     * */
    public void setOnActivityResultListener(PreferenceManager.OnActivityResultListener onActivityResultListener)
    {
        this.onActivityResultListener = onActivityResultListener;
    }

    /**
     * 设置状态栏颜色，实现沉浸式状态栏
     *
     * @param color 颜色id
     */
    public void setStatusBackgroundColor (int color)
    {
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setColorNoTranslucent(this, color);
    }

    /**
     * 跳转到目标屏幕
     *
     * @param targetViewController 目标屏幕
     * @param navigationbar        是否显示导航栏
     **/
    public void pushViewController (String targetViewController, Boolean navigationbar)
    {
        try
        {
            ActivityHelper open = new ActivityHelper();
            open.open(this, targetViewController, new DataCollection(), true, navigationbar);
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * 跳转到目标屏幕并传递参数
     *
     * @param targetViewController 目标屏幕
     * @param params               参数集合
     * @param navigationbar        是否显示导航栏
     **/
    public void pushViewController (String targetViewController, DataCollection params, Boolean navigationbar)
    {
        try
        {
            ActivityHelper open = new ActivityHelper();
            open.open(this, targetViewController, params, true, navigationbar);
        }
        catch (Exception ex)
        {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void onDestroy ()
    {
        super.onDestroy();
        for (View view : views)
        {
            if (view != null && view.hasOnClickListeners())
            {
                view.setOnClickListener(null);
            }
            view = null;
        }
        views.clear();
        views = null;

        if (mTransferParams != null)
        {
            mTransferParams.clear();
            mTransferParams = null;
        }

        if (releaseParams != null)
        {
            releaseParams.clear();
            releaseParams = null;
        }

        if (mReceiveDataParams != null)
        {
            mReceiveDataParams.clear();
            mReceiveDataParams = null;
        }

        if (mTransferDataParams != null)
        {
            mTransferDataParams.clear();
            mTransferDataParams = null;
        }

        if (mUINavigationBar != null)
        {
            mUINavigationBar.setRightViewClickListener(null);
            mUINavigationBar.setLeftViewClickListener(null);
        }
        mUINavigationBar = null;
        mParentFragmentActivity = null;
        mParentFragment = null;
        mParentActivity = null;
        mParentContext = null;

    }
}
