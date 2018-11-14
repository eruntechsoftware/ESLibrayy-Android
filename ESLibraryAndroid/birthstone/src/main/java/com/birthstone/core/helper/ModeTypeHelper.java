package com.birthstone.core.helper;


public class ModeTypeHelper
{
	public static ModeType valueOf(int value)
	{
		ModeType modeType = null;
		switch (value){
			case 0:
				modeType=ModeType.AND;
				break;
			case 1:
				modeType=ModeType.OR;
				break;
		}
		return modeType;
	}
}
