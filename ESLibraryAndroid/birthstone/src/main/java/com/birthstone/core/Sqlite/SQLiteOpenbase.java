/*
 ***********************************************************************
 * This code sample is offered under a modified BSD license.           *
 * Copyright (C) 2010, Motorola, Inc. All rights reserved.             *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf * 
 * in your installation folder.                                        *
 ***********************************************************************
 */
package com.birthstone.core.sqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenbase extends SQLiteOpenHelper
{
	public static String DB_PATH;
	public static String DB_NAME;
	public Context myContext;
	public static SQLiteDatabase Db;

	/**
	 * Constructor Keeps a reference to the passed context in order to access
	 * the application's assets.
	 * 
	 * @param context Context to be used
	 */
	public SQLiteOpenbase( Context context )
	{
		super(context, DB_PATH + DB_NAME, null, 3);
		this.myContext = context;
	}

	public SQLiteDatabase open()
	{
		if(DB_PATH == null || DB_NAME == null)
		{
			SharedPreferences sharedPreferences = myContext.getSharedPreferences("checked", Context.MODE_PRIVATE);
			DB_PATH = sharedPreferences.getString("DB_PATH", "");
			DB_NAME = sharedPreferences.getString("DB_NAME", "");
		}
		if(Db==null)
		{
			Db = new DatabaseHelper(myContext, DB_PATH + DB_NAME, null, 1).getWritableDatabase();
		}
		if(!Db.isOpen())
		{
			Db = new DatabaseHelper(myContext, DB_PATH + DB_NAME, null, 1).getWritableDatabase();
		}
		return Db;
		/*
		 * try { Db=this.getWritableDatabase(); } catch(Exception ex) {
		 * Db=this.getReadableDatabase(); } return Db;
		 */
		// return myContext.openOrCreateDatabase(DB_PATH+DB_NAME,
		// Context.MODE_PRIVATE, null);
	}

	public void close()
	{
		if(Db != null)
		{
//			Db.close();
		}
//		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		super.onDowngrade(db, oldVersion, newVersion);
	}

	public class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper( Context context, String name, CursorFactory cursorFactory, int version )
		{
			super(context, name, cursorFactory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}

		@Override
		public void onOpen(SQLiteDatabase db)
		{
			super.onOpen(db);
		}
	}

}
