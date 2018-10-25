package com.birthstone.core.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间辅助类
 * **/
public class DateTimeHelper
{
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat ft = null;
	private static Date date = null;
	private static Calendar calendar = Calendar.getInstance();

	static int YEAR;
	static int MONTH;
	static int DAY;
	static int HOUR;
	static int MINUTE;

	/**
	 * 获取当前时间
	 */
	public static String getNow()
	{
		calendar.setTime(new Date());
		date = calendar.getTime();
		ft = new SimpleDateFormat(DATE_TIME_FORMAT);
		String dateTime = ft.format(date);
		return dateTime;
	}

	/**
	 * 获取当前日期
	 */
	public static String getToday()
	{
		calendar.setTime(new Date());
		date = calendar.getTime();
		ft = new SimpleDateFormat(DATE_FORMAT);
		String dateTime = ft.format(date);
		return dateTime;
	}

	/***
	 * 比较第一个时间是否大于第二个时间串
	 * @param date1 时间1
	 * @param date2 时间2
	 */
	public static boolean compar(String date1, String date2)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try
		{
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		if(d1.before(d2)) 
		{ 
			return true; 
		}
		return false;
	}

	/***
	 * 比较第一个时间是否大于第二个时间串
	 * @param date1 时间1
	 * @param date2 时间2
	 * @param fromat 时间格式
	 */
	public static boolean compar(String date1, String date2, String fromat)
	{
		SimpleDateFormat format = new SimpleDateFormat(fromat);
		Date d1 = null;
		Date d2 = null;
		try
		{
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		if(d1.before(d2))
		{
			return true;
		}
		return false;
	}

	/**
	 *获取指定时间格式的字符串
	 * @param value
	 * @param format
	 * @return
	 */
	public static String getDateString(String value, String format)
	{
		Date date = new Date(convertTimeToLong(value.toString()));
		SimpleDateFormat ft = new SimpleDateFormat(format);
		return ft.format(date);
	}
	
	/***
	 * 获取时间串，去掉-符号
	 * @param value 时间值
	 */
	public static String getLocalDateString(Object value)
	{
		String[] date = value.toString().split(" ")[0].split("-");
		return date[0]+""+date[1]+""+date[2]+"";
	}
	
	/**
	 * 获取两个日期时间相差的天数
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 */
	public static long getDiffDays(String beginDate, String endDate)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
		    Date d1 = df.parse(beginDate);
		    Date d2 = df.parse(endDate);
		    long diff = d1.getTime() - d2.getTime();
		    long days = diff / (1000 * 60 * 60 * 24);
		    return days;
		}
		catch (Exception e)
		{
		}
		return -1;
	}

	/**
	 * 获取两个日期时间相差的秒数
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 */
	public static long getDiffSeconds(String beginDate, String endDate)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date d1 = df.parse(beginDate);
			Date d2 = df.parse(endDate);
			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60);
			return days;
		}
		catch (Exception e)
		{
		}
		return -1;
	}
	
	/**
	 * 获取对应日期的周数
	 * @param date 日期
	 * @param desc 周描述，如周、星期
	 */
	public static String getDateOfWeek(String date, String desc)
	{
		String weekStr="";
		try
		{
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal.setTime(df.parse(date));
			int week = cal.get(Calendar.DAY_OF_WEEK);  
			switch(week)
			{
			case 1:
				weekStr="";
				break;
			case 2:
				weekStr="һ";
				break;
			case 3:
				weekStr="";
				break;
			case 4:
				weekStr="";
				break;
			case 5:
				weekStr="";
				break;
			case 6:
				weekStr="";
				break;
			case 7:
				weekStr="";
				break;
			}
		}
		catch(Exception ex)
		{
			
		}
		return desc+weekStr;
	}

	public static String getDateFormat()
	{
		return DATE_FORMAT;
	}

	public static String getDateTimeFormat()
	{
		return DATE_TIME_FORMAT;
	}
	
	/**
	 * ǰ
	 * @return
	 */
	public static int getYear()
	{
		YEAR = calendar.get(Calendar.YEAR);
		return YEAR;
	}
	
	/**
	 *
	 * @return
	 */
	public static int getMonth()
	{
		MONTH = calendar.get(Calendar.MONTH);
		return MONTH;
	}
	
	/**
	 *
	 * @return
	 */
	public static int getDay()
	{
		DAY = calendar.get(Calendar.DAY_OF_MONTH);
		return DAY;
	}
	
	/**
	 * @return
	 */
	public static int getHour()
	{
		HOUR = calendar.get(Calendar.HOUR_OF_DAY);
		return HOUR;
	}
	
	/**
	 * @return
	 */
	public static int getMinute()
	{
		MINUTE = calendar.get(Calendar.MINUTE);
		return MINUTE;
	}

	/**
	 * 给一个时间戳字符串将其转化成 "xx年xx月xx日xx时xx分xx秒" 的格式
	 *
	 * @param timeStamp 时间戳字符串
	 */
	public static String convertToDateString(Long timeStamp)
	{

		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = sdr.format(new Date(timeStamp));

		return timeStr;
	}

	/**
	 * 转换时间日期格式字串为long型
	 *
	 * @param datetime
	 */
	public static Long convertTimeToLong(String datetime)
	{
		Date date = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(DateTimeHelper.DATE_FORMAT);
			date = sdf.parse(datetime);
			return date.getTime();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 在当前日期上按天数累积并返回日期
	 *
	 * @param datetime 日期
	 * @param i 天
	 */
	public static String addDay(String datetime,int i)
	{
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeHelper.DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		try
		{
			date = sdf.parse(datetime);
			cal.setTime(date);//设置起时间
			cal.add(Calendar.DATE,i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 在当前日期上按天数累积并返回日期
	 *
	 * @param datetime 日期
	 * @param i 月
	 */
	public static String addMonth(String datetime,int i)
	{
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeHelper.DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		try
		{
			date = sdf.parse(datetime);
			cal.setTime(date);//设置起时间
			cal.add(Calendar.MONTH,i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 在当前日期上按天数累积并返回日期
	 *
	 * @param datetime 日期
	 * @param i 年
	 */
	public static String addYear(String datetime,int i)
	{
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeHelper.DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		try
		{
			date = sdf.parse(datetime);
			cal.setTime(date);//设置起时间
			cal.add(Calendar.YEAR,i);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

}
