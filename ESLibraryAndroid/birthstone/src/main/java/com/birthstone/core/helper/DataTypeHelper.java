package com.birthstone.core.helper;


public class DataTypeHelper {
	public static DataType valueOf(int value)
	{
		DataType dataType = null;
		switch (value){
			case 0:
				dataType=DataType.String;
				break;
			case 1:
				dataType=DataType.Integer;
				break;
			case 2:
				dataType=DataType.Numeric;
				break;
			case 3:
				dataType=DataType.Date;
				break;
			case 4:
				dataType=DataType.DateTime;
				break;
			case 5:
				dataType=DataType.EMail;
				break;
			case 6:
				dataType=DataType.URL;
				break;
			case 7:
				dataType=DataType.IDCard;
				break;
			case 8:
				dataType=DataType.Phone;
				break;
			case 9:
				dataType=DataType.Phone;
				break;
		}
		return dataType;	
	}
}
