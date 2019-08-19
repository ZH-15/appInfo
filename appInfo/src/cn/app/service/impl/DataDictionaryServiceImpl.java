package cn.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.DataDictionaryMapper;
import cn.app.pojo.DataDictionary;
import cn.app.service.DataDictionaryService;

@Service("DataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {

	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	
	@Override
	public List<DataDictionary> findappStatus() {
		return dataDictionaryMapper.findappStatus();
	}

	@Override
	public List<DataDictionary> findappflatform() {
		// TODO Auto-generated method stub
		return dataDictionaryMapper.findappflatform();
	}

}
