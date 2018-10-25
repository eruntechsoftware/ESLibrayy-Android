package com.birthstone.core.interfaces;

public interface IValidatible {
	/** 
	 * 数据类型校验，并返回是否成功 
	 * @return 是否校验成功 
	 * **/
	Boolean dataValidator();

	/**
	 * 提示校验错误
	 * **/
	void hint();
}
