package cn.app.service;

import java.util.List;

import cn.app.pojo.AppCategory;

public interface AppCategoryService {
	/**
	 * 查找一级分类列表
	 * @return
	 */
	public List<AppCategory> findAppCategorys1();
	
	/**
	 * 根据一级分类id查找二级分类列表
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> findAppCategorysByParentId(int parentId);
}
