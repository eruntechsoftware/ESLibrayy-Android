package com.birthstone.core.interfaces;

import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataTable;


public interface IDataIndexer
{
	DataTable getAllData();
    Data getData(String name);
    void loadDataSource(Object dataSource);
}

