package com.birthstone.core.parse;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class DataTable extends LinkedList<DataCollection> implements Serializable
{
	private static final long serialVersionUID = -7116458462588700584L;
	private int index = 0;
	private int lastRet = -1;
	private List<String> columns = new LinkedList<>();

	public DataTable( )
	{

	}

	@Override
	public boolean add(DataCollection params)
	{
		return super.add(params);
	}

	public boolean hasNext()
	{
		return index != size();
	}

	public DataCollection next()
	{
		DataCollection datas = this.get(index);
		lastRet = index++;
		return datas;
	}

	public void setPosition(int position)
	{
		if(position < size() && position > -1)
		{
			index = position;
			lastRet = index;
		}
	}

	@Override
	public void addFirst(DataCollection data)
	{
		super.addFirst(data);
	}

	/**
	 * 获取某一列数据集合
	 * @param columnName 列名
	 * **/
	public DataCollection getColumn(String columnName)
	{
		DataCollection params = new DataCollection();
		try
		{
			for(DataCollection row:this)
			{
				params.add(row.get(columnName));
			}
		}
		catch(Exception ex)
		{

		}
		return params;
	}

	public DataTable getIndexData(int startIndex, int rows)
	{
		DataTable DataTable = new DataTable();
		try
		{
			int size = this.size();
			int len = startIndex + rows;
			if(len >= 0)
			{
				for(int i = startIndex; i < len; i++)
				{
					if(i < size)
					{
						DataTable.add(this.get(i));
					}
				}
			}
		}
		catch(Exception ex)
		{
			Log.e("getAllData", ex.getMessage());
		}
		return DataTable;
	}
}
