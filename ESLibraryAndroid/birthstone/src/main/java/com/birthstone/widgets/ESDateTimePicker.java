package com.birthstone.widgets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间：2016/8/8
 * <p/>
 * 作者：张景瑞
 * <p/>
 * 功能：日期时间选择器
 */
public class ESDateTimePicker implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener
{

    /**
     * 声明控件
     **/
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog alertDialog;
    private LinearLayout dateTimeLayout;
    /**
     * 声明私有变量
     **/
    private String dateTimeFormat = "yyyy-MM-dd HH:mm";
    private String dateTime;
    private String initDateTime;
    private Activity context;

    public ESDateTimePicker (Activity context, String initDateTime)
    {
        this.context = context;
        this.initDateTime = initDateTime;

        dateTimeLayout = (LinearLayout) context.getLayoutInflater()
                .inflate(R.layout.es_date_time_picker, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
    }

    /**
     * 初始化显示时间
     *
     * @param datePicker
     * @param timePicker
     */
    public void init (DatePicker datePicker, TimePicker timePicker)
    {
        Calendar c = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime)))
        {
            c = this.getCalendarByInintData(initDateTime);
        }
        else
        {
            initDateTime = c.get(Calendar.YEAR) + "年" + c.get(Calendar.MONTH) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + c
                    .get(Calendar.HOUR_OF_DAY) + "." + c.get(Calendar.MINUTE);
        }
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
    }

    public AlertDialog dateTimePickDialog (final TextView inputDate)
    {
        init(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);

        alertDialog = new AlertDialog.Builder(context).setTitle(initDateTime)
                .setView(dateTimeLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        inputDate.setText(dateTime);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
        onDateChanged(null, 0, 0, 0);
        return alertDialog;
    }


    @Override
    public void onTimeChanged (TimePicker view, int hourOfDay, int minute)
    {
        onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onDateChanged (DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        //获得日历实例
        Calendar c = Calendar.getInstance();
        c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker
                .getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        dateTime = sdf.format(c.getTime());
        alertDialog.setTitle(dateTime);
    }

    /**
     * 设置时间格式
     * @param dateTimeFormat 时间格式串
     * **/
    public void setDateTimeFormat(String dateTimeFormat)
    {
        this.dateTimeFormat = dateTimeFormat;
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    public static Calendar getCalendarByInintData (String initDateTime)
    {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期
        String time = spliteString(initDateTime, "日", "index", "back"); // 时间

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = spliteString(time, ":", "index", "front"); // 时
        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();
        int currentHour = Integer.valueOf(hourStr.trim()).intValue();
        int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString (String srcStr, String pattern, String indexOrLast, String frontOrBack)
    {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index"))
        {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        }
        else
        {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front"))
        {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        }
        else
        {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    /**
     * 获取日期控件实例
     **/
    public DatePicker getDatePicker ()
    {
        return datePicker;
    }

    /**
     * 获取时间控件实例
     **/
    public TimePicker getTimePicker ()
    {
        return timePicker;
    }
}
