package com.birthstone.core.interfaces;


import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

/**
 * ⲿʴ
 * */
public interface IProxy {

	/**
	 *
	 * dataCollection
	 * **/
	DataTable request(DataCollection dataCollection);
}
