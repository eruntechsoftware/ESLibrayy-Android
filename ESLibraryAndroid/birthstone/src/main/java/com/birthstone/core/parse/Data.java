package com.birthstone.core.parse;

import android.util.Log;

import com.birthstone.core.helper.DataType;

import java.io.Serializable;

public class Data implements Cloneable, Serializable
{

    private static final long serialVersionUID = 3158113122131617945L;
    private String Name;
    private DataType DataType;
    private Object mValue;

    public Data ()
    {
    }

    public Data (String name, Object value)
    {
        this.Name = name;
        this.DataType = DataType.String;
        this.mValue = value;
    }

    public Data (String name, Object value, DataType datatype)
    {
        this.Name = name;
        this.DataType = datatype;
        this.mValue = value;
    }

    public Data copy ()
    {
        Data data = new Data();
        try
        {
            data.setName(this.Name);
            data.setValue(this.mValue);
        }
        catch (Exception ex)
        {
            Log.e("copy", ex.getMessage());
        }
        return data;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String name)
    {
        Name = name;
    }

    public DataType getDataType ()
    {
        return DataType;
    }

    public void setDataType (DataType dataType)
    {
        DataType = dataType;
    }

    public Object getValue ()
    {
        if (mValue!=null)
        {
            return this.mValue;
        }
        return "";
    }

    public String getStringValue ()
    {
        if (mValue!=null)
        {
            return this.mValue.toString();
        }
        return "";
    }

    public int getIntValue ()
    {
        return Integer.valueOf(this.mValue.toString());
    }

    public float getFloatValue ()
    {
        return Float.valueOf(this.mValue.toString());
    }

    public Boolean getBooleanValue ()
    {
        Boolean result = Boolean.valueOf(false);

        if (this.mValue != null && this.mValue.toString().toUpperCase().equals("TRUE"))
        {
            result = Boolean.valueOf(true);
        }

        if (this.mValue != null && this.mValue.toString().toUpperCase().equals("False"))
        {
            result = Boolean.valueOf(false);
        }

        if (this.mValue != null && this.mValue.toString().equals("1"))
        {
            result = Boolean.valueOf(true);
        }

        if (this.mValue != null && this.mValue.toString().equals("0"))
        {
            result = Boolean.valueOf(false);
        }

        return result;
    }

    public void setBooleanValue (boolean value)
    {
        if (value)
        {
            mValue = 1;
        }
        else
        {
            mValue = 0;
        }
    }

    public void setValue (Object value)
    {
        mValue = value;
    }


    @Override
    public Object clone ()
    {
        Data data = null;
        try
        {
            data = (Data) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
