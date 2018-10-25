package com.birthstone.core.helper;

import java.util.Vector;

public class Basic
{

	public Basic( )
	{
	}

	public static String encode(String b)
	{
		if(b == null) { return b; }
		String s = "";
		String temp = "";
		String flag = "_";

		int asciiNum = 0;
		int n = 0;
		if((b == null) || (b.equals(""))) return "";

		char[] by = b.toCharArray();

		while(n < by.length)
		{

			asciiNum = by[n];
			temp = Long.toHexString(asciiNum);

			if(asciiNum > 255 || (asciiNum < 0))
			{

				temp = fillLeftWithZero(temp, 4);
				if(flag.equals("_"))
				{
					flag = "#";
					s += "[#" + temp;
				}
				else if(flag.equals("~"))
				{
					flag = "#";
					s += "#" + temp;
				}
				else if(flag.equals("#"))
				{
					s += temp;
				}
			}
			else if((asciiNum < 48) || (asciiNum > 57 && asciiNum < 65) || (asciiNum > 90 && asciiNum < 97) || (asciiNum > 122))
			{
				temp = fillLeftWithZero(temp, 2);

				if(flag.equals("_"))
				{
					flag = "~";
					s += "[~" + temp;
				}
				else if(flag.equals("~"))
				{
					s += temp;
				}
				else if(flag.equals("#"))
				{
					flag = "~";
					s += "~" + temp;
				}

			}
			else
			{
				if((flag.equals("#")) || (flag.equals("~")))
				{
					flag = "_";
					s += "]" + (char) asciiNum;
				}
				else
				{
					s += (char) asciiNum;
				}

			}

			n++;
		}

		return s;
	}

