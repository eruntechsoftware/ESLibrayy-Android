//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.birthstone.core.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.birthstone.core.helper.DataIndexer;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Database
{
	private SqlParserToos sqlSplit;
	private SQLiteOpenbase dbHelper;
	private SQLiteDatabase db;
	private DataIndexer indexer;
	private Lock lock = new ReentrantLock();

	public Database(SQLiteOpenbase dbHelper)
	{
		this.dbHelper = dbHelper;
		this.indexer = new DataIndexer();
		this.sqlSplit = new SqlParserToos(dbHelper.myContext);
	}

	public void executeSql(String sql)
	{
		try
		{
			this.lock.lock();
			this.open();
			this.db.beginTransaction();
			this.db.execSQL(sql);
			this.db.setTransactionSuccessful();
		}
		catch(Exception var6)
		{
			Log.e("数据库操作：", var6.getMessage());
		}
		finally
		{
			this.db.endTransaction();
			this.close();
			this.lock.unlock();
		}

	}

	public void execute(String fileName, DataCollection datas)
	{
		try
		{
			this.sqlSplit.setDatas(datas);
			String ex = "";
			if (this.sqlSplit.readSql(fileName))
			{
				this.lock.lock();
				this.open();
				this.db.beginTransaction();

				for (int i = 0; i < this.sqlSplit.getSqlCollection().length; ++i)
				{
					ex = this.sqlSplit.getSqlCollection()[i].toString().trim();
					this.sqlSplit.getAction(ex);
					this.sqlSplit.parse(ex);
					Log.v("Sql", ex);
					this.db.execSQL(ex);
				}

				this.db.setTransactionSuccessful();
			}
		}
		catch(Exception var8)
		{
			Log.e("数据库查询错误：", var8.getMessage());
		}
		finally
		{
			this.db.endTransaction();
			this.close();
			this.lock.unlock();
		}

	}

	public DataTable executeTable(String fileName, DataCollection datas, String charcode)
	{
		DataTable table = new DataTable();

		try
		{
			this.sqlSplit.setDatas(datas);
			String ex = "";
			if (this.sqlSplit.readSql(fileName))
			{
				this.lock.lock();
				this.open();
				this.db.beginTransaction();

				for (int i = 0; i < this.sqlSplit.getSqlCollection().length; ++i)
				{
					ex = this.sqlSplit.getSqlCollection()[i].toString().trim();
					this.sqlSplit.getAction(ex);
					this.sqlSplit.parse(ex);
					Log.v("Sql", ex);
					if (this.sqlSplit.getAction().equals("Select") && this.sqlSplit.getTableName() != null)
					{
						Cursor cursor = this.execute(this.sqlSplit.getTableName(), this.sqlSplit.getColumns(), this.sqlSplit.getSelection(), this.sqlSplit.getGroupBy(), this.sqlSplit.getHaving(), this.sqlSplit.getOrderBy(), charcode);
						if (cursor != null && cursor.getCount() > 0 && !cursor.isClosed())
						{
							this.indexer.loadDataSource(cursor);
							if (charcode != null && !charcode.equals(""))
							{
								table = this.indexer.getAllData(charcode);
							}
							else
							{
								table = this.indexer.getAllData();
							}

							if (cursor != null)
							{
								cursor.close();
							}
						}
					}
					else
					{
						this.db.execSQL(ex);
					}
				}

				this.db.setTransactionSuccessful();
			}
		}
		catch(Exception var11)
		{
			Log.e("数据库查询错误：", var11.getMessage());
		}
		finally
		{
			this.db.endTransaction();
			this.close();
			this.lock.unlock();
		}

		return table;
	}

	public DataTable executeTable(String fileName, String charcode) throws Exception
	{
		DataTable table = new DataTable();

		try
		{
			this.sqlSplit.setDatas((DataCollection) null);
			String ex = "";
			if (this.sqlSplit.readSql(fileName))
			{
				this.lock.lock();
				this.open();
				this.db.beginTransaction();

				for (int i = 0; i < this.sqlSplit.getSqlCollection().length; ++i)
				{
					ex = this.sqlSplit.getSqlCollection()[i].toString().trim();
					this.sqlSplit.getAction(ex);
					this.sqlSplit.parse(ex);
					if (this.sqlSplit.getAction().equals("Select") && this.sqlSplit.getTableName() != null)
					{
						Cursor cursor = this.execute(this.sqlSplit.getTableName(), this.sqlSplit.getColumns(), this.sqlSplit.getSelection(), this.sqlSplit.getGroupBy(), this.sqlSplit.getHaving(), this.sqlSplit.getOrderBy(), charcode);
						if (cursor != null && cursor.getCount() > 0 && !cursor.isClosed())
						{
							this.indexer.loadDataSource(cursor);
							if (charcode != null && !charcode.equals(""))
							{
								table = this.indexer.getAllData(charcode);
							}
							else
							{
								table = this.indexer.getAllData();
							}

							if (cursor != null && !cursor.isClosed())
							{
								cursor.close();
							}
						}
					}
					else
					{
						this.db.execSQL(this.sqlSplit.getSqlCollection()[i].toString().trim());
					}
				}

				this.db.setTransactionSuccessful();
			}
		}
		catch(Exception var10)
		{
			Log.e("数据库查询错误：", var10.getMessage());
		}
		finally
		{
			this.db.endTransaction();
			this.close();
			this.lock.unlock();
		}

		return table;
	}

	public Cursor executeCursor(String fileName, DataCollection datas)
	{
		Cursor cursor = null;

		try
		{
			this.sqlSplit.setDatas(datas);
			String ex = "";
			if (this.sqlSplit.readSql(fileName))
			{
				this.lock.lock();
				this.open();
				this.db.beginTransaction();

				for (int i = 0; i < this.sqlSplit.getSqlCollection().length; ++i)
				{
					ex = this.sqlSplit.getSqlCollection()[i].toString().trim();
					this.sqlSplit.getAction(ex);
					this.sqlSplit.parse(ex);
					Log.v("Sql", ex);
					if (this.sqlSplit.getAction().equals("Select") && this.sqlSplit.getTableName() != null)
					{
						cursor = this.execute(this.sqlSplit.getTableName(), this.sqlSplit.getColumns(), this.sqlSplit.getSelection(), this.sqlSplit.getGroupBy(), this.sqlSplit.getHaving(), this.sqlSplit.getOrderBy(), (String) null);
					}
					else
					{
						this.db.execSQL(ex);
					}
				}

				this.db.setTransactionSuccessful();
			}
		}
		catch(Exception var9)
		{
			Log.e("数据库查询错误：", var9.getMessage());
		}
		finally
		{
			this.db.endTransaction();
			this.close();
			this.lock.unlock();
		}

		return cursor;
	}

	public Cursor execute(String tableName, String[] columns, String selection, String groupBy, String having, String orderBy, String charcode)
	{
		Cursor cursor = null;

		try
		{
			if (selection != null)
			{
				if (!this.db.isOpen())
				{
					this.dbHelper.open();
				}

				cursor = this.db.query(tableName, columns, selection.toLowerCase(), (String[]) null, groupBy, having, orderBy);
				if (cursor.getCount() == 0)
				{
					cursor.close();
					cursor = this.db.query(tableName, columns, selection, (String[]) null, groupBy, having, orderBy);
				}

				if (cursor.getCount() == 0)
				{
					cursor.close();
					cursor = this.db.query(tableName, columns, selection.toUpperCase(), (String[]) null, groupBy, having, orderBy);
				}
			}
			else
			{
				cursor = this.db.query(tableName, columns, (String) null, (String[]) null, groupBy, having, orderBy);
				if (cursor.getCount() == 0)
				{
					cursor.close();
					cursor = this.db.query(tableName.toLowerCase(), columns, selection, (String[]) null, groupBy, having, orderBy);
				}

				if (cursor.getCount() == 0)
				{
					cursor.close();
					cursor = this.db.query(tableName.toUpperCase(), columns, selection, (String[]) null, groupBy, having, orderBy);
				}
			}
		}
		catch(SQLException var10)
		{
			throw var10;
		}

		Log.v("rowscount", String.valueOf(cursor.getCount()));
		if (cursor.getCount() == 0)
		{
			cursor.close();
		}

		return cursor;
	}

	public void open()
	{
		this.db = this.dbHelper.open();
	}

	public void close()
	{
		if (this.db != null && this.db.isOpen())
		{
			this.dbHelper.close();
		}

	}
}
