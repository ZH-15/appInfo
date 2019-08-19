package cn.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.AppCategoryMapper;
import cn.app.pojo.AppCategory;
import cn.app.service.AppCategoryService;

@Service("AppCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {

	@Resource
	private AppCategoryMapper appCategoryMapper;
	
	@Override
	public List<AppCategory> findAppCategorys1() {
		return appCategoryMapper.findAppCategorys1();
	}

	@Override
	public List<AppCategory> findAppCategorysByParentId(int parentId) {
		return appCategoryMapper.findAppCategorysByParentId(parentId);
	}
	
}
