package com.birthstone.core.interfaces;


import com.birthstone.core.parse.DataTable;

/**
 * 
 * @author 杜明悦 
 *实现分页功能接口
 */
public interface IRange {
	/**
	 * 设置数据源
	 * @param source
	 */
	public void setSource(DataTable source);
	
	/**
	 * 初始化并绑定列表
	 * 
	 * @param startIndex 分页开始索引
	 * @param endIndex 分页截止索引
	 * **/
	public void bindListView(int startIndex, int endIndex);
	
	/**
	 * 
	 * @创建者: 杜明悦 
	 * @修改人:
	 * @修改时间：
	 * @功能：跳转到第一页
	 * @return 当前页索引
	 */
	public int toFirst();
	
	/**
	 * 
	 * @创建者: 杜明悦
	 * @修改人:
	 * @修改时间：
	 * @功能：跳转到最后一页
	 * @return 当前页索引
	 */
	public int toLast();
	
	/**
	 * 
	 * @创建者: 杜明悦
	 * @修改人:
	 * @修改时间：
	 * @功能：转到下一页
	 * @return 当前页索引
	 */
	public int next();
	
	/**
	 * 
	 * @创建者: 杜明悦 
	 * @修改人:
	 * @修改时间：
	 * @功能：转到上一页
	 * @return 当前页索引
	 */
	public int previous();
	
	/**
	 * @创建者: 杜明悦 
	 * @修改人:
	 * @修改时间：
	 * @功能：获取当前页面索引
	 */
	public int getPageIndex();
	
	/**
	 * @创建者: 杜明悦 
	 * @修改人:
	 * @修改时间：
	 * @功能：设置当前页面索引
	 */
	public void setPageIndex(int pageIndex);
}
