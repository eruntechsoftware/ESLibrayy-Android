//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.birthstone.core.sqlite;


import android.content.Context;
import android.util.Log;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.File;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParserToos
{
	private SqlParser sqlParser;
	private String sql;
	private String action;
	private String tableName;
	private String[] columns;
	private String selection;
	private String groupBy;
	private String having;
	private String orderBy;
	private String outSqlstr;
	private String[] sqlCollection;
	private DataCollection datas;
	private Context context;
	public Map<String, String> mapSqlString;

	public SqlParserToos(Context myContext)
	{
		this.context = myContext;
		this.mapSqlString = new HashMap();
	}

	public boolean readSql(String sqlName) throws Exception
	{
		try
		{
			this.action = null;
			this.tableName = null;
			this.columns = null;
			this.selection = null;
			this.groupBy = null;
			this.having = null;
			this.orderBy = null;
			if (this.mapSqlString.containsKey(sqlName))
			{
				this.sql = new String((String) this.mapSqlString.get(sqlName));
			}
			else
			{
				try
				{
					this.sql = File.getAssetsString("sql/" + sqlName + ".sql", this.context);
					this.mapSqlString.put(sqlName, new String(this.sql));
				}
				catch(Exception ex)
				{
					throw new Exception("Assets资源文件下的"+sqlName+"文件读取失败，请检查sql文件是否拼写正确！");
				}
			}

			if (this.sql != null && this.sql.length() > 0)
			{
				this.fillinString();
				this.split();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception var3)
		{
			throw var3;
		}
	}

	public void parse(String selectSql)
	{
		try
		{
			if (selectSql.toUpperCase().contains("SELECT"))
			{
				this.sqlParser = new SqlParser(selectSql);
				this.columns = this.sqlParser.parseCols();
				this.selection = this.sqlParser.parseConditions();
				this.groupBy = this.sqlParser.parseGroupCols();
				this.orderBy = this.sqlParser.parseOrderCols();
				this.tableName = this.sqlParser.parseTables();
			}
		}
		catch(Exception var3)
		{
			Log.e("匹配参数错误：", var3.getMessage());
		}

	}

	public void getAction(String Sql)
	{
		try
		{
			if (Sql.toUpperCase().contains("SELECT"))
			{
				this.action = "Select";
			}
			else
			{
				this.action = "General";
			}
		}
		catch(Exception var3)
		{
			Log.e("分析错误：", var3.getMessage());
		}

	}

	private void fillinString() throws Exception
	{
		try
		{
			Pattern ex = Pattern.compile("(?:@)(\\w+)(?:\\b)");
			Matcher m = ex.matcher(this.sql);
			StringBuffer result = new StringBuffer();
			if (this.datas != null && this.datas.size() > 0)
			{
				while(m.find())
				{
					String key = m.group(1);
					Data data = this.datas.get(key);
					if (data == null)
					{
						m.appendReplacement(result, "null");
					}

					if (data != null)
					{
						if (data.getValue() != null && !data.getValue().toString().trim().equals(""))
						{
							if (data.getDataType() != null && (data.getDataType().equals(DataType.Integer) || data.getDataType().equals(DataType.Numeric)))
							{
								m.appendReplacement(result, data.getValue().toString().replace("\'", "\'\'"));
							}
							else
							{
								m.appendReplacement(result, "\'" + data.getValue().toString().replace("\'", "\'\'").replaceAll("\\n", "\\\\n").replaceAll("\\r", "\\\\r") + "\'");
							}
						}
						else
						{
							m.appendReplacement(result, "null");
						}
					}
				}

				m.appendTail(result);
				this.sql = result.toString();
				this.sql = this.replace("(and|or|AND|OR)\\s+\\w+\\s*=\\s*null", this.sql);
				this.sql = this.replace("(order by|Order By|ORDER BY)\\s*null", this.sql);
			}

		}
		catch(Exception var6)
		{
			throw var6;
		}
	}

	private String replace(String regex, String sql)
	{
		try
		{
			Pattern ex = Pattern.compile(regex);
			Matcher m = ex.matcher(sql);
			StringBuffer result = new StringBuffer();
			if (this.datas != null && this.datas.size() > 0)
			{
				while(m.find())
				{
					m.appendReplacement(result, " ");
				}

				m.appendTail(result);
				sql = result.toString();
				return sql;
			}
		}
		catch(Exception var6)
		{
			Log.e("替换", var6.getMessage());
		}

		return sql;
	}

	private void split() throws Exception
	{
		try
		{
			this.sqlCollection = this.sql.split("GO");
		}
		catch(Exception var2)
		{
			throw var2;
		}
	}

	public String getSql()
	{
		return this.sql;
	}

	public String getAction()
	{
		return this.action;
	}

	public String getTableName()
	{
		return this.tableName.trim();
	}

	public String[] getColumns()
	{
		return this.columns;
	}

	public String getSelection()
	{
		return this.selection;
	}

	public String getGroupBy()
	{
		return this.groupBy;
	}

	public String getHaving()
	{
		return this.having;
	}

	public String getOrderBy()
	{
		return this.orderBy;
	}

	public Context getContext()
	{
		return this.context;
	}

	public DataCollection getDatas()
	{
		return this.datas;
	}

	public void setDatas(DataCollection datas)
	{
		this.datas = datas;
	}

	public String getOutSqlstr()
	{
		return this.outSqlstr;
	}

	public String[] getSqlCollection()
	{
		return this.sqlCollection;
	}
}
