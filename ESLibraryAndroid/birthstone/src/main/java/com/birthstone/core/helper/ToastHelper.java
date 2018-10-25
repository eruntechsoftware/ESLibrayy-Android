package com.birthstone.core.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast消息辅助类
 */
public class ToastHelper {
    /**
     * 修改时间: 2016年4月13日
     * 功能:消息提醒
     * @param context 上下文
     * @param msg 消息文本
     */
    public static void toastShow(Context context, String msg){
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 修改时间: 2016年4月13日
     * 功能:消息提醒
     * @param context 上下文
     * @param rsid 消息文本id
     */
    public static void toastShow(Context context, int rsid){
        Toast.makeText(context.getApplicationContext(), rsid, Toast.LENGTH_SHORT).show();
    }
}
