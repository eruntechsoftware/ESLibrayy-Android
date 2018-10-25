package com.birthstone.core.helper;

import java.io.UnsupportedEncodingException;

/**
 * תַı
 */
public class ChangeCharSet
{
	/** 7λASCIIַҲISO646-USUnicodeַ */
	public static final String US_ASCII = "US-ASCII";

	/** ISO ĸ No.1Ҳ ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 λ UCS תʽ */
	public static final String UTF_8 = "UTF-8";

	/** 16 λ UCS תʽBig Endian͵ַŸλֽڣֽ˳ */
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 λ UCS תʽLittle-endianߵַŵλֽڣֽ˳ */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 λ UCS תʽֽ˳ɿѡֽ˳ʶ */
	public static final String UTF_16 = "UTF-16";

	/** ĳַ */
	public static final String GBK = "GBK";

	/** ĳַ */
	public static final String GB2312 = "GB2312";

	public static final String ANSI = "ANSI";

	public static final String UNICODE = "UNICODE";

	/**
	 * ַתUS-ASCII
	 */
	public String toASCII(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, US_ASCII);
	}

	/**
	 * ַתISO-8859-1
	 */
	public String toISO_8859_1(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, ISO_8859_1);
	}

	/**
	 * ַתUTF-8
	 */
	public String toUTF_8(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, UTF_8);
	}

	/**
	 * ַתUTF-16BE
	 */
	public String toUTF_16BE(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, UTF_16BE);
	}

	/**
	 * ַתUTF-16LE
	 */
	public String toUTF_16LE(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, UTF_16LE);
	}

	/**
	 * ַתUTF-16
	 */
	public String toUTF_16(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, UTF_16);
	}

	/**
	 * ַתGBK
	 */
	public String toGBK(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, GBK);
	}

	/**
	 * ַתGB2312
	 */
	public String toGB2312(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, GB2312);
	}

	/**
	 * ַתANSI
	 */
	public String toANSI(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, ANSI);
	}

	/**
	 * ַתUNICODE
	 */
	public String toUNICODE(String str) throws UnsupportedEncodingException
	{
		return this.changeCharset(str, UNICODE);
	}

	/**
	 * ַתʵַ
	 * 
	 * @param str תַ
	 * @param newCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException
	{
		if(str != null)
		{
			byte[] bs = str.getBytes();
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * ַתʵַ
	 * 
	 * @param str תַ
	 * @param oldCharset ԭ
	 * @param newCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String changeCharset(String str, String oldCharset, String newCharset) throws UnsupportedEncodingException
	{
		if(str != null)
		{
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}
}
