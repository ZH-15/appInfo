package cn.app.service;

import java.util.List;

import cn.app.pojo.DataDictionary;

public interface DataDictionaryService {
	/**
	 * 查找全部APP状态信息
	 * @return
	 */
	public List<DataDictionary> findappStatus();
	
	/**
	 * 查找全部APP所属平台信息
	 * @return
	 */
	public List<DataDictionary> findappflatform();
}
