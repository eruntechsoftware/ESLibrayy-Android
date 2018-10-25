package com.birthstone.core.helper;

import java.io.ByteArrayOutputStream;

import android.util.Log;

public class Base64
{
	private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

	private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1,
			-1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

	// / 锟斤拷锟斤拷锟斤拷菁锟斤拷锟
	public static String enCode(String strIn)
	{
		if(strIn == null) return "";
		int intLen = strIn.length();
		String strOut = "";

		char charTemp;
		String strTemp;
		String flag = "_";

		for(int i = 0; i < intLen; i++)
		{
			charTemp = strIn.charAt(i);

			if(charTemp > 255)
			{
				strTemp = String.valueOf(charTemp);

				for(int j = strTemp.length(); j < 4; j++)
					strTemp = "0" + strTemp;

				if(flag.equals("_"))
				{
					flag = "#";
					strOut += "[#" + strTemp;
				}
				else if(flag.equals("~"))
				{
					flag = "#";
					strOut += "#" + strTemp;
				}
				else if(flag.equals("#"))
				{
					strOut += strTemp;
				}
			}
			else
			{
				if(charTemp < 48 || (charTemp > 57 && charTemp < 65) || (charTemp > 90 && charTemp < 97) || charTemp > 122)
				{
					strTemp = String.valueOf(charTemp);
					for(int j = strTemp.length(); j < 2; j++)
						strTemp = "0" + strTemp;

					if(flag.equals("_"))
					{
						flag = "~";
						strOut += "[~" + strTemp;

					}
					else if(flag.equals("~"))
					{
						strOut += strTemp;

					}
					else if(flag.equals("#"))
					{
						flag = "~";
						strOut += "~" + strTemp;
					}
				}
				else
				{
					if((flag.equals("#")) || (flag.equals("~")))
					{
						flag = "_";
						strOut += "]" + charTemp;
					}
					else
					{
						strOut += charTemp;
					}
				}
			}
		}
		return(strOut);
	}

	// / 锟斤拷锟斤拷锟斤拷萁锟斤拷锟
	public static String deCode(String strIn)
	{
		int intLen = strIn.length();
		String strOut = "";

		String strTemp;
		int asciiNum = 0;
		String flag = "_";
		try
		{
			for(int i = 0; i < intLen; i++)
			{
				strTemp = strIn.substring(i, i + 1);

				if(strTemp.equals("["))
				{
					i++;
					strTemp = strIn.substring(i, i + 1);
				}

				if(strTemp.equals("]"))
				{
					flag = "_";
					continue;
				}

				if(strTemp.equals("~"))
				{
					flag = "~";
					i++;
				}

				if(strTemp.equals("#"))
				{
					flag = "#";
					i++;
				}

				if(flag.equals("~"))
				{
					strTemp = strIn.substring(i, i + 2);

					asciiNum = Integer.parseInt(strTemp, 16);
					strTemp = String.valueOf((char) asciiNum);
					strOut = strOut + strTemp;

					i++;
				}

				if(flag.equals("#"))
				{
					strTemp = strIn.substring(i, i + 4);
					if(strTemp.toUpperCase().equals("FFFF"))
					{
						i += 4;
						strTemp = strIn.substring(i, i + 4);
					}
					asciiNum = Integer.parseInt(strTemp, 16);
					strTemp = String.valueOf((char) asciiNum);
					strOut = strOut + strTemp;

					i += 3;
				}
				if(flag.equals("_"))
				{
					strOut = strOut + strTemp;
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("deCode", ex.getMessage());
		}
		return(strOut);
	}

	private Base64( )
	{
	}

	public static String encode(byte[] data)
	{
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;

		while(i < len)
		{
			b1 = data[i++] & 0xff;
			if(i == len)
			{
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if(i == len)
			{
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str)
	{
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while(i < len)
		{

			/* b1 */
			do
			{
				b1 = base64DecodeChars[data[i++]];
			}
			while(i < len && b1 == -1);
			if(b1 == -1)
			{
				break;
			}

			/* b2 */
			do
			{
				b2 = base64DecodeChars[data[i++]];
			}
			while(i < len && b2 == -1);
			if(b2 == -1)
			{
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

			/* b3 */
			do
			{
				b3 = data[i++];
				if(b3 == 61) { return buf.toByteArray(); }
				b3 = base64DecodeChars[b3];
			}
			while(i < len && b3 == -1);
			if(b3 == -1)
			{
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			/* b4 */
			do
			{
				b4 = data[i++];
				if(b4 == 61) { return buf.toByteArray(); }
				b4 = base64DecodeChars[b4];
			}
			while(i < len && b4 == -1);
			if(b4 == -1)
			{
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		return buf.toByteArray();
	}

}