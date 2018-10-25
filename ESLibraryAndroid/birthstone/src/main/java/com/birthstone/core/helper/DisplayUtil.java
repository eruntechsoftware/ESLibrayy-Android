package com.birthstone.core.helper;

/**
 * @author MinG 
 **/
public class DisplayUtil
{

	/**
	 * @author MinG
	 * @param strValue dp
	 * @param scale
	 * @return px
	 * **/
	public static int getPixelValue(String strValue, float scale)
	{
		float value;
		if(strValue != null)
		{
			if(strValue.toLowerCase().contains("sp"))
			{
				value = Integer.valueOf(strValue.substring(0, strValue.length() - 2));
				return sp2px(value, scale);
			}
			else if(strValue.toLowerCase().contains("dp"))
			{
				value = Integer.valueOf(strValue.substring(0, strValue.length() - 2));
				return sp2px(value, scale);
			}
			return Integer.valueOf(strValue);
		}
		return 18;
	}

	/**
	 * @author MinG 
	 * @param pxValue 
	 * @param scale
	 * @return dip
	 */
	public static int px2dip(float pxValue, float scale)
	{
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * @author MinG 
	 * @param dipValue
	 * @param scale
	 * @return px
	 */
	public static int dip2px(float dipValue, float scale)
	{
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * @author MinG
	 * @param pxValue
	 * @param fontScale
	 * @return sp
	 */
	public static int px2sp(float pxValue, float fontScale)
	{
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * @author MinG
	 * @param spValue
	 * @param fontScale
	 * @return px
	 */
	public static int sp2px(float spValue, float fontScale)
	{
		return (int) (spValue * fontScale + 0.5f);
	}
}
