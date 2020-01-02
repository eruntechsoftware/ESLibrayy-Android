package com.birthstone.base.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.birthstone.core.Sqlite.SQLiteDatabase;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.lang.ref.WeakReference;

/***
 * 线程任务处理类
 * 作者：杜明悦
 */
public abstract class AsyncTaskSQL
{
    private Context mContext;
    private String mSql;
    private DataCollection mParams;
    private SQLiteDatabase mDb;
    private DataTable mTable;
    private UpdateUIHandler updateUIHandler;

    public AsyncTaskSQL(Context context, String sql, DataCollection params)
    {
        this.mContext = context.getApplicationContext();
        this.mSql = sql;
        this.mParams = params;
        this.updateUIHandler = new UpdateUIHandler(this, context.getApplicationContext());
    }

    /***
     * 开始执行任务
     *
     * @return AsyncTaskServer对象
     */
    public void execute ()
    {
        try
        {
            new TaskThread().start();
        }
        catch (Exception ex)
        {
            Log.e("异步任务", ex.getMessage());
        }
    }

    /***
     * 后台执行任务
     *
     * @return 返回执行是否成功
     */
    protected boolean doInBackground ()
    {
        try
        {
            mDb = new SQLiteDatabase(mContext);
            mTable = mDb.executeTable(mSql, mParams);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }


    /**
     * 执行成功后的方法
     */
    public abstract void onSuccess(DataTable rs) throws Exception;

    /**
     * 执行失败后的处理方法
     */
    public abstract void onFail () throws Exception;


    /***
     * 线程调度任务
     * 作者：杜明悦
     */
    class TaskThread extends Thread
    {
        public void run ()
        {
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            if (doInBackground())
            {
                Message msg = updateUIHandler.obtainMessage();
                msg.obj = mTable;
                msg.what = 1;
                updateUIHandler.sendMessage(msg);
            }
            else
            {
                Message msg = updateUIHandler.obtainMessage();
                msg.obj = null;
                msg.what = 0;
                updateUIHandler.sendMessage(msg);
            }
            updateUIHandler = null;
            Looper.loop();
        }
    }

    /**
     * 静态内部类不会持有外部类的引用
     * 处理UI事务
     **/
    private static class UpdateUIHandler extends Handler
    {

        private final WeakReference<AsyncTaskSQL> weakAsyncTaskServer;
        private Context context;

        public UpdateUIHandler(AsyncTaskSQL asyncTaskServer, Context context)
        {
            weakAsyncTaskServer = new WeakReference<AsyncTaskSQL>(asyncTaskServer);
            this.context = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg)
        {
            AsyncTaskSQL asyncTaskServer = weakAsyncTaskServer.get();
            switch (msg.what)
            {
                case 1:
                    try
                    {
                        asyncTaskServer.onSuccess((DataTable) msg.obj);
                    }
                    catch (Exception e)
                    {
                    }
                    break;
                case 0:
                    try
                    {
                        asyncTaskServer.onFail();
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    ;
}
