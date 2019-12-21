package com.birthstone.core.interfaces;

public interface IValidatible {
	/** 
	 * 数据类型校验，并返回是否成功 
	 * @return 是否校验成功 
	 * **/
	Boolean dataValidator();

	Boolean getIsRequired();

	Boolean getIsEmpty();

	/**
	 * 非空提示
	 * **/
	void message();

	/**
	 * 校验正则表达式错误
	 * **/
	void expressionMessage();
}
