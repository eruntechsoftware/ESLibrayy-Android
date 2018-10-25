package com.birthstone.core.helper;


import android.database.Cursor;
import android.util.Log;

import com.birthstone.core.interfaces.IDataIndexer;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.parse.DataTable;

public class DataIndexer implements IDataIndexer
{
    private Cursor datareader;

    public DataTable getAllData ()
    {
        DataTable dataTable = new DataTable();
        try
        {
            DataCollection params;
            Data data;
            String[] names;
            String value = null;
            int columnIndex = 0;

            this.datareader.moveToFirst();
            for (datareader.moveToFirst(); !datareader.isAfterLast(); datareader.moveToNext())
            {
                columnIndex = 0;
                params = new DataCollection();
                names = datareader.getColumnNames();
                for (String name : names)
                {
                    data = new Data();
                    data.setName(name);
                    value = null;
                    value = datareader.getString(columnIndex);
                    if (value != null)
                    {
                        data.setValue(value);
                    }
                    else
                    {
                        data.setValue("");
                    }
                    params.add(data);
                    columnIndex++;
                }
                dataTable.add(params);
            }
        }
        catch (Exception ex)
        {
            Log.e("DataTable", ex.getMessage());
        }
        finally
        {
            datareader.close();
        }
        return dataTable;
    }

    public DataTable getAllData (String charCode)
    {
        DataTable dataTable = new DataTable();
        try
        {
            DataCollection params;
            String[] names;
            Data data;
            String value = null;
            int columnIndex = 0;
            datareader.moveToFirst();
            for (datareader.moveToFirst(); !datareader.isAfterLast(); datareader.moveToNext())
            {
                columnIndex = 0;
                params = new DataCollection();
                names = datareader.getColumnNames();
                for (String name : names)
                {
                    data = new Data();
                    data.setName(name);
                    value = null;
                    value = datareader.getString(columnIndex);
                    if (value == null || "".equals(value))
                    {
                        try
                        {
                            value = new String(datareader.getBlob(columnIndex), charCode);
                        }
                        catch (Exception ex)
                        {
                            value = "";
                        }
                    }
                    if (!value.trim()
                            .equals("") && !ValidatorHelper.isMached("^[A-Za-z0-9]+$", value.toString()) && !ValidatorHelper
                            .isMached("^\\s*-?\\s*\\d+(.\\d+)?\\s*$", value.toString()) && !ValidatorHelper
                            .isMached("^[\u4E00-\u9FFF]+$", value.toString()))
                    {
                        value = new String(datareader.getBlob(columnIndex), charCode);
                    }
                    data.setValue(value);
                    params.add(data);
                    columnIndex++;
                }
                dataTable.add(params);
            }
        }
        catch (Exception ex)
        {
            Log.e("DataIndexer", ex.getMessage());
        }
        finally
        {
            datareader.close();
        }
        return dataTable;
    }

    public Data getData (String dataname)
    {
        Data data = new Data();
        try
        {
            datareader.moveToFirst();
            int columnIndex = datareader.getColumnIndex(dataname);
            if (columnIndex < 0)
            {
                Log.v(dataname, "null");
                data = null;
            }
            else
            {
                String value = datareader.getString(columnIndex);
                if (value != null)
                {
                    data.setName(dataname);
                    data.setValue(value);
                    Log.v(dataname, value.toString());
                }
                else
                {
                    data = null;
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("DataIndexer", ex.getMessage());
        }
        finally
        {
            // datareader.close();
        }
        return data;
    }

    public Data getData (String dataname, int position)
    {
        Data data = new Data();
        try
        {
            if (!datareader.isClosed())
            {
                datareader.moveToPosition(position);
                int columnIndex = datareader.getColumnIndex(dataname);
                if (columnIndex < 0)
                {
                    data = null;
                }
                else
                {
                    String value = datareader.getString(columnIndex);
                    if (value != null)
                    {
                        data.setName(dataname);
                        data.setValue(value);
                    }
                    else
                    {
                        data = null;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("DataIndexer", ex.getMessage());
        }
        finally
        {
            // datareader.close();
        }
        return data;
    }

    public void loadDataSource (Object datasource)
    {
        if (datasource instanceof Cursor)
        {
            datareader = (Cursor) datasource;
        }
    }
}
