package com.birthstone.core.helper;

import java.util.Random;
import android.graphics.Color;
 
/**
 *
 * **/
public class ColorHelper {

	/**
	 * 鳤ȷColor
	 * **/
	public static int[] getColors(int count) throws Exception
	{
		//
		int [] colors = new int[count];
		try
		{
			Random random = new Random();
			int alpha=250;
			for(int i=0; i<count; i++)
			{
				int color=0;
//				while(true)
//				{
					int R1 = random.nextInt(255);
					int R2 = random.nextInt(255);
					int G1 = random.nextInt(255);
					int G2 = random.nextInt(255);
					int B1 = random.nextInt(255);
					int B2 = random.nextInt(255);
					int R = ( R1 * (100 - alpha) + R2 * alpha ) / 100;
					int G = ( G1 * (100 - alpha) + G2 * alpha ) / 100;
					int B = ( B1 * (100 - alpha) + B2 * alpha ) / 100;
					color = Color.argb(alpha, R, G, B);
//					if(difference(colors, color))
//					{
//						break;
//					}
//				}
				colors[i] = color;
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return colors;
	}
	
	private static Boolean difference(int [] colors, int color)
	{
		int size = colors.length;
		for(int i=0; i<size; i++)
		{
			if(colors[i]-color<250000)
			{
				return false;
			}
		}
		return true;
	}
}
