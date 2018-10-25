//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.birthstone.core.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.birthstone.core.interfaces.ICollector;
import com.birthstone.core.interfaces.IReleaser;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.LinkedList;

public class SQLiteDatabase
{
	private Database db;
	private DataCollection dataCollection;
	private DataCollection datas;
	private ICollector collector;
	private SQLiteOpenbase dbHelper;
	private DataTable dataSource;
	protected String sql;
	protected LinkedList<ICollector> collectors = new LinkedList();
	protected LinkedList<IReleaser> releasers = new LinkedList();

	public SQLiteDatabase(Context context)
	{
		this.dbHelper = new SQLiteOpenbase(context);
		this.db = new Database(this.dbHelper);
	}

	public DataTable executeTable()
	{
		try
		{
			if (this.sql != null)
			{
				this.dataCollection = new DataCollection();
				this.collector = (ICollector) this.collectors.get(0);
				this.datas = this.collector.collect();
				this.dataCollection.addAll(this.datas);
				this.dataSource = this.db.executeTable(this.sql, this.dataCollection, (String) null);
				if (this.dataSource != null && this.dataSource.size() != 0)
				{
					int ex = this.releasers.size();

					for (int i = 0; i < ex; ++i)
					{
						((IReleaser) this.releasers.get(i)).release(this.dataSource);
					}
				}
			}
		}
		catch(Exception var3)
		{
			Log.e("execute", var3.getMessage());
		}

		return this.dataSource;
	}

	public Cursor executeCursor()
	{
		Cursor cursor = null;

		try
		{
			if (this.sql != null)
			{
				this.dataCollection = new DataCollection();
				this.collector = (ICollector) this.collectors.get(0);
				this.datas = this.collector.collect();
				this.dataCollection.addAll(this.datas);
				cursor = this.db.executeCursor(this.sql, this.dataCollection);
			}
		}
		catch(Exception var3)
		{
			Log.e("execute", var3.getMessage());
		}

		return cursor;
	}

	public void execute(String sql, DataCollection datas)
	{
		try
		{
			if (sql != null)
			{
				this.sql = sql;
				this.dataCollection = new DataCollection();
				if (datas != null)
				{
					this.dataCollection.addAll(datas);
				}

				this.db.execute(sql, this.dataCollection);
			}
		}
		catch(Exception var4)
		{
			Log.e("execute", var4.getMessage());
		}

	}

	public DataTable executeTable(String sql, DataCollection datas)
	{
		try
		{
			if (sql != null)
			{
				this.sql = sql;
				this.dataCollection = new DataCollection();
				if (datas != null)
				{
					this.dataCollection.addAll(datas);
				}

				this.dataSource = this.db.executeTable(sql, this.dataCollection, (String) null);
				if (this.dataSource != null && this.dataSource.size() != 0)
				{
					int ex = this.releasers.size();

					for (int i = 0; i < ex; ++i)
					{
						((IReleaser) this.releasers.get(i)).release(this.dataSource);
					}
				}
			}
		}
		catch(Exception var5)
		{
			Log.e("executeTable", var5.getMessage());
		}

		return this.dataSource;
	}

	public DataTable executeTable(String sql, DataCollection datas, String charcode)
	{
		try
		{
			if (sql != null)
			{
				this.sql = sql;
				this.dataCollection = new DataCollection();
				if (datas != null)
				{
					this.dataCollection.addAll(datas);
				}

				this.dataSource = this.db.executeTable(sql, this.dataCollection, charcode);
				if (this.dataSource != null && this.dataSource.size() != 0)
				{
					int ex = this.releasers.size();

					for (int i = 0; i < ex; ++i)
					{
						((IReleaser) this.releasers.get(i)).release(this.dataSource);
					}
				}
			}
		}
		catch(Exception var6)
		{
			Log.e("executeTable", var6.getMessage());
		}

		return this.dataSource;
	}

	public Cursor executeCursor(String sql, DataCollection datas)
	{
		Cursor cursor = null;

		try
		{
			if (sql != null)
			{
				this.sql = sql;
				this.dataCollection = new DataCollection();
				if (datas != null)
				{
					this.dataCollection.addAll(datas);
				}

				cursor = this.db.executeCursor(sql, this.dataCollection);
			}
		}
		catch(Exception var5)
		{
			Log.e("executeTable", var5.getMessage());
		}

		return cursor;
	}

	public void execute(DataTable dataSource)
	{
		try
		{
			if (dataSource != null && dataSource.size() != 0)
			{
				int ex = this.releasers.size();

				for (int i = 0; i < ex; ++i)
				{
					((IReleaser) this.releasers.get(i)).release(dataSource);
				}
			}
		}
		catch(Exception var4)
		{
			Log.e("execute", var4.getMessage());
		}

	}

	public void open()
	{
		if (this.dbHelper != null)
		{
			this.dbHelper.open();
		}

	}

	public void close()
	{
		if (this.dbHelper != null)
		{
			this.dbHelper.close();
		}

	}

	public String getSql()
	{
		return this.sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public LinkedList<ICollector> getCollectors()
	{
		return this.collectors;
	}

	public LinkedList<IReleaser> getReleasers()
	{
		return this.releasers;
	}

	public DataTable getDataSource()
	{
		return this.dataSource;
	}
}
