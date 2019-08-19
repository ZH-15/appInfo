package cn.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.AppCategory;

public interface AppCategoryMapper {
	/**
	 * 查找一级分类列表
	 * @return
	 */
	public List<AppCategory> findAppCategorys1();
	
	/**
	 * 根据父类分类id查找分类列表
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> findAppCategorysByParentId(@Param("parentId")int parentId);
}
