
package com.birthstone.core.interfaces;


import com.birthstone.core.helper.DataType;

public interface ICellTitleStyleRequire
{
	void setDataType(DataType dataType);
	DataType getDataType();
	void setIsRequired(Boolean isRequired);
	Boolean getIsRequired();
}