	public static String decode(String Str)
	{
		try
		{
			String temp = "";
			String OutStr = "";

			String flag = "_";

			int asciiNum = 0;
			if((Str == null) || (Str.equals(""))) return "";

			for(int i = 0; i < Str.length(); i++)
			{
				temp = Str.substring(i, i + 1);

				if(temp.equals("["))
				{
					i++;
					temp = Str.substring(i, i + 1);

				}
				if(temp.equals("]"))
				{
					flag = "_";
					continue;

				}
				if(temp.equals("~"))
				{
					flag = "~";

					i++;

				}
				if(temp.equals("#"))
				{
					flag = "#";

					i++;

				}

				if(flag.equals("~"))
				{
					temp = Str.substring(i, i + 2);
					asciiNum = Integer.parseInt(temp, 16);
					OutStr += String.valueOf((char) asciiNum);
					i++;
				}

				if(flag.equals("#"))
				{
					temp = Str.substring(i, i + 4);
					if(temp.toUpperCase().equals("FFFF"))
					{
						i += 4;
						temp = Str.substring(i, i + 4);
					}
					asciiNum = Integer.parseInt(temp, 16);
					OutStr += String.valueOf((char) asciiNum);
					i += 3;

				}
				if(flag.equals("_"))
				{
					OutStr += temp;

				}

			}

			return OutStr;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}

	public static String fillLeftWithZero(String inStr, int len)
	{
		int count = inStr.length();
		String outStr = inStr;
		while(count < len)
		{
			outStr = "0" + outStr;
			count = outStr.length();
		}
		return outStr;
	}

	public static String[] splite(String spliteStr, String subStr)
	{
		int count = subStr.length();
		int i = 100;
		String[] returnStr; // =new String[100];//=new
		// String[]{"dd","ewew","sdfsd"} ; //ַָ

		Vector<String> StrVec = new Vector<String>();

		while(i > 0)
		{
			i = spliteStr.indexOf(subStr);
			if(i < 0) break;
			StrVec.add(spliteStr.substring(0, i));
			spliteStr = spliteStr.substring(i + count);
		}

		StrVec.add(spliteStr);
		returnStr = new String[StrVec.size()];
		for(int j = 0; j < StrVec.size(); j++) {
			returnStr[j] = StrVec.get(j).toString();
		}
		return returnStr;
	}

	public static String ABCToDBC(String changeStr)
	{
		char[] by = changeStr.toCharArray();
		int charNum = 0;

		String retStr = "";

		for(int i = 0; i < by.length; i++)
		{
			charNum = by[i];

			if(charNum > 65280 && charNum < 65375)
			{
				charNum = charNum - 65248;
			}
			if(charNum == 183) charNum = 46;

			retStr += String.valueOf((char) charNum);
		}

		return retStr;
	}

	// htmlʽת
	public static String htmEncode(String s)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int j = s.length();
		for(int i = 0; i < j; i++)
		{
			char c = s.charAt(i);
			switch(c)
			{
			case 60: // '<'
				stringbuffer.append("&lt;");
				break;

			case 62: // '>'
				stringbuffer.append("&gt;");
				break;

			case 38: // '&'
				stringbuffer.append("&amp;");
				break;

			case 34: // '"'
				stringbuffer.append("&quot;");
				break;

			case 169:
				stringbuffer.append("&copy;");
				break;

			case 174:
				stringbuffer.append("&reg;");
				break;

			case 165:
				stringbuffer.append("&yen;");
				break;

			case 8364:
				stringbuffer.append("&euro;");
				break;

			case 8482:
				stringbuffer.append("&#153;");
				break;

			case 13: // '\r'
				if(i < j - 1 && s.charAt(i + 1) == '\n')
				{
					stringbuffer.append("<br>");
					i++;
				}
				break;

			case 32: // ' '
				if(i < j - 1 && s.charAt(i + 1) == ' ')
				{
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
				// fall through

			default:
				stringbuffer.append(c);
				break;
			}
		}

		return stringbuffer.toString();
	}

	public static String[] commaToArray(String s, String s1)
	{
		if(s == null) return new String[0];
		if(s1 == null) s1 = ",";
		String as[] = s.split(s1, -2);
		return as;
	}

	public static String[] commaToArray(String s)
	{
		return commaToArray(s, ",");
	}

	public static Vector<String> commaToVector(String s, String s1)
	{
		if(s == null) return new Vector<String>();
		if(s1 == null) s1 = ",";
		String as[] = s.split(s1);
		Vector<String> vector = new Vector<String>();
		for(int i = 0; i < as.length; i++)
			vector.add(as[i]);

		return vector;
	}

	public static Vector<?> commaToVector(String s)
	{
		return commaToVector(s, ",");
	}

	public static String replace(String s, String s1, String s2)
	{
		if(s == null) return null;
		if(s1 == null || s2 == null) return s;
		StringBuffer stringbuffer = new StringBuffer();
		int i = 0;
		int j = s.indexOf(s1);
		int k = s1.length();
		for(; j >= 0; j = s.indexOf(s1, i))
		{
			stringbuffer.append(s.substring(i, j));
			stringbuffer.append(s2);
			i = j + k;
		}

		stringbuffer.append(s.substring(i));
		return stringbuffer.toString();
	}

	public static String delete(String s, String s1)
	{
		return replace(s, s1, "");
	}

	public static boolean IsTrue(String s)
	{
		if(s == null || s.trim().equals("")) return false;

		s = s.trim().toLowerCase();
		return s.equals("1") | s.equals("yes") | s.equals("true") | s.equals("on");
	}

	public static int getInt(String s) throws Exception
	{
		if(s.trim().equals("")) return 0;
		try
		{
			return Integer.parseInt(s);
		}
		catch(Exception exception)
		{
			throw exception;
		}
	}

	public static double getDouble(String s) throws Exception
	{
		if(s.trim().equals("")) return 0.0D;
		try
		{
			return Double.parseDouble(s);
		}
		catch(Exception exception)
		{
			throw exception;
		}
	}

	/**
	 * ָʽת
	 */
	public static String convertCharset(String content, String from, String to)
	{
		try
		{
			return new String(content.getBytes(from), to);
		}
		catch(Exception e)
		{
			throw new RuntimeException("תʧ", e.getCause());
		}
	}

	/***********************************************************
	 * 
	 *  תΪֵ
	 * 
	 * ʱ 䣺2009-08-12
	 * 
	 *  ߣwzd
	 * 
	 * ޸ıע{޸ĵĹܣ޸˺޸ʱ}
	 * 
	 * @param value ַ
	 * @param defaultvalue Ĭֵ
	 * @return ֵ
	 */
	public static double toDouble(String value, double defaultvalue)
	{
		if(value == null || value.equals("")) { return defaultvalue; }
		return Double.parseDouble(value);
	}

	/***********************************************************
	 * 
	 *  תΪ
	 * 
	 * ʱ 䣺2009-08-12
	 * 
	 *  ߣwzd
	 * 
	 * ޸ıע{޸ĵĹܣ޸˺޸ʱ}
	 * 
	 * @param value ַ
	 * @param defaultvalue Ĭֵ
	 * @return ֵ
	 */
	public static int toInt(String value, int defaultvalue)
	{
		if(value == null || value.equals("")) { return defaultvalue; }
		return Integer.parseInt(value);
	}

}
