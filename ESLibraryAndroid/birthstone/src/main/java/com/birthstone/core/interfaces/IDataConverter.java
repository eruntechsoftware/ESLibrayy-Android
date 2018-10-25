package com.birthstone.core.interfaces;


import com.birthstone.core.parse.DataCollection;

/**
 * ת
 * **/
public interface IDataConverter {
	
	/**
	 * תΪ
	 * DataCollection ݼ
	 * **/
	Object toTarget(DataCollection dataCollection);
	
	/**
	 * תΪƽ̨Ҫ϶
	 * **/
	DataCollection toDataCollection(Object obj);
}
